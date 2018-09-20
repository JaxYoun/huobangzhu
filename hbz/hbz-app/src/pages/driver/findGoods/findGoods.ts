import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { FindGoodsMap } from './findGoodsMap';
import { Config } from '../../../app/config';
import { OrderDetail } from '../../shipper/fulltransport/orderDetail';
import { HelpMeSendTakeConfirmOrder } from '../helpMeSend/confirmOrder';
import { helpMeBuyTakeConfirmOrder } from '../helpMeBuy/confirmOrder';

@Component({
  selector: 'findGoods',
  templateUrl: 'findGoods.html'
})
export class FindGoods {
    private queryNearUrl: string = "/api/order/task/near/queryPage";//3.3.3.4	待接运订单查询
    private orderDetailUrl: string = "/api/order/get";//订单详情
    
    
    public helperService:HelperService;
    public nearSourceArray=[];
    public obj:any={};
    public title="找货";
    public page=0;
    public flag:boolean=true;//控制下拉多次触发问题
    
    public paramsArray=[];
    public orderTypeObj={};
    public transType={};

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

          this.storage.get("transType").then(
            (data)=>{
              this.transType = data;
            });

          this.storage.get("orderType").then(
            (data)=>{
                this.orderTypeObj = data;
                for(var p in data){
                  if (p == 'FSL' || p =='LTL') {//只显示整车、零担货。
                    this.paramsArray.push(p);
                  }
                }
                this.initPage();
            });
        }

  buildParamObj(page){
    let paramObj:any= {};
    paramObj.page = page;
    paramObj.size = this.config.pageSize;
    paramObj.settlementType="ONLINE_PAYMENT";
    paramObj.orderTypes=this.paramsArray;
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
              element.orderTypeChi = this.helperService.getValueByJson(element.orderType, this.orderTypeObj);
              element.transType = this.helperService.getValueByJson(element.transType, this.transType);

              // //帮我买
              // if(element.orderType == 'BUY'){
              //   element.destLocation = element.destInfo;
              //   element.destLocationInfo = element.destAddress;
              // }else if(element.orderType == 'SEND'){
              //   element.destLocation = element.destAddress;
              //   element.destLocationInfo = element.destInfo;
              //   element.originLocation = element.originAddress;
              //   element.originLocationInfo = element.originInfo;
              // } else {
                element.destLocation = this.helperService.buildAreaLocation(element.destArea.level1Name,element.destArea.level2Name,element.destArea.level3Name);
                element.destLocationInfo = element.destAddress;
                element.originLocation = this.helperService.buildAreaLocation(element.originArea.level1Name,element.originArea.level2Name,element.originArea.level3Name);
                element.originLocationInfo = element.originAddress;
              // }
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
              element.orderTypeChi = this.helperService.getValueByJson(element.orderType, this.orderTypeObj);
              element.transType = this.helperService.getValueByJson(element.transType, this.transType);
              //  //帮我买
              //  if(element.orderType == 'BUY'){
              //   element.destLocation = element.destInfo;
              //   element.destLocationInfo = element.destAddress;
              // }else if(element.orderType == 'SEND'){
              //   element.destLocation = element.destAddress;
              //   element.destLocationInfo = element.destInfo;
              //   element.originLocation = element.originAddress;
              //   element.originLocationInfo = element.originInfo;
              // } else {
                element.destLocation = this.helperService.buildAreaLocation(element.destArea.level1Name,element.destArea.level2Name,element.destArea.level3Name);
                element.destLocationInfo = element.destAddress;
                element.originLocation = this.helperService.buildAreaLocation(element.originArea.level1Name,element.originArea.level2Name,element.originArea.level3Name);
                element.originLocationInfo = element.originAddress;
              // }
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
                element.orderTypeChi = this.helperService.getValueByJson(element.orderType, this.orderTypeObj);
                element.transType = this.helperService.getValueByJson(element.transType, this.transType);
              //    //帮我买
              // if(element.orderType == 'BUY'){
              //   element.destLocation = element.destInfo;
              //   element.destLocationInfo = element.destAddress;
              // }else if(element.orderType == 'SEND'){
              //   element.destLocation = element.destAddress;
              //   element.destLocationInfo = element.destInfo;
              //   element.originLocation = element.originAddress;
              //   element.originLocationInfo = element.originInfo;
              // } else {
                element.destLocation = this.helperService.buildAreaLocation(element.destArea.level1Name,element.destArea.level2Name,element.destArea.level3Name);
                element.destLocationInfo = element.destAddress;
                element.originLocation = this.helperService.buildAreaLocation(element.originArea.level1Name,element.originArea.level2Name,element.originArea.level3Name);
                element.originLocationInfo = element.originAddress;
              // }
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
            console.log(this.obj.orderType)
            //根据类型不同，跳转到不同详情页
            if(this.obj.orderType == "LTL" || this.obj.orderType == "FSL"){
                this.nav.push(OrderDetail,{obj: this.obj, bottomType:'fsl-taking'})
            }
            // if(this.obj.orderType == "SEND"){
            //   this.nav.push(HelpMeSendTakeConfirmOrder,{obj: this.obj})
            // }
            // if(this.obj.orderType == "BUY"){
            //   this.nav.push(helpMeBuyTakeConfirmOrder,{obj: this.obj})
            // }
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
    }

    //地图
    showMap(){
      this.nav.push(FindGoodsMap,{orderTypes:this.paramsArray});
    }




     // 联系地址选择
     onChangeAddressOrigin(res) {
      this.originAreaCode = res.outCode;
      this.nearSourceArray = [];
      this.http.post(this.queryNearUrl,this.buildParamObj(0),this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            console.log(result.data)
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                element.orderTypeChi = this.helperService.getValueByJson(element.orderType, this.orderTypeObj);
                element.transType = this.helperService.getValueByJson(element.transType, this.transType);
                //  //帮我买
                //  if(element.orderType == 'BUY'){
                //   element.destLocation = element.destInfo;
                //   element.destLocationInfo = element.destAddress;
                // }else if(element.orderType == 'SEND'){
                //   element.destLocation = element.destAddress;
                //   element.destLocationInfo = element.destInfo;
                //   element.originLocation = element.originAddress;
                //   element.originLocationInfo = element.originInfo;
                // } else {
                  element.destLocation = this.helperService.buildAreaLocation(element.destArea.level1Name,element.destArea.level2Name,element.destArea.level3Name);
                  element.destLocationInfo = element.destAddress;
                  element.originLocation = this.helperService.buildAreaLocation(element.originArea.level1Name,element.originArea.level2Name,element.originArea.level3Name);
                  element.originLocationInfo = element.originAddress;
                // }
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
                element.orderTypeChi = this.helperService.getValueByJson(element.orderType, this.orderTypeObj);
                element.transType = this.helperService.getValueByJson(element.transType, this.transType);
                //  //帮我买
                //  if(element.orderType == 'BUY'){
                //   element.destLocation = element.destInfo;
                //   element.destLocationInfo = element.destAddress;
                // }else if(element.orderType == 'SEND'){
                //   element.destLocation = element.destAddress;
                //   element.destLocationInfo = element.destInfo;
                //   element.originLocation = element.originAddress;
                //   element.originLocationInfo = element.originInfo;
                // } else {
                  element.destLocation = this.helperService.buildAreaLocation(element.destArea.level1Name,element.destArea.level2Name,element.destArea.level3Name);
                  element.destLocationInfo = element.destAddress;
                  element.originLocation = this.helperService.buildAreaLocation(element.originArea.level1Name,element.originArea.level2Name,element.originArea.level3Name);
                  element.originLocationInfo = element.originAddress;
                // }
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
