import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController,ModalController, LoadingController,NavParams } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import {Http}from '@angular/http';
import {AddressList} from './addressList';
import {SelectGoodsModal} from './selectGoodsModal';
import { Config } from '../../../app/config';
import { PayOnline } from '../fulltransport/payOnline';


@Component({
  selector: 'send-goods',
  templateUrl: 'index.html'
})
export class SendGoods {
  public linkDeliverObj:any={};//寄件人
  public linkPickUpObj:any={};//收件人
  public originAreaCode="";
  public destAreaCode="";

  public isDeliverShowLink = false;
  public isPickUpShowLink = false;

  public goodsObj = {
    commodityClassSelected:'',
    exWeightemSelected:'',
    exWeightemSelected2:'',//超过20KG，自己填写
    commodityVolume:'',
    takeTime:'',
    commodityDesc:'',
    value:'',
    commodityName:'',
    relatedPictures:''
  }
  public exWeightemSelectedValue="";
  public isShow = false;
  public commodityClassSelectedName = "";
  public weightSelectedName = "";
  public amount;
  public commodityClassObj;
  public exWeightemArry;

  private saveOrderUrl: string = "/api/order/ex/create";//3.4.4.1	快递单创建
  private queryLinkUrl: string = "/api/user/app/link/query";//查询
  private priceUrl: string = "/api/order/ex/price/compute";//3.4.4.2	订单价格计算
  public helperService:HelperService;
  public defaultAddressId="";//默认地址
  public defaultDeliverAddressId="";//寄件人默认地址
  public defaultPickUpAddressId="";//收件人默认地址
  public defaultDeliverAddressIdValid="";
  public defaultPickUpAddressIdValid="";
  
  constructor(
    public params: NavParams,
    private nav:NavController,
    public alertCtrl: AlertController,
    public modalCtrl: ModalController,
    public loadingCtrl: LoadingController,
    public toastCtrl: ToastController,
    public platform:Platform,
    private http:Http,
    public config:Config,
    public storage: Storage) {
      this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
      this.saveOrderUrl = this.helperService.api + this.saveOrderUrl;
      this.queryLinkUrl = this.helperService.api + this.queryLinkUrl;
      this.priceUrl = this.helperService.api + this.priceUrl;

      this.queryDefaultLink();
      //不能用页面跳转，要整死个人，还是用模态框哦。

      this.storage.get("commodityClass").then(
        (data)=>{
          this.commodityClassObj = data;
        });
      this.storage.get("exWeightem").then(
        (data)=>{
          this.exWeightemArry = data;
        });
    }

    gotoList(usein) {
      // usein; 1、发货    2、收货
      if(usein == 1) {
        this.defaultAddressId = this.defaultDeliverAddressId;
      }
      if(usein == 2) {
        this.defaultAddressId = this.defaultPickUpAddressId;
      }
      let modal = this.modalCtrl.create(AddressList, {
        usein:usein,
        defaultAddressId:this.defaultAddressId
      });
      modal.onDidDismiss((item) => {
            if(item){
              //回填
              if(item.usein == 1){
                this.linkDeliverObj = item.obj;
                this.originAreaCode = item.obj.area.outCode;
                this.defaultDeliverAddressId = item.obj.id;
                this.isDeliverShowLink = true;
                this.defaultDeliverAddressIdValid = item.obj.id;
              }
              if(item.usein == 2){
                this.linkPickUpObj =  item.obj;
                this.destAreaCode = item.obj.area.outCode;
                this.defaultPickUpAddressId =item.obj.id;
                this.isPickUpShowLink = true;
                this.defaultPickUpAddressIdValid = item.obj.id;
              }
              //计算价格
              this.priceComputer();
            } else {
              if(this.defaultDeliverAddressIdValid){
                // console.log("===")
                this.validIdExist(this.defaultDeliverAddressIdValid, 1);
              }
              if(this.defaultPickUpAddressIdValid){
                // console.log("===1111")
                this.validIdExist(this.defaultPickUpAddressIdValid, 2);
              }
            }
      });
      modal.present();
    }


    //验证ID是否存在
    validIdExist(id, usein){
       this.http.post(this.queryLinkUrl, {id:id}, this.helperService.getOptions())
       .subscribe((data)=> {
         let result = data.json();
         if (result.code == '200') {
           if(result.data.length == 0){
              if(usein == 1) {
                this.isDeliverShowLink = false;
              }
              if(usein == 2) {
                this.isPickUpShowLink = false;
              }
           }
         } else {
           this.helperService.presentToast('查询常用地址失败');
         }
       },
       (err)=> {
         this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
       });
    }

