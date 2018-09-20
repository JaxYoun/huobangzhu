import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { SubmitComment } from './submitComment';
@Component({
  selector: 'page-driverComment',
  templateUrl: 'driverComment.html'
})
export class DriverComment {
    private queryUrl: string = "/api/rate/queryPage";
    public helperService:HelperService;
    public title:string = '';
    public type;
    public hasComment;

    public page=0;
    public myOrdersArray=[];
    public flag:boolean=true;//控制下拉多次触发问题
    public orderTypeObj={};
    public paramObj;

    constructor(
        public params: NavParams,
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
          this.type = params.get("type");
          this.hasComment = params.get("hasComment");

          // let paramObj;
          if(this.type == 'PROVIDER'){
            //司机方：评价货主
            if(this.hasComment == 'waitComment'){
              this.title = '评价货主-待评价';
              this.paramObj = {type:'PROVIDER',star:'0'};// 查询货主待评价数量
            }
            if(this.hasComment == 'alreadyComment'){
              this.title = '评价货主-已评价';
              this.paramObj = {type:'PROVIDER',starGT:'0'};// 查询货主已评价数量
            }
          }
          if(this.type == 'CONSIGNOR'){
            //货主方：评价司机
            if(this.hasComment == 'waitComment'){
              this.title = '评价司机-待评价';
              this.paramObj = {type:'CONSIGNOR',star:'0'};// 查询司机待评价数量
            }
            if(this.hasComment == 'alreadyComment'){
              this.title = '评价司机-已评价';
              this.paramObj = {type:'CONSIGNOR',starGT:'0'};// 查询司机已评价数量
            }
          }
          this.paramObj.page = this.page;
          this.paramObj.size = this.config.pageSize;
          this.storage.get("orderType").then(
            (data)=>{
                this.orderTypeObj = data;
                this.initPage();
            });
        }
        generatorData(element){
          //快递，取货地址、配送地址
          if(element.orderType == 'EX'){
           element.originLocation = this.helperService.buildAreaLocation(element.originArea.level1Name,element.originArea.level2Name,element.originArea.level3Name);
           element.originLocationInfo = element.originAddr;
           element.destLocation = this.helperService.buildAreaLocation(element.destArea.level1Name,element.destArea.level2Name,element.destArea.level3Name);
           element.destLocationInfo = element.destAddr;
         } else if(element.orderType == 'BUY'){
           element.destLocation = element.destInfo;
           element.destLocationInfo = element.destAddress;
         } else if(element.orderType == 'SEND'){
           element.destLocation = element.destAddress;
           element.destLocationInfo = element.destInfo;
           element.originLocation = element.originAddress;
           element.originLocationInfo = element.originInfo;
         } else {
           element.destLocation = this.helperService.buildAreaLocation(element.destArea.level1Name,element.destArea.level2Name,element.destArea.level3Name);
           element.destLocationInfo = element.destAddress;
           element.originLocation = this.helperService.buildAreaLocation(element.originArea.level1Name,element.originArea.level2Name,element.originArea.level3Name);
           element.originLocationInfo = element.originAddress;
         }
       }

    //初始化数据
    initPage(){
      this.http.post(this.queryUrl,this.paramObj,this.helperService.getOptions())
        .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                this.generatorData(element.order);
                element.order.orderTypeChi = this.helperService.getValueByJson(element.order.orderType, this.orderTypeObj);
                element.order.orderTrans = this.helperService.getOrderStatusChi(element.order.orderTrans);
                this.myOrdersArray.push(element);
              });
            }
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
    }

   //下拉刷新
   doRefresh(refresher) {
    this.myOrdersArray = [];
    this.paramObj.page = 0;
    this.http.post(this.queryUrl,this.paramObj,this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
              this.generatorData(element.order);
              element.order.orderTypeChi = this.helperService.getValueByJson(element.order.orderType, this.orderTypeObj);
              element.order.orderTrans = this.helperService.getOrderStatusChi(element.order.orderTrans);
              this.myOrdersArray.push(element);
            });
          }
          //复位下拉刷新
          refresher.complete();
          this.page = 0;
          this.flag = true;
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      });
  }


  //上拉加载
  doInfinite(infiniteScroll){
    //上拉加载前先为当前page++
    console.log(this.paramObj)
    this.page++;
    this.paramObj.page = this.page;
    this.http.post(this.queryUrl,this.paramObj,this.helperService.getOptions())
      .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          //等待2秒，2秒后执行
          setTimeout(()=> {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                this.generatorData(element.order);
                element.order.orderTypeChi = this.helperService.getValueByJson(element.order.orderType, this.orderTypeObj);
                element.order.orderTrans = this.helperService.getOrderStatusChi(element.order.orderTrans);
                this.myOrdersArray.push(element);
              });
            } else {
              if(this.flag){
                this.helperService.presentToast('没有更多数据');
                this.flag = false;
              }
            }
            infiniteScroll.complete();
          }, this.config.scrollWaitTime);

        } else {
          this.helperService.presentToast('数据初始化失败：' + result.msg);
          infiniteScroll.complete();
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        infiniteScroll.complete();
      });
  }


    goDetail(myComment) {
      this.nav.push(SubmitComment,{myComment:myComment,title:this.title})
    }
}
