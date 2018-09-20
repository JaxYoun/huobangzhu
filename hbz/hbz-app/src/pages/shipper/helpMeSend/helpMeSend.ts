import { Component, EventEmitter } from '@angular/core';
import {
  Platform,
  NavController,
  ToastController,
  AlertController,
  LoadingController,
  ModalController,
  ActionSheetController
} from 'ionic-angular';
import { Camera,FilePath } from 'ionic-native';
import { Http } from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { HelpMeBuyModals } from '../helpMeBuy/modals';
import { HelpMeSendConfirmOrder } from './confirmOrder';
import { Config } from '../../../app/config';
import { FileUploadService } from '../../../app/services/fileUpload.service';

@Component({
  selector: 'page-helpMeSend',
  templateUrl: 'helpMeSend.html',
})
export class HelpMeSend {
  private orderCreateUrl: string = '/api/order/send/create'; //帮我送订单创建
  private queryAddressUrl: string = '/api/user/app/link/query'; //查询常用地址

  public canLeave:boolean = true;
  public helperService: HelperService;
  public obj: any = {
    commodityName: '',
    commodityWeight: '',
    commodityVolume: '',
    commodityDesc: '',
    //取货字段
    originLinker: '',
    originLinkTelephone: '',
    originAddress: '',
    originInfo: '',
    originX: '',
    originY: '',
    takeLimit: '',
    orderTakeTime:  new Date().toISOString(),
    //配送字段
    linker: '',
    linkTelephone: '',
    destAddress: '',
    destInfo: '',
    destX: '',
    destY: '',
    timeLimit: '',
    startTime: new Date().toISOString(), //起送时间
    //预估费用
    amount: '',
    orderId: '',
    orderNo: '',
  };
  public takeLimitSelect: string = '1';
  public takeLimit;
  public timeLimitSelect: string = 'Immediately';
  public timeLimit = {};

  public defaultPickUpAddressId;
  public defaultDeliverAddressId;

  public photo1;
  public photo1serve="";

