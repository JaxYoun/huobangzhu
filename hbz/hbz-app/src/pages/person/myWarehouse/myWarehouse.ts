import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController} from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { MyWarehouseEdit } from './myWarehouseEdit';
import { MyWarehouseDetail } from './myWarehouseDetail';


@Component({
  selector: 'page-warehouse',
  templateUrl: 'myWarehouse.html'
})

export class MyWarehouse {
    private queryUrl: string = "/api/web/warehouse/getMyWarehouseListByPage";
    private delUrl: string = "/api/web/warehouse/deleteWarehouse";
    public title:string = '我的仓储资讯';
    public helperService:HelperService;
    public wareHouseList=[];
    public page=0;
    public flag:boolean=true;//控制下拉多次触发问题
    
    public warehouseStatusArray;

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
          this.delUrl = this.helperService.api + this.delUrl;
          this.initPage();
        }

    initPage(){
      let $this = this;
      this.wareHouseList = [];
      this.storage.get("warehouseAuditType").then(
        (data)=>{
          this.warehouseStatusArray = data;
          this.http.post(this.queryUrl,{page:this.page,size:this.config.pageSize},this.helperService.getOptions())
          .subscribe((data)=> {
              let result = data.json();
              if (result.code == '200') {
                if(result.data.content.length > 0){
                  result.data.content.forEach(element =>{
                    element.lifecycleChi = $this.helperService.getValueByJsonArray('val',element.lifecycle,$this.warehouseStatusArray);
                    this.wareHouseList.push(element);
                  });
                }
              }
            },
            (err)=> {
              this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });
        });
      

    }

    //下拉刷新    
    doRefresh(refresher) { 
      this.wareHouseList = [];
      let $this = this;
      this.http.post(this.queryUrl,{page:0,size:this.config.pageSize},this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                element.lifecycleChi = $this.helperService.getValueByJsonArray('val',element.lifecycle,$this.warehouseStatusArray);
                this.wareHouseList.push(element);
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
    let $this = this;
    this.http.post(this.queryUrl,{page:this.page,size:this.config.pageSize},this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          //等待2秒，2秒后执行
          setTimeout(()=> {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                element.lifecycleChi = $this.helperService.getValueByJsonArray('val',element.lifecycle,$this.warehouseStatusArray);
                this.wareHouseList.push(element);
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


    goToDetail(item){
      let mapObj={
        destX:'',
        destY:''
      };
      mapObj.destX = item.coordX;
      mapObj.destY = item.coordY;
      
      let obj={
        id:'',
        name:'',
        capacity:'',
        unitPrice:'',
        minRentTime:'',
        ownerName:'',
        telephone:'',
        address:'',
        coordX:'',
        coordY:'',
        titleImageList:'',
        warehouseDescribe:''
      };
      obj.id = item.id;
      obj.name = item.name;
      obj.capacity = item.capacity;
      obj.unitPrice = item.unitPrice;
      obj.minRentTime = item.minRentTime;
      obj.ownerName = item.ownerName;
      obj.telephone = item.telephone;
      obj.address = item.address;
      obj.warehouseDescribe = item.warehouseDescribe;
      obj.coordX = item.coordX;
      obj.coordY = item.coordY;
      if(item.titleImageList){
        obj.titleImageList = item.titleImageList[0];
      } else {
        obj.titleImageList = '';
      }
      //0未审核，1审核通过，2审核未通过
      //审核通过，审核未通过,不能进行编辑
      //未审核可以编辑
      if(item.lifecycle == '0'){
        //编辑
        this.nav.push(MyWarehouseEdit, {obj:obj,mapObj:mapObj})
      } else{
        //详情
        this.nav.push(MyWarehouseDetail, {obj:obj,mapObj:mapObj})
      }
    }



    delWarehouse(item){
      this.http.post(this.delUrl,{id:item.id},this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.helperService.presentToast("删除仓储成功");
            this.initPage();
          } else {
            this.helperService.presentToast(result.msg);
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
    }
    deleteOrder(item){
      console.log(item);
    }
}
