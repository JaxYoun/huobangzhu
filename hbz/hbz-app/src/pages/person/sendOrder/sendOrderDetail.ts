import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController,ModalController, LoadingController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { SendList } from './sendList';
import { OrderDetail } from '../../shipper/fulltransport/orderDetail';
import { HelpMeBuyConfirmOrder } from '../../shipper/helpMeBuy/confirmOrder';
import { HelpMeSendConfirmOrder } from '../../shipper/helpMeSend/confirmOrder';
import { SelectPayWayModal } from './selectPayWayModal';
import { PayOnline } from '../../shipper/fulltransport/payOnline';
import { PayMonthly } from '../../shipper/fulltransport/payMonthly';


@Component({
  selector: 'sendOrderDetail',
  templateUrl: 'sendOrderDetail.html',
})
export class SendOrderDetail {
  public helperService:HelperService;
  public obj:any;
  public bottomType;
  public originAreaName;
  public destAreaName;

  public orderTypeChi;
  public orderTransChi;

  public receiveUrl="";
  public fslReceiveUrl: string = "/api/order/fsl/receive"; // 整车货主确认收货
  public ltlReceiveUrl: string = "/api/order/ltl/receive"; // 零担货主确认收货
  public buyReceiveUrl: string = "/api/order/buy/receive"; // 帮我买货主确认收货
  public sendReceiveUrl: string = "/api/order/send/receive"; // 帮我送货主确认收货
  public refuseUrl="";
  public fslRefuseUrl: string = "/api/order/fsl/refuse"; // 整车拒绝司机接运
  public ltlRefuseUrl: string = "/api/order/ltl/refuse"; // 零担拒绝司机接运
  public agreeUrl="";
  public agreeFslUrl: string = "/api/order/fsl/agree"; // 整车货主 接运司机确认
  public agreeLtlUrl: string = "/api/order/ltl/agree"; // 零担货主 接运司机确认
  public recordUrl: string = "/api/order/record/query";// 物流历史记录
  public recordArray=[];
  
  // 接运详情：有空值问题，报错。故提取变量
  public nickName="";
  public telephone="";
  public takeTime="";

  public typeName;//零担、整车
  

