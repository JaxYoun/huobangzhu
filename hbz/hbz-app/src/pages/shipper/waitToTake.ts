import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController ,NavParams} from 'ionic-angular';
import { Storage } from '@ionic/storage';
import {Http}from '@angular/http';
import { HelperService } from '../../app/services/help.service';
import { OrderDetail } from '../shipper/fulltransport/orderDetail';
import { Config } from '../../app/config';
import { PayOnline } from './fulltransport/payOnline';
import { SendList } from '../person/sendOrder/sendList';

@Component({
  selector: 'waitToTake',
  templateUrl: 'waitToTake.html'
})
export class WaitToTake {
  public helperService:HelperService;
  private orderDetalUrl: string = "/api/order/get";//订单详情
  private queryUrl: string = "/api/order/tender/get";//对车辆征集订单查询
  private queryConsignorUrl: string = "/api/order/taker/consignor/query";//对车辆征集列表查询
  public receiveUrl="";
  public fslReceiveUrl: string = "/api/order/fsl/receive"; // 整车货主确认收货
  public ltlReceiveUrl: string = "/api/order/ltl/receive"; // 零担货主确认收货
  
  public orderId;

  public driverSelect;
  public registryMoneySelect;
  public bondSelect;
  public needSelect;
  public obj:any={};
  //承运方信息
  public orgName="";
  public nickName;
  public telephone;
  public starLevel;
  public isJoinSuccess:boolean=false;

  public settlementTypeObj={"online":"线上支付","ondown":"线下支付"};
  public settlementTypeSelect="online";//  "online" : "线上支付",  "ondown" : "线下支付"
  public typeName;//零担、整车
  
  

