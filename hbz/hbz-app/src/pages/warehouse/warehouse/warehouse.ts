import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController} from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { WarehouseDetail } from './warehouseDetail';


@Component({
  selector: 'page-warehouse',
  templateUrl: 'warehouse.html'
})

export class Warehouse {
    private queryUrl: string = "/api/web/warehouse/getAvailableWarehouseListByPage";
    // private batchUrl: string = "/api/upload/queryFileByBatchId";
    // private attachUrl: string = "/api/upload/downloadFileById";
    public title:string = '仓储';
    public helperService:HelperService;
    public wareHouseList=[];
    public page=0;
    public flag:boolean=true;//控制下拉多次触发问题
    

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
          // this.initPage();
        }

        ionViewWillEnter(){
          this.initPage();
        }

    initPage(){
      this.wareHouseList = [];
      this.http.post(this.queryUrl,{page:this.page,size:this.config.pageSize},this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                this.wareHouseList.push(element);
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
      this.wareHouseList = [];
      this.http.post(this.queryUrl,{page:0,size:this.config.pageSize},this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                this.wareHouseList.push(element);
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
    this.http.post(this.queryUrl,{page:this.page,size:this.config.pageSize},this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          //等待2秒，2秒后执行
          setTimeout(()=> {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                this.wareHouseList.push(element);
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


    goToDetail(item){
      this.nav.push(WarehouseDetail,{obj:item});
    }


    // 联系地址选择
    onChangeAddressOrigin(res) {
      this.wareHouseList = [];
      this.http.post(this.queryUrl,{page:0,size:this.config.pageSize,originAreaId:res.id},this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                this.wareHouseList.push(element);
              });
            }
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
  }

}
