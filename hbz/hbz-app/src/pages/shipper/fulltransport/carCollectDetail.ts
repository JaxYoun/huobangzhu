import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController ,NavParams} from 'ionic-angular';
import { Storage } from '@ionic/storage';
import {TabsPage} from '../tabs/tabs';
import {Http}from '@angular/http';
import { HelperService } from '../../../app/services/help.service';
import { PayOnline } from './payOnline';
import { Config } from '../../../app/config';

@Component({
  selector: 'carcollectDetail',
  templateUrl: 'carcollectDetail.html'
})
export class CarCollectDetail {
  public helperService:HelperService;
  public orderId;
  public orderNo;
  public amount;

  public driverSelect='运输企业';//司机类型
  public registryMoneySelect='50万以上';//注册资金要求
  public bondSelect='1000';//保证金额度
  public needSelect='★★';//信誉等级
  constructor(
    public params: NavParams,
    private nav:NavController,
    public alertCtrl: AlertController,
    public loadingCtrl: LoadingController,
    public toastCtrl: ToastController,
    public platform:Platform,
    private http:Http,
    public storage: Storage,
    public config:Config) {
      this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
    }
    
}
