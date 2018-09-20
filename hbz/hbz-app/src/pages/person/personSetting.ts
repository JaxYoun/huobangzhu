import {Component} from '@angular/core';
import {ActionSheetController, Platform, NavController, ToastController,AlertController, LoadingController, ModalController,NavParams,ViewController } from 'ionic-angular';
import { Camera,File,FilePath } from 'ionic-native';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../app/services/help.service';
import { Config } from '../../app/config';
import { PersonCenter } from './personCenter';
import { FileUploadService } from '../../app/services/fileUpload.service';



@Component({
  templateUrl: 'personSetting.html'
})
export class PersonSetting {

    private updateUrl: string = "/api/user/updateHbzUser";
    public helperService:HelperService;

    public obj={
      login:'',
      imageUrl:'',
      nickName:'',
      complexScore:''
    };
    public certificates="";

    constructor(
        private param:NavParams,
        public viewCtrl: ViewController,
        private nav:NavController,
        public alertCtrl: AlertController,
        public loadingCtrl: LoadingController,
        public toastCtrl: ToastController,
        public modalCtrl: ModalController,
        public platform:Platform,
        public actionSheetCtrl: ActionSheetController,
        public fileUploadService:FileUploadService,
        private http: Http,
        public storage: Storage,
        public config:Config) {
          this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
          this.updateUrl = this.helperService.api + this.updateUrl;

          this.obj = param.get("obj");
          this.certificates = this.obj.imageUrl;
        }

    // 关闭模态框
    dismiss() {
      this.viewCtrl.dismiss();
    }


    createImage(certificatesType){
      let actionSheet = this.actionSheetCtrl.create({
        title: '选择照片',
        buttons: [
          {
            text: '相册',
            handler: () => this.storeImage(Camera.PictureSourceType.PHOTOLIBRARY)
          },
          {
            text: '拍照',
            handler: () => this.storeImage(Camera.PictureSourceType.CAMERA)
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
        allowEdit: true,//在选择之前允许修改截图
        correctOrientation: true,//有可能会出现base64上传后图片自动旋转90度的问题,correctOrientation设置true即可
        saveToPhotoAlbum: false                                   //保存进手机相册
      }
      return options;
    }

    
    storeImage(sourceType) {
      let options = this.setOptions(sourceType);
        if (this.platform.is('android') && sourceType === Camera.PictureSourceType.PHOTOLIBRARY) {
          //相册
          Camera.getPicture(options).then(imagePath => {
            FilePath.resolveNativePath(imagePath).then(
              (data) => {
                this.upload(data);
              }
            );
          });
        } else{
          //拍照
          Camera.getPicture(options).then(imagePath => {
            this.upload(imagePath)
          });
        }
    }


    upload(filePath) {
      this.fileUploadService.uploadFile(filePath)
          .then((data) => {
            let result = JSON.parse(data.response);
            if (result.code == '200') {
                this.certificates = filePath;
                this.obj.imageUrl = result.data.fileList[0].filePath;
            }
          }, (err) => {
            console.log(err)
            alert("上传失败");
          });
    }


    submitUpload(){
      this.http.post(this.updateUrl,{imageUrl:this.obj.imageUrl},this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.helperService.presentToast('修改成功');
            // this.nav.push(PersonCenter);
            this.viewCtrl.dismiss({imageUrl:this.certificates});
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
    }

}
