import {Component} from '@angular/core';
import {ActionSheetController,Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Camera,FilePath } from 'ionic-native';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { FileUploadService } from '../../../app/services/fileUpload.service';
import { Config } from '../../../app/config';
import { HelpMeBuyMapModals } from '../../shipper/helpMeBuy/mapModals';
import { MyWarehouse } from './myWarehouse';

@Component({
  selector: 'page-fulltransport',
  templateUrl: 'myWarehouseEdit.html'
})

export class MyWarehouseEdit {
    private createUrl: string = "/api/web/warehouse/createWarehouse";
    private updateUrl: string = "/api/web/warehouse/updateWarehouse";
    private attachUploadUrl: string = "/api/upload/uploadFiles";
    public helperService:HelperService;
    public title:string = '仓储信息发布';
    public certificates;//照片
    
    public obj = {
      id:'',
      name:'',
      capacity:'',
      unitPrice:'',
      minRentTime:'',
      ownerName:'',
      telephone:'',
      address:'',
      coordX:'',
      coordY:'',
      warehouseDescribe:'',
      titleImageList:''
    };
    public mapObj = {
      destX:'',
      destY:''
    }

    constructor(
        private nav:NavController,
        public params: NavParams,
        public alertCtrl: AlertController,
        public loadingCtrl: LoadingController,
        public toastCtrl: ToastController,
        public modalCtrl: ModalController,
        public platform:Platform,
        private http: Http,
        public actionSheetCtrl: ActionSheetController,
        public storage: Storage,
        public fileUploadService:FileUploadService,
        public config:Config) {
          this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
          this.createUrl = this.helperService.api + this.createUrl;
          this.updateUrl = this.helperService.api + this.updateUrl;
          this.attachUploadUrl = this.helperService.api + this.attachUploadUrl;
          
          if(params.get("obj")){
            this.obj = params.get("obj");
            this.mapObj = params.get("mapObj");
            this.certificates = this.obj.titleImageList;

            this.obj.titleImageList = '[' + this.obj.titleImageList + ']';
            
            // delete this.obj.titleImageList;
          }

        }
        
      create() {
        if (this.obj.name == '' || this.obj.name == null) {
          this.helperService.presentToast('仓库名称必填');
          return ;
        }
        if (this.obj.capacity == '' || this.obj.capacity == null) {
          this.helperService.presentToast('仓库容量必填');
          return ;
        }
        if (this.obj.unitPrice == '' || this.obj.unitPrice == null) {
          this.helperService.presentToast('租赁价格必填');
          return ;
        } else {
          if (this.obj.unitPrice.length > 9) {
            this.helperService.presentToast('租赁价格长度大于9位');
            return ;
          }
        }
        if (this.obj.minRentTime == '' || this.obj.minRentTime == null) {
          this.helperService.presentToast('租赁时间必填');
          return ;
        } else {
          if ((this.obj.minRentTime+"").indexOf(".") != -1) {
            this.helperService.presentToast('租赁时间只能输入整数');
            return ;
          }
        }
        if (this.obj.ownerName == '' || this.obj.ownerName == null) {
          this.helperService.presentToast('联系人必填');
          return ;
        }
        if (this.obj.telephone == '' || this.obj.telephone == null) {
          this.helperService.presentToast('联系电话必填');
          return ;
        }
        if(!this.helperService.checkPhoneFormat(this.obj.telephone.toString())){
          this.helperService.presentToast('联系电话有误');
          return;
        }
        if (this.mapObj.destX == '') {
          this.helperService.presentToast('仓库地址必填');
          return ;
        }

        this.obj.coordX = this.mapObj.destX;
        this.obj.coordY = this.mapObj.destY;

        let url = "";
        if(this.params.get("obj")){
          //有值则编辑
          url = this.updateUrl;
        }else{
          //新增
          url = this.createUrl;
        }

        this.http.post(url, this.obj, this.helperService.getOptions())
          .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
              let toast = this.toastCtrl.create({
                message: "仓储资讯发布成功",
                duration: 5000,
                position: 'top'//'bottom'
              });
              toast.present();
              //设置参数供确认页面使用。
              this.nav.push(MyWarehouse);
            } else {
              this.helperService.presentToast(result.msg);
            }
          },
          (err)=> {
            this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
          });
      }


      openMapModal(){
        let modal = this.modalCtrl.create(HelpMeBuyMapModals,{obj:this.mapObj});
        modal.onDidDismiss((item) => {
              if(item && item.lat){
                this.obj.coordX = item.lng;
                this.obj.coordY = item.lat;
                this.mapObj.destX = item.lng;
                this.mapObj.destY = item.lat;
                this.obj.address = item.title;
              }
        });
        modal.present();
      }


      createImage(){
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
          allowEdit: false,//在选择之前允许修改截图
          correctOrientation: true,//有可能会出现base64上传后图片自动旋转90度的问题,correctOrientation设置true即可
          saveToPhotoAlbum: false                                   //保存进手机相册
        }
        return options;
      }
  
      storeImage(sourceType) {
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
              this.upload(filePath)
            });
          } else{
            //拍照
            Camera.getPicture(options).then(imagePath => {
              return imagePath;
            }).then(value => {
              // currentName = value.substr(value.lastIndexOf('/') + 1);
              // currentPath = value.substr(0, value.lastIndexOf('/') + 1);
              // console.log('value: ', value);
              this.upload(value)

            });
          }
      }

          
  upload(filePath) {
    this.fileUploadService.uploadFile(filePath)
        .then((data) => {
          let result = JSON.parse(data.response);
          if (result.code == '200') {
              this.certificates = filePath;
              this.obj.titleImageList = '[' + result.data.fileList[0].filePath + ']';
          }
        }, (err) => {
          console.log(err)
          alert("上传失败");
        });
  }
}
