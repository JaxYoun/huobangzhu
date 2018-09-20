import { Component } from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams, ViewController,ActionSheetController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { Camera,FilePath } from 'ionic-native';
import { FileUploadService } from '../../../app/services/fileUpload.service';



@Component({
  templateUrl: 'selectGoodsModal.html'
})
export class SelectGoodsModal {
  public exWeightemArry;
  public commodityClassObj;
  public isShow=false;
  public obj = {
    commodityClassSelected:'',
    exWeightemSelected:'',
    exWeightemSelected2:'',//超过20KG，自己填写
    commodityVolume:'',
    takeTime: new Date().toISOString(),
    commodityDesc:'',
    commodityName:'',
    relatedPictures:''
  }
  public helperService;
  public photo1;
  public photo1serve="";

  constructor(
    public params: NavParams,
    public viewCtrl: ViewController,
    private nav:NavController,
    public alertCtrl: AlertController,
    public loadingCtrl: LoadingController,
    public toastCtrl: ToastController,
    public modalCtrl: ModalController,
    public fileUploadService:FileUploadService,
    public actionSheetCtrl: ActionSheetController,
    public platform:Platform,
    private http: Http,
    public storage: Storage,
    public config:Config
  ) {
    this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
    this.commodityClassObj = params.get("commodityClassObj");
    this.exWeightemArry = params.get("exWeightemArry");

    if(params.get("dateFlag")){
      //回填货物内容
      this.obj = params.get("goodsObj");
      if(this.obj.takeTime != ""){

            //回填时日期处理
          if(this.obj.takeTime.length == 20){
            // let poi = this.obj.takeTime.indexOf('T');
            // this.obj.takeTime = this.obj.takeTime.substring(0,poi) + " "
            //     + this.obj.takeTime.substring(poi+1,this.obj.takeTime.length-4);
          } else {
            this.obj.takeTime = this.obj.takeTime.replace(" ","T") + ":00Z";
          }


        if(this.obj.relatedPictures) this.photo1 = this.obj.relatedPictures;
      }
    }
    this.onSelectExWeightem();
  }

  dismiss(){
    this.viewCtrl.dismiss();
  }


  // 保存地址
  saveNew(saveType,addRessId){
    if (this.obj.commodityName == '' || this.obj.commodityName == null) {
      this.helperService.presentToast('请填写货物名称');
      return;
    }
    if (this.obj.commodityClassSelected == '' || this.obj.commodityClassSelected == null) {
      this.helperService.presentToast('请选择货物类型');
      return;
    }
    if (this.obj.exWeightemSelected == '' || this.obj.exWeightemSelected == null) {
      this.helperService.presentToast('请选择货物重量');
      return;
    }
    if (this.obj.commodityVolume == '' || this.obj.commodityVolume == null) {
      this.helperService.presentToast('请填写预估体积');
      return;
    } else {
      if (!/^(([1-9][0-9]*)|(([0]\.\d{0,2}|[1-9][0-9]*\.\d{0,2})))$/.test(this.obj.commodityVolume)) {
        this.helperService.presentToast('预估体积最多为两位小数的正数');
        return;
      }
    }
    if (this.obj.takeTime == '' || this.obj.takeTime == null) {
      this.helperService.presentToast('请填写取件时间');
      return;
    }

    //回填时日期处理
    if(this.obj.takeTime.length == 24){
      let poi = this.obj.takeTime.indexOf('T');
      this.obj.takeTime = this.obj.takeTime.substring(0,poi) + " "
           + this.obj.takeTime.substring(poi+1,this.obj.takeTime.length-8);
    }

    if(this.photo1serve != '' ){
      this.obj.relatedPictures = this.photo1serve;
    }
    this.viewCtrl.dismiss({obj:this.obj});
  }


  //货物重量ID32
  onSelectExWeightem(){
    if(this.obj.exWeightemSelected == "32"){
      this.isShow = true;
    } else {
      this.isShow = false;
      this.obj.exWeightemSelected2 = "";
    }
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