  constructor(
      public params: NavParams,
      private nav:NavController,
      public alertCtrl: AlertController,
      public loadingCtrl: LoadingController,
      public toastCtrl: ToastController,
      public platform:Platform,
      public modalCtrl: ModalController,
      private http: Http,
      public storage: Storage,
      public config:Config) {
        this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
        this.obj = params.get('obj');
        this.recordUrl = this.helperService.api + this.recordUrl;

        if(this.obj.orderType == 'FSL'){
          this.typeName = "专线运输-整车";
          this.receiveUrl = this.helperService.api + this.fslReceiveUrl;
          this.refuseUrl = this.helperService.api + this.fslRefuseUrl;
          this.agreeUrl = this.helperService.api + this.agreeFslUrl;
        }
        if(this.obj.orderType == 'LTL'){
          this.typeName = "专线运输-零担";
          this.receiveUrl = this.helperService.api + this.ltlReceiveUrl;
          this.refuseUrl = this.helperService.api + this.ltlRefuseUrl;
          this.agreeUrl = this.helperService.api + this.agreeLtlUrl;
        }
        if(this.obj.orderType == 'BUY'){
          this.receiveUrl = this.helperService.api + this.buyReceiveUrl;
        }
        if(this.obj.orderType == 'SEND'){
          this.receiveUrl = this.helperService.api + this.sendReceiveUrl;
        }

        this.storage.get("orderType").then(
          (data)=>{
            this.orderTypeChi = this.helperService.getValueByJson(this.obj.orderType, data);
          });

        this.orderTransChi = this.helperService.getOrderStatusChi(this.obj.orderTrans);


        // //帮我买没有取货地址
        // if(this.obj.orderType == 'BUY'){
        //   this.originAreaName = "";
        //   this.destAreaName = this.obj.destArea.level1Name + "-" + this.obj.destArea.level2Name + "-" 
        //       + this.obj.destArea.level3Name + "    " + this.obj.destAddress;
        // }
        
        //快递，取货地址、配送地址
        if(this.obj.orderType == 'EX'){
          // this.obj.commodityName = this.obj.commodityDesc;
          // this.originAreaName = this.obj.originArea.level1Name + "-" + this.obj.originArea.level2Name + "-" 
          // + this.obj.originArea.level3Name + "    " + this.obj.originAddr;
          // this.destAreaName = this.obj.destArea.level1Name + "-" + this.obj.destArea.level2Name + "-" 
          // + this.obj.destArea.level3Name + "    " + this.obj.destAddr;

          this.originAreaName = this.helperService.buildAreaLocation(this.obj.originArea.level1Name
            ,this.obj.originArea.level2Name,this.obj.originArea.level3Name) + "    " + this.obj.originAddr;
          this.destAreaName = this.helperService.buildAreaLocation(this.obj.destArea.level1Name
            ,this.obj.destArea.level2Name,this.obj.destArea.level3Name) + "    " + this.obj.destAddr;
          
        }
        else if (this.obj.orderType == 'SEND'){
          this.originAreaName = this.obj.originAddress + "    " + this.obj.originInfo;
          this.destAreaName = this.obj.destAddress + "    " + this.obj.destInfo;
        }
        else if (this.obj.orderType == 'BUY'){
          this.originAreaName = "";
          this.destAreaName = this.obj.destInfo + "    " + this.obj.destAddress;
        }
        else {
          // this.originAreaName = this.obj.originArea.level1Name + "-" + this.obj.originArea.level2Name + "-" 
          //     + this.obj.originArea.level3Name + "    " + this.obj.originAddress;
          // this.destAreaName = this.obj.destArea.level1Name + "-" + this.obj.destArea.level2Name + "-" 
          //     + this.obj.destArea.level3Name + "    " + this.obj.destAddress;

          this.originAreaName = this.helperService.buildAreaLocation(this.obj.originArea.level1Name
            ,this.obj.originArea.level2Name,this.obj.originArea.level3Name) + "    " + this.obj.originAddress;
          this.destAreaName = this.helperService.buildAreaLocation(this.obj.destArea.level1Name
            ,this.obj.destArea.level2Name,this.obj.destArea.level3Name) + "    " + this.obj.destAddress;
        }

        if(this.obj.takeUser){
          this.nickName = this.obj.takeUser.nickName;
          this.telephone = this.obj.takeUser.telephone;
        }
        if(this.obj.takeTime){
          this.takeTime = this.obj.takeTime;
        }
        this.historyOrder();
      }

