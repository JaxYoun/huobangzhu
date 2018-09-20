import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController,NavParams,ModalController } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import {Http}from '@angular/http';
import { Config } from '../../app/config';
import { HelperService } from '../../app/services/help.service';


@Component({
  selector: 'find-car-detail',
  templateUrl: 'findCarDetail.html'
})
export class FindCarDetail {
  public helperService:HelperService;
  public obj = {
    orderId:'null',
    orderNo:'null',
    amount:'null',
    orderType:'null',
    payType:'null'
  }

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
    }


}
