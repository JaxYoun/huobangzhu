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
export class HelpMeSendConfirmOrder {
  public helperService:HelperService;
  private confirmUrl: string = "/api/order/send/confirm";//订单确认
  private computeUrl: string = "/api/order/send/price/compute";//订单价格计算
  
  public obj:any={
    "commodityName":"",
    "commodityWeight":"",
    "commodityVolume":"",
    "commodityDesc":"",
    //取货字段
    "originLinker":"",
    "originLinkTelephone":"",
    "originAddress":"",
    "originInfo":"",
    "originX":"",
    "originY":"",
    "takeLimit":"",
    "orderTakeTime":null,
    //配送字段
    "linker":"",
    "linkTelephone":"",
    "destAddress":"",
    "destInfo":"",
    "destX":"",
    "destY":"",
    "timeLimit":"",
    "startTime":null,
    //预估费用
    "amount":"",
    "orderId":"",
    "orderNo":""
    };
  public remunerationRe:any;//参考费用
  public remuneration:any;//支付费用
  public totalPrice:any;//总价
  public isNext: string = 'disabled'; // 禁用状态: disabled || ''
  public cucumber: boolean=false;
  public startTimeDivShow: boolean=false;
  public takeTimeDivShow: boolean=false;

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
        this.computeUrl = this.helperService.api + this.computeUrl;
        this.obj = params.get('obj');

        this.bottomType = params.get('bottomType');
        if(this.bottomType){
          this.title = "订单详情";
        }else{
          this.title = "订单确认";
        }

        this.countAmount();
        if(this.obj.timeLimit == 'Immediately'){
          this.startTimeDivShow = true;
        }
        if(this.obj.takeLimit == '1'){
          this.takeTimeDivShow = true;
        }
        this.storage.get("takeLimit").then(
          (data)=>{
            this.obj.takeLimit = this.helperService.getValueByJsonArray("val",this.obj.takeLimit, data);
            
          });
        this.storage.get("timeLimit").then(
          (data)=>{
            this.obj.timeLimit = this.helperService.getValueByJson(this.obj.timeLimit, data);
          });
          // this.totalPrice = parseFloat(this.remuneration) + parseFloat(this.obj.amount);
      }

      // onChange(){
      //   this.totalPrice = parseFloat(this.remuneration) + parseFloat(this.obj.amount);
      // }

      updateCucumber() {
        if(this.cucumber){
          this.isNext = "";
        } else {
          this.isNext= 'disabled';
        }
      }
      presentModal() {
        let modal = this.modalCtrl.create(ProtocolPage, {newsId:'1002'});
        modal.present();
      }

      confirm() {
        if (this.remuneration == 0 || this.remuneration == null) {
          this.helperService.presentToast('配送费用必填');
          return false;
        }
        this.http.post(this.confirmUrl
          , {id:this.obj.orderId,amount:this.remuneration}
          , this.helperService.getOptions())
          .subscribe((data)=> {
              let result = data.json();
              if (result.code == '200') {
                  //"在线支付"
                  this.nav.push(PayOnline,{obj: {
                    orderId:this.obj.orderId,
                    orderNo:this.obj.orderNo,
                    amount:this.remuneration,
                    orderType:'帮我送'}})
              }
            },
            (err)=> {
              this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });
      }

  countAmount(){
    if(this.obj.originX != "" && this.obj.originY != "" && this.obj.destX != "" && this.obj.destY != ""){
      this.http.post(this.computeUrl, {
        originX:this.obj.originX,
        originY:this.obj.originY,
        destX:this.obj.destX,
        destY:this.obj.destY
        }, this.helperService.getOptions())
      .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          this.remuneration = result.data;
          this.remunerationRe = result.data;
        } else {
          this.remuneration = 0;
          this.remunerationRe = 0;
        }
        // this.onChange();//计算总价
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      });
    }
  }
}
