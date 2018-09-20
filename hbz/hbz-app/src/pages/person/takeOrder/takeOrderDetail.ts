import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { TakeList } from './takeList';
import { TakeSign } from './takeSign';
import { OrderDetail } from '../../shipper/fulltransport/orderDetail';
import { HelpMeBuyConfirmOrder } from '../../shipper/helpMeBuy/confirmOrder';
import { HelpMeSendConfirmOrder } from '../../shipper/helpMeSend/confirmOrder';


@Component({
  selector: 'takeOrderDetail',
  templateUrl: 'takeOrderDetail.html',
})
export class TakeOrderDetail {
  public helperService:HelperService;
  public obj:any;
  public bottomType;
  public originAreaName;
  public destAreaName;

  public orderTypeChi;
  public orderTransChi;

  public takeUrl="";
  public takeFslUrl: string = "/api/order/fsl/take";
  public takeLtlUrl: string = "/api/order/ltl/take";
  public takeBuyUrl: string = "/api/order/buy/take";
  public takeSendUrl: string = "/api/order/send/take";
  public drivingAgreeUrl="";
  public drivingAgreeFslUrl: string = "/api/order/fsl/driving/agree";
  public drivingAgreeLtlUrl: string = "/api/order/ltl/driving/agree";
  public endUrl="";
  public endFslUrl: string = "/api/order/fsl/end";
  public endLtlUrl: string = "/api/order/ltl/end";
  public refuseAgreeUrl="";
  public refuseAgreeFslUrl: string = "/api/order/fsl/driving/refuse";
  public refuseAgreeLtlUrl: string = "/api/order/ltl/driving/refuse";
  public refuseUrl: string = "/api/order/refuse";
  public recordUrl: string = "/api/order/record/query";
  public recordArray=[];
  public offlineProcess="0";//用户拒绝签收1,默认0
  

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
        
        if(this.obj.offlineProcess == '1'){
          this.offlineProcess = "1";
        }

