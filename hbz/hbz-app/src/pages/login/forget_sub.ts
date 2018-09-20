import {Component} from '@angular/core';
import {NavParams,ViewController, NavController, ToastController,AlertController, LoadingController } from 'ionic-angular';
import { ModalController } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import {Http}from '@angular/http';
import {LoginPage} from "./login";
import { HelperService } from '../../app/services/help.service';
import { Config } from '../../app/config';

@Component({
  templateUrl: 'forget_sub.html'
})
export class ForgetSubPage {
  private loader: any;    
  phone:string = '';
  nickName:string = '';
  authCode:string = '';
  password_1:string = '';
  password_2:string = '';
  public resetPasswordUrl: string = "/api/user/app/reset";//密码重置
  public helperService:HelperService;
  public isNext: string = ''; // 下一步禁用状态: disabled || ''
  cucumber: boolean=true;
  
  constructor(private nav:NavController, params:NavParams,
    public toastCtrl: ToastController,
    public loadingCtrl: LoadingController,
    public alertCtrl: AlertController,    
    public storage: Storage,
    public modalCtrl: ModalController,
    public config:Config,
    private http:Http, private viewCtrl:ViewController) {
      this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
      this.resetPasswordUrl = this.helperService.api + this.resetPasswordUrl;
      
      this.phone = params.get('phone');
      this.authCode = params.get('authCode');
  }

  goBack() {
    this.nav.pop();
  }

  modifyPassword() {
    if(!this.helperService.checkStrFormat(this.password_1)){
      this.helperService.presentToast("只能输入由数字和26个英文字母或者下划线组成的字符串");
      return;
    }
    if (this.password_1.length < 6 ) {
      this.helperService.presentToast('密码长度不能小于6位');
    } else if (this.password_1 != this.password_2) {
      this.helperService.presentToast('两次密码输入不一致');
    } else {
      this.http.post(this.resetPasswordUrl, {
        'telephone': this.phone,
        'password': this.password_1,
        'authCode': this.authCode
      }, this.helperService.getOptions()).subscribe((data)=> {
        let response = data.json();
        if (response.code == "200") {
          this.goBackLoginPage();
        } else {
          this.helperService.presentToast('修改密码失败：' + response.msg);
        }
      }, (err)=> {
        this.helperService.presentToast('修改密码失败：网络连接失败！');
      });
    }
  }

  //页面加载完毕 出初始化数据
  ionViewLoaded() {
  }

  ionViewWillEnter() {
    this.viewCtrl.setBackButtonText('返回');
  }

  goBackLoginPage() {
    let loading = this.loadingCtrl.create({
      // spinner:'hide', //隐藏进度款
      showBackdrop: false,
      content: '修改密码成功，正在为您跳转至登录界面'
    });
    loading.present();
    
    setTimeout(() => {
      loading.dismiss();
      this.nav.push(LoginPage);
    }, 3000);
  }

}
