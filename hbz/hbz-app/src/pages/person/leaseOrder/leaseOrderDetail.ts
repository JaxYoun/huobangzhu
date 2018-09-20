import {Component,ViewChild,ElementRef} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { PayOnline } from '../../shipper/fulltransport/payOnline';



declare var BMap;
@Component({
  selector: 'leaseOrderDetail',
  templateUrl: 'leaseOrderDetail.html',
})
export class LeaseOrderDetail {
  @ViewChild('map') map_container: ElementRef;
  public map: any;//地图对象
  public helperService:HelperService;
  public wareHouseObj:any;
  public obj:any;


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
        this.wareHouseObj = this.obj.warehouse;
      }

      ionViewDidEnter() {
        this.map = new BMap.Map("map_container");
        let myIcon = new BMap.Icon("assets/icon/marker1.png", new BMap.Size(30, 30));
        let p = new BMap.Point(this.wareHouseObj.coordX,this.wareHouseObj.coordY);
        this.map.centerAndZoom(p, 15);//设置中心和地图显示级别
        let marker = new BMap.Marker(p,{icon: myIcon});  //当前用户GPS获取的实时位置
        this.map.addOverlay(marker);
      }


    //"在线支付"
    gotoPay() {
      this.nav.push(PayOnline,{obj: {
        orderId:this.obj.id,
        orderNo:this.obj.orderNo,
        amount:this.obj.earnestPrice,
        orderType:"诚意金",
        businessType:"WAREHOUSE_EARNEST"
      }})
    }

    //去退款
    gotoRetun(){
      alert("退款")
    }
}
