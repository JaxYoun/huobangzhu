import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { SendList } from '../../person/sendOrder/sendList';
import { SpecialLineModals } from './modals';
import { Config } from '../../../app/config';

@Component({
  selector: 'page-specialLine',
  templateUrl: 'specialLine.html',
})
export class SpecialLine {
  private createUrl: string = "/api/driverLine/create";//专线发布
  private queryCarSizeUrl: string = "/api/transSize/all";//车长规格数据
  public helperService:HelperService;

  //车长数组
  public transArray;
  public transArrayString="";
  //区域数组
  // public areaJsonArray;
  // //货物发站
  // public provinceSendList:Array<{}> = new Array;
  // public citySendList:Array<{}>= new Array;
  // public countySendList:Array<{}>= new Array;
  // public provinceSend="";
  // public citySend = "";
  // public countySend = "";
  // //货物达到
  // public provinceArriveList:Array<{}> = new Array;
  // public cityArriveList:Array<{}>= new Array;
  // public countyArriveList:Array<{}>= new Array;
  // public provinceArrive="";
  // public cityArrive = "";
  // public countyArrive = "";

  public sendInfoName="";
  public arriveInfoName="";

  public obj:any={
    "transSizes":Array,
    "maxLoad":"",
    "unitPrices":"",
    "originAreaCode":"",
    "destAreaCode":""
    };
  public transTypeSelect:string = "UNLIMITED";
  public transType={};

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
        this.createUrl = this.helperService.api + this.createUrl;
        this.queryCarSizeUrl = this.helperService.api + this.queryCarSizeUrl;

        // this.storage.get("areaJson").then(
        //   (data)=>{
        //     this.areaJsonArray = data;
        //     //初始加载所有省
        //     this.areaJsonArray.forEach(element => {
        //       if(element.level == 1){//level=1直辖市、省
        //         this.provinceArriveList.push(element);
        //         this.provinceSendList.push(element);
        //       }
        //     });
        //   });


        this.storage.get("transType").then(
          (data)=>{
            this.transType = data;
          });

        this.queryCarSize();
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
    if (this.sendInfoName == '' || this.sendInfoName == null) {
      this.helperService.presentToast('货物发站必填');
      return false;
    }
    if (this.arriveInfoName == '' || this.arriveInfoName == null) {
      this.helperService.presentToast('货物达到必填');
      return false;
    }
    if (this.obj.maxLoad == '' || this.obj.maxLoad == null) {
      this.helperService.presentToast('最低载重必填');
      return false;
    }
    if (!this.obj.transSizes[0]) {
      this.helperService.presentToast('车长必填');
      return false;
    }
    if (this.obj.unitPrices == '' || this.obj.unitPrices == null) {
      this.helperService.presentToast('单价必填');
      return false;
    }
    return true;
  }

  fslCreate(){
    if(this.buildObj()){
      this.obj.transType = this.transTypeSelect;
      this.obj.originAreaCode = this.sendInfoName;
      this.obj.destAreaCode = this.arriveInfoName;

      this.http.post(this.createUrl, this.obj, this.helperService.getOptions())
        .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            let toast = this.toastCtrl.create({
              message: "保存专线订单成功",
              duration: 5000,
              position: 'top'//'bottom'
            });
            toast.present();
            this.nav.push(SendList);
          } else {
            this.helperService.presentToast('专线订单创建失败');
          }
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


  // onSelectProvince(type){
  //   let province="";
  //   if(type == 'send') {
  //     province = this.provinceSend;
  //     this.citySendList = [];
  //     this.countySendList = [];
  //   }
  //   if(type == 'arrive') {
  //     province = this.provinceArrive;
  //     this.cityArriveList = [];
  //     this.countyArriveList = [];
  //   }
  //   this.areaJsonArray.forEach(element => {
  //     if(element.parent){
  //       if(element.parent.id == province){
  //         if(type == 'send') {
  //           this.citySendList.push(element);
  //         }
  //         if(type == 'arrive') {
  //           this.cityArriveList.push(element);
  //         }
  //       }
  //     }
  //   });
  // }

  // onSelectCity(type){
  //   let city="";
  //   if(type == 'send') {
  //     city = this.citySend;
  //     this.countySendList = [];
  //   }
  //   if(type == 'arrive') {
  //     city = this.cityArrive;
  //     this.countyArriveList = [];
  //   }
  //   this.areaJsonArray.forEach(element => {
  //     if(element.parent){
  //       if(element.parent.id == city){
  //         if(type == 'send') {
  //           this.countySendList.push(element);
  //         }
  //         if(type == 'arrive') {
  //           this.countyArriveList.push(element);
  //         }
  //       }
  //     }
  //   });
  // }



    // 联系地址选择
    onChangeAddressSend(res) {
      this.sendInfoName = res.outCode;
    }
    // 联系地址选择
    onChangeAddressArrive(res) {
      this.arriveInfoName = res.outCode;
    }

}
