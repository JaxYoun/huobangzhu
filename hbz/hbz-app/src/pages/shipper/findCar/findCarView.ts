import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController,NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { SendList } from '../../person/sendOrder/sendList';
import { SpecialLineModals } from './modals';
import { Config } from '../../../app/config';

@Component({
  selector: 'page-specialLine',
  templateUrl: 'findCarView.html',
})
export class FindCarView {
  public helperService:HelperService;
  public obj:any={};
  public transTypeSelect:string = "UNLIMITED";
  public transArrayString="";

  public originAreaName;
  public destAreaName;

  constructor(
      public params: NavParams,
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
        this.obj = params.get("obj");

        this.originAreaName = this.helperService.buildAreaLocation( this.obj.originArea.level1Name, this.obj.originArea.level2Name, this.obj.originArea.level3Name);
        this.destAreaName = this.helperService.buildAreaLocation( this.obj.destArea.level1Name, this.obj.destArea.level2Name, this.obj.destArea.level3Name);

        this.obj.transSizes.forEach(element => {
          this.transArrayString += element.transSize + ",";
        });
        // this.storage.get("transType").then(
        //   (data)=>{
        //     this.transTypeSelect = this.helperService.getValueByJson(this.obj.transType, data);
        // });
  }

}
