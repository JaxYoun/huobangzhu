import {Component,ViewChild,ElementRef} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { PersonShipperAuth } from './personShipperAuth';
import { PersonDriverAuth } from './personDriverAuth';
import { ShipperCompanyAuth } from './shipperCompanyAuth';
import { TransportCompanyAuth } from './transportCompanyAuth';
import { DeliveryBoyAuth } from './deliveryBoyAuth';


@Component({
  selector: 'page-auth',
  templateUrl: 'auth.html'
})

export class AuthPage {
    public title:string = '平台认证';
    public helperService:HelperService;
    public PersonShipperAuth = PersonShipperAuth;
    public PersonDriverAuth = PersonDriverAuth;
    public ShipperCompanyAuth = ShipperCompanyAuth;
    public TransportCompanyAuth = TransportCompanyAuth;
    public DeliveryBoyAuth = DeliveryBoyAuth;
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

      }


    gotoAnyWhere(page) {
      this.nav.push(page);
    }
}
