import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { ProductDetail } from './productDetail';

@Component({
  selector: 'page-productOfType',
  templateUrl: 'productOfType.html'
})

export class ProductOfType {
    public title:string = '分类名称';
    public helperService:HelperService;
    private queryUrl: string = "/api/product/queryPage";//	查询商品
    public productArray=[];
    public page=0;
    public flag:boolean=true;//控制下拉多次触发问题
    

    constructor(
        private nav:NavController,
        public params: NavParams,
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
          this.title = params.get('title');
          this.initPage();
          
      }


      initPage(){
        this.http.post(this.queryUrl,{page:this.page,size:this.config.pageSize,typeId:this.params.get("typeId")},this.helperService.getOptions())
        .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
              if(result.data.content.length > 0){
                result.data.content.forEach(element =>{
                  this.productArray.push(element);
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
        this.productArray = [];
        this.http.post(this.queryUrl,{page:0,size:this.config.pageSize,typeId:this.params.get("typeId")},this.helperService.getOptions())
          .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
              if(result.data.content.length > 0){
                result.data.content.forEach(element =>{
                  this.productArray.push(element);
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
    this.http.post(this.queryUrl,{page:this.page,size:this.config.pageSize,typeId:this.params.get("typeId")},this.helperService.getOptions())
      .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          //等待2秒，2秒后执行
          setTimeout(()=> {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                this.productArray.push(element);
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
        this.nav.push(ProductDetail,{product:item});
      }

}
