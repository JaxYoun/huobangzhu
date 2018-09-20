import { Component } from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams, ViewController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';


@Component({
  templateUrl: 'modals.html'
})
export class FullModals {
  public character; // 当前模态框信息
  public pickUp:Array<{}> = new Array;
  public areaJsonArray;

  private addAddressUrl: string = "/api/user/app/address/create";//添加常用地址
  private editAddressUrl: string = "/api/user/app/address/update";//更新常用地址
  public helperService:HelperService;
  public destAddress:String="";
  public contactPerson:String="";
  public contactTelephone:String="";
  public flag;//判断是取货还是送货flag

  public destInfo;
  public destInfoName;

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
    this.editAddressUrl = this.helperService.api + this.editAddressUrl;
    

    // 模拟的数据，需要清除掉
    let characters = {
      pickUp: { title: '取货信息', visible: false, type: 'pickUp' },
      deliver: { title: '送货信息', visible: false, type: 'deliver' },
    };
    // 设置模态框信息
    this.character = characters[this.params.get('type')];
    this.areaJsonArray = this.params.get('areaJsonArray');
    // this.defaultPickUpAddressId = this.params.get('defaultPickUpAddressId');
    // this.defaultDeliverAddressId = this.params.get('defaultDeliverAddressId');
    this.character.visible = true;

    this.flag = this.params.get('type');
    
   if(this.params.get('addOrEdit') == 'add'){
    this.addNew();
   }
   if(this.params.get('addOrEdit') == 'edit'){
    this.goEdit(this.params.get('addr'));
   }

  }


  // 关闭模态框
  dismiss() {
    this.viewCtrl.dismiss();
  }

  //添加地址
  addNew(){
    this.character = { title: '新增地址信息', visible: true, type: 'new' ,saveType:'add'};
    this.destAddress ="";
    this.contactPerson ="";
    this.contactTelephone = "";
  }


  // 保存地址
  saveNew(saveType,addRessId){
    if (this.contactPerson == '' || this.contactPerson == null) {
      this.helperService.presentToast('联系人必填');
      return;
    }
    if (this.contactTelephone == '' || this.contactTelephone == null) {
      this.helperService.presentToast('联系电话必填');
      return;
    }
    if(!this.helperService.checkPhoneFormat(this.contactTelephone.toString())){
      this.helperService.presentToast('联系电话有误');
      return;
    }
    if (this.destInfo == '' || this.destInfo == null) {
      this.helperService.presentToast('联系地址必填');
      return;
    }
    if (this.destAddress == '' || this.destAddress == null) {
      this.helperService.presentToast('详细地址必填');
      return;
    }


   //type:1-收货 2-发货
   let type;
   if(this.flag == 'deliver'){
      type = 2;
   }
   if(this.flag == 'pickUp'){
      type = 1;
   }
   if(saveType == 'add'){
      this.http.post(this.addAddressUrl,
        {destX:0,
        destY:0,
        type:type,
        areaCode:this.destInfo.outCode,
        linker:this.contactPerson,
        telephone:this.contactTelephone,
        destAddress:this.destAddress
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
        destX:0,
        destY:0,
        type:type,
        // idefault:idefault,
        areaCode:this.destInfo.outCode,
        linker:this.contactPerson,
        telephone:this.contactTelephone,
        destAddress:this.destAddress
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
  goEdit(src){
    this.character = { title: '编辑地址信息', visible: true, type: 'new' ,saveType:'edit',addRessId:src.id};
    this.destAddress = src.destAddress;
    this.contactPerson = src.linker;
    this.contactTelephone = src.telephone;
    this.destInfoName = this.helperService.buildAreaLocation(src.area.Level1AreaName,src.area.Level2AreaName,src.area.Level3AreaName);
    this.destInfo = src.area;
  }


    // 联系地址选择
    onChangeAddress(res) {
      this.destInfo = res;
    }
}
