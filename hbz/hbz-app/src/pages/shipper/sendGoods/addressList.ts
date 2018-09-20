import {Component} from '@angular/core';
import {Platform, NavController,ViewController, ToastController,AlertController,ModalController, LoadingController,NavParams } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import {Http}from '@angular/http';
import { Config } from '../../../app/config';
import { LinkModal } from './linkModal';


@Component({
  selector: 'address-list',
  templateUrl: 'addressList.html'
})
export class AddressList {
  private queryLinkUrl: string = "/api/user/app/link/query";//查询
  private delLinkUrl: string = "/api/user/app/link/delete";//删除
  private defaultLinkUrl: string = "/api/user/app/link/setDefault";//设为默认
  public helperService:HelperService;

  public usein;//1、发货    2、收货
  public defaultAddressId;//默认地址
  public addressArray;

  constructor(
    public params: NavParams,
    private nav:NavController,
    public viewCtrl: ViewController,
    public alertCtrl: AlertController,
    public modalCtrl: ModalController,
    public loadingCtrl: LoadingController,
    public toastCtrl: ToastController,
    public platform:Platform,
    private http:Http,
    public config:Config,
    public storage: Storage) {
      this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
      this.queryLinkUrl = this.helperService.api + this.queryLinkUrl;
      this.delLinkUrl = this.helperService.api + this.delLinkUrl;
      this.defaultLinkUrl = this.helperService.api + this.defaultLinkUrl;

      this.usein = params.get("usein");
      this.defaultAddressId = params.get("defaultAddressId");

      this.queryAddressInfo();
    }

  // "BUY" : "帮买",  "SEND" : "帮送", "FSL" : "整车专线", "LTL" : "零担专线", "EX" : "快递", "FAC" : "找车"
  //查询常用地址
  queryAddressInfo(){
    this.http.post(this.queryLinkUrl, {orderType:'EX',usein:this.usein}, this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          result.data.forEach(element => {
            if(element.id == this.defaultAddressId){
              element.isSelected = true;
            } else {
              element.isSelected = false;
            }
          });
          this.addressArray = result.data;
        } else {
          this.helperService.presentToast('查询常用地址失败');
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      });
  }

  //新增常用地址
  gotoAdd(type){
    let modal = this.modalCtrl.create(LinkModal, {
      usein:this.usein,
      type:type
    });
    modal.onDidDismiss((item) => {
          if(item){
            this.queryAddressInfo();
          }
    });
    modal.present();
  }
  //编辑常用地址
  gotoEdit(type,src){
    let modal = this.modalCtrl.create(LinkModal, {
      usein:this.usein,
      obj:{
          linker:src.linker,
          linkTelephone:src.linkTelephone,
          location:src.location,
          address:src.address,
          orderType:src.orderType,
          areaCode:src.area.outCode,
          lng:src.lng,
          lat:src.lat,
          usein:src.usein,
          type:src.type,
          id:src.id
      },
      area:src.area,
      type:type
    });
    modal.onDidDismiss((item) => {
          if(item){
            this.queryAddressInfo();
          }
    });
    modal.present();
  }


  //设为默认
  setDefaultLink(id){
    this.http.post(this.defaultLinkUrl, {id:id}, this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          this.addressArray.forEach(element => {
            if(element.id == id){
              element.isSelected = true;
            } else {
              element.isSelected = false;
            }
          });

          this.queryAddressInfo();
          
        } else {
          this.helperService.presentToast('设为默认地址失败');
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      });
  }

  //删除常用地址
  delLink(id){
    this.http.post(this.delLinkUrl, {id:id}, this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.queryAddressInfo();
            this.helperService.presentToast('删除成功');
          } else {
            this.helperService.presentToast('设为默认地址失败');
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
  }

  selectBack(item){
    this.viewCtrl.dismiss({
        obj:item,
        usein:this.usein
      });
  }
  dismiss(){
    this.viewCtrl.dismiss();
  }
}
