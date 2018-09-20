import { Component, ViewChild } from '@angular/core';
import { Nav, Platform } from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { TranslateService } from '@ngx-translate/core';
import { WelcomePage } from '../pages/welcome/welcome';
import { Storage } from '@ionic/storage';
// import { BackgroundMode } from '@ionic-native/background-mode';
import { InformationDetail } from '../pages/warehouse/information/informationDetail';


declare var window;

@Component({
  templateUrl: 'app.html'
})
export class MyApp {
    @ViewChild('myNav') nav: Nav;
    // public backgroundMode: BackgroundMode;
    public rootPage: any = WelcomePage;
  constructor(private translate: TranslateService,platform: Platform
    , statusBar: StatusBar, splashScreen: SplashScreen
    , public storage: Storage,) {

      let $this = this;
    platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      statusBar.styleLightContent();
      splashScreen.hide();
      // statusBar.backgroundColorByName("green");
      // this.nav.push(WelcomePage);
      // this.backgroundMode.enable();


      //浏览器上推送，获取不到插件
      if(window.plugins){
        //启动极光推送服务 
        window.plugins.jPushPlugin.init(); 
        //设置显示最新得条数
        window.plugins.jPushPlugin.setLatestNotificationNum(5);
        //调试模式
        window.plugins.jPushPlugin.setDebugMode(true);
        //点击通知栏的回调
        window.plugins.jPushPlugin.openNotificationInAndroidCallback= function(data){    
          // console.log(data);  
          $this.nav.push(InformationDetail,{isJpush:true,itemId:data.extras.n_sitePushMessageId,title:'通知'});
        }  
      }

    });
    this.initTranslate();
  }

  initTranslate() {
    // Set the default language for translation strings, and the current language.
    this.translate.setDefaultLang('en');
    if (this.translate.getBrowserLang() !== undefined) {
      this.translate.use(this.translate.getBrowserLang());
    } else {
      this.translate.use('en'); // Set your language here
    }
  }

}

