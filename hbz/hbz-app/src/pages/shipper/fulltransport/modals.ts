import { Component } from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams, ViewController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';


@Component({
  templateUrl: 'modals.html'
})
export class FulltransportModals {
  public character; // 当前模态框信息
  public historyModal:object = {};  // 记录上一次模态框信息
  // public pickUp = x; // 模拟用的数据，需要清除掉
  // public pickUp = [{destAddress: '四川省，成都市'}]; // 模拟用的数据，需要清除掉
  public pickUp:Array<{}> = new Array;
  public areaJsonArray;
  public province="";
  public provinceList:Array<{}> = new Array;
  public city = "";
  public cityList:Array<{}>= new Array;
  public county = "";
  public countyList:Array<{}>= new Array;

  private addAddressUrl: string = "/api/user/app/address/create";//添加常用地址
  private queryAddressUrl: string = "/api/user/app/address/query";//查询常用地址
  private editAddressUrl: string = "/api/user/app/address/update";//更新常用地址
  public helperService:HelperService;
  public destAddress:String="";
  public contactPerson:String="";
  public contactTelephone:String="";
  public defaultPickUpAddressId;
  public defaultDeliverAddressId;
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
    this.queryAddressUrl = this.helperService.api + this.queryAddressUrl;
    this.editAddressUrl = this.helperService.api + this.editAddressUrl;
    

    // 模拟的数据，需要清除掉
    let characters = {
      pickUp: { title: '取货信息', visible: false, type: 'pickUp' },
      deliver: { title: '送货信息', visible: false, type: 'deliver' },
    };
    // 设置模态框信息
    this.character = characters[this.params.get('type')];
    this.areaJsonArray = this.params.get('areaJsonArray');
    this.defaultPickUpAddressId = this.params.get('defaultPickUpAddressId');
    this.defaultDeliverAddressId = this.params.get('defaultDeliverAddressId');
    this.character.visible = true;
    this.historyModal = this.character;

    this.flag = this.params.get('type');
    this.queryAddressInfo(this.flag);
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
            this.queryAddressInfo(this.flag);
            // 返回上一次模态框
            this.character = this.historyModal
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
      let idefault = 0;
      if(addRessId == this.defaultPickUpAddressId){
        idefault = 1;
      }
      this.http.post(this.editAddressUrl,
        {id:addRessId,
        destX:0,
        destY:0,
        type:type,
        idefault:idefault,
        areaCode:this.destInfo.outCode,
        linker:this.contactPerson,
        telephone:this.contactTelephone,
        destAddress:this.destAddress
        }, this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.queryAddressInfo(this.flag);
            // 返回上一次模态框
            this.character = this.historyModal
          } else {
            this.helperService.presentToast('更新常用地址失败');
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
    }
  }

  goSelectBack(type,src){
    let itemId = src.id;
    if(type == 'pickUp'){
      // let pickUpAddress = src.area.Level1AreaName+"-"+src.area.Level2AreaName+"-"+src.area.Level3AreaName;
      let pickUpAddress = this.helperService.buildAreaLocation(src.area.Level1AreaName,src.area.Level2AreaName,src.area.Level3AreaName);
      let pickUpAddressInfo = src.destAddress;
      let pickUpAddressId = src.area.outCode;
      this.viewCtrl.dismiss({defaultPickUpAddressId:itemId,type:'pickUp',
            pickUpAddress:pickUpAddress,pickUpAddressInfo:pickUpAddressInfo,
            pickUpAddressId:pickUpAddressId,pickUpOutCode:src.area.outCode,
            contactPerson:src.linker,contactTelephone:src.telephone});
    }
    if(type == 'deliver'){
      // let deliverAddress = src.area.Level1AreaName+"-"+src.area.Level2AreaName+"-"+src.area.Level3AreaName;
      let deliverAddress = this.helperService.buildAreaLocation(src.area.Level1AreaName,src.area.Level2AreaName,src.area.Level3AreaName);
      let deliverAddressInfo = src.destAddress;
      let deliverAddressId = src.area.outCode;
      this.viewCtrl.dismiss({defaultDeliverAddressId:itemId,type:'deliver',
          deliverAddress:deliverAddress,deliverAddressInfo:deliverAddressInfo,
          deliverAddressId:deliverAddressId,deliverOutCode:src.area.outCode,
          contactPerson:src.linker,contactTelephone:src.telephone});
    }
  }

  //编辑地址
  goEdit(type,src){
    this.character = { title: '编辑地址信息', visible: true, type: 'new' ,saveType:'edit',addRessId:src.id};
    this.destAddress = src.destAddress;
    this.contactPerson = src.linker;
    this.contactTelephone = src.telephone;
    // this.destInfoName = src.area.Level1AreaName  +"-"+ src.area.Level2AreaName  +"-"+ src.area.Level3AreaName;
    this.destInfoName = this.helperService.buildAreaLocation(src.area.Level1AreaName,src.area.Level2AreaName,src.area.Level3AreaName);
    this.destInfo = src.area;
  }

  //查询常用地址
  queryAddressInfo(type){
    let defaultAddressId = "";
    let type2;
    if(type == "pickUp"){
      defaultAddressId = this.defaultPickUpAddressId;
      type2 = 1;
    }
    if(type == "deliver"){
      defaultAddressId = this.defaultDeliverAddressId;
      type2 = 2;
    }
    this.http.post(this.queryAddressUrl, {type:type2}, this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          result.data.forEach(element => {
            if(element.id == defaultAddressId){
              element.isSelected = true;
            } else {
              element.isSelected = false;
            }
            element.destLocation = this.helperService.buildAreaLocation(element.area.Level1AreaName,element.area.Level2AreaName,element.area.Level3AreaName);
          });
          this.pickUp = result.data;
        } else {
          this.helperService.presentToast('查询常用地址失败');
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      });
  }


    // 联系地址选择
    onChangeAddress(res) {
      this.destInfo = res;
    }
}
