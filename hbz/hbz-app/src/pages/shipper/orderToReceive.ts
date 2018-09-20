import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController ,NavParams} from 'ionic-angular';
import { Storage } from '@ionic/storage';
import {Http}from '@angular/http';
import { HelperService } from '../../app/services/help.service';
import { OrderDetail } from '../shipper/fulltransport/orderDetail';
import { Config } from '../../app/config';
import { SelectDriver } from "./selectDriver";
@Component({
  selector: 'orderToReceive',
  templateUrl: 'orderToReceive.html'
})
export class OrderToReceive {
  public helperService:HelperService;
  private orderDetalUrl: string = "/api/order/get";//订单详情
  private queryUrl: string = "/api/order/tender/get";//对车辆征集订单查询
  private queryConsignorUrl: string = "/api/order/taker/consignor/query";//对车辆征集列表查询

  public obj:any={};
  public orderId;

  public driverSelect;
  public registryMoneySelect;
  public bondSelect;
  public needSelect;

  //参与司机
  public joinDriversArray = [];
  public joinDriversArrayLength=0;

  //查询变量
  public flag="";

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
      this.flag = params.get("flag");
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

          //查询参与司机
          this.http.post(this.queryConsignorUrl,{orderId:this.orderId},this.helperService.getOptions())
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

    selectDriver() {
      if(this.joinDriversArray.length > 0){
        this.nav.push(SelectDriver,{drivers:this.joinDriversArray});
      }
    }

    doDetail(){
      this.nav.push(OrderDetail,{obj: this.obj})
    }
}
