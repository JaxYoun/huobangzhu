import {Component} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import {TabsPage} from '../tabs/tabs';
import {RegisterPage} from '../login/register';
import {ForgetPage} from '../login/forget';
import {Http}from '@angular/http';
import { HelperService } from '../../app/services/help.service';
import { UserService } from '../../app/services/user.service';
import { Config } from '../../app/config';
import { BackButtonService } from '../../providers/BackButtonService';

@Component({
  selector: 'page-login',
  templateUrl: 'login.html'
})
export class LoginPage {
  public loginName:string = "";
  public password:string = "";
  public authCode:string = "";
  public loginUrl:string = "/api/security";//登录
  public enumsUrl: string = "/api/public/enums";//枚举类型
  public authCodeUrl = "/api/user/authCode/peek";//发送验证码
  private typevalUrl: string = "/api/public/typeval";//数据字典
  private queryMyRoleUrl: string = "/api/user/roles";//当前角色列表
  public helperService:HelperService;
  public userService:UserService;
  public errLoginCount=0;
  public btnText: string = "获取验证码";
  public codeImage:any;
  public isShow:boolean = false;

  constructor(
      public backButtonService: BackButtonService,
      private nav:NavController,
      public alertCtrl: AlertController,
      public loadingCtrl: LoadingController,
      public toastCtrl: ToastController,
      public platform:Platform,
      private http:Http,
      public storage: Storage,
      public config:Config) {
        this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
        this.userService = new UserService(this.helperService,storage,http);
        this.loginUrl = this.helperService.api + this.loginUrl;
        this.enumsUrl = this.helperService.api + this.enumsUrl;
        this.authCodeUrl = this.helperService.api + this.authCodeUrl;
        this.typevalUrl = this.helperService.api + this.typevalUrl;
        this.queryMyRoleUrl = this.helperService.api + this.queryMyRoleUrl;

        platform.ready().then(() => {
          this.backButtonService.registerBackButtonAction(null);
        });
  }

  //当进入页面时触发
  ionViewDidEnter(){
    let $this = this;
    this.storage.get("loginName").then((data)=>{this.loginName = data});
    this.storage.get("password").then((data)=>{this.password = data});
    const $login = document.getElementsByClassName('login')[0];
    const originHeight = document.body.clientHeight;
    window.onresize = () => {
      const currentHeight = document.body.clientHeight;
      if (originHeight > currentHeight) {
        $login.className = 'login focus';
      }else{
        $login.className = 'login';
      }
    }
  }

  //用户登录
  login() {
    if (this.loginName == "") {
      this.helperService.presentToast("用户名不能为空");
      return;
    }
    if(this.password == ""){
      this.helperService.presentToast("密码不能为空");
      return;
    }
    if(!this.helperService.checkPhoneFormat(this.loginName)){
        this.helperService.presentToast("手机号格式不正确");
        return;
    }
    let $this = this;
    this.helperService.getOptionss().then(options=>{
          if($this.errLoginCount>=3){
            //用户名或密码错误三次需要输入验证码
            if($this.authCode == ""){
              $this.helperService.presentToast("验证码为空");
              return;
            }
            $this.isShow = true;
            $this.helperService.presentLoader('Login...');
            $this.http.post($this.loginUrl, {
            'j_user':$this.loginName,
            'j_pass':$this.password,
            'language':"zh_CN",
            'loginFor':'App',
            'authCode':$this.authCode
          }, options).subscribe((data) => {
                $this.helperService.dismissLoader();// dismiss loading
                let myData = data.json();
                if (myData.code == "200") {
                  // $this.saveUserInfoToLocalStorage(myData,$this.password);
                  $this.userService.saveUserInfoToLocalStorage(myData,$this.password);
                  $this.nav.push(TabsPage);
                } else {
                  $this.errLoginCount++;
                  $this.storage.set("errLoginCount", $this.errLoginCount);
                  if (myData.code == "401"){
                    $this.helperService.presentToast("用户名或密码错误");
                  } else {
                    $this.helperService.presentToast(myData.msg);
                  }
                  if($this.errLoginCount>=3){
                    $this.sendYZM();
                  }
                }
              },
              (err) => {
                $this.helperService.dismissLoader();//请求失败 dismiss 掉 loading
                $this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
              });
          } else {
            $this.helperService.presentLoader('Login...');
            $this.http.post($this.loginUrl, {
              j_user:$this.loginName,
              j_pass:$this.password,
              language:"zh_CN",
              loginFor:'App'
            }, options).subscribe((data) => {
                  $this.helperService.dismissLoader();// dismiss loading
                  let myData = data.json();
                  if (myData.code == "200") {
                    // $this.saveUserInfoToLocalStorage(myData,$this.password);
                    $this.userService.saveUserInfoToLocalStorage(myData,$this.password);
                    $this.nav.push(TabsPage);
                  } else {
                    $this.errLoginCount++;
                    this.storage.set("errLoginCount", $this.errLoginCount);
                    if (myData.code == "401"){
                      $this.helperService.presentToast("用户名或密码错误");
                    } else {
                      $this.helperService.presentToast(myData.msg);
                    }
                    if($this.errLoginCount>=3){
                      $this.sendYZM();
                    }
                  }
                },
                (err) => {
                  console.log(err)
                  $this.helperService.dismissLoader();//请求失败 dismiss 掉 loading
                  $this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
                });
            }
        }).catch(
          (err) => $this.helperService.presentToast('网络请求失败,请确保你的网络环境正常')
        );
  }

