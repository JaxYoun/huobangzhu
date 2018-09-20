import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import {Http}from '@angular/http';
import { HelperService } from '../../app/services/help.service';
import { Fulltransport } from '../shipper/fulltransport/index';
import { Config } from '../../app/config';
import { SelectCarByOrders } from "../shipper/selectCarByOrders";
import { FindCarDetail } from "./findCarDetail";
import { HelpMeBuy } from '../shipper/helpMeBuy/helpMeBuy';
import { HelpMeSend } from '../shipper/helpMeSend/helpMeSend';
import { SendGoods } from '../shipper/sendGoods';
import { LoginPage } from '../login/login';
import { SendList } from '../person/sendOrder/sendList';
import { DriverComment } from '../person/commentOrder/driverComment';
import { FindCars } from './findCar/findCars';
import { Person } from '../person/auth/person';

@Component({
  selector: 'page-shipper',
  templateUrl: 'shipper.html'
})
export class ShipperPage {
  public helperService:HelperService;
  public hasRole:boolean=false;

  
  public queryUrl: string = "/api/biz/consignor/statics";
  public obj = {
    transport_count:0,   //待送达
    wait_paymentonline:0,//待支付
    wait_receive:0,     //待确认收货
    waite_rate:0        //待评论
  } 

  constructor(
    private nav:NavController,
    public alertCtrl: AlertController,
    public loadingCtrl: LoadingController,
    public toastCtrl: ToastController,
    public platform:Platform,
    private http:Http,
    public storage: Storage,
    public config:Config) {
      this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
      this.queryUrl = this.helperService.api + this.queryUrl;
      
      this.storage.get("currentRoles").then(
        (data) => {
              // {role: "EnterpriseConsignor", roleName: "货主企业", id: 1, createdDate: "1970-01-01"}
              // {role: "EnterpriseAdmin", roleName: "运输企业", id: 2, createdDate: "1970-01-01"}
              // {role: "Consignor", roleName: "个人货主", id: 3, createdDate: "1970-01-01"}
              // {role: "PersionDriver", roleName: "司机", id: 4, createdDate: "1970-01-01"}
              // {role: "DeliveryBoy", roleName: "配送员", id: 5, createdDate: "1970-01-01"}
              // {role: "EnterpriseAssist", roleName: "企业助理", id: 6, createdDate: "1970-01-01"}
              // 权限判断： 【货主企业、个人货主】 角色用户  拥有导航我是货主的所有权限
              // 权限判断： 【运输企业、司机、配送员、企业助理】 角色用户   拥有导航我是司机的所有权限
              data.forEach(
                (element)=>{
                  if(element.role == 'EnterpriseConsignor' || element.role == 'Consignor'){
                    this.hasRole = true;
                    return;
                  }
                }
              )
          }
        );

    }

    queryStatics(){
      this.http.post(this.queryUrl,{},this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.obj = result.data;
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
    }

    ionViewWillEnter(){
      this.queryStatics();
    }

    goToAnyWhere(nali, type, orderTrans) {
      if(this.hasRole  == false){
        let confirm = this.alertCtrl.create({
            title: '你没有此权限！',
            message: '你确定跳转到认证页面吗?',
            buttons: [
              {
                text: '确定',
                handler: () => {
                  this.nav.push(Person);                  
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
          return;
      }

      switch (nali) {
        case 'Fulltransport':
          this.nav.push(Fulltransport,{type:type});
          break;
        case 'SelectCarByOrders':
          this.nav.push(SelectCarByOrders);
          break;
        case 'FindCarDetail':
          this.nav.push(FindCarDetail);
          break;
        case 'FindCars':
          this.nav.push(FindCars);
          break;
        case 'HelpMeBuy':
          this.nav.push(HelpMeBuy);
          break;
        case 'HelpMeSend':
          this.nav.push(HelpMeSend);
          break;
        case 'SendGoods':
          this.nav.push(SendGoods);
          break;
        case 'LoginPage':
          this.storage.set("token", null);
          this.nav.push(LoginPage);
          break;
        case 'DriverComment':
          this.nav.push(DriverComment,{hasComment:'waitComment', type:'CONSIGNOR'});
          break;
        case 'SendList':
          // 'WAIT_TO_CONFIRM'://待确认
          // 'TRANSPORT'://配送中
          // 'WAIT_FOR_PAYMENT'://待付款
          this.nav.push(SendList,{orderTrans:orderTrans});
          break;
        default:
          break;
      }
    }

    
}
