import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController,NavParams, ViewController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { SpecialLineModals } from '../../driver/specialLine/modals';
import { Config } from '../../../app/config';

@Component({
  templateUrl: 'carInfo.html',
})
export class CarInfo {
  private addUrl: string = "/api/vehicleInformation/addVehicleInformation";
  private updateUrl: string = "/api/vehicleInformation/updateVehicleInformation";
  public createUrl;
  private queryCarSizeUrl: string = "/api/transSize/all";//车长规格数据
  public helperService:HelperService;

  //车长数组
  public transArray;
  public transArrayString="";


  public obj:any={
    "id":"",
    "transSizes":Array,
    "maxLoad":"",
    "plateNumber":"",
    "transType":"",
    "vehicleDescription":""
    };
  public transTypeSelect:string = "UNLIMITED";
  public transType={};
  public title;
  public type;
  
  constructor(
      private nav:NavController,
      public params: NavParams,
      public alertCtrl: AlertController,
      public viewCtrl: ViewController,
      public loadingCtrl: LoadingController,
      public toastCtrl: ToastController,
      public modalCtrl: ModalController,
      public platform:Platform,
      private http: Http,
      public storage: Storage,
      public config:Config) {

        this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
        this.addUrl = this.helperService.api + this.addUrl;
        this.updateUrl = this.helperService.api + this.updateUrl;
        this.queryCarSizeUrl = this.helperService.api + this.queryCarSizeUrl;
        
        this.type = params.get('type');
        if(this.type == 'edit'){
          this.title = "编辑车辆信息"
          this.createUrl = this.updateUrl;
          this.obj = params.get('item');
          this.transTypeSelect = this.obj.transType;
          this.obj.transSizes.forEach(element => {
            this.transArrayString += element + ",";
          });          
        } else{
          this.title = "新增车辆信息"
          this.createUrl = this.addUrl;
        }
        
        this.storage.get("transType").then(
          (data)=>{
            this.transType = data;
          });

        this.queryCarSize();
  }
  // 关闭模态框
  dismiss() {
    this.viewCtrl.dismiss();
  }
  //查询车长规格数据
  queryCarSize(){
    this.http.post(this.queryCarSizeUrl, {}, this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          this.transArray = result.data;
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      });
  }

  buildObj(){
    if (this.obj.plateNumber == '' || this.obj.plateNumber == null) {
      this.helperService.presentToast('车牌必填');
      return false;
    }
    if (this.obj.maxLoad == '' || this.obj.maxLoad == null) {
      this.helperService.presentToast('载重必填');
      return;
    } else {
      if (!/^(([1-9][0-9]*)|(([0]\.\d{0,2}|[1-9][0-9]*\.\d{0,2})))$/.test(this.obj.maxLoad)) {
        this.helperService.presentToast('载重最多为两位小数的正数');
        return;
      }
    }
    if (!this.obj.transSizes[0]) {
      this.helperService.presentToast('车长必填');
      return false;
    }
    return true;
  }

  fslCreate(){
    if(this.buildObj()){
      this.obj.transType = this.transTypeSelect;
      this.http.post(this.createUrl, this.obj, this.helperService.getOptions())
        .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            if(this.type == 'edit'){
              this.helperService.presentToast("更新车辆信息成功");
            } else {
              this.helperService.presentToast("保存车辆信息成功");
            }
          } else {
            if(this.type == 'edit'){
              this.helperService.presentToast("更新车辆信息失败");
            } else {
              this.helperService.presentToast("保存车辆信息失败");
            }
          }
          this.viewCtrl.dismiss();
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });

    }
  }

  // 弹出框
  openModal() {
    let modal = this.modalCtrl.create(SpecialLineModals,{transArray:this.transArray,transArrayString:this.transArrayString});
    modal.onDidDismiss((item) => {
          if(item){
              this.transArrayString = "";
              this.obj.transSizes = item.transSizes;
              for(let i=0;i<item.transSizes.length;i++){
                if(i == item.transSizes.length-1){
                  this.transArrayString += item.transSizes[i];
                } else {
                  this.transArrayString += item.transSizes[i] + ",";
                }
              }
          }
    });
    modal.present();
  }

}