  // //保存用户信息到localstorage
  // public saveUserInfoToLocalStorage(myData,password) {
  //   this.storage.set("loginName", myData.data["X-AUTH-USER"]);
  //   this.storage.set("password", password);
  //   this.storage.set("errLoginCount", 0);
  //   this.storage.set("token", myData.data["X-AUTH-TOKEN"]).then(data => {
  //   this.helperService.getOptionss().then(options=>{
  //         //获取当前登录用户角色
  //       this.http.post(this.queryMyRoleUrl, {}, options)
  //         .subscribe((data)=> {
  //             let result = data.json();
  //             if (result.code == '200') {
  //               this.storage.set("currentRoles", result.data);
  //             } else {
  //               this.helperService.presentToast('获取当前登录用户角色失败');
  //             }
  //           },
  //           (err)=> {
  //             this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
  //           });
  //         //仓储订单状态：PayProgress
  //       this.http.post(this.enumsUrl, {"enumname":"PayProgress"}, options)
  //         .subscribe((data)=> {
  //             let result = data.json();
  //             if (result.code == '200') {
  //               this.storage.set("payProgress", result.data);
  //             } else {
  //               this.helperService.presentToast('获取数据字典【仓储订单状态】失败');
  //             }
  //           },
  //           (err)=> {
  //             this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
  //           });
  //         //订单：OrderType
  //       this.http.post(this.enumsUrl, {"enumname":"OrderType"}, options)
  //         .subscribe((data)=> {
  //             let result = data.json();
  //             if (result.code == '200') {
  //               this.storage.set("orderType", result.data);
  //             } else {
  //               this.helperService.presentToast('获取数据字典【订单类型】失败');
  //             }
  //           },
  //           (err)=> {
  //             this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
  //           });
  //         //业务类型：BusinessType
  //       this.http.post(this.enumsUrl, {"enumname":"BusinessType"}, options)
  //         .subscribe((data)=> {
  //             let result = data.json();
  //             if (result.code == '200') {
  //               this.storage.set("businessType", result.data);
  //             } else {
  //               this.helperService.presentToast('获取数据字典【业务类型】失败');
  //             }
  //           },
  //           (err)=> {
  //             this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
  //           });
  //         //快递货物类型：CommodityClass
  //       this.http.post(this.enumsUrl, {"enumname":"CommodityClass"}, options)
  //         .subscribe((data)=> {
  //             let result = data.json();
  //             if (result.code == '200') {
  //               this.storage.set("commodityClass", result.data);
  //             } else {
  //               this.helperService.presentToast('获取数据字典【货物类型】失败');
  //             }
  //           },
  //           (err)=> {
  //             this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
  //           });
  //         //整车、零担货物类型：CommodityType
  //       this.http.post(this.enumsUrl, {"enumname":"CommodityType"}, options)
  //         .subscribe((data)=> {
  //             let result = data.json();
  //             if (result.code == '200') {
  //               this.storage.set("commodityType", result.data);
  //             } else {
  //               this.helperService.presentToast('获取数据字典【货物类型】失败');
  //             }
  //           },
  //           (err)=> {
  //             this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
  //           });
  //         //保存车辆类型：TransType
  //       this.http.post(this.enumsUrl, {"enumname":"TransType"}, options)
  //         .subscribe((data)=> {
  //             let result = data.json();
  //             if (result.code == '200') {
  //               this.storage.set("transType", result.data);
  //             } else {
  //               this.helperService.presentToast('获取数据字典【车辆类型】失败');
  //             }
  //           },
  //           (err)=> {
  //             this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
  //           });
  //       //保存体积单位：VolumeUnit
  //       this.http.post(this.enumsUrl, {"enumname":"VolumeUnit"}, options)
  //         .subscribe((data)=> {
  //             let result = data.json();
  //             if (result.code == '200') {
  //               this.storage.set("volumeUnit", result.data);
  //             } else {
  //               this.helperService.presentToast('获取数据字典【体积单位】失败');
  //             }
  //           },
  //           (err)=> {
  //             this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
  //           });
  //       //保存重量单位：WeightUnit
  //       this.http.post(this.enumsUrl, {"enumname":"WeightUnit"}, options)
  //         .subscribe((data)=> {
  //             let result = data.json();
  //             if (result.code == '200') {
  //               this.storage.set("weightUnit", result.data);
  //             } else {
  //               this.helperService.presentToast('获取数据字典【重量单位】失败');
  //             }
  //           },
  //           (err)=> {
  //             this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
  //           });
  //       //结算类型：SettlementType
  //       this.http.post(this.enumsUrl, {"enumname":"SettlementType"}, options)
  //         .subscribe((data)=> {
  //             let result = data.json();
  //             if (result.code == '200') {
  //               this.storage.set("settlementType", result.data);
  //             } else {
  //               this.helperService.presentToast('获取数据字典【结算类型】失败');
  //             }
  //           },
  //           (err)=> {
  //             this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
  //           });
  //       //在线支付类型：PayType
  //       this.http.post(this.enumsUrl, {"enumname":"PayType"}, options)
  //         .subscribe((data)=> {
  //             let result = data.json();
  //             if (result.code == '200') {
  //               this.storage.set("payType", result.data);
  //             } else {
  //               this.helperService.presentToast('获取数据字典【在线支付类型】失败');
  //             }
  //           },
  //           (err)=> {
  //             this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
  //           });