    priceComputer(){

      if(this.originAreaCode == ''){
        return;
      }
      if(this.destAreaCode == ''){
        return;
      }
      this.exWeightemSelectedValue;
      if(this.goodsObj.exWeightemSelected == "32"){
        this.exWeightemSelectedValue = this.goodsObj.exWeightemSelected2;
      } else {
        this.exWeightemSelectedValue = this.goodsObj.value;
      }
      if(this.goodsObj.commodityClassSelected == ""){
        return;
      }

      this.http.post(this.priceUrl, {
        originAreaCode:this.originAreaCode,
        destAreaCode:this.destAreaCode,
        commodityWeight:this.exWeightemSelectedValue,
        commodityVolume:this.goodsObj.commodityVolume}, this.helperService.getOptions())
      .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          this.amount = result.data;
        } else {
          this.helperService.presentToast('查询常用地址失败');
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      });
    }

    queryDefaultLink(){
      //查询默认地址
      this.http.post(this.queryLinkUrl, {orderType:'EX',usein:1,idefault:1}, this.helperService.getOptions())
      .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.length == 0){
            this.defaultDeliverAddressId = "";
          } else {
            this.defaultDeliverAddressId = result.data[0].id;
          }
        } else {
          this.helperService.presentToast('查询常用地址失败');
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      });
      //查询默认地址
      this.http.post(this.queryLinkUrl, {orderType:'EX',usein:2,idefault:1}, this.helperService.getOptions())
      .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.length == 0){
            this.defaultPickUpAddressId = "";
          } else {
            this.defaultPickUpAddressId = result.data[0].id;
          }
        } else {
          this.helperService.presentToast('查询常用地址失败');
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      });
    }



    //选择货物
    selectGoods(){
      let modal = this.modalCtrl.create(SelectGoodsModal, {
        exWeightemArry:this.exWeightemArry,
        commodityClassObj:this.commodityClassObj,
        goodsObj:this.goodsObj,
        dateFlag:this.isShow
      });
      modal.onDidDismiss((item) => {
            if(item){
              this.goodsObj = item.obj;
              let weightObj = this.helperService.getValueByJsonArray("id",item.obj.exWeightemSelected,this.exWeightemArry);
              this.goodsObj.value = weightObj.val;
              this.isShow = true;
              this.weightSelectedName = weightObj.name;
              this.commodityClassSelectedName = this.helperService.getValueByJson(item.obj.commodityClassSelected,this.commodityClassObj);
              
              //计算价格
              this.priceComputer();
            }
      });
      modal.present();
    }


    confirm(){
      if (this.linkDeliverObj.linker == '' || this.linkDeliverObj.linker == null) {
        this.helperService.presentToast('寄件人必填');
        return;
      }
      if (this.linkPickUpObj.linker == '' || this.linkPickUpObj.linker == null) {
        this.helperService.presentToast('收件人必填');
        return;
      }
      if (this.goodsObj.exWeightemSelected == '' || this.goodsObj.exWeightemSelected == null) {
        this.helperService.presentToast('货物详情必填');
        return;
      }
      if (this.amount == '' || this.amount == null) {
        this.helperService.presentToast('预估费用必填');
        return;
      }


      let paramsObj:any={};
      //货物信息 
      paramsObj.commodityClass = this.goodsObj.commodityClassSelected;
      paramsObj.commodityDesc = this.goodsObj.commodityDesc;
      paramsObj.commodityVolume = this.goodsObj.commodityVolume;
      paramsObj.commodityWeight = this.exWeightemSelectedValue;
      paramsObj.orderTakeTime = this.goodsObj.takeTime;
      paramsObj.commodityName = this.goodsObj.commodityName;
      if(this.goodsObj.relatedPictures){
        paramsObj.relatedPictures = this.goodsObj.relatedPictures;
      }
      //发件人
      paramsObj.originLinker = this.linkDeliverObj.linker;
      paramsObj.originTelephone = this.linkDeliverObj.linkTelephone;
      paramsObj.originAddr = this.linkDeliverObj.address;
      paramsObj.originInfo = this.linkDeliverObj.location;
      paramsObj.originX = this.linkDeliverObj.lng;
      paramsObj.originY = this.linkDeliverObj.lat;
      paramsObj.originAreaCode =this.linkDeliverObj.area.outCode;
      //收件人信息
      paramsObj.linker = this.linkPickUpObj.linker;
      paramsObj.telephone = this.linkPickUpObj.linkTelephone;
      paramsObj.destAddr = this.linkPickUpObj.address;
      paramsObj.destInfo = this.linkPickUpObj.location;
      paramsObj.destX = this.linkPickUpObj.lng;
      paramsObj.destY = this.linkPickUpObj.lat;
      paramsObj.destAreaCode =this.linkPickUpObj.area.outCode;
      //金额
      paramsObj.amount = this.amount;

      this.http.post(this.saveOrderUrl, paramsObj, this.helperService.getOptions())
          .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
                //"在线支付"
                this.nav.push(PayOnline,{obj: {
                  orderId:result.data.id,
                  orderNo:result.data.orderNo,
                  amount:this.amount,
                  orderType:"快递"}});
            }
          },
          (err)=> {
            this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
          });
    }
}
