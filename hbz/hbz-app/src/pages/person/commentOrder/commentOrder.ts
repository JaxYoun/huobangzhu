import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController} from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { DriverComment } from './driverComment';
@Component({
  selector: 'page-commentOrder',
  templateUrl: 'commentOrder.html'
})
export class CommentOrder {
    private queryUrl: string = "/api/rate/count";
    public helperService:HelperService;
    public title:string = '订单评价';
    public providerWaitCount = 0;
    public providerAlreadyCount = 0;
    public consignorWaitCount = 0;
    public consignorAlreadyCount = 0;

    public currentUser:any={};
    public driverStarLevel=1;
    public shipperStarLevel=1;
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

          this.storage.get("currentUser").then(
            (data)=>{
              this.currentUser = data;
              this.driverStarLevel = data.starLevel;
              this.shipperStarLevel = data.userStarLevel;
              if(this.currentUser.imageUrl == null || this.currentUser.imageUrl == ''){
                this.currentUser.imageUrl = 'assets/images/boy.png';
              }
            }
          );

          let paramObj = {type:'CONSIGNOR',star:'0'};
          this.initPage(paramObj, 'providerWaitCount');// 查询司机待评价数量
          let paramObj1 = {type:'CONSIGNOR',starGT:'0'};
          this.initPage(paramObj1, 'providerAlreadyCount');// 查询司机已评价数量
          let paramObj2 = {type:'PROVIDER',star:'0'};
          this.initPage(paramObj2, 'consignorWaitCount');// 查询货主待评价数量
          let paramObj3 = {type:'PROVIDER',starGT:'0'};
          this.initPage(paramObj3, 'consignorAlreadyCount');// 查询货主已评价数量
        }

  //初始化数据
  initPage(paramObj, paramCount){
    this.http.post(this.queryUrl,paramObj,this.helperService.getOptions())
      .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          console.log(paramCount + "=" +  result.data)
          switch (paramCount) {
            case 'providerWaitCount': this.providerWaitCount = result.data; break;
            case 'providerAlreadyCount': this.providerAlreadyCount = result.data; break;
            case 'consignorWaitCount': this.consignorWaitCount = result.data; break;
            case 'consignorAlreadyCount': this.consignorAlreadyCount = result.data; break;
            default:
              break;
          }
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      });
  }

    goto(type,hasComment) {
      this.nav.push(DriverComment,{type:type,hasComment:hasComment})
    }

}
