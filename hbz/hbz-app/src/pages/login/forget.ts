import {Component} from '@angular/core';
import {ViewController, NavController, ToastController,AlertController, LoadingController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../app/services/help.service';
import {ForgetSubPage} from '../login/forget_sub';
import { Config } from '../../app/config';
@Component({
  templateUrl: 'forget.html'
})
export class ForgetPage {
  public btnText: string = "获取验证码";
  public inputPhoneNumber: number;
  public inputYZM: string = '';
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
        this.yzmUrl = this.helperService.api + this.yzmUrl;
        this.checkYzmUrl = this.helperService.api + this.checkYzmUrl;
  }


  //发送验证码方法
  sendYZM(count: number) {

    if (this.inputPhoneNumber != null && this.inputPhoneNumber.toString().length == 11
      && this.helperService.checkPhoneFormat(this.inputPhoneNumber.toString())) {

        let $this = this;
        this.helperService.getOptionss().then(options=>{
            $this.http.post($this.yzmUrl, {'phone': $this.inputPhoneNumber,'source':'FORGET_PASSWORD'}, options)
                .subscribe((data) => {
                    let result = data.json();
                    if (result.code == '200') {
                      $this.isSendingYZM = true;
                      $this.btnText = "发送验证码...";

                      $this.helperService.presentToast('验证码发送成功');
                      $this.setTime(count);//验证码发送成功 开始倒计时
                      $this.isNext = "";
                    } else {
                      $this.helperService.presentToast(result.msg);
                    }
                  },
                  (err) => {
                    $this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
                  });
        });
        // this.http.post(this.yzmUrl, {'phone': this.inputPhoneNumber}, this.helperService.getOptions())
        //   .subscribe((data)=> {
        //       let result = data.json();
        //       if (result.code == '200') {
        //         this.helperService.presentToast('验证码发送成功');
        //         this.setTime(count);//验证码发送成功 开始倒计时
        //         this.isNext = "";
        //       } else {
        //         this.helperService.presentToast('短信发送失败,同一小时内不允许发送三次验证码');
        //       }
        //     },
        //     (err)=> {
        //       this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        //     });
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
    this.inputPhoneNumber=null;
    this.inputYZM = "";
  }


  goBack() {
    this.nav.pop();
  }

  //下一步 跳转设置密码界面
  goToSetPasswordPage() {
    if (this.inputPhoneNumber == null ){
      this.helperService.presentToast('手机号不能为空！');
      return;
    }
    if (this.inputYZM == "" ){
      this.helperService.presentToast('验证码不能为空！');
      return;
    }
    //通过token来识别是哪个用户的验证码
    this.http.post(this.checkYzmUrl, {'authCode': this.inputYZM}, this.helperService.getOptions())
      .subscribe((data)=> {
          let result=data.json();
          if (result.code == '200') {
            this.nav.push(ForgetSubPage,
              {'phone': this.inputPhoneNumber,
              'authCode':this.inputYZM}
              );//跳转页面传递参数
          }else{
            this.helperService.presentToast('验证码:'+result.msg);
          }
        }
        , (err)=> {
          this.helperService.presentToast('网络错误，请稍后再试！');
        });
  }


  //页面加载完毕 出初始化数据
  ionViewLoaded() {
  }

  ionViewWillEnter() {
    this.viewCtrl.setBackButtonText('返回');
  }
}