  constructor(
    public params: NavParams,
    private nav:NavController,
    public alertCtrl: AlertController,
    public loadingCtrl: LoadingController,
    public toastCtrl: ToastController,
    public platform:Platform,
    private http:Http,
    public storage: Storage,
    public config:Config) {
      this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
      this.orderDetalUrl = this.helperService.api + this.orderDetalUrl;
      this.queryUrl = this.helperService.api + this.queryUrl;
      this.queryConsignorUrl = this.helperService.api + this.queryConsignorUrl;

      
      this.obj = params.get("obj");


      if(this.obj.orderType == 'FSL'){
        this.receiveUrl = this.helperService.api + this.fslReceiveUrl;
        this.typeName = "专线运输-整车";
      }
      if(this.obj.orderType == 'LTL'){
        this.typeName = "专线运输-零担";
        this.receiveUrl = this.helperService.api + this.ltlReceiveUrl;
      }

      this.orderId = this.obj.id;
      this.storage.get("transType").then(
      (data)=>{
        this.obj.transType = this.helperService.getValueByJson(this.obj.transType, data);
      });
          //查询车辆征集条件
          this.http.post(this.queryUrl,{orderId:this.orderId},this.helperService.getOptions())
          .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
              this.storage.get("driverType").then(
                (data)=>{
                  data.forEach(element => {
                    if(element.val == result.data.need){
                      this.driverSelect = element.name; 
                      }
                    });
                  });
                  this.storage.get("registeredFunds").then(
                    (data)=>{
                      data.forEach(element => {
                        if(element.val == result.data.registryMoney){
                          this.registryMoneySelect = element.name; 
                        }
                      });
                    });
                this.storage.get("creditLevel").then(
                  (data)=>{
                    data.forEach(element => {
                      if(element.val == result.data.starLevel){
                        this.needSelect = element.name; 
                      }
                    });
                  });
                  this.storage.get("securityDeposit").then(
                    (data)=>{
                      data.forEach(element => {
                        if(element.val == result.data.bond){
                          this.bondSelect = element.name; 
                        }
                      });
                    });
                  }
                },
                (err)=> {
                  this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
                });

              //查询承运方信息  ===》 承运方信息从列表中takeUser获取

            
              console.log(this.obj)

              this.orgName = this.obj.takeUser.entOrganizationName;
              this.nickName = this.obj.takeUser.nickName;
              this.telephone = this.obj.takeUser.telephone;
              this.starLevel = this.obj.takeUser.starLevel;

              this.storage.get("loginName").then(data=>{
                if(this.obj.createUser.login == data){
                  this.isJoinSuccess = false;
                } else{
                  //参与人不是发布人要显示是否被选中
                  if(this.telephone != data){
                    this.isJoinSuccess = true;
                  }
                }
              });
              // this.http.post(this.queryConsignorUrl,{orderId:this.orderId},this.helperService.getOptions())
              // .subscribe((data)=> {
              //     let result = data.json();
              //     if (result.code == '200') {
              //       if(result.data.length !=0 ){
              //         if(result.data[0].user.orgName){
              //           this.orgName = result.data[0].user.orgName;
              //         }
              //         this.nickName = result.data[0].user.nickName;
              //         this.telephone = result.data[0].user.telephone;
              //         this.starLevel = result.data[0].user.starLevel;
              //         this.storage.get("loginName").then(data=>{
              //           if(this.telephone == data){
              //             this.isJoinSuccess = true;
              //           }
              //         })
              //       }
              //     }
              //   },
              //   (err)=> {
              //     this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
              //   });
            }

     doDetail(){
      this.nav.push(OrderDetail,{obj: this.obj})
     }


     doConfirm(){
       // 1、线上支付2、线下支付
       let paySelectionNum = this.settlementTypeSelect == 'online' ? 1 : 2;
        this.http.post(this.receiveUrl,{id:this.obj.id,paySelection:paySelectionNum},this.helperService.getOptions())
        .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
              this.helperService.presentToast('确认收件成功');
              if(paySelectionNum == 1){
                //"在线支付"
                this.nav.push(PayOnline,{obj: {
                  orderId:this.obj.orderId,
                  orderNo:this.obj.orderNo,
                  amount:this.obj.amount,
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

    //  doConfirm(){
      // if (this.settlementTypeSelect == 'online' ) {
        //线上支付
        //   let alert = this.alertCtrl.create({
        //     title: '填写支付金额',
        //     inputs: [
        //       {
        //         name: 'offer',
        //         placeholder: '金额',
        //         type:'number'
        //       }
        //     ],
        //     buttons: [
        //       {
        //         text: '取消',
        //         role: 'cancel',
        //         handler: data => {
        //           console.log('Cancel clicked');
        //         }
        //       },
        //       {
        //         text: '保存',
        //         handler: data => {
        //           let amount = data.offer;
        //           if(amount != ''){
        //             // 1、线上支付2、线下支付
        //             this.http.post(this.receiveUrl,{id:this.obj.id,paySelection:1,amount:amount},this.helperService.getOptions())
        //             .subscribe((data)=> {
        //                 let result = data.json();
        //                 if (result.code == '200') {
        //                  this.helperService.presentToast('确认收件成功');
        //                     //"在线支付"
        //                     this.nav.push(PayOnline,{obj: {
        //                       orderId:this.obj.orderId,
        //                       orderNo:this.obj.orderNo,
        //                       amount:amount,
        //                       orderType:this.typeName}})
        //                 }else{
        //                   this.helperService.presentToast(result.msg);
        //                 }
        //               },
        //               (err)=> {
        //                 this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        //               });

        //           } else{
        //             this.helperService.presentToast('金额不能为空');
        //           }
        //         }
        //       }
        //     ]
        //   });
        //   alert.present();
        // } else {
        //   //线下支付
        //    this.http.post(this.receiveUrl,{id:this.obj.id,paySelection:2},this.helperService.getOptions())
        //    .subscribe((data)=> {
        //        let result = data.json();
        //        if (result.code == '200') {
        //         this.helperService.presentToast('确认送达成功');
        //         this.nav.push(SendList);
        //        }else{
        //          this.helperService.presentToast(result.msg);
        //        }
        //      },
        //      (err)=> {
        //        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        //      });
        // }
    //  }
  // }
}