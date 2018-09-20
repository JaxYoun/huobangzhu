import {Component} from '@angular/core';
import {ViewController, NavController, ToastController,AlertController, LoadingController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../app/services/help.service';
import {RegisterSubPage} from '../login/register_sub';
import { Config } from '../../app/config';
import { LoginPage } from './login';

@Component({
  templateUrl: 'register.html'
})
export class RegisterPage {
  public btnText: string = "获取验证码";
  public inputPhoneNumber: number;
  public inputYZM: string = '';
  public nickName: string = '';
  private checkPhoneUrl: string = "/api/user/app/telephone/check";//检查手机号是否注册
  private yzmUrl: string = "/api/user/authCode/send";//获取短信验证码
  private checkYzmUrl: string = "/api/user/app/authCode/check";//短信验证码校验
  public helperService:HelperService;
  public isNext: string = 'disabled'; // 下一步禁用状态: disabled || ''
  public isSendingYZM = false; // 是否正在发送验证码

  constructor(private nav: NavController,
     public toastCtrl: ToastController,
     public loadingCtrl: LoadingController,
     public alertCtrl: AlertController,
     private http: Http,
     public storage: Storage,
     public config:Config,
     private viewCtrl: ViewController) {
        this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
        this.checkPhoneUrl = this.helperService.api + this.checkPhoneUrl;
        this.yzmUrl = this.helperService.api + this.yzmUrl;
        this.checkYzmUrl = this.helperService.api + this.checkYzmUrl;
  }


  //发送验证码方法
  sendYZM(count: number) {
    if (!(this.nickName.length >= 3 && this.nickName.length <= 20)) {
      this.helperService.presentToast('请输入3到20位长度的昵称');
      return;
    }
    if (this.inputPhoneNumber == null ){
      this.helperService.presentToast('请输入手机号！');
      return;
    }

    this.isSendingYZM = true;
    this.btnText = "发送验证码...";

    //判断手机是否已注册
    if (this.inputPhoneNumber != null && this.inputPhoneNumber.toString().length == 11
      && this.helperService.checkPhoneFormat(this.inputPhoneNumber.toString())) {

      this.helperService.getOptionss().then(options=>{
        console.log("检查手机号是否注册token" + options)
        this.http.post(this.checkPhoneUrl, {"telephone": this.inputPhoneNumber}, options)
        .subscribe((data) => {
            let result = data.json();
            if (result.code == '200') {
              this.http.post(this.yzmUrl, {'phone': this.inputPhoneNumber}, options)
                .subscribe((data)=> {
                    let result = data.json();
                    if (result.code == '200') {
                      this.helperService.presentToast('验证码发送成功');
                      this.setTime(count);//验证码发送成功 开始倒计时
                      this.isNext = "";
                    } else {
                      this.helperService.presentToast('短信发送失败,同一小时内不允许发送三次验证码');
                    }
                  },
                  (err)=> {
                    this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
                  });
            } else {
              this.helperService.presentToast('手机已被注册');
            }
          },
          (err) => {
            this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
          });
      });

    } else {
      this.helperService.presentToast('请输入正确的手机号');
      this.isSendingYZM = false;
      this.btnText = "获取验证码";
    }
  }

  //计时器
  setTime(count: number) {
    setTimeout(() => this.showDelay(count - 1), 1000);
    // var element = <HTMLInputElement> document.getElementById("btnExcel");
    // element.disabled = true;
  }

  //延迟执行方法
  showDelay(num: number) {
    this.btnText = num + "秒后重新发送"
    if (num < 1) {
      // var element = <HTMLInputElement> document.getElementById("btnExcel");
      // element.disabled = false;
      this.btnText = "重新发送";
      return;
    }
    this.setTime(num);
  }

  reset(){
    this.nickName = "";
    this.inputPhoneNumber=null;
    this.inputYZM = "";
  }


  goBack() {
    this.nav.push(LoginPage);
  }

  //下一步 跳转设置密码界面
  goToSetPasswordPage() {
    if (this.nickName == "" ){
      this.helperService.presentToast('昵称不能为空！');
      return;
    }
    if (this.inputPhoneNumber == null ){
      this.helperService.presentToast('手机号不能为空！');
      return;
    }
    if (this.inputYZM == "" ){
      this.helperService.presentToast('请输入验证码！');
      return;
    }
    //通过token来识别是哪个用户的验证码
    this.helperService.getOptionss().then(options=>{
      this.http.post(this.checkYzmUrl, {'authCode': this.inputYZM}, options)
        .subscribe((data)=> {
          let result=data.json();
          if (result.code == '200') {
            this.nav.push(RegisterSubPage,
              {'phone': this.inputPhoneNumber,
              'nickName': this.nickName,
              'authCode':this.inputYZM}
              );//跳转页面传递参数
          }else{
            this.helperService.presentToast('验证码错误，请重新获取');
          }
        }
        , (err)=> {
          this.helperService.presentToast('网络错误，请稍后再试！');
        });
    });
  }


  //页面加载完毕 出初始化数据
  ionViewLoaded() {
  }

  ionViewWillEnter() {
    this.viewCtrl.setBackButtonText('返回');
  }
  gotolo(){
    this.nav.push(RegisterSubPage);
  }
}
