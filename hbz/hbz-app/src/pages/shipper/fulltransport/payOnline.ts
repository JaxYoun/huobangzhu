import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController,NavParams } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import {Http}from '@angular/http';
import { HelperService } from '../../../app/services/help.service';
import { WechatService } from '../../../app/services/wechat.service';
import { Config } from '../../../app/config';
import { TabsPage } from '../../tabs/tabs';

declare let Wechat;
declare let cordova;

@Component({
  selector: 'pay-online',
  templateUrl: 'payOnline.html'
})
export class PayOnline {
  public helperService:HelperService;
  public obj = {
    orderId:'',
    orderNo:'',
    amount:'',
    orderType:'',
    payType:'',
    businessType:''
  }
  public payTypeObj={};
  public payTypeSelect:string = "Wechat";
  private confirmUrl: string = "/api/pay/attach";//订单发起支付
  public alipayHtml = "";


  constructor(
    public params: NavParams,
    private nav:NavController,
    public alertCtrl: AlertController,
    public loadingCtrl: LoadingController,
    public toastCtrl: ToastController,
    public platform:Platform,
    private http:Http,
    public storage: Storage,
    public wechatService: WechatService,
    public config:Config) {
      this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
      this.confirmUrl = this.helperService.api + this.confirmUrl;
      this.obj = params.get('obj') || this.obj ;
      //订单相关业务（整车、零担、配送、快递）：ORDER;   其他业务需要传递---业务类型参数过来
      if(!this.obj.businessType){
        this.obj.businessType = 'ORDER';
      }

      this.storage.get("payType").then(
        (data)=>{
          delete data['Union'];
          delete data['WebAlipay'];
          delete data['WebUnion'];
          delete data['WebWechat'];
          this.payTypeObj = data;
        });
    }

    confirm() {
      if(this.payTypeSelect == "" || this.payTypeSelect == null){
        this.helperService.presentToast('支付类型必填');
        return false;
      }
      this.http.post(this.confirmUrl,
        {
          orderNo:this.obj.orderNo,
          payType:this.payTypeSelect,
          businessType:this.obj.businessType
        }, this.helperService.getOptions())
        .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
              // //微信拉起支付
              // if(this.payTypeSelect == 'Wechat'){
              //   //   "wechatpay": {
              //   //     "appid": "wxb8a558a5b70f7ef2",
              //   //     "noncestr": "47bf6e29a97c4efeae9d414600c83ef6",
              //   //     "package": "Sign=WXPay",
              //   //     "partnerid": "1487171572",
              //   //     "prepayid": "wx201711131616411920228c050339715771",
              //   //     "timestamp": 1510560990,
              //   //     "sign": "261468195F15620D79AFE59B11C9D8CB"
              //   // }
              //   this.payWechat(result.data.wechatpay);
              // }
              //支付宝拉起支付
              if(this.payTypeSelect == 'Alipay'){
                cordova.plugins.alipay.payment(result.data.alipay.body, (success) => {
                  console.log(success);
                  this.helperService.presentAlert("支付成功！",null, "关闭");
                  this.nav.push(TabsPage);
                }, (error) => {
                  console.log(error);
                });
              }
            }
          },
          (err)=> {
            this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
          });
    }

    payWechat(params){
              //客户端拉起微信支付
          // let params = {
          //     'partnerid': mch_id, // merchant id
          //     'prepayid': prepay_id, // prepay id returned from server
          //     'noncestr': nonce, // nonce string returned from server
          //     'timestamp': timestamp, // timestamp
          //     'sign': sign, // signed string
          //   };
          //  let params = {
          //     mch_id: '10000100', // merchant id
          //     prepay_id: 'wx201411101639507cbf6ffd8b0779950874', // prepay id returned from server
          //     nonce: '1add1a30ac87aa2db72f57a2375d8fec', // nonce string returned from server
          //     timestamp: '1439531364', // timestamp
          //     sign: '0CB01533B8C1EF103065174F50BCA001', // signed string
          //   };
            // this.wechatService.sendPaymentRequest(params);
          console.log(params)
          Wechat.sendPaymentRequest(params, function () {
              alert("Success");
          }, function (reason) {
              alert("Failed: " + reason);
          });
    }

}
