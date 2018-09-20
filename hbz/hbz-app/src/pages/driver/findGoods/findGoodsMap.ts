import {Component,ViewChild, ElementRef} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams} from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
import { OrderDetail } from '../../shipper/fulltransport/orderDetail';
import { HelpMeSendTakeConfirmOrder } from '../helpMeSend/confirmOrder';
import { helpMeBuyTakeConfirmOrder } from '../helpMeBuy/confirmOrder';

declare var BMap;
declare var BMAP_STATUS_SUCCESS;
declare var BMAP_ANCHOR_TOP_LEFT;
declare var BMAP_ANCHOR_TOP_RIGHT;
declare var BMAP_NAVIGATION_CONTROL_ZOOM;

@Component({
  selector: 'findGoodsMap',
  templateUrl: 'findGoodsMap.html'
})
export class FindGoodsMap {
    public helperService:HelperService;
    private queryNearUrl: string = "/api/order/task/near/query";//3.3.3.4	待接运订单查询
    private orderDetailUrl: string = "/api/order/get";//订单详情
    
    @ViewChild('map') map_container: ElementRef;
    map: any;//地图对象
    marker: any;//标记
    myIcon: any;

    public currentPoint;
    public distanceObj = {"10":"10KM","20":"20KM","100":"100KM"};
    public selectedDistance="10";
    public orderTypes = [];

    public obj:any={};

    constructor(
        public params: NavParams,
        private nav:NavController,
        public alertCtrl: AlertController,
        public loadingCtrl: LoadingController,
        public toastCtrl: ToastController,
        public modalCtrl: ModalController,
        public platform:Platform,
        private http: Http,
        public storage: Storage,
        public config:Config
        // private geolocation: Geolocation
        // private locationProvider:LocationProvider
      ) {
          this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
          this.queryNearUrl = this.helperService.api + this.queryNearUrl;
          this.orderDetailUrl = this.helperService.api + this.orderDetailUrl;
          
          this.myIcon = new BMap.Icon("assets/icon/huowu.png", new BMap.Size(30, 30));

          this.orderTypes = this.params.get('orderTypes');
        }


    ionViewDidEnter() {
      this.map = new BMap.Map("map_container");
      let geolocation = new BMap.Geolocation(); 
      let $this = this;
      geolocation.getCurrentPosition(function(r){  
        if(this.getStatus() == BMAP_STATUS_SUCCESS){  
          // console.log(r.point)
          $this.currentPoint = r.point;
          //默认查询
          $this.getNearCar(r.point.lng, r.point.lat, $this.selectedDistance);
        }
      });



        // 添加定位控件
      let geolocationControl = new BMap.GeolocationControl();
      geolocationControl.addEventListener("locationSuccess", function(e){
        // 定位成功事件
      });
      geolocationControl.addEventListener("locationError",function(e){
        // 定位失败事件
        alert(e.message);
      });
      this.map.addControl(geolocationControl);   //定位控件，针对移动端开发，默认位于地图左下方。


    }

    getNearCar2(){
      this.getNearCar(this.currentPoint.lng, this.currentPoint.lat,this.selectedDistance);
    }

    //附近车辆
    getNearCar(x,y,distance){
        //获取附近车辆，默认10KM
        this.http.post(this.queryNearUrl,{
          settlementType:"ONLINE_PAYMENT",
          orderTypes:this.orderTypes,
          locationX:x,
          locationY:y,
          distance:distance
        },this.helperService.getOptions())
        .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
              // console.log(result);
              this.map.clearOverlays();
              let marker = new BMap.Marker(this.currentPoint);//当前用户GPS获取的实时位置
              this.map.addOverlay(marker);

              var circle = new BMap.Circle(this.currentPoint, distance * 1000,{
                    strokeColor: "blue",
                    strokeWeight: 0.2,
                    fillColor: "#ffffff",
                    fillOpacity: 0.6
                });//设置圆形的半径，单位为米
              this.map.addOverlay(circle);            //增加圆

              let jibie = 15;
              if(distance == 10){
                jibie = 11;//比例尺10公里
              }
              if(distance == 20){
                jibie = 11;//比例尺10公里
              }
              if(distance == 100){
                jibie = 10;//比例尺20公里
              }

              this.map.centerAndZoom(this.currentPoint, jibie);//设置中心和地图显示级别
              let top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
              let top_right_navigation = new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_ZOOM}); //右上角
              this.map.addControl(top_left_control);
              this.map.addControl(top_right_navigation);


              result.data.forEach(element => {
                  let point1 = new BMap.Point(element.destX, element.destY);
                  let marker1 = new BMap.Marker(point1, { icon: this.myIcon });
                  this.map.addOverlay(marker1);

                  let $this = this;
                  marker1.addEventListener("click", function(){
                    $this.gotoDetail(element)
                  });
              });
            }
          },
          (err)=> {
            console.log(err);
            this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
          });
    }

    gotoDetail(item){
      this.http.post(this.orderDetailUrl,{id:item.id},this.helperService.getOptions())
      .subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.obj = result.data;
            console.log(this.obj.orderType)
            //根据类型不同，跳转到不同详情页
            if(this.obj.orderType == "LTL" || this.obj.orderType == "FSL"){
                this.nav.push(OrderDetail,{obj: this.obj, bottomType:'fsl-taking'})
            }
            if(this.obj.orderType == "SEND"){
              this.nav.push(HelpMeSendTakeConfirmOrder,{obj: this.obj})
            }
            if(this.obj.orderType == "BUY"){
              this.nav.push(helpMeBuyTakeConfirmOrder,{obj: this.obj})
            }
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
    }

}
