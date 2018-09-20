import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { CarCollect } from '../../shipper/fulltransport/carCollect';
import {WaitToTake} from '../../shipper/waitToTake';
import {OrderToReceive} from '../../shipper/orderToReceive';
import { OrderDetail } from '../../shipper/fulltransport/orderDetail';


@Component({
  selector: 'joinList',
  templateUrl: 'joinList.html',
})
export class JoinList {
  private queryMyOrderUrl: string = "/api/order/taker/driver/queryPage";//我参与的订单查询
  private orderDetalUrl: string = "/api/order/get";//订单详情
  public helperService:HelperService;
  public myOrdersArray=[];
  public obj:any={};
  public page=0;
  public flag:boolean=true;//控制下拉多次触发问题

  // public orderTransObj={};
  // public orderTransSelect="all";
  // public paramsOrderTransArray=[];
  // public paramsOrderTransHisArray=[];

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

        // this.orderTransObj = this.helperService.getOrderStatusJson();
        // for(var o in this.orderTransObj){
        //   this.paramsOrderTransArray.push(o);
        //   this.paramsOrderTransHisArray.push(o);
        // }
        // //查询条件默认值
        // this.orderTransObj['all'] = '请选择';

        this.initPage();
  }

  //初始化数据
  initPage(){
    this.http.post(this.queryMyOrderUrl,{
      page:this.page,
      size:this.config.pageSize
      // orderTranses:this.paramsOrderTransArray
    },this.helperService.getOptions())
      .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
              element.order.orderTransChi = this.helperService.getOrderStatusChi(element.order.orderTrans);
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
    this.http.post(this.queryMyOrderUrl,{
      page:0,
      size:this.config.pageSize
      // orderTranses:this.paramsOrderTransArray
    },this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
              element.order.orderTransChi = this.helperService.getOrderStatusChi(element.order.orderTrans);
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
    this.http.post(this.queryMyOrderUrl,{
      page:this.page,
      size:this.config.pageSize
      // orderTranses:this.paramsOrderTransArray
    },this.helperService.getOptions())
      .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          //等待2秒，2秒后执行
          setTimeout(()=> {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                element.order.orderTransChi = this.helperService.getOrderStatusChi(element.order.orderTrans);
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
    this.http.post(this.orderDetalUrl,{id:myOrder.orderId},this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          this.obj = result.data;
          if(this.obj.settlementType == 'LEVY_ONLINE_PAYMENT'){
            //车辆征集详情
            this.goCarDetail(this.obj);
          }
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      });
  }


  goCarDetail(item){
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
      this.nav.push(OrderToReceive,{obj: item, flag:'query'});
    } else {
      //其他，跳转到详情
      this.nav.push(OrderDetail,{obj: item});
    }
  }



  onSelectChange2(){
    // this.paramsOrderTransArray = [];
    // if(this.orderTransSelect != 'all'){
    //  this.paramsOrderTransArray.push(this.orderTransSelect);
    // } else {
    //  this.paramsOrderTransArray = this.paramsOrderTransHisArray;
    // }
    //查询，将集合清空，重新查询添加
    this.myOrdersArray = [];
    //重新查询分页始终从第一也开始
    this.page = 0;
     this.http.post(this.queryMyOrderUrl,{
       page:this.page,
       size:this.config.pageSize
      //  orderTranses:this.paramsOrderTransArray
     },this.helperService.getOptions())
     .subscribe((data)=> {
       let result = data.json();
       if (result.code == '200') {
         if(result.data.content.length > 0){
           result.data.content.forEach(element =>{
            element.order.orderTransChi = this.helperService.getOrderStatusChi(element.order.orderTrans);
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
