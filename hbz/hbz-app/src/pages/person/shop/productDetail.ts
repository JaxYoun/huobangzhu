import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { Score } from '../score/score';

@Component({
  selector: 'page-productDetail',
  templateUrl: 'productDetail.html'
})

export class ProductDetail {
    public helperService:HelperService;
    private queryUrl: string = "/api/product/query";//	查询商品
    private confirmUrl: string = "/api/scoreOrder/create";
    public isChoose:boolean = false; // 显示商品选择框
    public isExchange:boolean = false;
    public amountNumber:number = 0;
    public obj;

    public destInfo;
    public paramObj={
      productId:"",
      destAddr:"",
      link:"",
      telephone:"",
      areaCode:""
    };

    constructor(
        private nav:NavController,
        public params: NavParams,
        public alertCtrl: AlertController,
        public loadingCtrl: LoadingController,
        public toastCtrl: ToastController,
        public modalCtrl: ModalController,
        public platform:Platform,
        private http: Http,
        public storage: Storage,
        public config:Config) {
          this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
          this.queryUrl = this.helperService.api + this.queryUrl;
          this.confirmUrl = this.helperService.api + this.confirmUrl;
          this.obj = params.get("product");
          this.paramObj.productId = this.obj.id;
      }

      // 第一次点兑换拉起选择商品信息
      exchange() {
        this.isChoose = true;
      }


      // 关闭商品信息选择框
      closeChoose(){
        this.isChoose = false;
      }

      // 计数
      amount(type) {
        if(type === 'add') {
          this.amountNumber += 1;
        } else if(type === 'min' && this.amountNumber > 0){
          this.amountNumber -= 1;
        }
      }


      confirm(){
        if (this.paramObj.link == '' || this.paramObj.link == null) {
          this.helperService.presentToast("联系人不能为空");
          return;
        }
        if (this.paramObj.telephone == '' || this.paramObj.telephone == null) {
          this.helperService.presentToast("联系电话为空");
          return;
        }
        if(!this.helperService.checkPhoneFormat(this.paramObj.telephone.toString())){
          this.helperService.presentToast('联系电话有误');
          return;
        }
        if (this.destInfo == '' || this.destInfo == null) {
          this.helperService.presentToast('联系地址必填');
          return;
        }
        if (this.paramObj.destAddr == '' || this.paramObj.destAddr == null) {
          this.helperService.presentToast("详细地址不能为空");
          return;
        }
        this.paramObj.areaCode = this.destInfo.outCode;

        this.http.post(this.confirmUrl,this.paramObj,this.helperService.getOptions())
        .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
              this.helperService.presentToast("积分兑换成功");
              this.nav.push(Score);
              //跳转个人积分详情页
            }else{
              this.helperService.presentToast(result.msg);
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


    // openMapModal(){
    //   let modal = this.modalCtrl.create(HelpMeBuyMapModals,{obj:this.obj});
    //   modal.onDidDismiss((item) => {
    //         if(item && item.lat){
    //           this.obj.destX = item.lng;
    //           this.obj.destY = item.lat;
    //           this.obj.destAddress = item.title;
    //         }
    //   });
    //   modal.present();
    // }
}
