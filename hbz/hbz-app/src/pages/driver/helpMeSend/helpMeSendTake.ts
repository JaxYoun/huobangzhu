import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { HelpMeSendTakeConfirmOrder } from './confirmOrder';
import { Config } from '../../../app/config';
// import {CityPickerService} from "../../../providers/city.picker.service";

@Component({
  selector: 'page-helpMeSendTake',
  templateUrl: 'helpMeSendTake.html',
})
export class HelpMeSendTake {
  private queryOrderUrl: string = "/api/order/send/task/near/queryPage";//帮我送司机、同城快递找货源
  private queryDetailUrl: string = "/api/order/get";//订单信息
  public helperService:HelperService;
  public myOrdersArray=[];
  public obj:any={};
  public page=0;
  public flag:boolean=true;//控制下拉多次触发问题
  

  // cityData: any[]; //城市数据
  // cityName:string = '北京市-北京市-东城区'; //初始化城市名
  code:string; //城市编码

  public originAreaCode = "";
  public destAreaCode = "";

  constructor(
      // public cityPickerSev: CityPickerService,
      private nav:NavController,
      public alertCtrl: AlertController,
      public loadingCtrl: LoadingController,
      public toastCtrl: ToastController,
      public modalCtrl: ModalController,
      public platform:Platform,
      private http: Http,
      public storage: Storage,
      public config:Config) {
        // this.setCityPickerData();
        
        this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
        this.queryOrderUrl = this.helperService.api + this.queryOrderUrl;
        this.queryDetailUrl = this.helperService.api + this.queryDetailUrl;

        this.initPage();
        
        
  }


  buildParamObj(page){
    let paramObj:any= {};
    paramObj.page = page;
    paramObj.size = this.config.pageSize;
    if(this.destAreaCode != ""){
      paramObj.destAreaCode = this.destAreaCode;
    }
    if(this.originAreaCode != ""){
      paramObj.originAreaCode = this.originAreaCode;
    }
    return paramObj;
  }

  initPage(){
    this.http.post(this.queryOrderUrl,this.buildParamObj(0),this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
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
    this.http.post(this.queryOrderUrl,this.buildParamObj(0),this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
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
    this.http.post(this.queryOrderUrl,this.buildParamObj(this.page),this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          //等待2秒，2秒后执行
          setTimeout(()=> {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
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
    this.http.post(this.queryDetailUrl,{id:myOrder.id},this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          this.obj = result.data;
          this.nav.push(HelpMeSendTakeConfirmOrder,{obj: this.obj})
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      });
  }


  /**
   * 获取城市数据
   */
  // setCityPickerData(){
  //   this.cityPickerSev.getCitiesData()
  //     .then( data => {
  //       this.cityData = data;
  //     });
  // }

  /**
   * 城市选择器被改变时触发的事件
   * @param event
   */
  cityChange(event){
    console.log(event);
    this.code = event['region'].value
  }




  // 联系地址选择
  onChangeAddressOrigin(res) {
    this.originAreaCode = res.outCode;
    this.myOrdersArray = [];
    this.http.post(this.queryOrderUrl,this.buildParamObj(0),this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          console.log(result.data)
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
              this.myOrdersArray.push(element);
            });
          }
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      });
}

// 联系地址选择
onChangeAddressDest(res) {
    this.destAreaCode = res.outCode;
    this.myOrdersArray = [];
    this.http.post(this.queryOrderUrl,this.buildParamObj(0),this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          console.log(result.data)
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
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
