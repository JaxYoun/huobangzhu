import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { Wallet } from './wallet';


@Component({
  selector: 'page-bondrefund',
  templateUrl: 'bondrefund.html'
})

export class BondRefund {
    public title:string = '退保证金';
    private reFundUrl: string = "/api/refund/submit";
    public helperService:HelperService;
    public obj;
    
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
        this.reFundUrl = this.helperService.api + this.reFundUrl;
        this.obj = params.get("obj");
        console.log(this.obj)
          
      }



      confirm() {
          this.http.post(this.reFundUrl,{businessNo:this.obj.bondNo,businessType:'BOND'},this.helperService.getOptions())
          .subscribe((data)=> {
              let result = data.json();
              if (result.code == '200') {
                this.nav.push(Wallet);
                this.helperService.presentToast('退款成功')
              } else{
                this.helperService.presentToast(result.msg)
              }
            },
            (err)=> {
              this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });
      }

}
