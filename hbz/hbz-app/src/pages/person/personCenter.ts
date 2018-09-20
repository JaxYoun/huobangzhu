import {Component} from '@angular/core';
import {Platform, NavController, Loading,ToastController,AlertController, LoadingController, ModalController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../app/services/help.service';
import { SendList } from '../person/sendOrder/sendList';
import { TakeList } from './takeOrder/takeList';
import { JoinList } from './takeOrder/joinList';
import { Person} from '../person/auth/person';
import { Shop} from './shop/index';
import { Score} from './score/score';
import { Config } from '../../app/config';
import { Wallet } from "./wallet/wallet";
import { AuthPage } from './auth/auth';
import { MessagePage } from './message/message';
import { LeaseList } from './leaseOrder/leaseList';
import { CommentOrder } from './commentOrder/commentOrder';
import { MyWarehouse } from './myWarehouse/myWarehouse';
import { MyWarehouseEdit } from './myWarehouse/myWarehouseEdit';
import { SystemConfig } from './systemConfig/systemConfig';
import { PushMessagePage } from '../pushMessage/pushMessage';
import { FulltransportAddress } from './oftenAddress/fulltransportAddress';
import { HelpAddress } from './oftenAddress/helpAddress';
import { PersonSetting } from './personSetting';
import { CarInfoList } from './carInfo/carInfoList';
import { ContractList } from './contract/contractList';
// import { WechatPay  } from './test2/test2';
@Component({
  selector: 'personCenter',
  templateUrl: 'personCenter.html'
})
export class PersonCenter {
    private queryMyRolesUrl: string = "/api/user/roles";//我的角色列表
    public helperService:HelperService;
    public title:string = '个人中心';

    public isShow:boolean=false;
    public isShowWarehouse:boolean=false;
    public isShowAddress:boolean=false;

    public currentUser={
      login:'',
      imageUrl:'',
      nickName:'',
      complexScore:100
    };
    public currentUserComplexScore=100;

    constructor(
        public nav:NavController,
        public alertCtrl: AlertController,
        public loadingCtrl: LoadingController,
        public toastCtrl: ToastController,
        public modalCtrl: ModalController,
        public platform:Platform,
        private http: Http,
        public storage: Storage,
        public config:Config) {
          this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
          this.queryMyRolesUrl = this.helperService.api + this.queryMyRolesUrl;

          this.storage.get("currentUser").then(
            (data)=>{
              this.currentUser = data;
              if(this.currentUser.imageUrl == null || this.currentUser.imageUrl == ''){
                this.currentUser.imageUrl = 'assets/images/boy.png';
              }
            }
          );
          this.storage.get("currentUserComplexScore").then(
            (data)=>{
              this.currentUserComplexScore = data;
            }
          );
          
        }

        goToAnyWhere(nali) {
            switch (nali) {
              case 'SendList':
                this.nav.push(SendList);
                break;
              case 'TakeList':
                this.nav.push(TakeList);
                break;
              case 'JoinList':
                this.nav.push(JoinList);
                break;
              case 'LeaseList':
                this.nav.push(LeaseList);
                break;
              case 'Score':
                this.nav.push(Score);
                break;
              case 'Person':
                this.nav.push(Person)
                break;
              case 'Shop':
                this.nav.push(Shop)
                break;
              case 'wallet':
                this.nav.push(Wallet)
                break;
              case 'auth':
                this.nav.push(AuthPage)
                break;
              case 'MessagePage':
                this.nav.push(MessagePage)
                break;
              case 'commentOrder':
                this.nav.push(CommentOrder)
                break;
              case 'MyWareHouse':
                this.nav.push(MyWarehouse)
                break;
              case 'MyWarehouseEdit':
                this.nav.push(MyWarehouseEdit)
                break;
              case 'SystemConfig':
                this.nav.push(SystemConfig)
                break;
              // case 'PushMessagePage':
              //   this.nav.push(PushMessagePage)
              //   break;
              // case 'WechatPay':
              //   this.nav.push(WechatPay)
              //   break;
              case 'FulltransportAddress':
                this.nav.push(FulltransportAddress)
                break;
              case 'HelpAddress-Buy':
                this.nav.push(HelpAddress,{addresType:'BUY'});
                break;
              case 'HelpAddress-Ex':
                this.nav.push(HelpAddress,{addresType:'EX'});
                break;
              case 'CarInfoList':
                this.nav.push(CarInfoList);
                break;
              case 'ContractList':
                this.nav.push(ContractList);
                break;
              default:
                break;
            }
          }

    goToSetting(){
      this.currentUser.complexScore = this.currentUserComplexScore;
      let modal = this.modalCtrl.create(PersonSetting,{obj:this.currentUser});
      modal.onDidDismiss((item) => {
            if(item){
              this.currentUser.imageUrl = item.imageUrl;
            }
      });
      modal.present();
    }

    onChangeStatus(){
      if(this.isShow){
        this.isShow = false;
      } else {
        this.isShow = true;
      }
    }
    onChangeStatusWareHouse(){
      if(this.isShowWarehouse){
        this.isShowWarehouse = false;
      } else {
        this.isShowWarehouse = true;
      }
    }
    onChangeStatusShowAddress(){
      if(this.isShowAddress){
        this.isShowAddress = false;
      } else {
        this.isShowAddress = true;
      }
    }
}
