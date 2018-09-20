import { Component } from '@angular/core';
import { Platform, NavController, ToastController, AlertController, LoadingController, ModalController, NavParams } from 'ionic-angular';
import { Http } from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { DomSanitizer } from '@angular/platform-browser';


declare var BMap;
@Component({
  selector: 'page-contractDetail',
  templateUrl: 'contractDetail.html',
})
export class ContractDetail {
    private contractDetalUrl: string = "/api/web/agreement/showAgreementDetail";
    public title: string = "合同详情";
    public helperService:HelperService;
    public contractObj:any;
    public attachPath:any;
    public pageUrl:any;


    constructor(
        private sanitizer: DomSanitizer,
        public params: NavParams,
        private nav: NavController,
        public alertCtrl: AlertController,
        public loadingCtrl: LoadingController,
        public toastCtrl: ToastController,
        public modalCtrl: ModalController,
        public platform: Platform,
        private http: Http,
        public storage: Storage,
        public config: Config) {
            this.helperService = new HelperService(nav, alertCtrl, loadingCtrl, toastCtrl, storage, http, config);
            this.contractDetalUrl = this.helperService.api + this.contractDetalUrl;
            this.contractObj = params.get('obj');
            let url = this.contractDetalUrl + "?attachPath=" + this.contractObj.attachPath;
            this.getSafeUrl(url);
        }

        getSafeUrl(url){
            this.pageUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url); 
        }
}
