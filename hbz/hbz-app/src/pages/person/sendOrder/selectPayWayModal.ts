import { Component } from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams, ViewController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';


@Component({
  templateUrl: 'selectPayWayModal.html'
})
export class SelectPayWayModal {
  public payWayObj;
  public obj:any = {};
  public helperService;

  constructor(
    public params: NavParams,
    public viewCtrl: ViewController,
    private nav:NavController,
    public alertCtrl: AlertController,
    public loadingCtrl: LoadingController,
    public toastCtrl: ToastController,
    public modalCtrl: ModalController,
    public platform:Platform,
    private http: Http,
    public storage: Storage,
    public config:Config
  ) {
    this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
    this.payWayObj = {'1':'线上支付', '2': '线下支付'};//1、线上支付2、线下支付
    this.obj.payWaySelected = '1';

    // //回填货物内容
    // this.obj = params.get("goodsObj");
    // if(this.obj.takeTime != ""){
    //   this.obj.takeTime = this.obj.takeTime.replace(" ","T") + ":00Z";
    // }
  }

  dismiss(){
    this.viewCtrl.dismiss();
  }


  // 保存地址
  saveNew(){

    if(this.obj.payWaySelected == '1'){
      if (this.obj.amount == '' || this.obj.amount == null) {
        this.helperService.presentToast('请填写订单价格');
        return;
      } else {
        if (!/^(([1-9][0-9]*)|(([0]\.\d{0,2}|[1-9][0-9]*\.\d{0,2})))$/.test(this.obj.amount)) {
          this.helperService.presentToast('订单价格最多为两位小数的正数');
          return;
        }
      }
    }

    this.viewCtrl.dismiss({obj:this.obj});
  }


}
