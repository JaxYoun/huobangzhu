import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import {Http}from '@angular/http';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import {getDateDiff} from '../../components/utils'
import { SelectCarDetail } from './selectCarDetail';

@Component({
  templateUrl: 'index.html'
})
export class SelectCarDriver {
  public helperService:HelperService;
  private queryMyOrderUrl: string = "/api/order/task/near/queryPage";//3.4.3.4	待接运订单查询
  private takeCreateOrderUrl: string = "/api/order/taker/create";//司机接受车辆征集

  public myOrdersArray=[]; // 首次获取的数据筛选后的结果，需要改成异步拉取最新数据
  public myOrdersArrayResult; // 首次获取数据结果
  public page=0;
  public flag:boolean=true;//控制下拉多次触发问题
  
  public destArea:string = '75'; // 到达目的地code
  public originArea:string = '75'; // 出发目的地code

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
      this.takeCreateOrderUrl = this.helperService.api + this.takeCreateOrderUrl;

      this.initPage();
      
        // //  "ONLINE_PAYMENT" : "在线支付",  "MONTHLY_SETTLEMENT" : "月结",  "LEVY_ONLINE_PAYMENT" : "车辆征集"
        // this.http.post(this.queryMyOrderUrl,{orderTrans:'ORDER_TO_BE_RECEIVED',settlementType:'LEVY_ONLINE_PAYMENT'},this.helperService.getOptions())
        // .subscribe((data)=> {
        //     let result = data.json();
        //     if (result.code == '200') {
        //         this.myOrdersArray = result.data.map(item => {
        //           item.createdDateDiff = getDateDiff(item.orderTakeStart);
        //           return item;
        //         });
        //     }
        //   },
        //   (err)=> {
        //     this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        //   });
    }

  buildParamObj(page){
    let paramObj:any= {};
    paramObj.page = page;
    paramObj.size = this.config.pageSize;
    paramObj.orderTrans = 'ORDER_TO_BE_RECEIVED';
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
        this.nav.push(SelectCarDetail,{id:item.id});
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


    //接受车辆征集
    takeCreateOrder(id){
      this.http.post(this.takeCreateOrderUrl,{orderId:id},this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          console.log(result.data)
          if (result.code == '200') {
            // this.nav.push(FullCarry);
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
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
          console.log(result.data)
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
              element.originLocation = this.helperService.buildAreaLocation(element.originArea.level1Name,element.originArea.level2Name,element.originArea.level3Name);
              element.destLocation = this.helperService.buildAreaLocation(element.destArea.level1Name,element.destArea.level2Name,element.destArea.level3Name);
              
              element.createdDateDiff = getDateDiff(element.orderTakeStart);
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
