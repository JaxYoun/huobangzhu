import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController,NavParams} from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { SendOrderDetail } from './sendOrderDetail';
import { Config } from '../../../app/config';
import { CarCollect } from '../../shipper/fulltransport/carCollect';
// import { CarCollectDetail } from '../../shipper/fulltransport/carCollectDetail';
import {WaitToTake} from '../../shipper/waitToTake';
import {OrderToReceive} from '../../shipper/orderToReceive';
import { OrderDetail } from '../../shipper/fulltransport/orderDetail';
import { HelpMeBuyConfirmOrder } from '../../shipper/helpMeBuy/confirmOrder';
import { HelpMeSendConfirmOrder } from '../../shipper/helpMeSend/confirmOrder';
import { ConfirmOrder } from '../../shipper/fulltransport/confirmOrder';



@Component({
  selector: 'sendList',
  templateUrl: 'sendList.html',
})
export class SendList {
  private queryMyOrderUrl: string = "/api/order/queryPage";//我创建的订单查询
  private orderDetalUrl: string = "/api/order/get";//订单详情
  private delUrl: string = "";
  private delBuyUrl: string = "/api/order/buy/delete";
  private delSendUrl: string = "/api/order/send/delete";
  private delFslUrl: string = "/api/order/fsl/delete";
  private delLtlUrl: string = "/api/order/ltl/delete";
  public helperService:HelperService;
  public myOrdersArray=[];
  public obj:any={};
  public paramsArray=[];
  public paramsHisArray=[];
  public orderTypeObj={};
  public orderTypeSelect="all";
  // public orderTransObj:any = {};
  // public orderTransSelect="all";
  // public paramsOrderTransArray=[];
  // public paramsOrderTransHisArray=[];
  public createTime="";
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
        this.queryMyOrderUrl = this.helperService.api + this.queryMyOrderUrl;
        this.orderDetalUrl = this.helperService.api + this.orderDetalUrl;


        // this.orderTransObj = this.helperService.getOrderStatusJson();
        // for(var o in this.orderTransObj){
        //   this.paramsOrderTransArray.push(o);
        //   this.paramsOrderTransHisArray.push(o);
        // }
        
