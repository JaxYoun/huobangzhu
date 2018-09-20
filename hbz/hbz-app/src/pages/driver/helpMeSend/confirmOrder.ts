import {Component} from '@angular/core';
import {Platform, NavController, ModalController,ToastController,AlertController, LoadingController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { ProtocolPage } from '../../protocol/protocol';
import { TakeList } from '../../person/takeOrder/takeList';



@Component({
  selector: 'confirmOrder',
  templateUrl: 'confirmOrder.html',
})
export class HelpMeSendTakeConfirmOrder {
  public helperService:HelperService;
  private confirmUrl: string = "/api/order/send/carry";//接单
  
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
  public remuneration:any;//配送费用
  public totalPrice:any;//总价
  public isNext: string = 'disabled'; // 禁用状态: disabled || ''
  public cucumber: boolean=false;
  public startTimeDivShow: boolean=false;
  public takeTimeDivShow: boolean=false;
  
  

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
        this.remuneration = this.obj.remuneration;
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
      }

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
        this.http.post(this.confirmUrl
          , {id:this.obj.id}
          , this.helperService.getOptions())
          .subscribe((data)=> {
              let result = data.json();
              if (result.code == '200') {
                  // //"在线支付"
                  // this.nav.push(PayOnline,{obj: {
                  //   orderId:this.obj.orderId,
                  //   orderNo:this.obj.orderNo,
                  //   amount:this.totalPrice,
                  //   orderType:'帮我送抢单'}})
                  this.helperService.presentToast('接单成功');
                  this.nav.push(TakeList);
              }else{
                this.helperService.presentToast(result.data[0]);
              }
            },
            (err)=> {
              this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });
      }

}
