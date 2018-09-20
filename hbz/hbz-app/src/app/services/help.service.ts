import { Injectable } from '@angular/core';
import {NavController, ToastController,AlertController, LoadingController} from 'ionic-angular';
import {Http, Headers, RequestOptions}from '@angular/http';
import { Storage } from '@ionic/storage';
import { Config } from '../../app/config';

declare let window;
@Injectable()
export class HelperService {

    private loader: any;
    private alert: any;
    public api;
    constructor(
        public nav:NavController,
        public alertCtrl: AlertController,
        public loadingCtrl: LoadingController,
        public toastCtrl: ToastController,
        public storage: Storage,
        private http:Http,
        public config:Config
    ) {
      this.api = this.config.API;
    }

  presentLoader(text: string) {
    this.loader = this.loadingCtrl.create({ content: text });
    this.loader.present();
  }
  dismissLoader() {
    if (this.loader) this.loader.dismiss();
  }
  dismissAlert() {
    if (this.alert) this.alert.dismiss();
  }
  presentAlert(title: string, subTitle: any, button: string) {
    this.alert = this.alertCtrl.create({
      title: title,
      subTitle: subTitle,
      buttons: [button]
    });
    this.alert.present();
  }
  presentToast(text) {
    let toast = this.toastCtrl.create({
      message: text,
      duration: 1000,
      position: 'bottom'//'bottom'
    });
    toast.present();
  }

//获取HTTP请求头协议
getOptionss(){
  let $this = this;
  return new Promise(function(resolve, reject) {
        $this.storage.get("token").then((data)=>{
          let headers;
          let options;
          if(data=="" || data == null){
            //重新获取token
            let tokenUrl = $this.api +  "/api/session/token";
            $this.http.post(tokenUrl,{},{}).subscribe(
              (data) => {
                let myData = data.json();
                if(myData.code == 200){
                  $this.storage.set("token", data.json().data);
                  headers = new Headers({
                    'Content-Type': 'application/json; charset=UTF-8',
                    'X-AUTH-TOKEN': data.json().data
                  });
                  options = new RequestOptions({
                    headers: headers
                  });
                }
                window.options = options;
                resolve(options);
              },
              (err) => {
                console.log(err);
                // $this.presentToast('网络请求失败,请确保你的网络环境正常');
                reject(err);
              }
            )
          }else {
            headers = new Headers({
              'Content-Type': 'application/json; charset=UTF-8',
              'X-AUTH-TOKEN': data
            });
            options = new RequestOptions({
              headers: headers
            });
            window.options = options;
            resolve(options);
          }
        }).catch((error)=>{
          $this.presentToast('网络请求失败,请确保你的网络环境正常');
        })
    });
}

getOptions(){
  return window.options;
}

  //获取HTTP请求头协议
  // getOptions(){
  //   this.storage.get("token").then((data)=>{
  //     if(data==""){
  //       //重新获取token
  //       let tokenUrl = this.api +  "/api/session/token";
  //       this.http.post(tokenUrl,null).subscribe(
  //         (data) => {
  //           let myData = data.json();
  //           if(myData.code == 200){
  //             this.storage.set("token", data.json().data);
  //             this.headers = new Headers({
  //               'Content-Type': 'application/json; charset=UTF-8',
  //               'X-AUTH-TOKEN': data.json().data
  //             });
  //             this.options = new RequestOptions({
  //               headers: this.headers
  //             });
  //           }
  //         },
  //         (err) => {
  //           console.log(err);
  //           this.presentToast('网络请求失败,请确保你的网络环境正常');
  //         }
  //       )
  //     }else {
  //       this.headers = new Headers({
  //         'Content-Type': 'application/json; charset=UTF-8',
  //         'X-AUTH-TOKEN': data
  //       });
  //       this.options = new RequestOptions({
  //         headers: this.headers
  //       });
  //     }
  //   }).catch((error)=>{
  //     this.presentToast('网络请求失败,请确保你的网络环境正常');
  //   })
  //   return this.options;
  // }
  //用于处理请求中body格式
  headleAjaxBodyParam(param) {
    let bodyStr = [];
    for (let item in param) {
      bodyStr.push(item + '=' + param[item])
    }
    return bodyStr.join('&');
  }

