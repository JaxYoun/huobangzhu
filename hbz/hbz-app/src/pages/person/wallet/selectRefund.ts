import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams,ViewController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';

import { BondRefund } from './bondrefund';
import { BondPay } from './bondpay';

@Component({
  templateUrl: 'selectRefund.html',
})
export class SelectRefund {
  public character:object = { title: '选择退款' };
  private queryUrl: string = "/api/bond/query";//保证金查询
  public helperService:HelperService;
  public walletArray=[];
  public flag:boolean=true;
  public title="";
  constructor(
    private nav:NavController,
    public params: NavParams,
    public viewCtrl: ViewController,
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
      this.title = params.get("title");

      this.http.post(this.queryUrl,{bondGradeId:params.get("bondGradeId")},this.helperService.getOptions())
        .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
              this.walletArray = result.data;
            }
          },
          (err)=> {
            this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
          });
    }

  dismiss(characterNum) {
    this.viewCtrl.dismiss();
  }
  selectBond(item) {
    if(item.bondStatus == 0){
      //去支付
      item.total=item.amount;
      item.direct=true;
      this.nav.push(BondPay,{obj:item});
    } 
    if(item.bondStatus == 1){
      //去退款
      this.nav.push(BondRefund,{obj:item});
    } 
    if(item.bondStatus == 2){
      //冻结
      this.helperService.presentToast('请选择未冻结的保证金进行操作');
    }
  }
  selectAndGo(item) {
    if(this.flag){
      this.helperService.presentToast('请选择保证金');
    }
  }
}
