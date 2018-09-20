import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { PersonShipperAuth } from './personShipperAuth';
import { PersonDriverAuth } from './personDriverAuth';
import { ShipperCompanyAuth } from './shipperCompanyAuth';
import { TransportCompanyAuth } from './transportCompanyAuth';
import { DeliveryBoyAuth } from './deliveryBoyAuth';
import { Config } from '../../../app/config';


@Component({
  selector: 'person',
  templateUrl: 'person.html'
})

export class Person {
    private queryMyRolesUrl: string = "/api/user/roles";//我的角色列表
    private registryUrl: string = "/api/user/app/registry/typed/registry";//注册进度查询
    public helperService:HelperService;
    // public roleListArray = [
    //   {role:'Consignor',roleName:'个人货主',isAuth:false},
    //   {role:'PersionDriver',roleName:'个人司机',isAuth:false},
    //   {role:'EnterpriseConsignor',roleName:'货主企业',isAuth:false},
    //   {role:'EnterpriseAdmin',roleName:'运输企业',isAuth:false},
    //   {role:'DeliveryBoy',roleName:'配送员',isAuth:false}];

    public registryArray=[];

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
          this.queryMyRolesUrl = this.helperService.api + this.queryMyRolesUrl;
          this.registryUrl = this.helperService.api + this.registryUrl;

          this.http.post(this.registryUrl,{},this.helperService.getOptions())
          .subscribe((data)=> {
              let result = data.json();
              if (result.code == '200') {
                this.registryArray = result.data;
                this.registryArray.forEach(element =>{
                  //注册状态：RegistryProgress: UN_DO("未注册"),UN_REGISTER("审核中"),REGISTRYED("已注册"),ERR_REGISTER("注册失败");
                  switch (element.authType) {
                    case 'UN_DO':
                      element.flag = false;
                      element.authTypeChi = "未注册";
                      break;
                    case 'UN_REGISTER':
                      element.flag = false;
                      element.authTypeChi = "审核中";
                      break;
                    case 'REGISTRYED':
                      element.flag = true;
                      element.authTypeChi = "已注册";
                      break;
                    case 'ERR_REGISTER':
                      element.flag = false;
                      element.authTypeChi = "注册失败";
                      break;
                    default:
                      break;
                  }  
                })
              }
            },
            (err)=> {
              this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });
      }


    //去认证
    goToAuth(item){

      if(item.authType == 'UN_REGISTER'){
        //审核中，不能进入修改
        this.helperService.presentToast("审核中");
        return;
      }

      switch (item.key) {
        case 'PersonConsignor':
          this.nav.push(PersonShipperAuth);
          // this.nav.push(PersonShipperAuth,{certificates:this.certificates,certifiedPhoto:this.certifiedPhoto});
          break;
        case 'PersonDriver':
          this.nav.push(PersonDriverAuth);
          break;
        case 'EnterpriseConsignor':
          this.nav.push(ShipperCompanyAuth);
          break;
        case 'TransEnterprise':
          this.nav.push(TransportCompanyAuth);
          break;
        case 'DeliveryBoy':
          this.nav.push(DeliveryBoyAuth);
          break;
        default:
          break;
      }
    }
}
