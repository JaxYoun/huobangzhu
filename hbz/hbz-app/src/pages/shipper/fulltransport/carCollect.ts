import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController ,NavParams} from 'ionic-angular';
import { Storage } from '@ionic/storage';
import {TabsPage} from '../tabs/tabs';
import {Http}from '@angular/http';
import { HelperService } from '../../../app/services/help.service';
import { PayOnline } from './payOnline';
import { Config } from '../../../app/config';
import { SendList } from '../../person/sendOrder/sendList';
import { Wallet } from '../../person/wallet/wallet';

@Component({
  selector: 'carcollect',
  templateUrl: 'carcollect.html'
})
export class CarCollect {
  public helperService:HelperService;
  public orderId;
  public orderNo;
  public amount;
  public settlementType;
  private confirmUrl: string = "/api/order/tender/create";//对车辆征集订单添加条件
  private queryUrl: string = "/api/order/tender/get";//对车辆征集订单查询

  public driverSelect='3';//司机类型
  // public driverObj={    "val" : "3",    "name" : "不限",   "type" : "Driver",  "id" : 1};
  public driverList:Array<{}> = new Array;
  public registryMoneySelect='0';//注册资金要求
  // public registryMoneyObj={"val" : "0",    "name" : "不限",  "type" : "Registered_funds",    "id" : 4}
  public registryMoneyList:Array<{}> = new Array;
  public bondSelect='0';//保证金额度
  // public bondObj={ "val" : "0",  "name" : "不限", "type" : "Security_deposit", "id" : 17};
  public bondList:Array<{}> = new Array;
  public needSelect='0';//信誉等级
  // public needObj={{    "val" : "0",    "name" : "不限", "type" : "Credit_level",  "id" : 11};
  public needList:Array<{}> = new Array;
  public typeName = "";
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
      this.confirmUrl = this.helperService.api + this.confirmUrl;
      this.queryUrl = this.helperService.api + this.queryUrl;
      this.orderId = params.get('orderId');
      this.orderNo = params.get('orderNo');
      this.amount = params.get('amount');
      this.settlementType = params.get('settlementType');
      let orderType = params.get('orderType');
      if(orderType == 'FSL'){
        this.typeName = "专线运输-整车";
      }
      if(orderType == 'LTL'){
        this.typeName = "专线运输-零担";
      }

      //编辑页面进行回填  driverSelect:,registryMoneySelect：bondSelect，needSelect
        this.http.post(this.queryUrl,{orderId:this.orderId},this.helperService.getOptions())
        .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
              this.driverSelect = result.data.need;
              this.registryMoneySelect = result.data.registryMoney;
              this.bondSelect = result.data.bond;
              this.needSelect = result.data.starLevel;
            }
          },
          (err)=> {
            this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
          });

      this.storage.get('driverType').then((data)=>this.driverList=data);
      this.storage.get('registeredFunds').then((data)=>this.registryMoneyList=data);
      this.storage.get('creditLevel').then((data)=>this.needList=data);
      this.storage.get('securityDeposit').then((data)=>this.bondList=data);
      
    }
    
    confirm() {
      this.http.post(this.confirmUrl,
         {
           orderId:this.orderId,
           registryMoney:this.registryMoneySelect,
           starLevel:this.needSelect,
           bond:this.bondSelect,
           need:this.driverSelect
        }
        , this.helperService.getOptions())
        .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
                //  //"在线支付"
                //  this.nav.push(PayOnline,{obj: {
                //   orderId:this.orderId,
                //   orderNo:this.orderNo,
                //   amount:this.amount,
                //   orderType:this.typeName}})
                this.nav.push(SendList);
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
