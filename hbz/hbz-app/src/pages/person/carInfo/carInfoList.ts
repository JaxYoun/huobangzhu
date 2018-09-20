import { Component } from '@angular/core';
import {
  Platform,
  NavController,
  ToastController,
  AlertController,
  LoadingController,
  ModalController,
} from 'ionic-angular';
import { Http } from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { SpecialLineModals } from '../../driver/specialLine/modals';
import { Config } from '../../../app/config';
import { CarInfo } from './carInfo';

@Component({
  templateUrl: 'carInfoList.html',
})
export class CarInfoList {
  private queryUrl: string = '/api/vehicleInformation/queryPage';
  public helperService: HelperService;

  public currentUser:any = {};

  public carList = [];
  public huocheImg = "assets/icon/huoche.png";
  

  constructor(
    private nav: NavController,
    public alertCtrl: AlertController,
    public loadingCtrl: LoadingController,
    public toastCtrl: ToastController,
    public modalCtrl: ModalController,
    public platform: Platform,
    private http: Http,
    public storage: Storage,
    public config: Config,
  ) {
    this.helperService = new HelperService(
      nav,
      alertCtrl,
      loadingCtrl,
      toastCtrl,
      storage,
      http,
      config,
    );
    this.queryUrl = this.helperService.api + this.queryUrl;

    this.storage.get('currentUser').then(data => {
      this.currentUser = data;
      console.log(data);
    });
    this.storage.get('currentRoles').then(data => {
      // {role: "EnterpriseConsignor", roleName: "货主企业", id: 1, createdDate: "1970-01-01"}
      // {role: "EnterpriseAdmin", roleName: "运输企业", id: 2, createdDate: "1970-01-01"}
      // {role: "Consignor", roleName: "个人货主", id: 3, createdDate: "1970-01-01"}
      // {role: "PersionDriver", roleName: "司机", id: 4, createdDate: "1970-01-01"}
      // {role: "DeliveryBoy", roleName: "配送员", id: 5, createdDate: "1970-01-01"}
      // {role: "EnterpriseAssist", roleName: "企业助理", id: 6, createdDate: "1970-01-01"}
      // 判断： 【司机、配送员】 角色用户                   显示个人司机
      // 权限判断： 【运输企业、企业助理】 角色用户   显示运输企业
      let flag = true;
      for (let i = 0; i < data.length; i++) {
        let element = data[i];
        if (flag) {
          if (
            element.role == 'EnterpriseAdmin' ||
            element.role == 'EnterpriseAssist'
          ) {
            this.currentUser.driverType = 'enterprise';
            this.currentUser.driverTypeName = element.roleName;
            flag = false;
            break;
          } else if (
            element.role == 'PersonDriver' ||
            element.role == 'DeliveryBoy'
          ) {
            this.currentUser.driverType = 'driver';
            this.currentUser.driverTypeName = element.roleName;
            flag = true;
          }
        }
      }
    });

    this.queryCars();
  }

  //查询车辆信息
  queryCars() {
    this.http
      .post(this.queryUrl, {}, this.helperService.getOptions())
      .subscribe(
        data => {
          let result = data.json();
          if (result.code == '200') {
            console.log(result);
            this.carList = result.data.content;
          }
        },
        err => {
          this.helperService.presentToast(
            '网络请求失败,请确保你的网络环境正常',
          );
        },
      );
  }

  // 弹出框
  saveOrUpdate(type, item) {
    let modal = this.modalCtrl.create(CarInfo, { type: type, item: item });
    modal.onDidDismiss(item => {
      this.queryCars();
    });
    modal.present();
  }
}
