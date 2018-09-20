import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController ,NavParams} from 'ionic-angular';
import { Storage } from '@ionic/storage';
import {Http}from '@angular/http';
import { HelperService } from '../../app/services/help.service';
import { SelectCarByOrders } from '../shipper/selectCarByOrders';
import { Config } from '../../app/config';

@Component({
  selector: 'SelectDriver',
  templateUrl: 'selectDriver.html'
})
export class SelectDriver {
  public drivers:any=[];
  public isSelectId="";
  public helperService:HelperService;
  private consignorTakeUrl: string = "/api/order/taker/consignor/take";//中标

  // public newDrivers=[];
  
  constructor(
    public params: NavParams,
    private nav:NavController,
    public alertCtrl: AlertController,
    public loadingCtrl: LoadingController,
    public toastCtrl: ToastController,
    public platform:Platform,
    private http:Http,
    public storage: Storage,
    public config:Config) {
      this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
      this.consignorTakeUrl = this.helperService.api + this.consignorTakeUrl;
      params.get('drivers').forEach(element => {
          element.isSelected = false;
          this.storage.get("transType").then(
            (data)=>{
              element.order.transType = this.helperService.getValueByJson(element.order.transType, data);
            });
          console.log(element);

          // 需要处理司机、企业类型（判断只有两种类型[运输企业TransEnterprise、个人司机PersonDriver]，其他类型没有资格）
          if(element.user.TransEnterprise){
            //同时满足企业和个人，则显示企业信息
            element.showName = element.user.TransEnterprise.organizationName;
            element.type = 'TransEnterprise';
          }else{
            //显示个人信息
            element.showName = element.user.nickName;
            // element.showLicensePlateNumber = element.user.PersonDriver.licensePlateNumber;
            element.type = 'PersonDriver';
          }
          this.drivers.push(element);
      });
    }

    onChange(item){
      this.isSelectId = item.id;
      this.drivers.forEach(element => {
        if(element.id == item.id){
          element.isSelected = true;
        } else {
          element.isSelected = false;
        }
    });
    }

    confirm() {
      if (this.isSelectId == '') {
        this.helperService.presentToast('请选择司机');
        return false;
      }
       this.http.post(this.consignorTakeUrl,{id:this.isSelectId},this.helperService.getOptions())
       .subscribe((data)=> {
           let result = data.json();
           if (result.code == '200') {
             this.nav.push(SelectCarByOrders);
           }
         },
         (err)=> {
           this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
         });
    }

}
