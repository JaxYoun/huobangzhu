import {Component} from '@angular/core';
import {ActionSheetController,Platform, NavController, Loading,ToastController,AlertController, LoadingController, ModalController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';

@Component({
  selector: 'page-fulltransport',
  templateUrl: 'myWarehouseDetail.html'
})

export class MyWarehouseDetail {
    public helperService:HelperService;
    public title:string = '仓储信息详情';
    public certificates;//照片
    
    public obj = {
      id:'',
      name:'',
      capacity:'',
      unitPrice:'',
      minRentTime:'',
      ownerName:'',
      telephone:'',
      address:'',
      coordX:'',
      coordY:'',
      warehouseDescribe:'',
      titleImageList:''
    };
    public mapObj = {
      destX:'',
      destY:''
    }


    constructor(
        private nav:NavController,
        public params: NavParams,
        public alertCtrl: AlertController,
        public loadingCtrl: LoadingController,
        public toastCtrl: ToastController,
        public modalCtrl: ModalController,
        public platform:Platform,
        private http: Http,
        public actionSheetCtrl: ActionSheetController,
        public storage: Storage,
        public config:Config) {
          this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
          
            this.obj = params.get("obj");
            this.mapObj = params.get("mapObj");
            this.certificates = this.obj.titleImageList;
            this.obj.titleImageList = '[' + this.obj.titleImageList + ']';
            
          }

      // openMapModal(){
      //   let modal = this.modalCtrl.create(HelpMeBuyMapModals,{obj:this.mapObj});
      //   modal.onDidDismiss((item) => {
      //         if(item && item.lat){
      //           this.obj.coordX = item.lng;
      //           this.obj.coordY = item.lat;
      //           this.mapObj.destX = item.lng;
      //           this.mapObj.destY = item.lat;
      //           this.obj.address = item.title;
      //         }
      //   });
      //   modal.present();
      // }

    


}
