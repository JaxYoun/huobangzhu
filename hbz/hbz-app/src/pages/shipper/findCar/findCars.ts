import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { FindCarView } from './findCarView';

@Component({
  templateUrl: 'findCars.html'
})
export class FindCars {
    private queryNearUrl: string = "/api/driverLine/queryPage";
    private queryDrlUrl: string = "/api/driverLine/query";
    
    
    public helperService:HelperService;
    public nearSourceArray=[];
    public obj:any={};
    public title="找车";
    public page=0;
    public flag:boolean=true;//控制下拉多次触发问题
    
    public paramsArray=[];
    public transType={};

    public originAreaCode = "";
    public destAreaCode = "";

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
          this.queryNearUrl = this.helperService.api + this.queryNearUrl;
          this.queryDrlUrl = this.helperService.api + this.queryDrlUrl;

          this.storage.get("transType").then(
            (data)=>{
              this.transType = data;
              this.initPage();
            });
        }


  //初始化数据
  initPage(){
    this.http.post(this.queryNearUrl,{
      page:this.page,
      size:this.config.pageSize
    },this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          console.log(result.data)
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
              element.transType = this.helperService.getValueByJson(element.transType, this.transType);

              element.originAreaName = this.helperService.buildAreaLocation( element.originArea.level1Name, element.originArea.level2Name, element.originArea.level3Name);
              element.destAreaName = this.helperService.buildAreaLocation( element.destArea.level1Name, element.destArea.level2Name, element.destArea.level3Name);

              this.nearSourceArray.push(element);
            });
          }
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      });
  }

  buildParamObj(page){
    let paramObj:any= {};
    paramObj.page = page;
    paramObj.size = this.config.pageSize;
    if(this.destAreaCode != ""){
      paramObj.destAreaCode = this.destAreaCode;
    }
    if(this.originAreaCode != ""){
      paramObj.originAreaCode = this.originAreaCode;
    }
    return paramObj;
  }

  //下拉刷新    
  doRefresh(refresher) { 
    this.originAreaCode = "";
    this.destAreaCode = "";
    this.nearSourceArray = [];
    this.http.post(this.queryNearUrl,this.buildParamObj(0),this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
              element.transType = this.helperService.getValueByJson(element.transType, this.transType);
              element.originAreaName = this.helperService.buildAreaLocation( element.originArea.level1Name, element.originArea.level2Name, element.originArea.level3Name);
              element.destAreaName = this.helperService.buildAreaLocation( element.destArea.level1Name, element.destArea.level2Name, element.destArea.level3Name);
              this.nearSourceArray.push(element);
            });
          }
          //复位下拉刷新
          refresher.complete();
          this.page = 0;
          this.flag = true;
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      });
  }  

  //上拉加载
  doInfinite(infiniteScroll){
    //上拉加载前先为当前page++
    this.page++;
    this.http.post(this.queryNearUrl,this.buildParamObj(this.page),this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          //等待2秒，2秒后执行
          setTimeout(()=> {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                element.transType = this.helperService.getValueByJson(element.transType, this.transType);
                element.originAreaName = this.helperService.buildAreaLocation( element.originArea.level1Name, element.originArea.level2Name, element.originArea.level3Name);
                element.destAreaName = this.helperService.buildAreaLocation( element.destArea.level1Name, element.destArea.level2Name, element.destArea.level3Name);
                this.nearSourceArray.push(element);
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
    
    gotoDetail(item){
            this.nav.push(FindCarView, {obj:item});
      // this.http.post(this.queryDrlUrl,{id:item.id},this.helperService.getOptions())
      // .subscribe((data)=> {
      //     let result = data.json();
      //     if (result.code == '200') {
      //       this.nav.push(FindCarView, {obj:result.data[0]});
      //     }
      //   },
      //   (err)=> {
      //     this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      //   });
    }


        // 联系地址选择
        onChangeAddressOrigin(res) {
            this.originAreaCode = res.outCode;
            this.nearSourceArray = [];
            this.http.post(this.queryNearUrl,this.buildParamObj(0),this.helperService.getOptions())
            .subscribe((data)=> {
                let result = data.json();
                if (result.code == '200') {
                  console.log(result.data)
                  if(result.data.content.length > 0){
                    result.data.content.forEach(element =>{
                      element.transType = this.helperService.getValueByJson(element.transType, this.transType);
                      element.originAreaName = this.helperService.buildAreaLocation( element.originArea.level1Name, element.originArea.level2Name, element.originArea.level3Name);
                      element.destAreaName = this.helperService.buildAreaLocation( element.destArea.level1Name, element.destArea.level2Name, element.destArea.level3Name);
                      this.nearSourceArray.push(element);
                    });
                  }
                }
              },
              (err)=> {
                this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
              });
        }

        // 联系地址选择
        onChangeAddressDest(res) {
            this.destAreaCode = res.outCode;
            this.nearSourceArray = [];
            this.http.post(this.queryNearUrl,this.buildParamObj(0),this.helperService.getOptions())
            .subscribe((data)=> {
                let result = data.json();
                if (result.code == '200') {
                  console.log(result.data)
                  if(result.data.content.length > 0){
                    result.data.content.forEach(element =>{
                      element.transType = this.helperService.getValueByJson(element.transType, this.transType);
                      element.originAreaName = this.helperService.buildAreaLocation( element.originArea.level1Name, element.originArea.level2Name, element.originArea.level3Name);
                      element.destAreaName = this.helperService.buildAreaLocation( element.destArea.level1Name, element.destArea.level2Name, element.destArea.level3Name);
                      this.nearSourceArray.push(element);
                    });
                  }
                }
              },
              (err)=> {
                this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
              });
        }
}
