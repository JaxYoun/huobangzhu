import {Component} from '@angular/core';
import {Platform, NavController, ModalController,ToastController,AlertController, LoadingController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { PayOnline } from '../../shipper/fulltransport/payOnline';
import { ProtocolPage } from '../../protocol/protocol';


@Component({
  selector: 'confirmOrder',
  templateUrl: 'confirmOrder.html',
})
export class HelpMeBuyConfirmOrder {
  public helperService:HelperService;
  private confirmUrl: string = "/api/order/buy/confirm";//订单确认
  public obj:any={
    "commodityName":"",//商品名称
    "commodityCount":"",//购买数量
    "buyNeedInfo":"",//补充说明
    "commodityAmount":"",//预估价格
    "destX":"",//经度
    "destY":"",//纬度
    "destInfo":"",//坐标中文名称
    "destAddress":"",//详细地址
    "linker":"",//联系人
    "linkTelephone":"",//联系电话
    "timeLimit":null,//时间要求
    "startTime":"",//起送时间
    "orderId":"",
    "orderNo":""
    };
  public remuneration:any;//配送费用
  public totalPrice:any;//总价
  public isNext: string = 'disabled'; // 禁用状态: disabled || ''
  public cucumber: boolean=false;
  public startTimeDivShow: boolean=false;
  public bottomType="";
  public title;


  constructor(
      public params: NavParams,
      private nav:NavController,
      public alertCtrl: AlertController,
      public loadingCtrl: LoadingController,
      public toastCtrl: ToastController,
      public platform:Platform,
      private http: Http,
      public storage: Storage,
      public modalCtrl: ModalController,
      public config:Config) {
        this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
        this.confirmUrl = this.helperService.api + this.confirmUrl;
        this.obj = params.get('obj');
        if(this.obj.timeLimit == 'Immediately'){
          this.startTimeDivShow = true;
        }
        
        this.storage.get("timeLimit").then(
          (data)=>{
            this.obj.timeLimit = this.helperService.getValueByJson(this.obj.timeLimit, data);
          });
          
          this.bottomType = params.get('bottomType');
          if(this.obj.remuneration){
            this.remuneration = this.obj.remuneration;
            this.title = "订单详情";
            this.totalPrice = (parseFloat(this.remuneration) + parseFloat(this.obj.commodityAmount)).toFixed(2);
          }else{
            // this.remuneration = 0;
            this.title = "订单确认";
            this.totalPrice =  parseFloat(this.obj.commodityAmount);
          }
      }

      onChange(){
        this.totalPrice = (parseFloat(this.remuneration == "" ? "0" : this.remuneration) + parseFloat(this.obj.commodityAmount)).toFixed(2);
      }

      updateCucumber() {
        if(this.cucumber){
          this.isNext = "";
        } else {
          this.isNext= 'disabled';
        }
      }
      presentModal() {
        let modal = this.modalCtrl.create(ProtocolPage, {newsId:'1001'});
        modal.present();
      }

      confirm() {
        if (this.remuneration == 0 || this.remuneration == null) {
          this.helperService.presentToast('配送费用必填');
          return false;
        }
        this.http.post(this.confirmUrl
          , {id:this.obj.orderId,remuneration:this.remuneration}
          , this.helperService.getOptions())
          .subscribe((data)=> {
              let result = data.json();
              if (result.code == '200') {
                  //"在线支付"
                  this.nav.push(PayOnline,{obj: {
                    orderId:this.obj.orderId,
                    orderNo:this.obj.orderNo,
                    amount:this.totalPrice,
                    orderType:'帮我买'}})
              }
            },
            (err)=> {
              this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });
      }

}
