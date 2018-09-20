import { Component, ViewChild } from '@angular/core';
import {NavController,ToastController,AlertController, LoadingController, Slides } from 'ionic-angular';
import {Http}from '@angular/http';

import { Storage } from '@ionic/storage';

import { WechatService } from '../../app/services/wechat.service';
// import { FileTransfer, FileUploadOptions, FileTransferObject } from '@ionic-native/file-transfer';
import { HelperService } from '../../app/services/help.service';
// import { TranslateService } from '@ngx-translate/core';
import { LocationProvider } from '../../providers/LocationProvider';
// import { BackgroundGeolocation } from 'ionic-native';
import { Config } from '../../app/config';
import { Score } from '../person/score/score';
import { Shop } from '../person/shop/index';
import { InformationDetail } from '../warehouse/information/informationDetail';

import * as base64js from "js-base64";


declare let cordova;
// declare let Wechat;
// declare var require;

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {
  @ViewChild(Slides) slides: Slides;

  html: string = "";
  // options;
  private syncGpsUrl: string = "/api/user/app/coordinate/sync";//用户位置同步
  private gpsConvertBaiduUrl = "http://api.map.baidu.com/ag/coord/convert?";
  public bannerArray:Array<object> = [
    {
      src: 'assets/images/banner/home.png'
    }
  ];
  pointX:number;
  pointY:number;
  // private zone: NgZone;

  private queryNewsUrl: string = "/api/web/news/getPublishedNewsListByPage";
  private queryBannersUrl: string = "/api/web/banner/getBannerListByLocation";
  public helperService:HelperService;
  public newsArray=[];
  public page=0;
  public flag:boolean=true;//控制下拉多次触发问题

  constructor(
    private nav: NavController,
    public toastCtrl: ToastController,
    public loadingCtrl: LoadingController,
    public alertCtrl: AlertController,
    private http: Http,
    public storage: Storage,
    // private translate: TranslateService,
    private locationProvider:LocationProvider,
    public wechatService: WechatService,
    public config:Config) {
      this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
      this.syncGpsUrl = this.helperService.api + this.syncGpsUrl;
      this.queryNewsUrl = this.helperService.api + this.queryNewsUrl;
      this.queryBannersUrl = this.helperService.api + this.queryBannersUrl;

      this.initPage();
  }
  ionViewWillEnter(){
    this.slides.startAutoplay();
  }
  ionViewWillLeave(){
    this.slides.stopAutoplay();
  }
  //进入页面时触发
  ionViewDidEnter(){
    this.slides.autoplayDisableOnInteraction = false;  
    let $this = this;
    let res = this.config.GPS_SYNCHRO_TIME;
      //GPS地理位置同步
      setInterval(
          () => {
            $this.locationProvider.startTracking();
            $this.locationProvider.getLocations().then(
              (location)=>{
                let longitude = 0;
                let latitude = 0;
                if(!location[0]){
                    // longitude=103.949386;
                    // latitude=30.750191;
                }else {
                  longitude = location[0].longitude;
                  latitude = location[0].latitude;
                }
                 //GPS坐标转换百度地图坐标；会出现跨越请求错误，APP运行正常的
                let url = this.gpsConvertBaiduUrl + "from=0&to=4&x="+ longitude + "longitude&y=" + latitude;
                this.http.get(url,this.helperService.getOptions()).subscribe(
                  (data) => {
                    let result = data.json();
                    let x = base64js.Base64.decode(result.x);
                    let y = base64js.Base64.decode(result.y);
                    //同步到数据库
                    $this.http.post($this.syncGpsUrl, {
                        'pointX': x,
                        'pointY': y
                      }, $this.helperService.getOptions()).subscribe((data)=> {
                        let result = data.json();
                        if (result.code != '200') {
                          console.log("地理位置保存失败");
                        }
                      },
                      (err)=> {
                        console.log('网络请求失败,请确保你的网络环境正常');
                      });

                  },(err) => {
                    console.log(err);
                  }
                )
              }
            ).catch(
              (erro)=>{console.log("erro:"+erro)}
            );
      }, res);
  }

  gotoInfo(item){
    let isShowDate;
    if(item.displayPublishDate == 0){
      isShowDate = false;
    } else{
      isShowDate = true;
    }
    //组装消息、资讯对象
    let obj={
      title: item.title,
      titleImage:item.titleImageList,
      isShowTitleImage:true,
      createDate:item.createDate,
      isShowDate:isShowDate,
      content:item.content
    };
    this.nav.push(InformationDetail,{item:obj,title:'平台新闻'});
  }

  initPage(){
    this.storage.get("token").then(data => {
      this.helperService.getOptionss().then(options=>{

        // newsType:'00' 新闻
        this.http.post(this.queryNewsUrl,{page:this.page,size:this.config.pageSize,newsType:'00'},options)
        .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
              if(result.data.content.length > 0){
                result.data.content.forEach(element =>{
                  this.newsArray.push(element);
                });
              }
            }
          },
          (err)=> {
            this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
          });

        this.http.post(this.queryBannersUrl,{location:'1'},options)
        .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
              this.bannerArray = result.data;
            }
          },
          (err)=> {
            this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
          });


      })
    })


  }


   //下拉刷新
   doRefresh(refresher) {
     this.nav.setRoot(HomePage);
  }

  //上拉加载
  doInfinite(infiniteScroll){
    //上拉加载前先为当前page++
    this.page++;
    this.http.post(this.queryNewsUrl,{page:this.page,size:this.config.pageSize,newsType:'00'},this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          //等待2秒，2秒后执行
          setTimeout(()=> {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                this.newsArray.push(element);
              });
            } else {
              if(this.flag){
                this.helperService.presentToast('没有更多数据');
                this.flag = false;
              }
            }
            infiniteScroll.complete();
          }, this.config.scrollWaitTime);

        } else {
          this.helperService.presentToast('数据初始化失败：' + result.msg);
          infiniteScroll.complete();
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        infiniteScroll.complete();
      });
  }



  slideTo(item){
    if(item.skipType.val == 2){
      var ref = cordova.InAppBrowser.open(item.url, '_blank', 'location=yes');
    }
    if(item.skipType.val == 1){
      //组装消息、资讯对象
      let obj={
        title: item.name,
        titleImage:item.imagePath,
        isShowTitleImage:true,
        createDate:item.createDate,
        isShowDate:false,
        content:''
      };
      this.nav.push(InformationDetail,{item:obj,title:'banner详情'});
    }
  }

  goToAnyWhere(nali) {
    switch (nali) {
      case 'Score':
        this.nav.push(Score);
        break;
      case 'Shop':
        this.nav.push(Shop);
        break;
      default:
        break;
    }

  }

}
