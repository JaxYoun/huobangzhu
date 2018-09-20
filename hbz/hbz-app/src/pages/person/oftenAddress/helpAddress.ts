import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { HelpModals } from './helpModals';


@Component({
  templateUrl: 'helpAddress.html'
})

export class HelpAddress {
    // private addAddressUrl: string = "/api/user/app/link/create";
    private queryAddressUrl: string = "/api/user/app/link/queryPage";
    // private editAddressUrl: string = "/api/user/app/link/update";
    private delAddressUrl: string = "/api/user/app/link/delete";
    public title:string = '配送地址列表';
    public helperService:HelperService;
    public items=[];
    public page=0;
    public flag:boolean=true;//控制下拉多次触发问题
   
    public typeObj={2: "收货地址", 1: "发货地址", all:"请选择" };
    public typeSelect="all";
    public paramsObj;
    public addresType;
    

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
          this.queryAddressUrl = this.helperService.api + this.queryAddressUrl;
          this.delAddressUrl = this.helperService.api + this.delAddressUrl;

          this.addresType = params.get('addresType');
          this.paramsObj = {};
          this.paramsObj.orderType = this.addresType;
          if(this.addresType == 'BUY'){
            this.title = '配送地址列表';
          }
          if(this.addresType == 'EX'){
            this.title = '快递地址列表';
          }
          this.initPage();
        }

        // 配送使用  this.paramsObj.orderType = 'BUY';
        // 快递使用  this.paramsObj.orderType = 'EX';
        
    initPage(){
      this.paramsObj = {}
      this.paramsObj.orderType = this.addresType;
      if(this.typeSelect != 'all'){
       this.paramsObj.usein = this.typeSelect;
      }
      this.paramsObj.page = 0;
      this.paramsObj.size = this.config.pageSize;

      this.http.post(this.queryAddressUrl,this.paramsObj,this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                element.location = this.helperService.buildAreaLocation(element.area.level1Name,element.area.level2Name,element.area.level3Name);
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
      this.paramsObj = {}
      this.paramsObj.orderType = this.addresType;
      if(this.typeSelect != 'all'){
        this.paramsObj.usein = this.typeSelect;
      }
      this.paramsObj.page = 0;
      this.paramsObj.size = this.config.pageSize;
      
      this.http.post(this.queryAddressUrl,this.paramsObj,this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                element.location = this.helperService.buildAreaLocation(element.area.level1Name,element.area.level2Name,element.area.level3Name);
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
    this.paramsObj = {}
    this.paramsObj.orderType = this.addresType;
    if(this.typeSelect != 'all'){
     this.paramsObj.usein = this.typeSelect;
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
                element.location = this.helperService.buildAreaLocation(element.area.level1Name,element.area.level2Name,element.area.level3Name);
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
    this.paramsObj = {}
    this.paramsObj.orderType = this.addresType;
    if(this.typeSelect != 'all'){
     this.paramsObj.usein = this.typeSelect;
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
              element.location = this.helperService.buildAreaLocation(element.area.level1Name,element.area.level2Name,element.area.level3Name);
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
      type['addresType'] = this.addresType;
      if(addr){
        type['addr'] = addr;
        type['type'] = addr.usein == 1 ? 'deliver' : 'pickUp';
      } else {
          if(this.typeSelect == 'all'){
            this.helperService.presentToast('请选择地址类型');
            return;
          }
      }
      if(this.typeSelect == '1'){
        type['type'] = 'deliver';
      }
      if(this.typeSelect == '2'){
        type['type'] = 'pickUp';
      }
     
     let modal = this.modalCtrl.create(HelpModals, type);
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
