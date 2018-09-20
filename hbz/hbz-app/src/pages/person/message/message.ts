import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import {InformationDetail} from '../../warehouse/information/informationDetail';

@Component({
  selector: 'page-message',
  templateUrl: 'message.html',
})
export class MessagePage {
  private queryUrl: string = "/api/sitePushMessageRecord/getMySitePushMessageRecordListByPage";
  private queryDetailUrl: string = "/api/sitePushMessageRecord/getSitePushMessageRecordDetail";
  
  public helperService:HelperService;
  public messageArray=[];
  public obj:any={};
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
        this.queryDetailUrl = this.helperService.api + this.queryDetailUrl;
        this.initPage();
        
  }

  initPage(){
    this.http.post(this.queryUrl,{page:this.page,size:this.config.pageSize},this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
              this.messageArray.push(element);
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
    this.messageArray = [];
    this.http.post(this.queryUrl,{page:0,size:this.config.pageSize},this.helperService.getOptions())
      .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
              this.messageArray.push(element);
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
            this.messageArray.push(element);
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
    //组装通知对象,需要调用接口修改状态
    this.http.post(this.queryDetailUrl,{id:item.id},this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          this.obj.title = result.data.sitePushMessage.title;
          if(result.data.sitePushMessage.imagePath){
            this.obj.isShowTitleImage = true;
            this.obj.titleImage = result.data.sitePushMessage.imagePath;
          } else{
            this.obj.isShowTitleImage = false;
          }
          this.obj.formatedCreateDate = result.data.formatedCreateDate;
          this.obj.content = result.data.sitePushMessage.content;
          this.obj.isShowDate=true;
          this.nav.push(InformationDetail,{item:this.obj, title:'通知'});
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      });
  }

}
