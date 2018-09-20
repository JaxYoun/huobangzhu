import { Component } from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams, ViewController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { HelpMeBuyMapModals } from '../../shipper/helpMeBuy/mapModals';


@Component({
  templateUrl: 'helpModals.html'
})
export class HelpModals {
  public character; // 当前模态框信息
  public pickUp:Array<{}> = new Array;
  public orderType;
  public type;
  public obj:any={
    "destX":"",//经度
    "destY":"",//纬度
    // "destInfo":"",//坐标中文名称，联系地址
    "destAddress":"",//详细地址
    "linker":"",//联系人
    "linkTelephone":""//联系电话
    };
  public defaultPickUpAddressId;


  private addAddressUrl: string = "/api/user/app/link/create";//添加常用地址
  private queryAddressUrl: string = "/api/user/app/link/query";//查询常用地址
  private editAddressUrl: string = "/api/user/app/link/update";//更新常用地址
  public helperService:HelperService;

  public destInfo;
  public destInfoName;
  public title;

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
    this.addAddressUrl = this.helperService.api + this.addAddressUrl;
    this.queryAddressUrl = this.helperService.api + this.queryAddressUrl;
    this.editAddressUrl = this.helperService.api + this.editAddressUrl;

    this.type = params.get('type');
    this.orderType = params.get('addresType');
    // this.defaultPickUpAddressId = this.params.get('defaultPickUpAddressId');
    
    
    if(this.orderType == 'BUY'){
      this.title = '配送地址';
    }
    if(this.orderType == 'EX'){
      this.title = '快递地址';
    }

    // 设置模态框信息
    this.character = { title: this.title,type:'pickUp', visible: false }
    this.character.visible = true;

    if(this.params.get('addOrEdit') == 'add'){
      if(this.orderType == 'BUY'){
        this.addNew('新增配送地址');
      }
      if(this.orderType == 'EX'){
        this.addNew('新增快递地址');
      }
     }
     if(this.params.get('addOrEdit') == 'edit'){
      if(this.orderType == 'BUY'){
        this.goEdit(this.params.get('addr'),'编辑配送地址');
      }
      if(this.orderType == 'EX'){
        this.goEdit(this.params.get('addr'),'编辑快递地址');
      }
     }
  }


  // 关闭模态框
  dismiss() {
    this.viewCtrl.dismiss();
  }

  //添加地址
  addNew(titleName){
    this.character = { title: titleName, visible: true, type: 'new' ,saveType:'add'};
    this.obj.linker = "";
    this.obj.linkTelephone = "";
    this.destInfoName =  "";
    this.obj.destAddress = "";
    this.obj.destX="";
  }


  // 保存地址
  saveNew(saveType,addRessId){
    let usein;//1、发货    2、收货
    if(this.type == 'pickUp'){
      usein = 2;
    }
    if(this.type == 'deliver'){
      usein = 1;
    }
    if (this.obj.linker == '' || this.obj.linker == null) {
      this.helperService.presentToast('联系人必填');
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
    if (this.obj.destAddress == '' || this.obj.destAddress == null) {
      this.helperService.presentToast('详细地址必填');
      return;
    }
    if (this.obj.destX == '' || this.obj.destX == null) {
      this.helperService.presentToast('详细地址未从地图选取');
      return;
    }
    if(saveType == 'add'){
      this.http.post(this.addAddressUrl,
        {
          linker:this.obj.linker,
          linkTelephone:this.obj.linkTelephone,
          // location:this.obj.destInfo,
          areaCode:this.destInfo.outCode,
          lng:this.obj.destX,
          lat:this.obj.destY,
          address:this.obj.destAddress,
          type:1,
          usein:usein,
          orderType:this.orderType
        }, this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.dismiss();
          } else {
            this.helperService.presentToast('添加常用地址失败');
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
    }
    if(saveType == 'edit'){
      //默认地址不更新，也要传入，因为后台要求必须传入，不然会自动更新为空。
      // let idefault = 0;
      // if(addRessId == this.defaultPickUpAddressId){
      //   idefault = 1;
      // }
      this.http.post(this.editAddressUrl,
        {id:addRessId,
        linker:this.obj.linker,
        linkTelephone:this.obj.linkTelephone,
        // location:this.obj.destInfo,
        areaCode:this.destInfo.outCode,
        lng:this.obj.destX,
        lat:this.obj.destY,
        // idefault:idefault,
        address:this.obj.destAddress,
        type:1,
        orderType:this.orderType,
        usein:usein
        }, this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.dismiss();
          } else {
            this.helperService.presentToast('更新常用地址失败');
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
    }
  }


  //编辑地址
  goEdit(src, titleName){
    this.destInfo = src.area;
    this.destInfoName = src.location;
    this.character = { title: titleName, visible: true, type: 'new' ,saveType:'edit',addRessId:src.id};
    this.obj.linker = src.linker;
    this.obj.linkTelephone = src.linkTelephone;
    this.obj.destAddress = src.address;
    this.obj.destX = src.lng;//经度
    this.obj.destY = src.lat;//纬度
  }


  openMapModal(){
    let modal = this.modalCtrl.create(HelpMeBuyMapModals,{obj:this.obj});
    modal.onDidDismiss((item) => {
          if(item && item.lat){
            this.obj.destX = item.lng;
            this.obj.destY = item.lat;
            this.obj.destAddress = item.title;
          }
    });
    modal.present();
  }

  // 联系地址选择
  onChangeAddress(res) {
    this.destInfo = res;
    console.log('res', res);
  }
}