        this.recordUrl = this.helperService.api + this.recordUrl;
        this.refuseUrl = this.helperService.api + this.refuseUrl;
        if(this.obj.orderType == 'FSL'){
          this.takeUrl = this.helperService.api + this.takeFslUrl;
          this.drivingAgreeUrl = this.helperService.api + this.drivingAgreeFslUrl;
          this.refuseAgreeUrl = this.helperService.api + this.refuseAgreeFslUrl;
          this.endUrl = this.helperService.api + this.endFslUrl;
        }
        if(this.obj.orderType == 'LTL'){
          this.takeUrl = this.helperService.api + this.takeLtlUrl;
          this.drivingAgreeUrl = this.helperService.api + this.drivingAgreeLtlUrl;
          this.refuseAgreeUrl = this.helperService.api + this.refuseAgreeLtlUrl;
          this.endUrl = this.helperService.api + this.endLtlUrl;
        }  
        if(this.obj.orderType == 'BUY'){
          this.takeUrl = this.helperService.api + this.takeBuyUrl;
        }  
        if(this.obj.orderType == 'SEND'){
          this.takeUrl = this.helperService.api + this.takeSendUrl;
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
        //       + this.obj.destArea.level3Name + "    " + this.obj.destInfo;
        // } else{
        //   this.originAreaName = this.obj.originArea.level1Name + "-" + this.obj.originArea.level2Name + "-" 
        //   + this.obj.originArea.level3Name + "    " + this.obj.originInfo;
        //   this.destAreaName = this.obj.destArea.level1Name + "-" + this.obj.destArea.level2Name + "-" 
        //   + this.obj.destArea.level3Name + "    " + this.obj.originInfo;
        // }

        //快递，取货地址、配送地址
        if(this.obj.orderType == 'EX'){
          // this.obj.commodityName = this.obj.commodityDesc;
          // this.originAreaName = this.obj.originArea.level1Name + "-" + this.obj.originArea.level2Name + "-" 
          // + this.obj.originArea.level3Name + "    " + this.obj.originAddr;
          this.originAreaName = this.helperService.buildAreaLocation(this.obj.originArea.level1Name
            ,this.obj.originArea.level2Name,this.obj.originArea.level3Name) + "    " + this.obj.originAddr;;
          // this.destAreaName = this.obj.destArea.level1Name + "-" + this.obj.destArea.level2Name + "-" 
          // + this.obj.destArea.level3Name + "    " + this.obj.destAddr;
          this.destAreaName = this.helperService.buildAreaLocation(this.obj.destArea.level1Name,
            this.obj.destArea.level2Name,this.obj.destArea.level3Name)+ "    " + this.obj.destAddr;
          
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
                ,this.obj.originArea.level2Name,this.obj.originArea.level3Name)+ "    " + this.obj.originAddress;
              this.destAreaName = this.helperService.buildAreaLocation(this.obj.destArea.level1Name
                ,this.obj.destArea.level2Name,this.obj.destArea.level3Name)+ "    " + this.obj.destAddress;
              
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

    //确认收件页面跳转
    doConfirm(flag, title){
      if(flag == 'drivingAgree'){
        let confirm = this.alertCtrl.create({
          title: '温馨提示',
          message: '你确定接单吗?',
          buttons: [
            {
              text: '确定',
              handler: () => {
                this.drivingAgree();
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
      if(flag == 'refuseAgree'){
        let confirm = this.alertCtrl.create({
          title: '温馨提示',
          message: '你确定拒绝接单吗?',
          buttons: [
            {
              text: '确定',
              handler: () => {
                this.refuseAgree();
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
      if(flag == 'take'){
        if(!title){
          title = '你确定收货吗?';
        }

        let confirm = this.alertCtrl.create({
          title: '温馨提示',
          message: title,
          buttons: [
            {
              text: '确定',
              handler: () => {
                this.receipt();
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
      if(flag == 'refuse'){
        let confirm = this.alertCtrl.create({
          title: '温馨提示',
          message: '你确定拒绝签收吗?',
          buttons: [
            {
              text: '确定',
              handler: () => {
                this.refuseSign();
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
      if(flag == 'ok'){
        this.nav.push(TakeSign,{obj:this.obj});
      }
      if(flag == 'end'){
        let confirm = this.alertCtrl.create({
          title: '温馨提示',
          message: '你确定进行收款确认吗?',
          buttons: [
            {
              text: '确定',
              handler: () => {
                this.end();
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
    }
    //在线支付、车辆征集   ：需要确认收款
    end(){
      this.http.post(this.endUrl,{id:this.obj.id},this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.helperService.presentToast('确认收款成功');
            this.nav.push(TakeList);          
          }else{
            this.helperService.presentToast(result.msg);
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
    }


    //月结---司机确认接单
    drivingAgree(){
      this.http.post(this.drivingAgreeUrl,{id:this.obj.id},this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.helperService.presentToast('确认接单成功');
            this.nav.push(TakeList);          
          }else{
            this.helperService.presentToast(result.msg);
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
    }
    //月结---司机确认拒绝接单
    refuseAgree(){
      this.http.post(this.refuseAgreeUrl,{id:this.obj.id},this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.helperService.presentToast('确认拒绝接单成功');
            this.nav.push(TakeList);          
          }else{
            this.helperService.presentToast(result.msg);
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
    }
    //收货确认
    receipt(){
      this.http.post(this.takeUrl,{id:this.obj.id},this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.helperService.presentToast('收货成功');
            this.nav.push(TakeList);          
          }else{
            this.helperService.presentToast(result.msg);
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
    }
    //拒绝签收
    refuseSign(){
      this.http.post(this.refuseUrl,{id:this.obj.id},this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.helperService.presentToast('拒绝签收成功');
            this.nav.push(TakeList);          
          } else {
            this.helperService.presentToast(result.msg);
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
    }


    doDetail(){
      switch (this.obj.orderType) {
        case 'FSL': this.nav.push(OrderDetail,{obj:this.obj}); break;
        case 'LTL': this.nav.push(OrderDetail,{obj:this.obj}); break;
        case 'BUY': this.nav.push(HelpMeBuyConfirmOrder,{obj:this.obj,bottomType:'detail'}); break;
        case 'SEND': this.nav.push(HelpMeSendConfirmOrder,{obj:this.obj,bottomType:'detail'}); break;
        
        default:
          break;
      }
    }
}
