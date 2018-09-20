import { Component } from '@angular/core';
import {NavParams, Platform, NavController, ToastController,AlertController, LoadingController } from 'ionic-angular';
import {Http}from '@angular/http';
import { LoginPage } from '../login/login';
import { RegisterPage } from '../login/register';
import { TabsPage } from '../tabs/tabs';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../app/services/help.service';
import { UserService } from '../../app/services/user.service';
import { Config } from '../../app/config';

// declare var require;

@Component({
  selector: 'page-welcome',
  templateUrl: 'welcome.html',
})

export class WelcomePage {

  public loginUrl:string = "/api/security";//登录
  public loginName:string = "";
  public password:string = "";
  public token:string = "";
  public helperService:HelperService;
  public userService:UserService;
  
  slides: any;
  showSkip = true;
  dir: string = 'ltr';

  constructor(
    private nav:NavController,
    public alertCtrl: AlertController,
    public loadingCtrl: LoadingController,
    public toastCtrl: ToastController,
    public platform:Platform,
    private http:Http,
    public navParams: NavParams,
    public storage: Storage,
    public config:Config) {
      this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
      this.userService = new UserService(this.helperService,storage,http);
      this.loginUrl = this.helperService.api + this.loginUrl;
        // this.downloadImg();
      // this.loadStorage();


      this.dir = platform.dir();
      this.slides = [
        {
          image: 'assets/images/welcome/welcome_01.png'
        },
        {
          image: 'assets/images/welcome/welcome_03.jpg'
        }
      ];

      this.isSkipLogin();
  }


  //页面加载完成后执行
  ionViewDidLoad() {
    // console.log(this.helperService.api)
  }


  //是否静默登录
  isSkipLogin(){
      let $this = this;
      this.storage.get("loginName").then((data)=>{this.loginName = data});
      this.storage.get("password").then((data)=>{this.password = data});
      this.storage.get("token").then((data)=>{this.token = data});
      this.storage.get("errLoginCount").then((data)=>{
        if(data == 0 && $this.token != null){
          //表示之前已经登录过。静默登录
          $this.http.post($this.loginUrl, {
            j_user:$this.loginName,
            j_pass:$this.password,
            language:"zh_CN",
            loginFor:'App'
          }, $this.helperService.getOptions()).subscribe((data) => {
                $this.helperService.dismissLoader();// dismiss loading
                let myData = data.json();
                if (myData.code == "200") {
                  $this.userService.saveUserInfoToLocalStorage(myData,$this.password);
                  $this.nav.push(TabsPage);
                }
              });
        }
      });
    }


  //启动
  goToLogin(){
    this.nav.setRoot(LoginPage);
  }
  goToRegister(){
    this.nav.setRoot(RegisterPage);
  }

  onSlideChangeStart(slider) {
    this.showSkip = !slider.isEnd();
  }
  // //启动完成后首先从本地缓存中获取，如果没有则调用APP服务端接口返回引导页，并把结果放到本地缓存中，
  // //并设置缓存失效默认2周
  // loadStorage(){
  //   let _this = this;
  //   this.storage.get("guidePages")
  //     .then(function(data){
  //       console.log(data);
  //       if(data == null){
  //         let p = _this.httpPostGuidePage();
  //         p.then(function(data){
  //           console.log(data);
  //           _this.storage.set("guidePages", data);
  //         }).catch(function(err){
  //           console.log(err);
  //         });
  //       } else {
  //         console.log(data);
  //       }
  //     })

  // }

  // //获取服务端引导页
  // httpPostGuidePage() {
  //      //设置请求方式并请求接口
  //     let headers = new Headers({
  //      'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
  //     });

  //     let options = new RequestOptions({
  //       headers: headers
  //     });
  //     let _this = this;
  //     return new Promise(function (resolve, reject) {
  //       _this.http.post(_this.loginUrl,_this.headleAjaxBodyParam(
  //         {
  //           'loginName':_this.loginName,
  //           'password':_this.password
  //         }),options)
  //           .subscribe(
  //             (data) => {
  //               console.log(data.json);
  //               resolve(data);
  //             },
  //             (err) => {
  //               console.log(err);
  //               reject(err);
  //             }
  //           );
  //     });
  // }



  // //下载图片到本地
  // downloadImg() {
  //   const url = 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510804111&di=1519857cb558b27ddfb5cd95eca3bca3&imgtype=jpg&er=1&src=http%3A%2F%2Fupload.mnw.cn%2F2017%2F0811%2F1502421768647.jpeg';
  //   this.fileTransfer.download(url, this.file.dataDirectory + 'file.jpeg').then((entry) => {
  //     alert('download complete: ' + entry.toURL());
  //     this.file.createFile(entry.toURL(), "file.jpeg", true)
  //       .then(
  //         function(data){
  //           alert("success")
  //         }
  //       ).catch(
  //         function(error){
  //         }
  //       );
  //   }, (error) => {
  //     // handle error
  //     alert(error);
  //   });

  // }
  //   //用于处理请求中body格式
  //   headleAjaxBodyParam(param) {
  //     let bodyStr = [];
  //     for (let item in param) {
  //       bodyStr.push(item + '=' + param[item])
  //     }
  //     return bodyStr.join('&');
  //   }



}
