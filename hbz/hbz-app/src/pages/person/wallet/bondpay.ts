import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { ProtocolPage } from '../../protocol/protocol';
import { PayOnline } from '../../shipper/fulltransport/payOnline';




@Component({
  selector: 'page-bondpay',
  templateUrl: 'bondpay.html'
})

export class BondPay {
    public title:string = '缴纳保证金';
    private payUrl: string = "/api/bond/pay";
    public helperService:HelperService;

    public obj;
    public isNext: string = 'disabled'; // 禁用状态: disabled || ''
    public cucumber: boolean=false;

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
        this.payUrl = this.helperService.api + this.payUrl;
        this.obj = params.get("obj");
          
      }

      updateCucumber() {
        if(this.cucumber){
          this.isNext = "";
        } else {
          this.isNext= 'disabled';
        }
      }
      presentModal() {
        let modal = this.modalCtrl.create(ProtocolPage, {newsId:'1004'});
        modal.present();
      }

      confirm() {
        if(this.obj.direct){
           //"在线支付"
           this.nav.push(PayOnline,{obj: {
            orderId:this.obj.id,
            orderNo:this.obj.bondNo,
            amount:this.obj.total,
            orderType:"保证金",
            businessType:"BOND"
          }})
        } else {
            this.http.post(this.payUrl,{bondType:this.obj.bondType,grade:this.obj.grade},this.helperService.getOptions())
            .subscribe((data)=> {
                let result = data.json();
                if (result.code == '200') {
                    //"在线支付"
                  this.nav.push(PayOnline,{obj: {
                    orderId:this.obj.id,
                    orderNo:result.data,
                    amount:this.obj.total,
                    orderType:"保证金",
                    businessType:"BOND"
                  }})
                }
              },
              (err)=> {
                this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
              });
        }

      }

}
