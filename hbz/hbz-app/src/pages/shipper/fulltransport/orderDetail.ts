import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { TakeList } from '../../person/takeOrder/takeList';
import { SelectCarByOrders } from '../selectCarByOrders';
import { Wallet } from '../../person/wallet/wallet';


@Component({
  selector: 'orderDetail',
  templateUrl: 'orderDetail.html',
})
export class OrderDetail {
  public helperService:HelperService;
  public obj:any;
  public bottomType;
  public originAreaName;
  public destAreaName;

  private takeOrderUrl;
  private takeOrderFslUrl: string = "/api/order/fsl/carry";//整车-司机接单
  private takeOrderLslUrl: string = "/api/order/ltl/carry";//零担-司机接单


  private createUrl="";
  private fslCreateUrl: string = "/api/order/fsl/create";//整车专线订单创建
  private lslCreateUrl: string = "/api/order/ltl/create";//零担专线订单创建


  private orderDetalUrl: string = "/api/order/get";//订单详情
  public photoList=[];
  
  constructor(
      public params: NavParams,
      private nav:NavController,
      public alertCtrl: AlertController,
      public loadingCtrl: LoadingController,
      public toastCtrl: ToastController,
      public platform:Platform,
      private http: Http,
      public storage: Storage,
      public config:Config) {
        this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
        this.obj = params.get('obj');
        if(this.obj.relatedPictures){
          this.photoList = this.obj.relatedPictures.split(',');
        }
        if(this.obj.orderType == 'FSL'){
          this.takeOrderUrl = this.helperService.api + this.takeOrderFslUrl;
          this.createUrl = this.helperService.api + this.fslCreateUrl;
        }
        if(this.obj.orderType == 'LTL'){
          this.takeOrderUrl = this.helperService.api + this.takeOrderLslUrl;
          this.createUrl = this.helperService.api + this.lslCreateUrl;
        }
        this.orderDetalUrl = this.helperService.api + this.orderDetalUrl;
        this.bottomType = params.get('bottomType');
        console.log(this.bottomType)
        // this.storage.get("weightUnit").then(
        //   (data)=>{
        //     this.obj.weightUnit = this.helperService.getValueByJson(this.obj.weightUnit, data);
        //   });
        // this.storage.get("volumeUnit").then(
        //   (data)=>{
        //     this.obj.volumeUnit = this.helperService.getValueByJson(this.obj.volumeUnit, data);
        //   });
        this.storage.get("transType").then(
          (data)=>{
            this.obj.transTypeChi = this.helperService.getValueByJson(this.obj.transType, data);
          });

          this.originAreaName = this.helperService.buildAreaLocation( this.obj.originArea.level1Name, this.obj.originArea.level2Name, this.obj.originArea.level3Name);
          this.destAreaName = this.helperService.buildAreaLocation( this.obj.destArea.level1Name, this.obj.destArea.level2Name, this.obj.destArea.level3Name);
          
      }


    //接单
    doTakingOrder(){
      this.http.post(this.takeOrderUrl,{id:this.obj.id},this.helperService.getOptions())
        .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
              this.helperService.presentToast('接单成功');
              this.nav.push(TakeList);
            } 
            if (result.code == '500') {
                this.helperService.presentToast(result.msg);
                if(result.data == 'NeedBond'){
                  let confirm = this.alertCtrl.create({
                    title: '请交纳保证金',
                    message: '你确定跳转到交纳保证金页面吗?',
                    buttons: [
                      {
                        text: '确定',
                        handler: () => {
                          this.nav.push(Wallet);
                        }
                      },
                      {
                        text: '取消',
                        handler: () => {
                          return;
                        }
                      }
                    ]
                  });
                  confirm.present();
              }
            }
          },
          (err)=> {
            this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
          });
    }

    copy(obj){
        var newobj = {};
        for ( var attr in obj) {
          if(attr != 'id' && attr != 'createTime' && attr != 'createdDate' && attr != 'createUser' && attr != 'orderNo'){
              newobj[attr] = obj[attr];
          }
        }
        newobj['originAreaCode'] = obj.originArea.outCode;
        newobj['destAreaCode'] = obj.destArea.outCode;
        return newobj;
    }
    //再来一单
    doRepeatOrder(){
      let newObj =  this.copy(this.obj);
      this.http.post(this.createUrl,newObj,this.helperService.getOptions())
        .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
              this.helperService.presentToast('保存成功');
              this.nav.push(SelectCarByOrders);
            } else {
              this.helperService.presentToast(result.msg);
            }
          },
          (err)=> {
            this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
          });
    }


    doDetail(){
      this.nav.push(OrderDetail,{obj: this.obj})
     }
}
