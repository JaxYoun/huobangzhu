import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { FullCarryMap } from './fullCarryMap';
import { Config } from '../../../app/config';
import { OrderDetail } from '../../shipper/fulltransport/orderDetail';

@Component({
  selector: 'fullCarry',
  templateUrl: 'fullCarry.html'
})
export class FullCarry {
    private queryNearUrl: string = "/api/order/task/near/queryPage";//3.3.3.4	待接运订单查询
    private orderDetailUrl: string = "/api/order/get";//订单详情
    
    
    public helperService:HelperService;
    public nearSourceArray=[];
    public obj:any={};
    public title="";
    public page=0;
    public flag:boolean=true;//控制下拉多次触发问题
    
    public originAreaCode = "";
    public destAreaCode = "";


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
          this.queryNearUrl = this.helperService.api + this.queryNearUrl;
          this.orderDetailUrl = this.helperService.api + this.orderDetailUrl;

          if(this.params.get('orderType') == 'FSL'){
            this.title = "整车接单";
          }
          if(this.params.get('orderType') == 'LTL'){
            this.title = "零担接单";
          }
          this.initPage();
        }

  buildParamObj(page){
    let paramObj:any= {};
    paramObj.page = page;
    paramObj.size = this.config.pageSize;
    paramObj.settlementType="ONLINE_PAYMENT";
    paramObj.orderType=this.params.get('orderType');
    if(this.destAreaCode != ""){
      paramObj.destAreaCode = this.destAreaCode;
    }
    if(this.originAreaCode != ""){
      paramObj.originAreaCode = this.originAreaCode;
    }
    return paramObj;
  }


  //初始化数据
  initPage(){
    this.http.post(this.queryNearUrl,this.buildParamObj(0),this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
              element.destLocation = this.helperService.buildAreaLocation(element.destArea.level1Name,element.destArea.level2Name,element.destArea.level3Name);
              element.originLocation = this.helperService.buildAreaLocation(element.originArea.level1Name,element.originArea.level2Name,element.originArea.level3Name);
              this.nearSourceArray.push(element);
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
    this.nearSourceArray = [];
    this.http.post(this.queryNearUrl,this.buildParamObj(0),this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
              element.destLocation = this.helperService.buildAreaLocation(element.destArea.level1Name,element.destArea.level2Name,element.destArea.level3Name);
              element.originLocation = this.helperService.buildAreaLocation(element.originArea.level1Name,element.originArea.level2Name,element.originArea.level3Name);
              this.nearSourceArray.push(element);
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
    this.http.post(this.queryNearUrl,this.buildParamObj(this.page),this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          //等待2秒，2秒后执行
          setTimeout(()=> {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                element.destLocation = this.helperService.buildAreaLocation(element.destArea.level1Name,element.destArea.level2Name,element.destArea.level3Name);
                element.originLocation = this.helperService.buildAreaLocation(element.originArea.level1Name,element.originArea.level2Name,element.originArea.level3Name);
                this.nearSourceArray.push(element);
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
    
    gotoDetail(item){
      this.http.post(this.orderDetailUrl,{id:item.id},this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.obj = result.data;
            this.storage.get("transType").then(
              (data)=>{
                this.obj.transType = this.helperService.getValueByJson(this.obj.transType, data);
              });
              this.nav.push(OrderDetail,{obj: this.obj, bottomType:'fsl-taking'})
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
    }

    //地图
    showMap(){
      this.nav.push(FullCarryMap,{orderType:this.params.get('orderType')});
    }


     // 联系地址选择
     onChangeAddressOrigin(res) {
      this.originAreaCode = res.outCode;
      this.nearSourceArray = [];
      this.http.post(this.queryNearUrl,this.buildParamObj(0),this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                element.destLocation = this.helperService.buildAreaLocation(element.destArea.level1Name,element.destArea.level2Name,element.destArea.level3Name);
                element.originLocation = this.helperService.buildAreaLocation(element.originArea.level1Name,element.originArea.level2Name,element.originArea.level3Name);
                this.nearSourceArray.push(element);
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
      this.nearSourceArray = [];
      this.http.post(this.queryNearUrl,this.buildParamObj(0),this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                element.destLocation = this.helperService.buildAreaLocation(element.destArea.level1Name,element.destArea.level2Name,element.destArea.level3Name);
                element.originLocation = this.helperService.buildAreaLocation(element.originArea.level1Name,element.originArea.level2Name,element.originArea.level3Name);
                this.nearSourceArray.push(element);
              });
            }
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
  }
}
