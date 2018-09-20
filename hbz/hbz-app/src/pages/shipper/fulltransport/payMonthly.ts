import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController ,NavParams} from 'ionic-angular';
import { Storage } from '@ionic/storage';
import {Http}from '@angular/http';
import { SendList } from '../../person/sendOrder/sendList';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { Wallet } from '../../person/wallet/wallet';

@Component({
  selector: 'pay-monthly',
  templateUrl: 'payMonthly.html'
})
export class PayMonthly {
  public helperService:HelperService;
  public obj = {
    orderId:'',
    orderNo:'',
    settlementType:'',
    takeUserId:'',
    type:''
  }
  public confirmUrl="";
  private fslConfirmUrl: string = "/api/order/fsl/confirm";//订单确认
  private lslConfirmUrl: string = "/api/order/ltl/confirm";//订单确认
  private userQueryUrl: string = "/api/user/queryPage";//用户查询
  public type;//零担、整车
  public userArray=[];

  public page=0;
  public flag:boolean=true;//控制下拉多次触发问题

  public telephone = "";

  constructor(
    public params: NavParams,
    private nav:NavController,
    public alertCtrl: AlertController,
    public loadingCtrl: LoadingController,
    public toastCtrl: ToastController,
    public platform:Platform,
    private http:Http,
    public storage: Storage,
    public config:Config) {
      this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
      this.userQueryUrl = this.helperService.api + this.userQueryUrl;
      this.obj = params.get('obj');
      this.type = this.obj.type;
      if(this.type == 'fsl'){
        this.confirmUrl = this.helperService.api + this.fslConfirmUrl;
      }
      if(this.type == 'lsl'){
        this.confirmUrl = this.helperService.api + this.lslConfirmUrl;
      }

          // this.http.post(this.userQueryUrl,{},this.helperService.getOptions())
          // .subscribe((data)=> {
          //     let result = data.json();
          //     if (result.code == '200') {
          //       result.data.forEach(element => {
          //         element.isSelected = false;
          //       });
          //       this.userArray = result.data;
          //     }
          //   },
          //   (err)=> {
          //     this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
          //   });
          this.initPage();
    }

    buildShowName(element){
        // 需要处理司机、企业类型（判断只有两种类型[运输企业TransEnterprise、个人司机PersonDriver]，其他类型没有资格）
        if(element.TransEnterprise){
          //同时满足企业和个人，则显示企业信息
          element.showName = element.TransEnterprise.organizationName;
          element.type = 'TransEnterprise';
        }else{
          //显示个人信息
          if(element.PersonDriver == undefined){
            element.showCarNumber= '';
            element.showCommodityVolume= '';
            element.showCommodityWeight= '';
            element.showTransType= '';
          } else{
            element.showCarNumber= element.PersonDriver.licensePlateNumber;
            element.showCommodityVolume= element.PersonDriver.commodityVolume;
            element.showCommodityWeight= element.PersonDriver.commodityWeight;
            element.showTransType= element.PersonDriver.transType;
          }
          element.showName = element.nickName;
          element.type = 'PersonDriver';
        }
    }

    //初始化数据
  initPage(){
    this.http.post(this.userQueryUrl,{
      page:this.page,
      size:this.config.pageSize,
      roles:["EnterpriseAdmin", "PersonDriver"]
    },this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
              element.isSelected = false;
              this.buildShowName(element);
              this.userArray.push(element);
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
    paramObj.roles=["EnterpriseAdmin", "PersonDriver"]
    if(this.telephone != ''){
      paramObj.telephone = this.telephone;
    }
    return paramObj;
  }

  //查询
  onSearch(){
    this.userArray = [];
    this.http.post(this.userQueryUrl,this.buildParamObj(0),this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
              element.isSelected = false;
              this.buildShowName(element);
              this.userArray.push(element);
            });
          }
          this.page = 0;
          this.flag = true;
        }
      },
      (err)=> {
        this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      });
  }

  //下拉刷新    
  doRefresh(refresher) { 
    this.userArray = [];
    this.http.post(this.userQueryUrl,this.buildParamObj(0),this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          if(result.data.content.length > 0){
            result.data.content.forEach(element =>{
              element.isSelected = false;
              this.buildShowName(element);
              this.userArray.push(element);
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
    this.http.post(this.userQueryUrl,this.buildParamObj(this.page),this.helperService.getOptions())
    .subscribe((data)=> {
        let result = data.json();
        if (result.code == '200') {
          //等待2秒，2秒后执行
          setTimeout(()=> {
            if(result.data.content.length > 0){
              result.data.content.forEach(element =>{
                element.isSelected = false;
                this.buildShowName(element);
                this.userArray.push(element);
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

    //选择指定司机
    goSelectBack(item){
      this.obj.takeUserId = item.id;//指定司机
      this.userArray.forEach(element => {
        if(element.id == item.id){
          element.isSelected = true;
        } else {
          element.isSelected = false;
        }
      });
    }

    confirm() {
      if(this.obj.takeUserId == "" || this.obj.takeUserId == null){
        this.helperService.presentToast('指定司机必填');
        return false;
      }
      this.http.post(this.confirmUrl,
        {id:this.obj.orderId,
          settlementType:this.obj.settlementType,
          takeUserId:this.obj.takeUserId}, this.helperService.getOptions())
        .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
                this.nav.push(SendList);
            }
            if (result.code == '500') {
              this.helperService.presentToast(result.msg);
              if(result.data == 'NeedBond'){
                let confirm = this.alertCtrl.create({
                  title: '请交纳保证金',
                  message: '你确定跳转到交纳保证金页面吗?',
                  buttons: [
                    {
                      text: '确定',
                      handler: () => {
                        this.nav.push(Wallet);
                      }
                    },
                    {
                      text: '取消',
                      handler: () => {
                        return;
                      }
                    }
                  ]
                });
                confirm.present();
            }
          }
          },
          (err)=> {
            this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
          });
    }
}
