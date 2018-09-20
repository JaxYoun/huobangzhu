import {Component} from '@angular/core';
import {Platform, NavController,ToastController,AlertController, LoadingController, ModalController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { ProductDetail } from './productDetail';
import { ProductOfType } from './productOfType';

@Component({
  selector: 'page-index',
  templateUrl: 'index.html'
})

export class Shop {
    private queryTypeUrl: string = "/api/wareType/top";//	查询顶级类型
    private queryHotProductUrl: string = "/api/recommendProduct/query";//3.3.16	查询推荐商品
    public helperService:HelperService;
    public title:string = '积分商城';

    public hotProductArray;
    public typeArray;
    public bannerArray;

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
          this.queryTypeUrl = this.helperService.api + this.queryTypeUrl;
          this.queryHotProductUrl = this.helperService.api + this.queryHotProductUrl;
          this.http.post(this.queryTypeUrl,{},this.helperService.getOptions())
              .subscribe((data)=> {
                  let result = data.json();
                  if (result.code == '200') {
                    this.typeArray = result.data;
                  }
                },
                (err)=> {
                  this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
                });
          this.http.post(this.queryHotProductUrl,{useType:1},this.helperService.getOptions())
              .subscribe((data)=> {
                  let result = data.json();
                  if (result.code == '200') {
                    this.hotProductArray = this.helperService.fillList(result.data, 3);
                    console.log('hotProductArray', this.hotProductArray);
                  }
                },
                (err)=> {
                  this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
                });
          this.http.post(this.queryHotProductUrl,{useType:2},this.helperService.getOptions())
              .subscribe((data)=> {
                  let result = data.json();
                  if (result.code == '200') {
                    this.bannerArray = result.data;
                  }
                },
                (err)=> {
                  this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
                });
      }


      goToType(item){
        this.nav.push(ProductOfType,{typeId:item.id,title:item.name});
      }
      goToDetail(item){
        this.nav.push(ProductDetail,{product:item.product});
      }
}
