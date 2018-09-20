import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { CommentOrder } from './commentOrder';

@Component({
  selector: 'page-submitComment',
  templateUrl: 'submitComment.html'
})
export class SubmitComment {
    private rateSignUrl: string = "/api/rate/rateSign";
    public helperService:HelperService;
    public title:string = '';
    public myComment;
    public comment="";
    public goodsName="";
    public star="1";


    constructor(
        public params: NavParams,
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
          this.rateSignUrl = this.helperService.api + this.rateSignUrl;
          this.title = params.get("title");
          this.myComment = params.get("myComment");
          this.goodsName = this.myComment.order.commodityName;

          if(this.myComment.comment){
            this.comment = this.myComment.comment;
            this.star = this.myComment.star;
          }
        }

    confirm(){
      if (this.comment == '' || this.comment == null) {
        this.helperService.presentToast('评价内容必填');
        return;
      }

      this.http.post(this.rateSignUrl,{id:this.myComment.id,star:this.star,comment:this.comment},this.helperService.getOptions())
        .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.helperService.presentToast("评价成功");
            this.nav.push(CommentOrder);
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
    }
    changeStar(level){
      this.star = level;
    }
}
