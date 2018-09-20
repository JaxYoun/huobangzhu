import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { PayOnline } from './payOnline';
import { PayMonthly } from './payMonthly';
import { CarCollect } from './carCollect';
import { Config } from '../../../app/config';
import { SendList } from '../../person/sendOrder/sendList';
import { Wallet } from '../../person/wallet/wallet';

@Component({
  selector: 'confirm-order',
  templateUrl: 'confirmOrder.html',
})
export class ConfirmOrder {
  public helperService:HelperService;
  public confirmUrl="";
  private fslConfirmUrl: string = "/api/order/fsl/confirm";//订单确认
  private lslConfirmUrl: string = "/api/order/ltl/confirm";//订单确认
  public obj:any;
  public settlementTypeObj={};
  public settlementTypeSelect="ONLINE_PAYMENT";//  "ONLINE_PAYMENT" : "在线支付",  "MONTHLY_SETTLEMENT" : "月结",  "LEVY_ONLINE_PAYMENT" : "车辆征集"
  public type;//零担、整车
  public typeName;//零担、整车
  public photoList=[];
  constructor(
      public params: NavParams,
      private nav:NavController,
      public alertCtrl: AlertController,
      public loadingCtrl: LoadingController,
      public toastCtrl: ToastController,
      public platform:Platform,
      private http: Http,
      public storage: Storage,
      public config:Config) {
        this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
        this.obj = params.get('obj');
        if(this.obj.relatedPictures){
          this.photoList = this.obj.relatedPictures.split(',');
        }
        this.type = params.get("type");
        if(this.type == 'fsl'){
          this.confirmUrl = this.helperService.api + this.fslConfirmUrl;
          this.typeName = "专线运输-整车";
        }
        if(this.type == 'lsl'){
          this.confirmUrl = this.helperService.api + this.lslConfirmUrl;
          this.typeName = "专线运输-零担";
        }
        this.storage.get("transType").then(
          (data)=>{
            this.obj.transType = this.helperService.getValueByJson(this.obj.transType, data);
          });
        this.storage.get("settlementType").then(
            (data)=>{
              this.settlementTypeObj = data;
          });
      }

      confirm() {
        if (this.settlementTypeSelect == '' || this.settlementTypeSelect == null) {
          this.helperService.presentToast('付款方式必填');
          return false;
        }
        this.obj.settlementType = this.settlementTypeSelect;
        if(this.settlementTypeSelect == 'MONTHLY_SETTLEMENT'){
          //"月结"   不调用确认，下一步选择司机后进行确认
          this.nav.push(PayMonthly,{obj: {
            orderId:this.obj.orderId,
            orderNo:this.obj.orderNo,
            settlementType:this.obj.settlementType,
            type:this.type}});
        } else {
          //其他类型，需要先调用确认接口
          this.http.post(this.confirmUrl
            , {id:this.obj.orderId,settlementType:this.obj.settlementType}
            , this.helperService.getOptions())
            .subscribe((data)=> {
                let result = data.json();
                if (result.code == '200') {
                  if(this.settlementTypeSelect == 'ONLINE_PAYMENT'){
                    // //"在线支付"
                    // this.nav.push(PayOnline,{obj: {
                    //   orderId:this.obj.orderId,
                    //   orderNo:this.obj.orderNo,
                    //   amount:this.obj.amount,
                    //   orderType:this.typeName}})
                    this.nav.push(SendList);
                  }
                  if(this.settlementTypeSelect == 'LEVY_ONLINE_PAYMENT'){
                    //"车辆征集"
                    this.nav.push(CarCollect,
                      {orderId:this.obj.orderId,
                        orderNo:this.obj.orderNo,
                        amount:this.obj.amount,
                        settlementType:this.obj.settlementType});
                  }
                } 
                if (result.code == '500') {
                  this.helperService.presentToast(result.msg);
                  if(result.data == 'NeedBond'){
                    let confirm = this.alertCtrl.create({
                      title: '请交纳保证金',
                      message: '你确定跳转到交纳保证金页面吗?',
                      buttons: [
                        {
                          text: '确定',
                          handler: () => {
                            this.nav.push(Wallet);
                          }
                        },
                        {
                          text: '取消',
                          handler: () => {
                            return;
                          }
                        }
                      ]
                    });
                    confirm.present();
                  }
                }
              },
              (err)=> {
                this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
              });
        }
      }
}
