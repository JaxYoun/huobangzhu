import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController,NavParams } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import {Http}from '@angular/http';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { SelectCarDriver } from './index';
import { OrderDetail } from '../../shipper/fulltransport/orderDetail';
import { Wallet } from '../../person/wallet/wallet';


@Component({
  templateUrl: 'selectCarDetail.html'
})
export class SelectCarDetail {
  public helperService:HelperService;
  private takeCreateOrderUrl: string = "/api/order/taker/create";//司机接受车辆征集
  private orderDetalUrl: string = "/api/order/get";//订单详情

  private queryUrl: string = "/api/order/tender/get";//对车辆征集订单查询
  private queryConsignorUrl: string = "/api/order/taker/consignor/query";//对车辆征集列表查询


  public obj:any={};
  public id;
  public driverSelect;
  public registryMoneySelect;
  public bondSelect;
  public needSelect;
  //参与司机
  public joinDriversArray = [];
  public joinDriversArrayLength=0;
  
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
      this.takeCreateOrderUrl = this.helperService.api + this.takeCreateOrderUrl;
      this.orderDetalUrl = this.helperService.api + this.orderDetalUrl;
      this.queryUrl = this.helperService.api + this.queryUrl;
      this.queryConsignorUrl = this.helperService.api + this.queryConsignorUrl;
      this.id = params.get("id");
      //查询订单详情
      this.http.post(this.orderDetalUrl,{id:this.id},this.helperService.getOptions())
        .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.obj = result.data;
            this.storage.get("transType").then(
              (data)=>{
                this.obj.transType = this.helperService.getValueByJson(this.obj.transType, data);
              });
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });


      //查询车辆征集条件
      this.http.post(this.queryUrl,{orderId:this.id},this.helperService.getOptions())
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

        //查询参与司机
        this.http.post(this.queryConsignorUrl,{orderId:this.id},this.helperService.getOptions())
        .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
              if(result.data.length !=0 ){
                this.joinDriversArray = result.data;
                this.joinDriversArrayLength = result.data.length;
              }
            }
          },
          (err)=> {
            this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
          });
    }

    doDetail(){
      this.nav.push(OrderDetail,{obj: this.obj})
    }

    //接受车辆征集
    takeCreateOrder(){

      let alert = this.alertCtrl.create({
        title: '参与征集-报价',
        inputs: [
          {
            name: 'offer',
            placeholder: '报价',
            type:'number'
          }
        ],
        buttons: [
          {
            text: '取消',
            role: 'cancel',
            handler: data => {
              console.log('Cancel clicked');
            }
          },
          {
            text: '保存',
            handler: data => {
              if(data.offer != ''){
                this.http.post(this.takeCreateOrderUrl,{orderId:this.id, offer:data.offer},this.helperService.getOptions())
                .subscribe((data)=> {
                    let result = data.json();
                    if (result.code == '200') {
                      this.helperService.presentToast("征集成功");
                      this.nav.push(SelectCarDriver);
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
              } else{
                this.helperService.presentToast('报价不能为空');
              }
            }
          }
        ]
      });
      alert.present();


      
    }
}
