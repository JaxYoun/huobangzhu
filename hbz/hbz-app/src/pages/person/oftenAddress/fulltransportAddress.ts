import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController} from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { FullModals } from './modals';


@Component({
  templateUrl: 'fulltransportAddress.html'
})

export class FulltransportAddress {
    // private addAddressUrl: string = "/api/user/app/address/create";
    private queryAddressUrl: string = "/api/user/app/address/queryPage";
    // private editAddressUrl: string = "/api/user/app/address/update";
    private delAddressUrl: string = "/api/user/app/address/delete";
    public title:string = '专线地址列表';
    public helperService:HelperService;
    public items=[];
    public page=0;
    public flag:boolean=true;//控制下拉多次触发问题
   
    public typeObj={1: "收货地址", 2: "发货地址", all:"请选择" };
    public typeSelect="all";
    public paramsObj;
    public areaJsonArray;
    
    

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
          // this.addAddressUrl = this.helperService.api + this.addAddressUrl;
          this.queryAddressUrl = this.helperService.api + this.queryAddressUrl;
          // this.editAddressUrl = this.helperService.api + this.editAddressUrl;
          this.delAddressUrl = this.helperService.api + this.delAddressUrl;
          this.storage.get("areaJson").then(
            (data)=>{
              this.areaJsonArray = data;
            });
          this.initPage();
        }

    initPage(){
      this.paramsObj = {};
      if(this.typeSelect != 'all'){
       this.paramsObj.type = this.typeSelect;
      }
      this.paramsObj.page = 0;
      this.paramsObj.size = this.config.pageSize;

      this.http.post(this.queryAddressUrl,this.paramsObj,this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                console.log(element)
                this.items.push(element);
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
      this.items = [];
      this.paramsObj = {};
      if(this.typeSelect != 'all'){
        this.paramsObj.type = this.typeSelect;
      }
      this.paramsObj.page = 0;
      this.paramsObj.size = this.config.pageSize;
      this.http.post(this.queryAddressUrl,this.paramsObj,this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                this.items.push(element);
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
    if(this.typeSelect != 'all'){
     this.paramsObj.type = this.typeSelect;
    }
    this.paramsObj.page = this.page;
    this.paramsObj.size = this.config.pageSize;
    this.http.post(this.queryAddressUrl,this.paramsObj,this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          //等待2秒，2秒后执行
          setTimeout(()=> {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                this.items.push(element);
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


  onSelectChange(){
    //查询，将集合清空，重新查询添加
    this.items = [];
    //重新查询分页始终从第一也开始
    this.page = 0;
    this.paramsObj = {};
    if(this.typeSelect != 'all'){
     this.paramsObj.type = this.typeSelect;
    }
    this.paramsObj.page = this.page;
    this.paramsObj.size = this.config.pageSize;
    this.http.post(this.queryAddressUrl,this.paramsObj,this.helperService.getOptions())
    .subscribe((data)=> {
       let result = data.json();
       if (result.code == '200') {
         console.log(result.data)
         if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
              this.items.push(element);
            });
         }
       }
     },
     (err)=> {
       this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
     });
   }


   addAddress(type, addr){
     // public typeObj={1: "收货地址pickUp", 2: "发货地址deliver", all:"请选择" };
     if(addr){
      type['addr'] = addr;
      type['type'] = addr.type == 1 ? 'pickUp' : 'deliver';
    } else {
      if(this.typeSelect == 'all'){
       this.helperService.presentToast('请选择地址类型');
       return;
     }
    }
     if(this.typeSelect == '1'){
       type['type'] = 'pickUp';
      }
      if(this.typeSelect == '2'){
        type['type'] = 'deliver';
      }
      type['areaJsonArray'] = this.areaJsonArray;
      
      let modal = this.modalCtrl.create(FullModals, type);
      modal.onDidDismiss((item) => {
            this.items = [];
            this.initPage();
      });
      modal.present();
   }

   delAddress(addr){
    let confirm = this.alertCtrl.create({
      title: '温馨提示',
      message: '你确定删除吗?',
      buttons: [
        {
          text: '确定',
          handler: () => {
            this.http.post(this.delAddressUrl,{id:addr.id},this.helperService.getOptions())
            .subscribe((data)=> {
               let result = data.json();
               if (result.code == '200') {
                 this.helperService.presentToast('删除成功');
                 this.items = [];
                 this.initPage();
               }
             },
             (err)=> {
               this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
             });
          }
        },
        {
          text: '取消',
          handler: () => {
          }
        }
      ]
    });
    confirm.present();
  }
}