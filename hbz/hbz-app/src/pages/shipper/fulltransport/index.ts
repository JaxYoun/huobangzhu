import {Component} from '@angular/core';
import {App, Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams, ViewController,ActionSheetController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { Camera,FilePath } from 'ionic-native';
import { FileUploadService } from '../../../app/services/fileUpload.service';
import { HelperService } from '../../../app/services/help.service';
import {HomePage} from '../home/home';
import { FulltransportModals } from './modals';
import { ConfirmOrder } from './confirmOrder';
import { Config } from '../../../app/config';
import {ShipperPage } from '../shipper';
import {SpecialLineModals } from '../../driver/specialLine/modals';
@Component({
  selector: 'page-fulltransport',
  templateUrl: 'index.html',
})
export class Fulltransport {
  private createUrl="";
  private priceUrl="";
  private fslCreateUrl: string = "/api/order/fsl/create";//整车专线订单创建
  private lslCreateUrl: string = "/api/order/ltl/create";//零担专线订单创建
  private fslPriceUrl: string = "/api/order/fsl/price";//价格计算
  private lslPriceUrl: string = "/api/order/ltl/price";//价格计算
  private queryAddressUrl: string = "/api/user/app/address/query";//查询常用地址
  private queryCarSizeUrl: string = "/api/transSize/all";//车长规格数据
  private binaryUploadUrl: string = "/api/binary/s/up";//图像上传

  public helperService:HelperService;

  //车长数组
  public transArray;
  public transArrayString="";

  public obj:any={
    "commodityName":"",//货物名称
    "commodityType":"",//货物类型
    "commodityDescribe":"",//货物描述
    "amount":"",//金额
    "expectedPrice":"",//预估金额
    "settlementType":null,//结算方法(线上，月结)
    "commodityWeight":"",//货物质量
    "commodityVolume":"",//货物体积
    "weightUnit":null,//计量单位(针对质量)
    "volumeUnit":null,//计量单位（会对体积）
    "transType":"",//车辆类型
    "maxLoad":"",//最大载重数
    "originAreaCode":"75",//发货区域百度
    "destAreaCode":"75",//目的区域百度Code
    "originX":"",//发货地址点经度
    "originY":"",//发货纬度
    "destX":"",//目标地址经度
    "destY":"",//目标地址纬度
    "originAddress":"",//取货地址
    "destAddress":"",//配送地址
    "orderTakeStart":new Date().toISOString(),//接运时间
    "linkMan":"",//联系人
    "linkTelephone":"",//货主电话
    "destLinker":"",
    "destTelephone":"",
    "destlimit":new Date().toISOString(),
    "linkRemark":"",//补充说明
    "unitPrice":"",//计量单价
    // "relatedPictures":[]//图片ticket数组
    };

  public relatedPictures="";

  public transTypeSelect:string = "UNLIMITED";
  public transType={};
  public goodsTypeSelect:string = "Light";
  public goodsType={};
  public areaJsonArray;
  public defaultPickUpAddressId;
  public defaultDeliverAddressId;
  public pickUpAddressId;//取货地址ID
  public pickUpAddress;//取货地址
  public pickUpAddressInfo;//取货详细地址
  public deliverAddressId;//送货地址ID
  public deliverAddress;//送货地址
  public deliverAddressInfo;//送货详细地址
  public refDistance;//参考距离
  public refPrice;//参考单价
  public refTotalPrice;//参考总价
  public price;//单价
  public totalPrice;//总价
  public isShowPrice = true;//是否显示单价

  public type;//零担、整车
  public typeName;//零担、整车

  public pictureList=[];//照片
  public photo1;
  public photo2;
  public photo3;
  public photo1serve="";
  public photo2serve="";
  public photo3serve="";

  public canLeave:boolean = true;
  constructor(
      private app: App,
      public viewCtrl: ViewController,
      public params: NavParams,
      private nav:NavController,
      public alertCtrl: AlertController,
      public loadingCtrl: LoadingController,
      public toastCtrl: ToastController,
      public modalCtrl: ModalController,
      public actionSheetCtrl: ActionSheetController,
      public fileUploadService:FileUploadService,
      public platform:Platform,
      private http: Http,
      public storage: Storage,
      public config:Config) {

        // this.pictureList.push( this.config.uploadPre + "test/20180227/7bf8b9b8-a9fb-4e34-976e-7c4cf87f2ce9.jpg");
        // this.pictureList.push( this.config.uploadPre + "test/20180227/7bf8b9b8-a9fb-4e34-976e-7c4cf87f2ce9.jpg");
        // this.pictureList.push( this.config.uploadPre + "test/20180227/7bf8b9b8-a9fb-4e34-976e-7c4cf87f2ce9.jpg");

        this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
        this.type = params.get("type");
        if(this.type == 'fsl'){
          this.createUrl = this.helperService.api + this.fslCreateUrl;
          this.priceUrl = this.helperService.api + this.fslPriceUrl;
          this.typeName = "整车发货";
        }
        if(this.type == 'lsl'){
          this.createUrl = this.helperService.api + this.lslCreateUrl;
          this.priceUrl = this.helperService.api + this.lslPriceUrl;
          this.typeName = "零担发货";
        }
        this.queryAddressUrl = this.helperService.api + this.queryAddressUrl;
        this.queryCarSizeUrl = this.helperService.api + this.queryCarSizeUrl;
        this.binaryUploadUrl = this.helperService.api + this.binaryUploadUrl;

        this.storage.get("transType").then(
          (data)=>{
            this.transType = data;
          });
        this.storage.get("areaJson").then(
          (data)=>{
            this.areaJsonArray = data;
          });
        this.storage.get("commodityType").then(
          (data)=>{
            this.goodsType = data;
          });
          this.queryCarSize();
      this.queryDefaultAddress();
  }

  backState(canLeave) {
    if(canLeave === false) {
      this.canLeave = false
    }
    if(canLeave === true) {
      this.canLeave = true
    }
    if(canLeave === undefined){
      return this.canLeave
    }
  }
  ionViewCanLeave() {
    let res = this.canLeave
    if(this.canLeave === false) {
      this.canLeave = !this.canLeave
      res = false
    }else {
      res = true
    }
    return res;
  }

   //查询车长规格数据
   queryCarSize(){
    this.http.post(this.queryCarSizeUrl, {}, this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          this.transArray = result.data;
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      });
  }

  //查询默认地址
  queryDefaultAddress(){
    this.http.post(this.queryAddressUrl,  {idefault:1,type:1}, this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.length == 0){
            this.defaultPickUpAddressId = "";
          } else {
            this.defaultPickUpAddressId = result.data[0].id;
          }
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      });
    this.http.post(this.queryAddressUrl,  {idefault:1,type:2}, this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.length == 0){
            this.defaultDeliverAddressId = "";
          } else {
            this.defaultDeliverAddressId = result.data[0].id;
          }
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      });
  }


  //价格计算
  queryPrice(){
    //货物类型为其他，不计算单价；隐藏，只显示预估总价
    if(this.goodsTypeSelect == 'Other'){
      this.isShowPrice = false;
      return false;
    } else {
      this.isShowPrice = true;
    }

    //取货地址、送货地址 为空不计算
    if (this.pickUpAddressId == '' || this.pickUpAddressId == null) {
      return false;
    }
    if (this.deliverAddressId == '' || this.deliverAddressId == null) {
      return false;
    }
    //预估重量、预估体积 为空不计算
    if (this.obj.commodityWeight == '' || this.obj.commodityWeight == null) {
      return false;
    }
    if (this.obj.commodityVolume == '' || this.obj.commodityVolume == null) {
      return false;
    }

    this.countPrice();

    this.http.post(this.priceUrl,  {
          originAreaCode:this.obj.originAreaCode,
          destAreaCode:this.obj.destAreaCode,
          originAddress:this.pickUpAddressInfo,
          destAddress:this.deliverAddressInfo,
          commodityWeight:this.obj.commodityWeight,
          commodityVolume:this.obj.commodityVolume,
          commodityType:this.goodsTypeSelect
        }, this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.refDistance = result.data.distance;
            this.refPrice = result.data.unitPrice;
            this.refTotalPrice = result.data.price;
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
  }

  //计算单价
  countPrice(){
     //货物类型为其他，不计算单价；隐藏，只显示预估总价
    if(this.goodsTypeSelect == 'Other'){
      this.isShowPrice = false;
      this.obj.unitPrice = "";
      return false;
    }

    if(this.goodsTypeSelect == 'Light'){
      //重货：单价= 总价/重量
      if (this.obj.commodityWeight == '' || this.obj.commodityWeight == null) {
        return false;
      }
      let temp = parseFloat(this.obj.expectedPrice) / parseFloat(this.obj.commodityVolume);
      this.obj.unitPrice = temp.toFixed(2);
    }
    if(this.goodsTypeSelect == 'Heavy'){
      //轻货：单价= 总价/体积
      if (this.obj.commodityVolume == '' || this.obj.commodityVolume == null) {
        return false;
      }
      let temp = parseFloat(this.obj.expectedPrice) / parseFloat(this.obj.commodityWeight);
      this.obj.unitPrice = temp.toFixed(2);
    }
  }


  buildObj(){
    if (this.obj.commodityName == '' || this.obj.commodityName == null) {
      this.helperService.presentToast('货物名称必填');
      return false;
    }
    if (this.obj.commodityWeight == '' || this.obj.commodityWeight == null) {
      this.helperService.presentToast('预估重量必填');
      return false;
    }
    if (this.obj.commodityVolume == '' || this.obj.commodityVolume == null) {
      this.helperService.presentToast('预估体积必填');
      return false;
    }
    if (this.obj.orderTakeStart == '' || this.obj.orderTakeStart == null) {
      this.helperService.presentToast('接运时间必填');
      return false;
    }
    if (this.obj.destlimit == '' || this.obj.destlimit == null) {
      this.helperService.presentToast('送达时间必填');
      return false;
    }
    if (this.transArrayString == '' || this.transArrayString == null) {
      this.helperService.presentToast('车长必填');
      return false;
    }
    if (this.obj.maxLoad == '' || this.obj.maxLoad == null) {
      this.helperService.presentToast('最低载重必填');
      return false;
    }
    // if (this.obj.unitPrice == '' || this.obj.unitPrice == null) {
    //   this.helperService.presentToast('单价必填');
    //   return false;
    // }
    if (this.pickUpAddressId == '' || this.pickUpAddressId == null) {
      this.helperService.presentToast('取货地址必填');
      return false;
    }
    if (this.deliverAddressId == '' || this.deliverAddressId == null) {
      this.helperService.presentToast('送货地址必填');
      return false;
    }
    if (this.obj.expectedPrice == '' || this.obj.expectedPrice == null) {
      this.helperService.presentToast('预估总价必填');
      return false;
    }
    return true;
  }

  fslCreate(){
    if(this.buildObj()){
      let poi = this.obj.orderTakeStart.indexOf('T');
      let takeTime = this.obj.orderTakeStart.substring(0,poi) + " "
                  + this.obj.orderTakeStart.substring(poi+1,this.obj.orderTakeStart.length-8);
      let poi2 = this.obj.destlimit.indexOf('T');
      let takeTime2 = this.obj.destlimit.substring(0,poi2) + " "
                  + this.obj.destlimit.substring(poi2+1,this.obj.destlimit.length-8);
      this.obj.orderTakeStart = takeTime;
      this.obj.destlimit = takeTime2;
      this.obj.transType = this.transTypeSelect;
      this.obj.commodityType = this.goodsTypeSelect;
      this.obj.originAddress = this.pickUpAddressInfo;
      this.obj.destAddress = this.deliverAddressInfo;

      if(this.photo1serve != '' || this.photo2serve != '' || this.photo3serve != '' ){
        this.obj.relatedPictures = this.photo1serve + "," + this.photo2serve + "," + this.photo3serve;
        // this.obj.relatedPictures = this.relatedPictures;
      }
      this.http.post(this.createUrl, this.obj, this.helperService.getOptions())
        .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            let toast = this.toastCtrl.create({
              message: "保存订单成功",
              duration: 5000,
              position: 'top'//'bottom'
            });
            toast.present();
            //设置参数供确认页面使用。
            this.obj.orderId = result.data.id;
            this.obj.orderNo = result.data.orderNo;
            this.obj.pickUpAddress = this.pickUpAddress;
            this.obj.pickUpAddressInfo = this.pickUpAddressInfo;
            this.obj.deliverAddress = this.deliverAddress;
            this.obj.deliverAddressInfo = this.deliverAddressInfo;
            this.nav.push(ConfirmOrder,{obj: this.obj,type:this.type});
          } else {
            this.helperService.presentToast('订单创建失败');
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });

    }
  }

  // 弹出框
  openModal(type: object) {
    type['areaJsonArray'] = this.areaJsonArray;
    type['defaultPickUpAddressId'] = this.defaultPickUpAddressId;
    type['defaultDeliverAddressId'] = this.defaultDeliverAddressId;
    let modal = this.modalCtrl.create(FulltransportModals, type);
    modal.onDidDismiss((item) => {
          if(item){
              if(item.type == 'pickUp'){
                this.pickUpAddressId = item.pickUpAddressId;
                this.pickUpAddress = item.pickUpAddress;
                this.pickUpAddressInfo = item.pickUpAddressInfo;
                this.defaultPickUpAddressId =item.defaultPickUpAddressId;
                this.obj.originAreaCode = item.pickUpOutCode;
                this.obj.linkMan = item.contactPerson;
                this.obj.linkTelephone = item.contactTelephone;
              }
              if(item.type == 'deliver'){
                this.deliverAddressId = item.deliverAddressId;
                this.deliverAddress = item.deliverAddress;
                this.deliverAddressInfo = item.deliverAddressInfo;
                this.defaultDeliverAddressId = item.defaultDeliverAddressId;
                this.obj.destAreaCode = item.deliverOutCode;
                this.obj.destLinker = item.contactPerson;
                this.obj.destTelephone = item.contactTelephone;
              }
              this.queryPrice();
          }
    });
    modal.present();
  }


    // 弹出框
    openSelectCarModal() {
      let modal = this.modalCtrl.create(SpecialLineModals,{transArray:this.transArray,transArrayString:this.transArrayString});
      modal.onDidDismiss((item) => {
            if(item){
                this.transArrayString = "";
                // this.obj.transSizes = item.transSizes;
                for(let i=0;i<item.transSizes.length;i++){
                  if(i == item.transSizes.length-1){
                    this.transArrayString += item.transSizes[i];
                  } else {
                    this.transArrayString += item.transSizes[i] + ",";
                  }
                }
                this.obj.transLen = this.transArrayString;
            }
      });
      modal.present();
    }



    createImage(photoImg){
      let actionSheet = this.actionSheetCtrl.create({
        title: '选择照片',
        buttons: [
          {
            text: '相册',
            handler: () => this.storeImage(Camera.PictureSourceType.PHOTOLIBRARY,photoImg)
          },
          {
            text: '拍照',
            handler: () => this.storeImage(Camera.PictureSourceType.CAMERA,photoImg)
          },
          {
            text: '取消',
            role: 'cancel'
          }
        ]
      });
      actionSheet.present();
    }

    setOptions(srcType: number) {
      let options = {
        quality: 100,//相片质量0-100
        targetWidth: 1200,//照片宽度
        targetHeight: 1200, //照片高度
        destinationType: Camera.DestinationType.FILE_URI,//返回类型：DATA_URL= 0，返回作为 base64 編碼字串。 FILE_URI=1，返回影像档的 URI。NATIVE_URI=2，返回图像本机URI (例如，資產庫)
        sourceType: srcType,//从哪里选择图片：PHOTOLIBRARY=0，相机拍照=1，SAVEDPHOTOALBUM=2。0和1其实都是本地图库
        encondingType: Camera.EncodingType.JPEG, //保存的图片格式： JPEG = 0, PNG = 1
        mediaType: Camera.MediaType.PICTURE,//可选媒体类型：圖片=0，只允许选择图片將返回指定DestinationType的参数。 視頻格式=1，允许选择视频，最终返回 FILE_URI。ALLMEDIA= 2，允许所有媒体类型的选择。
        allowEdit: false,//在选择之前允许修改截图
        correctOrientation: true,//有可能会出现base64上传后图片自动旋转90度的问题,correctOrientation设置true即可
        saveToPhotoAlbum: false                                   //保存进手机相册
      }
      return options;
    }

    storeImage(sourceType,photoImg) {
      let options = this.setOptions(sourceType);
      // let currentName: string = null;
      // let currentPath: string = null;
        if (this.platform.is('android') && sourceType === Camera.PictureSourceType.PHOTOLIBRARY) {
          //相册
          Camera.getPicture(options).then(imagePath => {
            return imagePath;
          }).then(value => {
            // currentName = value.substring(value.lastIndexOf('/') + 1, value.lastIndexOf('?'));
            return FilePath.resolveNativePath(value);
          }).then(filePath => {
            this.upload(filePath,photoImg)
          });
        } else{
          //拍照
          Camera.getPicture(options).then(imagePath => {
            return imagePath;
          }).then(value => {
            // currentName = value.substr(value.lastIndexOf('/') + 1);
            // currentPath = value.substr(0, value.lastIndexOf('/') + 1);
            // console.log('value: ', value);
            this.upload(value,photoImg)

          });
        }
    }


upload(filePath,photoImg) {

  this.fileUploadService.uploadFile(filePath)
      .then((data) => {
        let result = JSON.parse(data.response);
        if (result.code == '200') {
            // console.log(result)
            // //本地路径
            // this.pictureList.push(filePath);
            //上传后返回的服务器地址
            let picture = this.config.uploadPre + result.data.fileList[0].filePath;
            if(photoImg == 'photo1'){
              this.photo1 = filePath;
              this.photo1serve = picture;
            }
            if(photoImg == 'photo2'){
              this.photo2 = filePath;
              this.photo2serve = picture;
            }
            if(photoImg == 'photo3'){
              this.photo3 = filePath;
              this.photo3serve = picture;
            }
        }
      }, (err) => {
        console.log(err)
        alert("上传失败");
      });
}
}
