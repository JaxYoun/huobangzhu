import { Component } from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams, ViewController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';


@Component({
  templateUrl: 'modals.html'
})
export class SpecialLineModals {
  public character; // 当前模态框信息
  public historyModal:object = {};  // 记录上一次模态框信息
  public transArray;
  public transArrayString="";
  public helperService:HelperService;

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
    this.transArray = params.get('transArray');
    this.transArrayString = params.get('transArrayString');
    let selectedTransArray = this.transArrayString.split(",");
    for(let i=0; i<this.transArray.length;i++){
      for(let j=0; j<selectedTransArray.length;j++){
        if(this.transArray[i].transSize == selectedTransArray[j]){
          this.transArray[i].isSelected = true;
          break;
        } else {
          this.transArray[i].isSelected = false;
        }
      }
    }
  }


  // 关闭模态框
  dismiss() {
    this.viewCtrl.dismiss();
  }




  goSelectBack(src){
    this.transArray.forEach(element => {
      if(element.id == src.id){
        if(element.isSelected == true){
          element.isSelected = false;
        } else {
          element.isSelected = true;
        }
      }
    });
  }


  saveNew(){
    let transSizes = [];
    this.transArray.forEach(element => {
      if(element.isSelected == true){
        transSizes.push(element.transSize)
      }
    });
    this.viewCtrl.dismiss({transSizes:transSizes});
  }
}