  //       //货物重量
  //       this.http.post(this.typevalUrl,{type:'EX_WEIGHTEM'}, options).subscribe((data)=> {
  //         let result = data.json();
  //         if (result.code == '200') {
  //           this.storage.set("exWeightem", result.data);
  //         } else {
  //           this.helperService.presentToast('获取数据字典【货物重量】失败');
  //         }
  //       },
  //       (err)=> {
  //         this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
  //       });
  //       //司机类型
  //       this.http.post(this.typevalUrl,{type:'Driver'}, options).subscribe((data)=> {
  //         let result = data.json();
  //         if (result.code == '200') {
  //           this.storage.set("driverType", result.data);
  //         } else {
  //           this.helperService.presentToast('获取数据字典【司机类型】失败');
  //         }
  //       },
  //       (err)=> {
  //         this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
  //       });
  //       //注册资金要求
  //       this.http.post(this.typevalUrl,{type:'Registered_funds'}, options).subscribe((data)=> {
  //         let result = data.json();
  //         if (result.code == '200') {
  //           this.storage.set("registeredFunds", result.data);
  //         } else {
  //           this.helperService.presentToast('获取数据字典【注册资金要求】失败');
  //         }
  //       },
  //       (err)=> {
  //         this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
  //       });
  //       //信誉等级
  //       this.http.post(this.typevalUrl,{type:'Credit_level'}, options).subscribe((data)=> {
  //         let result = data.json();
  //         if (result.code == '200') {
  //           this.storage.set("creditLevel", result.data);
  //         } else {
  //           this.helperService.presentToast('获取数据字典【信誉等级】失败');
  //         }
  //       },
  //       (err)=> {
  //         this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
  //       });
  //       //保证金额度
  //       this.http.post(this.typevalUrl,{type:'Security_deposit'}, options).subscribe((data)=> {
  //         let result = data.json();
  //         if (result.code == '200') {
  //           this.storage.set("securityDeposit", result.data);
  //         } else {
  //           this.helperService.presentToast('获取数据字典【保证金额度】失败');
  //         }
  //       },
  //       (err)=> {
  //         this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
  //       });
  //       //帮我买配送时间要求： TimeLimit
  //       this.http.post(this.enumsUrl, {"enumname":"TimeLimit"}, options)
  //       .subscribe((data)=> {
  //           let result = data.json();
  //           if (result.code == '200') {
  //             this.storage.set("timeLimit", result.data);
  //           } else {
  //             this.helperService.presentToast('获取数据字典【车辆类型】失败');
  //           }
  //         },
  //         (err)=> {
  //           this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
  //         });

  //         //帮我送取货时间时间要求：
  //       this.http.post(this.typevalUrl,{type:'PTIME_LIMIT'}, options).subscribe((data)=> {
  //         let result = data.json();
  //         if (result.code == '200') {
  //           this.storage.set("takeLimit", result.data);
  //         } else {
  //           this.helperService.presentToast('获取数据字典【司机类型】失败');
  //         }
  //       },
  //       (err)=> {
  //         this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
  //       });
  //     })
  //   });

  //     //读取本地地区JSON文件
  //     const areaJson = require('../../assets/areaJson.json');
  //     this.storage.set("areaJson", areaJson);
  // }

  //跳转到注册界面
  goToRegister() {
    this.nav.push(RegisterPage);
  }

  //忘记密码
  forgetPassword() {
    this.nav.push(ForgetPage);
  }


  //发送验证码方法
  sendYZM() {
    this.isShow = true;
    //登录失败，token会过期需要重新获取
    this.storage.set("token", null).then(data => {
        this.helperService.getOptionss().then(options=>{
            this.http.post(this.authCodeUrl,null,options)
              .subscribe((data) => {
                let myData = data.json();
                this.codeImage = "data:image/jpeg;base64," + myData.data;
              });
        });
    });
  }

}
