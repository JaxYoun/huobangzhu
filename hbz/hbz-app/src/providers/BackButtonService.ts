import { Injectable } from '@angular/core';
import { Platform, ToastController, App, NavController, Tabs } from 'ionic-angular';
import { AlertController } from 'ionic-angular/components/alert/alert-controller';

@Injectable()
export class BackButtonService {

  //控制硬件返回按钮是否触发，默认false
  // backButtonPressed: boolean = true;

  //构造函数 依赖注入
  constructor(public platform: Platform,
              public appCtrl: App,
              public toastCtrl: ToastController,
              public alertCtrl:AlertController
            ) { }

  //注册方法
  registerBackButtonAction(tabRef: Tabs): void {
    this.platform.registerBackButtonAction(() => {
      //获取模态框      
      let activePortal = this.appCtrl._appRoot._modalPortal.getActive();
      if(activePortal){
        activePortal.dismiss().catch(() => {});
        activePortal.onDidDismiss(() => {});
        return;
      }
      let toastPortal = this.appCtrl._appRoot._toastPortal.getActive();
      if(toastPortal){
        toastPortal.dismiss().catch(() => {});
        toastPortal.onDidDismiss(() => {});
        return;
      }
      let loadingPortal = this.appCtrl._appRoot._loadingPortal.getActive();
      if(loadingPortal){
        loadingPortal.dismiss().catch(() => {});
        loadingPortal.onDidDismiss(() => {});
        return;
      }
      let overlayPortal = this.appCtrl._appRoot._overlayPortal.getActive();
      if(overlayPortal){
        overlayPortal.dismiss().catch(() => {});
        overlayPortal.onDidDismiss(() => {});
        return;
      }

      // 获取NavController
      let activeNav: NavController = this.appCtrl.getActiveNav();
      //如果可以返回上一页，则执行pop
      if (activeNav.canGoBack() && tabRef != null) {
        activeNav.pop();
      } else {
        if (tabRef == null || tabRef._selectHistory[tabRef._selectHistory.length - 1] === tabRef.getByIndex(0).id) {
          //执行退出
          this.showExit();
        } else {
          //选择首页第一个的标签
          tabRef.select(0);
        }
      }
    }, 1);
  }

  // //退出应用方法
  // private showExit(): void {
  //   //如果为true，退出
  //   if (this.backButtonPressed) {
  //     this.platform.exitApp();
  //   } else {
  //       //第一次按，弹出Toast
  //       this.toastCtrl.create({
  //           message: '再按一次退出应用',
  //           duration: 2000,
  //           position: 'top'
  //       }).present();
  //     //标记为true
  //     this.backButtonPressed = true;
  //     //两秒后标记为false，如果退出的话，就不会执行了
  //     setTimeout(() => this.backButtonPressed = false, 2000);
  //   }
  // }


  //退出应用方法
  private showExit(): void {
      let confirm = this.alertCtrl.create({
        title: '温馨提示',
        message: '你确定退出程序么?',
        buttons: [
          {
            text: '确定',
            handler: () => {
              this.platform.exitApp();
            }
          },
          {
            text: '取消',
            handler: () => {
              // this.backButtonPressed = true;
            }
          }
        ]
      });
      confirm.present();
    }
}