import { Component } from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams, ViewController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { HelpMeBuyMapModals } from '../helpMeBuy/mapModals';


@Component({
  templateUrl: 'linkModal.html'
})
export class LinkModal {
  // public exWeightemArry;
  // public commodityClassObj;
  // public isShow=false;
  public mapObj = {destX:"",destY:""};
  public obj = {
    linker:'',
    linkTelephone:'',
    location:'',
    address:'',
    orderType:'EX',
    areaCode:75,
    lng:0,
    lat:0,
    usein:'',
    type:'',
    id:""
  }
  public area;

  public destInfo;
  public destInfoName;
  

  public helperService;
  public type;
  private createLinkUrl: string = "/api/user/app/link/create";//创建
  private updateLinkUrl: string = "/api/user/app/link/update";//更新
  public addrTagArray = [
    {value:'1',name:'未知',isSelected:false},
    {value:'2',name:'家',isSelected:false},
    {value:'3',name:'公司',isSelected:false},
    {value:'4',name:'常用地',isSelected:false},
  ]

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
    this.createLinkUrl = this.helperService.api + this.createLinkUrl;
    this.updateLinkUrl = this.helperService.api + this.updateLinkUrl;
    
    this.obj.usein = params.get("usein");
    this.type = params.get("type");
    if(this.type == 'edit'){
      this.obj = params.get("obj");
      this.area = params.get("area");
      this.destInfo = this.area;
      this.destInfoName =  this.area.level1Name +"-"+ this.area.level2Name +"-"+ this.area.level3Name;
      //回填地址标签
      let item = {value:this.obj.type}
      this.goSelectBack(item);
    }

  }

  dismiss(){
    this.viewCtrl.dismiss();
  }


  // 保存地址
  saveNew(saveType){
    if (this.obj.linker == '' || this.obj.linker == null) {
      this.helperService.presentToast('姓名必填');
      return;
    }
    if (this.obj.linkTelephone == '' || this.obj.linkTelephone == null) {
      this.helperService.presentToast('联系电话必填');
      return;
    }
    if(!this.helperService.checkPhoneFormat(this.obj.linkTelephone.toString())){
      this.helperService.presentToast('联系电话有误');
      return;
    }
    if (this.destInfo == '' || this.destInfo == null) {
      this.helperService.presentToast('联系地址必填');
      return;
    }
    if (this.obj.address == '' || this.obj.address == null) {
      this.helperService.presentToast('详细地址必填');
      return;
    }
    if (this.obj.type == '' || this.obj.type == null) {
      this.helperService.presentToast('地址标签必填');
      return;
    }
    
    
    if(saveType == 'add'){
      this.obj.areaCode = this.destInfo.outCode;
      this.http.post(this.createLinkUrl, this.obj, this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.helperService.presentToast("保存成功");
            this.viewCtrl.dismiss({id:"id"});
          } else {
            this.helperService.presentToast('查询常用地址失败');
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
    }
    if(saveType == 'edit'){
      this.obj.areaCode = this.destInfo.outCode;
      this.http.post(this.updateLinkUrl, this.obj, this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.helperService.presentToast("保存成功");
            this.viewCtrl.dismiss({id:"id"});
          } else {
            this.helperService.presentToast('查询常用地址失败');
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
    }
  }



  openMapModal(){
    let modal = this.modalCtrl.create(HelpMeBuyMapModals,{obj:this.mapObj});
    modal.onDidDismiss((item) => {
          if(item && item.lat){
            this.obj.lng = item.lng;
            this.obj.lat = item.lat;
            this.obj.location = item.title;
            this.mapObj.destX = item.lng;
            this.mapObj.destY = item.lat;
          }
    });
    modal.present();
  }

  //地址标签选择
  goSelectBack(src){
    this.addrTagArray.forEach(element => {
      if(element.value == src.value){
        element.isSelected = true;
        this.obj.type = element.value;
      } else {
        element.isSelected = false;
      }
    });
  }


  // 联系地址选择
  onChangeAddress(res) {
    this.destInfo = res;
  }
}