  //检查电话号码格式是否正确
  checkPhoneFormat(phone: string): boolean {
    if ((/^1[3|4|5|7|8][0-9]{9}$/.test(phone))) {
      return true
    }
    return false;
  }
  //只能输入由数字和26个英文字母或者下划线组成的字符串：
  checkStrFormat(str: string): boolean {
    if ((/^[0-9a-zA-Z_]{1,}$/.test(str))) {
      return true
    }
    return false;
  }

  //根据key获取对象value
  getValueByJson(key, data){
    let valued;
    for(var p in data){
      if(p == key){
        valued = data[p];
        break;
      }
     }
     return valued;
  }

  //读取jsonArray的值
  getValueByJsonArray(key,targetId,jsonArray):any{
    let result;
    jsonArray.forEach(element => {
      if(element[key] == targetId){
        result = element;
      }
    });
    return result
  }

  buildAreaLocation(level1Name: any, level2Name: any, level3Name: any): any {
    let _level3Name = "";
    if(level3Name){
      _level3Name = "-" + level3Name;
    }
    return level1Name  +"-"+ level2Name  + _level3Name;
  }




//订单状态
/***
 * NEW新建          ：订单新建,点击确认后保存订单信息,此时只有创建者自己能见
    CONFIRMED 已确认 :订单确认页,需选择订单为支付方式为在线支付还是月结;
                      在线支付单点击确认修改订单状态为已确认,并保存支付方式为在线支付,新建支付订单;跳转至支付页面;支付时选择支付渠道(WECHAT_PAY微信/ALIPAY支付宝/UNION_PAY银联);如支付失败/取消/超时,不修改订单状态,只修改支付订单状态;二次支付时未超时则使用原有支付订单发起支付,超时则需新建支付订单;
                      支付成功回调时修改订单为ORDER_TO_BE_RECEIVED待接订单
                      月结订单点击确认修改订单为 WAIT_TO_TAKE已经接单待取货 状态
    ORDER_TO_BE_RECEIVED 待接订单:  待接订单为所有司机用户可见,可抢单接单
    LOCKED_ORDER订单锁定待货主确认:  司机用户点击接单,待货主确认,锁定时间为5分钟,超时则自动修改回退至CONFIRMED 已确认 状态
    LOCKED_ORDER_DRIVER订单锁定待司机确认:  货主用户选择月结时,选中司机待司机确认接单,锁定时间为10分钟,超时则自动修改回退至CONFIRMED 已确认
    WAIT_TO_TAKE已接单:   货主与司机确认订单,进入货运流程


    TRANSPORT运输中: 司机用户已经取货装车
    WAIT_TO_CONFIRM已收货: 司机用户送货完成,等待货主确认完成

    WAIT_FOR_PAYMENT_OFFLINE:线下支付中,货主选择线下支付
    WAIT_FOR_PAYMENT:等待在线支付货运款,订单流程完成,司机用户等待平台付款

    PAID已付款: 平台向司机用户付款完成

    // PAID_CONFIRM已付货运款确认:司机收款确认,如为线下付款,需司机点击确认,如为平台付款,司机未进行点击确认,PAID已付货运款状态的订单5天后自动确认

    LIQUIDATION_COMPLETED已清算: 平台N+日清算完成整个流程完成

    OVER_TIME订单超时: 当前时间超高跟订单的接运截止时间,订单被标记为超时
    WAITE_TO_REFUNDDE等待退款:客户在平台发起退款操作订单进入退款流程,超时订单自动进入退款流程,目前只能在已接单之前发起退款,等待退款需平台审核后通过后向支付机构发起退款
    APPLY_FOR_REFUND退款中: 超时订单无法处理,发起退款申请
    REFUND_FINISHT退款完成: 支付机构返回退款完成
    INVALID作废: 超时订单退款成功后,该订单作废
***/

