import {Component,ViewChild,ElementRef} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { EarnestMoney } from './earnestMoney';

declare var BMap;
@Component({
  selector: 'page-warehouseDetail',
  templateUrl: 'warehouseDetail.html'
})

export class WarehouseDetail {
    @ViewChild('map') map_container: ElementRef;

    private queryUrl: string = "/api/web/warehouse/getWarehouseDetail";
    private queryEarnestUrl: string = "/api/web/warehouse/getWarehouseEarnest";
    public helperService:HelperService;
    public title:string = '仓库详情';
    public wareHouseObj;
    public map: any;//地图对象

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
          this.queryUrl = this.helperService.api + this.queryUrl;
          this.queryEarnestUrl = this.helperService.api + this.queryEarnestUrl;
          this.wareHouseObj = params.get("obj");
        }
        
        ionViewDidEnter() {
          this.map = new BMap.Map("map_container");
          let myIcon = new BMap.Icon("assets/icon/marker1.png", new BMap.Size(30, 30));
          let p = new BMap.Point(this.wareHouseObj.coordX,this.wareHouseObj.coordY);
          this.map.centerAndZoom(p, 15);//设置中心和地图显示级别
          let marker = new BMap.Marker(p,{icon: myIcon});  //当前用户GPS获取的实时位置
          this.map.addOverlay(marker);
        }

        gotoPay() {
          this.http.post(this.queryEarnestUrl,{},this.helperService.getOptions())
          .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
              this.nav.push(EarnestMoney,{id:this.wareHouseObj.id, money:result.data});
            }
          },
          (err)=> {
            this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
          });
        }
}
