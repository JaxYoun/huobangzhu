import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController,NavParams,ViewController} from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { Config } from '../../app/config';
import { HelperService } from '../../app/services/help.service';

@Component({
  selector: 'page-protocol',
  templateUrl: 'protocol.html'
})
export class ProtocolPage {

  public helperService:HelperService;
  public title:string = '';
  public getNewsUrl: string = "/api/web/news/getNewsDetail";
  public obj={
    title:'',
    titleImage:'',
    isShowTitleImage:true,
    createDate:'',
    isShowDate:true,
    content:''
  };
  public newsId='';

  constructor(
    private nav:NavController,
    public params: NavParams,
    public alertCtrl: AlertController,
    public loadingCtrl: LoadingController,
    public toastCtrl: ToastController,
    public modalCtrl: ModalController,
    public viewCtrl: ViewController,
    public platform:Platform,
    private http: Http,
    public storage: Storage,
    public config:Config) {
      this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
      this.getNewsUrl = this.helperService.api + this.getNewsUrl;
      
      this.newsId = params.get('newsId');
      if(this.newsId == '1'){
        this.title = '关于货帮主';
      }
      if(this.newsId == '2'){
        this.title = '平台使用协议';
      }


      this.http.post(this.getNewsUrl,{id:this.newsId},this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.obj = result.data;
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
    }

  dismiss() {
    this.viewCtrl.dismiss();
  }
}
