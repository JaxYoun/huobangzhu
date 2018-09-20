import { Component } from '@angular/core';
import { Platform, NavController, ToastController, AlertController, LoadingController, ModalController } from 'ionic-angular';
import { Http } from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import {ContractDetail} from './contractDetail';

@Component({
    selector: 'page-contractList',
    templateUrl: 'contractList.html',
})
export class ContractList {
    private queryContractListUrl: string = "/api/web/agreement/getAgreementListByPage";
    public title: string = "合同管理";
    public helperService: HelperService;
    public contractArray = [];
    public obj;
    public page = 0;
    public img: string = "assets/images/hetong.jpg";  //合同图片位置

    public paramsObj;


    constructor(
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
        this.queryContractListUrl = this.helperService.api + this.queryContractListUrl;
        this.initPage();
    }

    //初始化数据
    initPage() {
        this.paramsObj = {};
        this.paramsObj.page = 0;
        this.paramsObj.size = this.config.pageSize;

        this.http.post(this.queryContractListUrl, this.paramsObj, this.helperService.getOptions())
            .subscribe((data) => {
                let result = data.json();
                if (result.code == '200') {
                    if (result.data.content.length > 0) {
                        result.data.content.forEach(element => {
                            this.contractArray.push(element);
                        });
                    }
                }
            },
            (err) => {
                this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });
    }


    //下拉刷新    
    doRefresh(refresher) {
        this.contractArray = [];
        this.paramsObj = {};
        this.paramsObj.page = 0;
        this.paramsObj.size = this.config.pageSize;

        this.http.post(this.queryContractListUrl, this.paramsObj, this.helperService.getOptions())
            .subscribe((data) => {
                let result = data.json();
                if (result.code == '200') {
                    if (result.data.content.length > 0) {
                        result.data.content.forEach(element => {
                            this.contractArray.push(element);
                        });
                    } else {
                        this.helperService.presentToast('没有更多数据');
                    }
                    //复位下拉刷新
                    refresher.complete();
                    this.page = 0;
                }
            },
            (err) => {
                this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });
    }



    //上拉加载
    doInfinite(infiniteScroll) {
        //上拉加载前先为当前page++
        this.page++;

        this.paramsObj = {};
        this.paramsObj.page = this.page;
        this.paramsObj.size = this.config.pageSize;

        this.http.post(this.queryContractListUrl, this.paramsObj, this.helperService.getOptions())
            .subscribe((data) => {
                let result = data.json();
                if (result.code == '200') {
                    //等待2秒，2秒后执行
                    setTimeout(() => {
                        if (result.data.content.length > 0) {
                            result.data.content.forEach(element => {
                                this.contractArray.push(element);
                            });
                        } else {
                            this.helperService.presentToast('没有更多数据');
                        }
                        infiniteScroll.complete();
                    }, this.config.scrollWaitTime);

                } else {
                    this.helperService.presentToast('数据初始化失败：' + result.msg);
                    infiniteScroll.complete();
                }
            },
            (err) => {
                this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
                infiniteScroll.complete();
            });
    }

    //查询详情
    goDetail(contract) {
        console.log("goDetail!")
        this.nav.push(ContractDetail,{obj: contract});
    }
}
