import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
@Component({
  selector: 'page-score',
  templateUrl: 'score.html'
})
export class Score {
    private queryUrl: string = "/api/user/get";
    private queryListUrl: string = "/api/scoreChange/query";
    public helperService:HelperService;
    public title:string = '积分详情';

    public totalScore:Number;
    public currentScore:Number;
    public spendScore:Number;
    public scoreArray=[];

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
          this.queryUrl = this.helperService.api + this.queryUrl;
          this.queryListUrl = this.helperService.api + this.queryListUrl;
          this.queryPersonScore();
          this.queryList();
        }  

        //查询个人积分信息
        queryPersonScore(){
          this.http.post(this.queryUrl, {}, this.helperService.getOptions())
          .subscribe((data)=> {
              let result = data.json();
              if (result.code == '200') {
                this.totalScore = result.data.totalScore;
                this.currentScore = result.data.currentScore;
                this.spendScore = result.data.spendScore;
              }
            },
            (err)=> {
              this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });
        }

        //查询消费记录
        queryList(){
          this.http.post(this.queryListUrl, {}, this.helperService.getOptions())
          .subscribe((data)=> {
              let result = data.json();
              if (result.code == '200') {
                if(result.data.length > 0){
                  result.data.forEach(element =>{
                    if(element.delta > 0){
                      element.deltaValue = "+" + element.delta;
                    }
                    else{
                      element.deltaValue = "" + element.delta;
                    }
                    this.scoreArray.push(element);
                  });
                }
              }
            },
            (err)=> {
              this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });
        }
}
