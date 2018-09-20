import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { PayOnline } from '../../shipper/fulltransport/payOnline';
import { ProtocolPage } from '../../protocol/protocol';



@Component({
  selector: 'page-earnestmoney',
  templateUrl: 'earnestmoney.html'
})

export class EarnestMoney {
    public title:string = '缴诚意金';
    public helperService:HelperService;
    // private queryUrl: string = "/api/web/warehouse/getWarehouseEarnest";
    private generateOrderUrl: string = "/api/web/warehouse/generateWarehouseEarnestOrder";
    public money;

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
        // this.queryUrl = this.helperService.api + this.queryUrl;
        this.generateOrderUrl = this.helperService.api + this.generateOrderUrl;

        this.money = params.get("money");
        
        // this.http.post(this.queryUrl,{},this.helperService.getOptions())
        //   .subscribe((data)=> {
        //     let result = data.json();
        //     if (result.code == '200') {
        //       this.money = result.data;
        //     }
        //   },
        //   (err)=> {
        //     this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        //   });
      }

      updateCucumber() {
        if(this.cucumber){
          this.isNext = "";
        } else {
          this.isNext= 'disabled';
        }
      }
      presentModal() {
        let modal = this.modalCtrl.create(ProtocolPage, {newsId:'1003'});
        modal.present();
      }

      confirm() {
        this.http.post(this.generateOrderUrl,{warehouseId:this.params.get('id'),earnestPrice:this.money},this.helperService.getOptions())
        .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
                //"在线支付"
               this.nav.push(PayOnline,{obj: {
                orderId:result.data.orderNo,
                orderNo:result.data.orderNo,
                amount:this.money,
                orderType:"诚意金",
                businessType:"WORDER"
              }})
            }
          },
          (err)=> {
            this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
          });
      }
}
