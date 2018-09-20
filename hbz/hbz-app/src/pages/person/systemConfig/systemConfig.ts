import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { Config } from '../../../app/config';
import { HelperService } from '../../../app/services/help.service';
import { LoginPage } from '../../login/login';
import { ProtocolPage } from '../../protocol/protocol';
import { InformationDetail } from '../../warehouse/information/informationDetail';
import {AppVersion} from '@ionic-native/app-version';
import {File} from '@ionic-native/file';
import {FileTransfer, FileTransferObject} from '@ionic-native/file-transfer';
import {FileOpener} from '@ionic-native/file-opener';



@Component({
  selector: 'personCenter',
  templateUrl: 'SystemConfig.html'
})

export class SystemConfig {
    public helperService:HelperService;
    public title:string = '系统设置';
    
    private logoutUrl: string = "/api/security/logout";
    private checkUpdateUrl: string = "/api/web/appVersion/getRecentAppByPlatformType";

    constructor(
        private nav:NavController,
        public alertCtrl: AlertController,
        public loadingCtrl: LoadingController,
        public toastCtrl: ToastController,
        public modalCtrl: ModalController,
        public platform:Platform,
        private http: Http,
        public storage: Storage,
        private appVersion: AppVersion,
        private fileOpener: FileOpener,
        private fileTransfer: FileTransfer,
        private file: File,
        public config:Config) {
          this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
          this.logoutUrl = this.helperService.api + this.logoutUrl;
          this.checkUpdateUrl = this.helperService.api + this.checkUpdateUrl;
          
        }

  goToAnyWhere(newsId) {
      this.nav.push(ProtocolPage,{newsId:newsId});
  }

  //清空缓存
  clearCache(){
    // TODO 主要是清理缓存图片（引导页、首页图片），目前图片没有缓存，暂时不做清理给一个效果。
    let loading = this.loadingCtrl.create({
      showBackdrop: false,
      content: '请等待...'
    });
    loading.present();

    setTimeout(() => {
        this.helperService.presentAlert("清理完成","","确定");
        loading.dismiss();//显示多久消失
      }, 500);

    // this.storage
  }

   //退出登录
   loinOut(){
    this.http.post(this.logoutUrl,{},this.helperService.getOptions())
      .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          this.storage.set("token", null);
          this.nav.push(LoginPage);
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      });
  }

  //检查更新
  checkUpdate(){
    this.http.post(this.checkUpdateUrl,{type:1},this.helperService.getOptions())
    .subscribe((data)=> {
      let result = data.json();
      console.log("result.code:" + result.code);
      if (result.code == '200') {
        console.log("fileUrl:" + result.data.fileUrl);
        let latestVersionNo:String = result.data.versionNo;
        this.appVersion.getVersionNumber().then((version:String) =>{
          console.log("本机version:" + version);
          this.doUpdate(version, latestVersionNo, result);
        });
      }
    },
    (err)=> {
      this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
    });
  }

  doUpdate(version, latestVersionNo, result){
    if(version != latestVersionNo){
      let versionArr = version.split(".");
      let latestVersionNoArr = latestVersionNo.split(".");
      let versionArr0:Number = Number.parseInt(versionArr[0]);
      let versionArr1:Number = Number.parseInt(versionArr[1]);
      let versionArr2:Number = Number.parseInt(versionArr[2]);  
      let latestVersionNoArr0:Number = Number.parseInt(latestVersionNoArr[0]);
      let latestVersionNoArr1:Number = Number.parseInt(latestVersionNoArr[1]);
      let latestVersionNoArr2:Number = Number.parseInt(latestVersionNoArr[2]);
      if(versionArr0 > latestVersionNoArr0){
        this.showNoNewVersion();
        return;
      }
      else if(versionArr0 == latestVersionNoArr0){
        if(versionArr1 > latestVersionNoArr1){
          this.showNoNewVersion();
          return;
        }
        else if(versionArr1 == latestVersionNoArr1){
          if(versionArr2 > latestVersionNoArr2){
            this.showNoNewVersion();
            return;
          }
          else if(versionArr2 == latestVersionNoArr2){
            this.showNoNewVersion();
            return;
          }
          else{
            this.detectionUpgrade(result.data.fileUrl, true); //提示升级
          }
        }
        else{
          this.detectionUpgrade(result.data.fileUrl, true); //提示升级
        }
      }
      else{
        this.detectionUpgrade(result.data.fileUrl, true); //提示升级
      }    
    }
    else{
      this.showNoNewVersion();
    }
  }

  showNoNewVersion(){
    this.alertCtrl.create({  
        title: '升级提示',  
        subTitle: '没有发现新版本!',  
        buttons: [{  
            text: '确定'  
        }]  
    }).present(); 
  }

  //检查app提示是否升级
  detectionUpgrade(apkUrl, allowChoose) {  
    if (allowChoose) {  
        this.alertCtrl.create({  
            title: '升级提示',  
            subTitle: '发现新版本,是否立即升级？',  
            buttons: [{  
                text: '取消'  
            }, {  
                text: '确定',  
                handler: () => {  
                    this.downloadApp(apkUrl);  
                }  
            }]  
        }).present();  
    } else {  
        this.downloadApp(apkUrl);  
    }
  }  

  //下载安装app
  downloadApp(apkUrl) {  
    let alert = this.alertCtrl.create({  
        title: '下载进度：0%',  
        enableBackdropDismiss: false,  
        buttons: ['后台下载']  
    });   
    alert.present();  
      
    const fileTransfer: FileTransferObject = this.fileTransfer.create();  
    // const apk = this.file.externalRootDirectory + 'hbz.apk'; //apk保存的目录 
    const apk = this.file.externalDataDirectory + 'hbz.apk'; //apk保存的目录 
    console.log("apk:" + apk);
    fileTransfer.download(apkUrl, apk).then((entry) => {  
      console.log("entry.toURL():" + entry.toURL());  
      this.fileOpener.open(entry.toURL(), 'application/vnd.android.package-archive').then(() =>{  
            console.log('File is opened')
        }).catch(e => {  
            console.log('Error openening file', e)  
        });  
    });  
    fileTransfer.onProgress((event: ProgressEvent) => {  
        let num = Math.floor(event.loaded / event.total * 100);  
        if (num === 100) {  
            alert.dismiss();  
        } else {  
            let title = document.getElementsByClassName('alert-title')[0];  
            title && (title.innerHTML = '下载进度：' + num + '%');  
        }  
    });  
  } 
}