  getOrderStatusChi(orderTrans){
      switch (orderTrans) {
        case 'NEW':return "新建";
        case 'CONFIRMED':return "已确认";
        case 'ORDER_TO_BE_RECEIVED':return "待接订单";
        case 'LOCKED_ORDER':return "订单锁定待货主确认";
        case 'LOCKED_ORDER_DRIVER':return "订单锁定待司机确认";
        case 'WAIT_TO_TAKE':return "已接单";
        case 'OVER_TIME':return "订单超时";
        case 'TRANSPORT':return "运输中";
        case 'WAIT_TO_CONFIRM':return "已收货";
        case 'WAIT_FOR_PAYMENT_OFFLINE':return "线下支付中";
        case 'WAIT_FOR_PAYMENT':return "待付款";
        case 'PAID':return "已付款";
        // case 'PAID_CONFIRM':return "已付货运款确认";
        case 'LIQUIDATION_COMPLETED':return "已清算";
        case 'APPLY_FOR_REFUND':return "退款中";
        case 'WAITE_TO_REFUNDDE':return "等待退款";
        case 'REFUND_FINISHT':return "退款完成";
        case 'INVALID':return "作废";
        default:return "未知状态";
      }
  }
  getOrderStatusJson(){
      return {
         'NEW': "新建",
         'CONFIRMED': "已确认",
         'ORDER_TO_BE_RECEIVED': "待接订单",
         'LOCKED_ORDER': "订单锁定待货主确认",
         'LOCKED_ORDER_DRIVER': "订单锁定待司机确认",
         'WAIT_TO_TAKE': "已接单",
         'OVER_TIME': "订单超时",
         'TRANSPORT': "运输中",
         'WAIT_TO_CONFIRM': "已收货",
         'WAIT_FOR_PAYMENT_OFFLINE':"线下支付中",
         'WAIT_FOR_PAYMENT': "待付款",
         'PAID': "已付款",
        //  'PAID_CONFIRM':"已付货运款确认",
         'LIQUIDATION_COMPLETED': "已清算",
         'APPLY_FOR_REFUND': "退款中",
         'WAITE_TO_REFUNDDE': "等待退款",
         'REFUND_FINISHT': "退款完成",
         'INVALID': "作废"
      }
  }

  //GPS坐标转换为百度坐标http://api.map.baidu.com/ag/coord/convert?from=0&to=4&x=longitude&y=latitude
  /* 其中：
  　 from: 来源坐标系   （0表示原始GPS坐标，2表示Google坐标）
　　　to: 转换后的坐标   （4就是百度自己啦，好像这个必须是4才行）
　　  x: 经度
      y: 纬度
      返回的结果是一个json字符串：
      {"error":0,"x":"MTIxLjUwMDIyODIxNDk2","y":"MzEuMjM1ODUwMjYwMTE3"}
      其中：
      error：是结果是否出错标志位，"0"表示OK
      x: 百度坐标系的经度(Base64加密)
      y: 百度坐标系的纬度(Base64加密) */
  convertBaiduByGPS(longitude,latitude){
    let url = "http://api.map.baidu.com/ag/coord/convert?from=0&to=4&x="+ longitude
      + "longitude&y=" + latitude;
      let headers = new Headers({ "myHeader": "myValue" });
      headers.append("Accept", "application/json");
      let options = new RequestOptions({ headers: headers});
    this.http.get(url,options).subscribe(
      (data) => {
        console.log(data);
        // let result = data.json();
        // var result2 = Base64.decode(result.x);
        // result2 = Base64.decode(result.y);
      }
    )
  }

  /**
   * 补全列表个数
   * data {array} 输入数组
   * resul {array} 输出数组
   * num {Number} 补全总个数
  */
  fillList(data:Array<any>, num:number) {
    let result = [];
    let len = data.length % num;
    let blankDataArray = [];
    let blankData = {isBlank: true};
    if(len > 0) {
      while (len > 0) {
        blankDataArray.push(blankData);
        len --;
      }
    }
    result = [...data, ...blankDataArray];
    return result;
  }
}
