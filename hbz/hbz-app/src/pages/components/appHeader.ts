import { Component, Input } from '@angular/core';
import {Platform, ViewController,NavController, ToastController,AlertController, LoadingController ,NavParams} from 'ionic-angular';
import { Storage } from '@ionic/storage';
import { MessagePage } from '../person/message/message';
import {Http}from '@angular/http';
import { Config } from '../../app/config';
import { HelperService } from '../../app/services/help.service';


@Component({
 selector: 'app-header',
 templateUrl: './appHeader.html'
})
export class AppHeader {
  @Input() title;
  @Input() dismiss;
  private queryUrl: string = "/api/sitePushMessageRecord/getUnreadMessageCount";
  public helperService:HelperService;
  public isRead:boolean=false;
  
  constructor(
    public viewCtrl: ViewController,
    private nav: NavController,
    public params: NavParams,
    public alertCtrl: AlertController,
    public loadingCtrl: LoadingController,
    public toastCtrl: ToastController,
    private http:Http,
    public storage: Storage,
    public config:Config) {    
    this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
    this.queryUrl = this.helperService.api + this.queryUrl;
    
    this.storage.get("token").then(data => {
      this.helperService.getOptionss().then(options=>{
            this.http.post(this.queryUrl,{},options)
            .subscribe((data)=> {
                let result = data.json();
                if (result.code == '200') {
                  if(result.data > 0 ) this.isRead = true;
                }
              },
              (err)=> {
                this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
              });
      })
  })
  }

  pagedDismiss(){
    this.viewCtrl.dismiss();
  }


  gotoMessage(){
    this.nav.push(MessagePage);
  }
}