    //历史订单
    historyOrder(){
      this.http.post(this.recordUrl,{orderId:this.obj.id},this.helperService.getOptions())
        .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
              this.recordArray = result.data;
              this.recordArray.forEach(element => {
                element.orderTrans = this.helperService.getOrderStatusChi(element.orderTrans);
              });
            }
          },
          (err)=> {
            this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
          });
    }

    //确认司机接运收件
    doAgree(){
      let confirm = this.alertCtrl.create({
        title: '温馨提示',
        message: '确认司机接运收件吗?',
        buttons: [
          {
            text: '确定',
            handler: () => {

              this.http.post(this.agreeUrl,{id:this.obj.id},this.helperService.getOptions())
              .subscribe((data)=> {
                  let result = data.json();
                  if (result.code == '200') {
                   this.helperService.presentToast('确认司机接运成功');
                   this.nav.push(SendList);
                  }else{
                    this.helperService.presentToast(result.msg);
                  }
                },
                (err)=> {
                  this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
                });
            }
          },
          {
            text: '取消',
            handler: () => {
            }
          }
        ]
      });
      confirm.present();

      
    }
    //确认收件
    doConfirm(){
      let confirm = this.alertCtrl.create({
        title: '温馨提示',
        message: '你确定收件吗?',
        buttons: [
          {
            text: '确定',
            handler: () => {

              //整车、零担，确认货物送达时需要交钱。
              if(this.obj.orderType == 'FSL' || this.obj.orderType == 'LTL'){
                if(this.obj.settlementType == 'MONTHLY_SETTLEMENT'){
                  this.http.post(this.receiveUrl,{id:this.obj.id},this.helperService.getOptions())
                  .subscribe((data)=> {
                      let result = data.json();
                      if (result.code == '200') {
                       this.helperService.presentToast('确认收件成功');
                       this.nav.push(SendList);
                      }else{
                        this.helperService.presentToast(result.msg);
                      }
                    },
                    (err)=> {
                      this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
                    });
                } else {
                  this.selectPayWay();
                }
              } else {
                //其他类型，直接确认货物已经送达
                this.http.post(this.receiveUrl,{id:this.obj.id},this.helperService.getOptions())
                .subscribe((data)=> {
                    let result = data.json();
                    if (result.code == '200') {
                     this.helperService.presentToast('确认收件成功');
                     this.nav.push(SendList);
                    }else{
                      this.helperService.presentToast(result.msg);
                    }
                  },
                  (err)=> {
                    this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
                  });
              }
            }
          },
          {
            text: '取消',
            handler: () => {
            }
          }
        ]
      });
      confirm.present();
    }

    //拒绝收件
    doCancle(){
      let confirm = this.alertCtrl.create({
        title: '温馨提示',
        message: '你确定拒绝收件吗?',
        buttons: [
          {
            text: '确定',
            handler: () => {
              this.http.post(this.refuseUrl,{id:this.obj.id},this.helperService.getOptions())
              .subscribe((data)=> {
                  let result = data.json();
                  if (result.code == '200') {
                   this.helperService.presentToast('拒绝收件成功');
                   this.nav.push(SendList);
                  }else{
                    this.helperService.presentToast(result.msg);
                  }
                },
                (err)=> {
                  this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
                });
            }
          },
          {
            text: '取消',
            handler: () => {
            }
          }
        ]
      });
      confirm.present();
    }


     //选择支付方式
     selectPayWay(){
      let modal = this.modalCtrl.create(SelectPayWayModal);
      modal.onDidDismiss((item) => {
            if(item){
              let ppparms = {};
              if(item.obj.payWaySelected == '1'){
                ppparms = {
                  id:this.obj.id,
                  paySelection:item.obj.payWaySelected,
                  amount:item.obj.amount
                }
              }else{
                  ppparms = {
                    id:this.obj.id,
                    paySelection:item.obj.payWaySelected
                  }
              }
              this.http.post(this.receiveUrl,ppparms,this.helperService.getOptions())
              .subscribe((data)=> {
                  let result = data.json();
                  if (result.code == '200') {
                      this.helperService.presentToast('确认收件成功');
                      if(item.obj.payWaySelected == "1"){
                        //"在线支付"
                        this.nav.push(PayOnline,{obj: {
                          orderId:this.obj.id,
                          orderNo:this.obj.orderNo,
                          amount:item.obj.amount,
                          orderType:this.typeName}})
                      } else {
                        this.nav.push(SendList);
                      }
                  }else{
                    this.helperService.presentToast(result.msg);
                  }
                },
                (err)=> {
                  this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
                });
            }
      });
      modal.present();
    }


    toPay(){
      let amount;
      if(this.obj.orderType == 'SEND'){
        amount = this.obj.amount;
      } else {
        amount = this.obj.expectedPrice;
      }
      //"在线支付"
      this.nav.push(PayOnline,{obj: {
        orderId:this.obj.id,
        orderNo:this.obj.orderNo,
        amount:amount,
        orderType:this.typeName}})
    }

    toSelectDriver(){
        let type = this.obj.orderType == 'FSL' ? 'fsl' : 'lsl';
        //"月结"   不调用确认，下一步选择司机后进行确认
        this.nav.push(PayMonthly,{obj: {
          orderId:this.obj.id,
          orderNo:this.obj.orderNo,
          settlementType:this.obj.settlementType,
          type:type}});
    }

    doDetail(){
      switch (this.obj.orderType) {
        case 'FSL': this.nav.push(OrderDetail,{obj:this.obj}); break;
        case 'LTL': this.nav.push(OrderDetail,{obj:this.obj}); break;
        case 'BUY': this.nav.push(HelpMeBuyConfirmOrder,{obj:this.obj,bottomType:'detail'}); break;
        case 'SEND': {
                //处理后退问题
          if(this.obj.takeLimit.val){
            this.obj.takeLimit = this.obj.takeLimit.val;
          }
          this.nav.push(HelpMeSendConfirmOrder,{obj:this.obj,bottomType:'detail'}); 
          break;
        }
        
        default:
          break;
      }
    }
}
