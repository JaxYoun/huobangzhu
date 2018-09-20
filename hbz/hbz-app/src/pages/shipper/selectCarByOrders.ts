import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import {Http}from '@angular/http';
import { HelperService } from '../../app/services/help.service';
import { CarCollect } from '../shipper/fulltransport/carCollect';
import {WaitToTake} from './waitToTake';
import { OrderDetail } from '../shipper/fulltransport/orderDetail';
import {OrderToReceive} from './orderToReceive';
import { Config } from '../../app/config';
import {getDateDiff} from '../components/utils'

@Component({
  selector: 'selectCarByOrders',
  templateUrl: 'selectCarByOrders.html'
})
export class SelectCarByOrders {
  public helperService:HelperService;
  private queryMyOrderUrl: string = "/api/order/queryPage";//我创建的订单查询
  public myOrdersArray=[]; // 首次获取的数据筛选后的结果，需要改成异步拉取最新数据
  public myOrdersArrayResult=[]; // 首次获取数据结果
  public destArea:string = '75'; // 到达目的地code
  public originArea:string = '75'; // 出发目的地code
  public page=0;
  public flag:boolean=true;//控制下拉多次触发问题

  public originAreaCode = "";
  public destAreaCode = "";
  
  constructor(
    private nav:NavController,
    public alertCtrl: AlertController,
    public loadingCtrl: LoadingController,
    public toastCtrl: ToastController,
    public platform:Platform,
    private http:Http,
    public storage: Storage,
    public config:Config) {
      this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
      this.queryMyOrderUrl = this.helperService.api + this.queryMyOrderUrl;

      this.initPage();
    }

  
    buildParamObj(page){
      let paramObj:any= {};
      paramObj.page = page;
      paramObj.size = this.config.pageSize;
      paramObj.settlementType = 'LEVY_ONLINE_PAYMENT';
      if(this.destAreaCode != ""){
        paramObj.destAreaCode = this.destAreaCode;
      }
      if(this.originAreaCode != ""){
        paramObj.originAreaCode = this.originAreaCode;
      }
      return paramObj;
    }


  initPage(){
    this.http.post(this.queryMyOrderUrl,this.buildParamObj(0),this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{

              element.originLocation = this.helperService.buildAreaLocation(element.originArea.level1Name,element.originArea.level2Name,element.originArea.level3Name);
              element.destLocation = this.helperService.buildAreaLocation(element.destArea.level1Name,element.destArea.level2Name,element.destArea.level3Name);
              
              element.createdDateDiff = getDateDiff(element.orderTakeStart);
              element.orderTransChi = this.helperService.getOrderStatusChi(element.orderTrans);
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
    this.http.post(this.queryMyOrderUrl,this.buildParamObj(0),this.helperService.getOptions())
      .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                element.originLocation = this.helperService.buildAreaLocation(element.originArea.level1Name,element.originArea.level2Name,element.originArea.level3Name);
                element.destLocation = this.helperService.buildAreaLocation(element.destArea.level1Name,element.destArea.level2Name,element.destArea.level3Name);
                
                element.createdDateDiff = getDateDiff(element.orderTakeStart);
                element.orderTransChi = this.helperService.getOrderStatusChi(element.orderTrans);
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
    this.http.post(this.queryMyOrderUrl,this.buildParamObj(this.page),this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          //等待2秒，2秒后执行
          setTimeout(()=> {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                element.originLocation = this.helperService.buildAreaLocation(element.originArea.level1Name,element.originArea.level2Name,element.originArea.level3Name);
                element.destLocation = this.helperService.buildAreaLocation(element.destArea.level1Name,element.destArea.level2Name,element.destArea.level3Name);
                
                element.createdDateDiff = getDateDiff(element.orderTakeStart);
                element.orderTransChi = this.helperService.getOrderStatusChi(element.orderTrans);
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

    goDetail(item){
      /**
        根据订单是否支付状态判断进入哪个页面
        NEW、CONFIRMED    未开始
        ORDER_TO_BE_RECEIVED、LOCKED_ORDER 征集中
        WAIT_TO_TAKE、TRANSPORT、WAIT_TO_CONFIRM、WAIT_FOR_PAYMENT、PAID、LIQUIDATION_COMPLETED  征集完成
        OVER_TIME、WAIT_TO_REFUNDDE、APPLY_FOR_REFUND、REFUND_FINISHT、INVALID 征集取消
      * 
      */
        if(item.orderTrans == 'NEW' || item.orderTrans == 'CONFIRMED'){
          //未开始
          this.nav.push(CarCollect,{orderId:item.id,orderNo:item.orderNo,amount:item.amount,orderType:item.orderType});
        }
        else if (item.orderTrans === 'WAIT_TO_TAKE' || item.orderTrans == 'TRANSPORT'
          || item.orderTrans == 'WAIT_TO_CONFIRM' || item.orderTrans == 'WAIT_FOR_PAYMENT'
          || item.orderTrans == 'PAID' || item.orderTrans == 'LIQUIDATION_COMPLETED') {
          //征集完成
          this.nav.push(WaitToTake,{obj: item});
        }
        else if (item.orderTrans === 'ORDER_TO_BE_RECEIVED' || item.orderTrans === 'LOCKED_ORDER'){
          //征集中
          this.nav.push(OrderToReceive,{obj: item});
        } else {
          //其他，跳转到详情
          this.nav.push(OrderDetail,{obj: item});
        }
    }
    // 搜索
    search(value, where){
      if (where === 'from') {
        this.originArea = value;
      } else {
        this.destArea = value;
      };
      this.myOrdersArray = this.myOrdersArrayResult.filter(n => n.destArea.level2AreaCode === this.destArea && n.originArea.level2AreaCode === this.originArea)
    }


     // 联系地址选择
     onChangeAddressOrigin(res) {
      this.originAreaCode = res.outCode;
      this.myOrdersArray = [];
      this.http.post(this.queryMyOrderUrl,this.buildParamObj(0),this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                element.originLocation = this.helperService.buildAreaLocation(element.originArea.level1Name,element.originArea.level2Name,element.originArea.level3Name);
                element.destLocation = this.helperService.buildAreaLocation(element.destArea.level1Name,element.destArea.level2Name,element.destArea.level3Name);
                
                element.createdDateDiff = getDateDiff(element.orderTakeStart);
                element.orderTransChi = this.helperService.getOrderStatusChi(element.orderTrans);
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
      this.http.post(this.queryMyOrderUrl,this.buildParamObj(0),this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                element.originLocation = this.helperService.buildAreaLocation(element.originArea.level1Name,element.originArea.level2Name,element.originArea.level3Name);
                element.destLocation = this.helperService.buildAreaLocation(element.destArea.level1Name,element.destArea.level2Name,element.destArea.level3Name);
                
                element.createdDateDiff = getDateDiff(element.orderTakeStart);
                element.orderTransChi = this.helperService.getOrderStatusChi(element.orderTrans);
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
