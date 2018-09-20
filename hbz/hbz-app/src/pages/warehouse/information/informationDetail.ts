import {Component} from '@angular/core';
import {Platform, NavController,ToastController,AlertController, LoadingController, ModalController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';

@Component({
  selector: 'page-informationDetail',
  templateUrl: 'informationDetail.html'
})

export class InformationDetail {
    private queryUrl: string = "/api/sitePushMessageRecord/readSitePushMessageRecord";
    public helperService:HelperService;
    public obj={
      title:'',
      titleImage:'',
      isShowTitleImage:true,
      createDate:'',
      isShowDate:true,
      content:''
    };
    public title:string='';
    public isJpush:boolean=false;
    
    constructor(
        private nav:NavController,
        public params: NavParams,
        public alertCtrl: AlertController,
        public loadingCtrl: LoadingController,
        public toastCtrl: ToastController,
        public modalCtrl: ModalController,
        public platform:Platform,
        private http: Http,
        public storage: Storage,
        public config:Config) {
          this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
          this.queryUrl = this.helperService.api + this.queryUrl;
          this.title = params.get('title');
          this.isJpush = params.get('isJpush');
          if(this.isJpush){
            this.http.post(this.queryUrl,{sitePushMessageId:params.get('itemId')},this.helperService.getOptions())
            .subscribe((data)=> {
                let result = data.json();
                if (result.code == '200') {
                  this.obj.title = result.data.sitePushMessage.title;
                  //推送消息没有图片：都没有图片字段
                   this.obj.isShowTitleImage = false;
                  // if(result.data.sitePushMessage.imagePath){
                  //   this.obj.isShowTitleImage = true;
                  //   this.obj.titleImage = result.data.sitePushMessage.imagePath;
                  // } else{
                  //   this.obj.isShowTitleImage = false;
                  // }
                  this.obj.createDate = result.data.createDate;
                  this.obj.content = result.data.sitePushMessage.content;
                }
              },
              (err)=> {
                this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
              });
          } else {
            this.obj = params.get('item');
          }
      }

}
