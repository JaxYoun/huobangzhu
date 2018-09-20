import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import {LeaseOrderDetail} from './leaseOrderDetail';

@Component({
  selector: 'leaseList',
  templateUrl: 'leaseList.html',
})
export class LeaseList {
  private queryMyOrderUrl: string = "/api/web/warehouse/getMyWarehouseEarnestOrderByPage";
  private orderDetalUrl: string = "/api/web/warehouse/getWarehouseEarnestOrderDetail";
  public helperService:HelperService;
  public myOrdersArray=[];
  public obj;
  public page=0;
  public flag:boolean=true;//控制下拉多次触发问题

  public payProgressObj={};
  public payProgressSelect="all";
  public paramsObj;
  

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
        this.queryMyOrderUrl = this.helperService.api + this.queryMyOrderUrl;
        this.orderDetalUrl = this.helperService.api + this.orderDetalUrl;
        this.storage.get("warehouseEarnestPayStatusEnum").then((data)=>{
          this.payProgressObj = data;
          //查询条件默认值
          this.payProgressObj['all'] = '请选择';
          console.log(this.payProgressObj)
          this.initPage();
        })
        
  }

  //初始化数据
  initPage(){
    this.paramsObj = {};
    if(this.payProgressSelect != 'all'){
     this.paramsObj.payStatus = this.payProgressSelect;
    }
    this.paramsObj.page = 0;
    this.paramsObj.size = this.config.pageSize;

    this.http.post(this.queryMyOrderUrl,this.paramsObj,this.helperService.getOptions())
      .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
              element.payStatusChi = this.helperService.getValueByJson(element.payStatus, this.payProgressObj);
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

    this.paramsObj = {};
    if(this.payProgressSelect != 'all'){
     this.paramsObj.payStatus = this.payProgressSelect;
    }
    this.paramsObj.page = 0;
    this.paramsObj.size = this.config.pageSize;

    this.http.post(this.queryMyOrderUrl,this.paramsObj,this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
              element.payStatusChi = this.helperService.getValueByJson(element.payStatus, this.payProgressObj);
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
    this.page++;

    this.paramsObj = {};
    if(this.payProgressSelect != 'all'){
     this.paramsObj.payStatus = this.payProgressSelect;
    }
    this.paramsObj.page = this.page;
    this.paramsObj.size = this.config.pageSize;

    this.http.post(this.queryMyOrderUrl,this.paramsObj,this.helperService.getOptions())
      .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          //等待2秒，2秒后执行
          setTimeout(()=> {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                element.payStatusChi = this.helperService.getValueByJson(element.payStatus, this.payProgressObj);
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
  

  //查询订单详情
  goDetail(myOrder){
    this.nav.push(LeaseOrderDetail,{obj: myOrder});
  }


  onSelectChange(){
    //查询，将集合清空，重新查询添加
    this.myOrdersArray = [];
    //重新查询分页始终从第一也开始
    this.page = 0;
    this.paramsObj = {};
    if(this.payProgressSelect != 'all'){
     this.paramsObj.payStatus = this.payProgressSelect;
    }
    this.paramsObj.page = this.page;
    this.paramsObj.size = this.config.pageSize;
     this.http.post(this.queryMyOrderUrl,this.paramsObj,this.helperService.getOptions())
     .subscribe((data)=> {
       let result = data.json();
       if (result.code == '200') {
         console.log(result.data)
         if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
              element.payStatusChi = this.helperService.getValueByJson(element.payStatus, this.payProgressObj);
              this.myOrdersArray.push(element);
            });
         }
       }
     },
     (err)=> {
       this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
     });
   }

}