  constructor(
    private nav: NavController,
    public alertCtrl: AlertController,
    public loadingCtrl: LoadingController,
    public toastCtrl: ToastController,
    public modalCtrl: ModalController,
    public fileUploadService:FileUploadService,
    public actionSheetCtrl: ActionSheetController,
    public platform: Platform,
    private http: Http,
    public storage: Storage,
    public config: Config,
  ) {
    this.helperService = new HelperService(
      nav,
      alertCtrl,
      loadingCtrl,
      toastCtrl,
      storage,
      http,
      config,
    );
    this.orderCreateUrl = this.helperService.api + this.orderCreateUrl;
    this.queryAddressUrl = this.helperService.api + this.queryAddressUrl;

    this.storage.get('timeLimit').then(data => {
      this.timeLimit = data;
    });
    this.storage.get('takeLimit').then(data => {
      this.takeLimit = data;
    });
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
  //查询默认地址
  queryDefaultAddress() {
    this.http
      .post(
        this.queryAddressUrl,
        { orderType: 'BUY', idefault: 1, usein: 2 },
        this.helperService.getOptions(),
      )
      .subscribe(
        data => {
          let result = data.json();
          if (result.code == '200') {
            if (result.data.length == 0) {
              this.defaultPickUpAddressId = '';
            } else {
              this.defaultPickUpAddressId = result.data[0].id;
            }
          }
        },
        err => {
          this.helperService.presentToast(
            '网络请求失败,请确保你的网络环境正常',
          );
        },
      );
    this.http
      .post(
        this.queryAddressUrl,
        { orderType: 'BUY', idefault: 1, usein: 1 },
        this.helperService.getOptions(),
      )
      .subscribe(
        data => {
          let result = data.json();
          if (result.code == '200') {
            if (result.data.length == 0) {
              this.defaultDeliverAddressId = '';
            } else {
              this.defaultDeliverAddressId = result.data[0].id;
            }
          }
        },
        err => {
          this.helperService.presentToast(
            '网络请求失败,请确保你的网络环境正常',
          );
        },
      );
  }

  buildObj() {
    if (this.obj.commodityName == '' || this.obj.commodityName == null) {
      this.helperService.presentToast('商品名称必填');
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
    if (this.obj.originAddress == '' || this.obj.originAddress == null) {
      this.helperService.presentToast('取货地址必填');
      return false;
    }
    if (this.takeLimitSelect != '1' && this.obj.orderTakeTime == null) {
      this.helperService.presentToast('取货时间必填');
      return false;
    }
    if (this.obj.destAddress == '' || this.obj.destAddress == null) {
      this.helperService.presentToast('配送地址必填');
      return false;
    }
    if (this.timeLimitSelect != 'Immediately' && this.obj.startTime == null) {
      this.helperService.presentToast('配送时间必填');
      return false;
    }
    if (this.obj.amount == '' || this.obj.amount == null) {
      this.helperService.presentToast('预估费用必填');
      return false;
    }
    return true;
  }

  saveOrder() {
    if (this.buildObj()) {
      this.obj.timeLimit = this.timeLimitSelect;
      if (this.obj.startTime != null) {
        let poi = this.obj.startTime.indexOf('T');
        let takeTime1 =
          this.obj.startTime.substring(0, poi) +
          ' ' +
          this.obj.startTime.substring(poi + 1, this.obj.startTime.length - 8);
        this.obj.startTime = takeTime1;
      }
      this.obj.takeLimit = this.takeLimitSelect;
      if (this.obj.orderTakeTime != null) {
        let poi = this.obj.orderTakeTime.indexOf('T');
        let takeTime2 =
          this.obj.orderTakeTime.substring(0, poi) +
          ' ' +
          this.obj.orderTakeTime.substring(
            poi + 1,
            this.obj.orderTakeTime.length - 8,
          );
        this.obj.orderTakeTime = takeTime2;
      }


      if(this.photo1serve != '' ){
        this.obj.relatedPictures = this.photo1serve;
      }
      this.http
        .post(this.orderCreateUrl, this.obj, this.helperService.getOptions())
        .subscribe(
          data => {
            let result = data.json();
            if (result.code == '200') {
              let toast = this.toastCtrl.create({
                message: '保存帮我送订单成功',
                duration: 5000,
                position: 'top', //'bottom'
              });
              toast.present();
              //设置参数供确认页面使用。
              this.obj.orderId = result.data.id;
              this.obj.orderNo = result.data.orderNo;
              this.nav.push(HelpMeSendConfirmOrder, { obj: this.obj });
            } else {
              this.helperService.presentToast('帮我送订单创建失败');
            }
          },
          err => {
            this.helperService.presentToast(
              '网络请求失败,请确保你的网络环境正常',
            );
          },
        );
    }
  }

  onSelect() {
    if (this.timeLimitSelect == 'Immediately') {
      document.getElementById('startTimeDiv').style.display = 'none';
      this.obj.startTime = null;
    } else {
      document.getElementById('startTimeDiv').style.display = '';
    }
  }
  onSelectTakeTime() {
    if (this.takeLimitSelect == '1') {
      document.getElementById('takeTimeDiv').style.display = 'none';
      this.obj.orderTakeTime = null;
    } else {
      document.getElementById('takeTimeDiv').style.display = '';
    }
  }

  // 弹出框
  openModal(type) {
    //type : 'pickUp'  'deliver'
    let id;
    if (type == 'pickUp') id = this.defaultPickUpAddressId;
    if (type == 'deliver') id = this.defaultDeliverAddressId;
    let modal = this.modalCtrl.create(HelpMeBuyModals, {
      type: type,
      defaultPickUpAddressId: id,
      orderType: 'BUY',
    });
    modal.onDidDismiss(item => {
      if (item) {
        if (type == 'pickUp') {
          //取货字段
          this.obj.originX = item.lng;
          this.obj.originY = item.lat;
          this.obj.originLinkTelephone = item.linkTelephone;
          this.obj.originLinker = item.linker;
          this.obj.originAddress = item.destInfo;
          this.obj.originInfo = item.destAddress;
          this.defaultPickUpAddressId = item.defaultPickUpAddressId;
        }
        if (type == 'deliver') {
          this.obj.destX = item.lng;
          this.obj.destY = item.lat;
          this.obj.linkTelephone = item.linkTelephone;
          this.obj.linker = item.linker;
          this.obj.destAddress = item.destInfo;
          this.obj.destInfo = item.destAddress;
          this.defaultDeliverAddressId = item.defaultPickUpAddressId;
        }
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
      }
    }, (err) => {
      console.log(err)
      alert("上传失败");
    });
}


}
