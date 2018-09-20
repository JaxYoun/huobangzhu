import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { BondPay } from './bondpay';
import { BondRefund } from './bondrefund';
import { SelectRefund } from "./selectRefund";

@Component({
  selector: 'page-wallet',
  templateUrl: 'wallet.html'
})

export class Wallet {
    public title:string = '钱包管理';
    private queryUrl: string = "/api/bond/list";//保证金查询
    public helperService:HelperService;

    public walletArray=[];

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

        this.http.post(this.queryUrl,{},this.helperService.getOptions())
        .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
              this.walletArray = result.data;
              this.walletArray.forEach(element => {
                if(element.totalCount == 0){
                  element.status = false;
                  element.statusChi = "未交";
                } else {
                  element.status = true;
                  element.statusChi = "已交";
                  // if(element.totalCount >=1){
                  //   element.statusChi = "已交 *" + element.totalCount;
                  // } else {
                  //   element.statusChi = "已交";
                  // }
                }
              })
              console.log(result.data);
            }
          },
          (err)=> {
            this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
          });
      }


    //缴纳保证金
    bondPay(item){
      this.nav.push(BondPay,{obj:item});
    }
    bondRefund(item){
        let modal = this.modalCtrl.create(SelectRefund, {bondGradeId:item.id, title:item.name});
        modal.present();
      // if(item.totalCount > 1){
      //   let modal = this.modalCtrl.create(SelectRefund, {bondGradeId:item.id, title:item.name});
      //   modal.present();
      // }else{
      //   item.amount=item.total;
      //   this.nav.push(BondRefund, {obj:item});
      // }

    }
}