        // //查询条件默认值
        // this.orderTransObj.all = '请选择';
        // "BUY" : "帮买",
        // "SEND" : "帮送",
        // "FSL" : "整车专线",
        // "LTL" : "零担专线",
        // "EX" : "快递",
        // "FAC" : "找车",
        // "S" : "积分订单"
        this.storage.get("orderType").then(
          (data)=>{
              this.orderTypeObj = data;
              for(var p in data){
                this.paramsArray.push(p);
                this.paramsHisArray.push(p);
              }

              if(params.get("orderTrans")){
                this.initPageByOrderTrans(params.get("orderTrans"));
              } else {
                this.initPage();
              }
              
              this.orderTypeObj['all'] = '请选择';
          });
  }


  // 待处理任务跳转，设置相应参数
  // setWaitParam(){
    // this.paramsOrderTransArray = [];
    // let orderTrans = this.params.get('orderTrans');
    // if(orderTrans){
      // this.paramsOrderTransArray.push(orderTrans);
      // this.orderTransSelect = orderTrans;
    // } else{
    //   this.paramsOrderTransArray = this.paramsOrderTransHisArray;
    // }
  // }

  generatorData(element){
     //快递，取货地址、配送地址
     if(element.orderType == 'EX'){
      element.originLocation = this.helperService.buildAreaLocation(element.originArea.level1Name,element.originArea.level2Name,element.originArea.level3Name);
      element.originLocationInfo = element.originAddr;
      element.destLocation = this.helperService.buildAreaLocation(element.destArea.level1Name,element.destArea.level2Name,element.destArea.level3Name);
      element.destLocationInfo = element.destAddr;
    } else if(element.orderType == 'BUY'){
      element.destLocation = element.destInfo;
      element.destLocationInfo = element.destAddress;
    } else if(element.orderType == 'SEND'){
      element.destLocation = element.destAddress;
      element.destLocationInfo = element.destInfo;
      element.originLocation = element.originAddress;
      element.originLocationInfo = element.originInfo;
    } else {
      element.destLocation = this.helperService.buildAreaLocation(element.destArea.level1Name,element.destArea.level2Name,element.destArea.level3Name);
      element.destLocationInfo = element.destAddress;
      element.originLocation = this.helperService.buildAreaLocation(element.originArea.level1Name,element.originArea.level2Name,element.originArea.level3Name);
      element.originLocationInfo = element.originAddress;
    }
  }

  //初始化数据
  initPage(){
    this.myOrdersArray = [];
    // 待处理任务跳转进入
    // this.setWaitParam();

    this.http.post(this.queryMyOrderUrl,{
      page:this.page,
      size:this.config.pageSize,
      orderTypes:this.paramsArray
      // orderTranses:this.paramsOrderTransArray
    },this.helperService.getOptions())
      .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          // console.log(result.data)
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
              //快递，取货地址、配送地址
              this.generatorData(element);
              element.orderTypeChi = this.helperService.getValueByJson(element.orderType, this.orderTypeObj);
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

  //初始化数据；待处理任务跳转过来的数据
  initPageByOrderTrans(orderTrans){
    let orderTranses = [];
    orderTranses.push(orderTrans)
    this.myOrdersArray = [];
    this.http.post(this.queryMyOrderUrl,{
      page:this.page,
      size:this.config.pageSize,
      orderTypes:this.paramsArray,
      orderTranses:orderTranses
    },this.helperService.getOptions())
      .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          // console.log(result.data)
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
              //快递，取货地址、配送地址
              this.generatorData(element);
              element.orderTypeChi = this.helperService.getValueByJson(element.orderType, this.orderTypeObj);
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
    // // 待处理任务跳转进入
    // this.setWaitParam();
    this.myOrdersArray = [];
    this.http.post(this.queryMyOrderUrl,{
      page:0,
      size:this.config.pageSize,
      orderTypes:this.paramsArray
      // orderTranses:this.paramsOrderTransArray
    },this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
              //快递，取货地址、配送地址
              this.generatorData(element);
              element.orderTypeChi = this.helperService.getValueByJson(element.orderType, this.orderTypeObj);
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
    // // 待处理任务跳转进入
    // this.setWaitParam();
    //上拉加载前先为当前page++
    this.page++;
    this.http.post(this.queryMyOrderUrl,{
      page:this.page,
      size:this.config.pageSize,
      orderTypes:this.paramsArray
      // orderTranses:this.paramsOrderTransArray
    },this.helperService.getOptions())
      .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          //等待2秒，2秒后执行
          setTimeout(()=> {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                //快递，取货地址、配送地址
                this.generatorData(element);
                element.orderTypeChi = this.helperService.getValueByJson(element.orderType, this.orderTypeObj);
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


  //查询订单详情
  goDetail(myOrder){
    this.http.post(this.orderDetalUrl,{id:myOrder.id},this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          this.obj = result.data;
          if(this.obj.settlementType == 'LEVY_ONLINE_PAYMENT'){
            //车辆征集详情
            this.goCarDetail(this.obj);
          } else {
            //专线、配送、快递
            if((this.obj.orderType == 'FSL' || this.obj.orderType == 'LTL' ) && this.obj.orderTrans == 'NEW'){
              this.obj.orderId = this.obj.id;
               //设置参数供确认页面使用。
               this.obj.pickUpAddress = this.helperService.buildAreaLocation(this.obj.originArea.level1Name,this.obj.originArea.level2Name,this.obj.originArea.level3Name);
               this.obj.pickUpAddressInfo = this.obj.originAddress;
               this.obj.deliverAddress = this.helperService.buildAreaLocation(this.obj.destArea.level1Name,this.obj.destArea.level2Name,this.obj.destArea.level3Name);
               this.obj.deliverAddressInfo = this.obj.destAddress;
               let type;
               if(this.obj.orderType == 'FSL'){
                  type = 'fsl';
               }
               if(this.obj.orderType == 'LTL'){
                  type = 'lsl';
               }
              this.nav.push(ConfirmOrder,{obj:this.obj,type:type}); 
            } else if(this.obj.orderType == 'BUY' && this.obj.orderTrans == 'NEW'){
              this.obj.orderId = this.obj.id;
              this.nav.push(HelpMeBuyConfirmOrder,{obj:this.obj}); 
            } else if(this.obj.orderType == 'SEND' && this.obj.orderTrans == 'NEW'){
              this.obj.orderId = this.obj.id;
              this.nav.push(HelpMeSendConfirmOrder,{obj:this.obj}); 
            } 
            else {
              this.nav.push(SendOrderDetail,{obj: this.obj});
            }
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
      this.nav.push(OrderToReceive,{obj: item});
    } else {
      //其他，跳转到详情
      this.nav.push(OrderDetail,{obj: item});
    }
  }


  onSelectChange(){
    // let paramsOrderTransArrayNew = [];
    this.paramsArray = [];
    if(this.orderTypeSelect != 'all'){
      this.paramsArray.push(this.orderTypeSelect);
      //不同订单类型，订单状态不一样，这里需要处理一下
      // if(this.orderTypeSelect == 'BUY' || this.orderTypeSelect == 'SEND' || this.orderTypeSelect == 'EX'){
        // this.paramsOrderTransHisArray.forEach(element => {
        //   if(element != 'LOCKED_ORDER' && element != 'LOCKED_ORDER_DRIVER' && element != 'WAIT_FOR_PAYMENT_OFFLINE'){
        //     paramsOrderTransArrayNew.push(element)
        //   }
        // });

        // let orderTransObjNew:any = {};
        // for(var o in this.orderTransObj){
        //   if(o != 'LOCKED_ORDER' && o != 'LOCKED_ORDER_DRIVER' && o != 'WAIT_FOR_PAYMENT_OFFLINE'){
        //     orderTransObjNew[o] = this.orderTransObj[o];
        //   }
        // }
        // this.orderTransObj = {};
        // this.orderTransObj = orderTransObjNew;
      // } else {
          // paramsOrderTransArrayNew = this.paramsOrderTransHisArray;
          // this.orderTransObj = {};//初始化对象
          // this.orderTransObj = this.helperService.getOrderStatusJson();
          // this.orderTransObj['all'] = '请选择';
      // }
   } else {
      this.paramsArray = this.paramsHisArray;
      // paramsOrderTransArrayNew = this.paramsOrderTransHisArray;
      // this.orderTransObj = {};//初始化对象
      // this.orderTransObj = this.helperService.getOrderStatusJson();
      // this.orderTransObj['all'] = '请选择';
   }

   //查询，将集合清空，重新查询添加
   this.myOrdersArray = [];
   //重新查询分页始终从第一也开始
   this.page = 0;
    this.http.post(this.queryMyOrderUrl,{
      page:this.page,
      size:this.config.pageSize,
      orderTypes:this.paramsArray
      // orderTranses:paramsOrderTransArrayNew
    },this.helperService.getOptions())
    .subscribe((data)=> {
      let result = data.json();
      if (result.code == '200') {
        if(result.data.content.length > 0){
          result.data.content.forEach(element =>{
            //快递，取货地址、配送地址
            this.generatorData(element);
            element.orderTypeChi = this.helperService.getValueByJson(element.orderType, this.orderTypeObj);
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


  onSelectChange2(){
  //  this.paramsOrderTransArray = [];
  //  if(this.orderTransSelect != 'all'){
  //   this.paramsOrderTransArray.push(this.orderTransSelect);
  //  } else {
  //   this.paramsOrderTransArray = this.paramsOrderTransHisArray;
  //  }
   //查询，将集合清空，重新查询添加
   this.myOrdersArray = [];
   //重新查询分页始终从第一也开始
   this.page = 0;
    this.http.post(this.queryMyOrderUrl,{
      page:this.page,
      size:this.config.pageSize,
      orderTypes:this.paramsArray
      // orderTranses:this.paramsOrderTransArray
    },this.helperService.getOptions())
    .subscribe((data)=> {
      let result = data.json();
      if (result.code == '200') {
        if(result.data.content.length > 0){
          result.data.content.forEach(element =>{
            //快递，取货地址、配送地址
            this.generatorData(element);
            element.orderTypeChi = this.helperService.getValueByJson(element.orderType, this.orderTypeObj);
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


  delOrder(item){
    //新建和确认可以删除
    if(item.orderTrans == 'NEW' || item.orderTrans == 'CONFIRMED'){
        if(item.orderType == 'BUY'){
          this.delUrl = this.helperService.api + this.delBuyUrl;
        }
        if(item.orderType == 'SEND'){
          this.delUrl = this.helperService.api + this.delSendUrl;
        }
        if(item.orderType == 'FSL'){
          this.delUrl = this.helperService.api + this.delFslUrl;
        }
        if(item.orderType == 'LTL'){
          this.delUrl = this.helperService.api + this.delLtlUrl;
        }
        if(this.delUrl == ''){
          this.helperService.presentToast('快递订单不允许删除');
          return;
        }
      this.http.post(this.delUrl,{id:item.id},this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.helperService.presentToast('删除成功');
            this.initPage();
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
    } else {
      this.helperService.presentToast('新建和确认状态的订单可以删除');
    }
  }
}
