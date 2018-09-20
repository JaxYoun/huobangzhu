import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../app/services/help.service';
import { FullCarry} from './fulltransport/fullCarry';
import { SelectCarDriver} from '../driver/selectCar/index';
import { HelpMeBuyTake} from '../driver/helpMeBuy/helpMeBuyTake';
import { FindGoods} from '../driver/findGoods/findGoods';
import { HelpMeSendTake} from '../driver/helpMeSend/helpMeSendTake';
import { SpecialLine } from '../driver/specialLine/specialLine';

import { Config } from '../../app/config';
import { DriverComment } from '../person/commentOrder/driverComment';
import { TakeList } from '../person/takeOrder/takeList';
import { Person } from '../person/auth/person';

@Component({
  selector: 'page-driver',
  templateUrl: 'driver.html'
})
export class Driver {
  
  public helperService:HelperService;
  public hasRole:boolean=false;
  public hasRoleDeliveryBoy:boolean=false;
  public queryUrl: string = "/api/biz/provider/statics";
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
        public modalCtrl: ModalController,
        public platform:Platform,
        private http: Http,
        public storage: Storage,
        public config:Config) {
          this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
          this.queryUrl = this.helperService.api + this.queryUrl;

          this.storage.get("currentRoles").then(
            (data) => {
                  // {role: "EnterpriseConsignor", roleName: "货主企业", id: 1, createdDate: "1970-01-01"}
                  // {role: "EnterpriseAdmin", roleName: "运输企业", id: 2, createdDate: "1970-01-01"}
                  // {role: "Consignor", roleName: "个人货主", id: 3, createdDate: "1970-01-01"}
                  // {role: "PersonDriver", roleName: "司机", id: 4, createdDate: "1970-01-01"}
                  // {role: "DeliveryBoy", roleName: "配送员", id: 5, createdDate: "1970-01-01"}
                  // {role: "EnterpriseAssist", roleName: "企业助理", id: 6, createdDate: "1970-01-01"}
                  // 权限判断： 【货主企业、个人货主】 角色用户  拥有导航我是货主的所有权限
                  // 权限判断： 【运输企业、司机】 角色用户   拥有导航我是司机的所有权限
                  // 权限判断： 【配送员】 角色用户   部分权限，根据excel表进行操作的
                  data.forEach(
                    (element)=>{
                      if(element.role == 'EnterpriseAdmin' || element.role == 'PersonDriver'){
                        this.hasRole = true;
                        return;
                      }
                      if(element.role == 'DeliveryBoy'){
                        this.hasRole = false;
                        this.hasRoleDeliveryBoy = true;
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

        goToAnyWhere(nali,orderType,orderTrans) {

          if(this.hasRole == false){
            if(this.hasRoleDeliveryBoy == false){
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
              else {
                //配送员： 参与征集\零担-接单\整车-接单\专线发布 这几块没有权限
                if(nali == 'SelectCarDriver' || nali == 'FullCarry' || nali == 'SpecialLine' || nali=='FindGoods'){
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
              }
            }

          
            switch (nali) {
              case 'FullCarry':
                  this.nav.push(FullCarry, {orderType:orderType});
                break;
              case 'SelectCarDriver':
                this.nav.push(SelectCarDriver);
                break;
              case 'FindGoods':
                this.nav.push(FindGoods);
                break;
              case 'HelpMeBuyTake':
                this.nav.push(HelpMeBuyTake);
                break;
              case 'HelpMeSendTake':
                this.nav.push(HelpMeSendTake);
                break;
              case 'SpecialLine':
                this.nav.push(SpecialLine);
                break;
              case 'DriverComment':
                this.nav.push(DriverComment,{hasComment:'waitComment', type:'PROVIDER'});
                break;
              case 'TakeList':
                // 'WAIT_TO_CONFIRM'://待确认
                // 'TRANSPORT'://配送中
                // 'WAIT_FOR_PAYMENT'://待付款
                this.nav.push(TakeList,{orderTrans:orderTrans});
                break;
              default:
                break;
            }

          }


}
