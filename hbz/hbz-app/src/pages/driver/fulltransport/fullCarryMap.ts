import {Component,ViewChild, ElementRef} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
// import { LocationProvider } from '../../../providers/LocationProvider';
import { Config } from '../../../app/config';
// import * as base64js from "js-base64";

declare var BMap;
declare var BMAP_STATUS_SUCCESS;
// declare var require;


@Component({
  selector: 'fullCarryMap',
  templateUrl: 'fullCarryMap.html'
})
export class FullCarryMap {
    public helperService:HelperService;
    private queryNearUrl: string = "/api/order/task/near/query";//3.3.3.4	待接运订单查询
    // private gpsConvertBaiduUrl = "http://api.map.baidu.com/ag/coord/convert?";

    @ViewChild('map') map_container: ElementRef;
    map: any;//地图对象
    marker: any;//标记
    myIcon: any;

    public markerArray = new Array(); //存放标注点对象的数组

    public currentPoint;
    public distanceObj = {"10":"10KM","20":"20KM","100":"100KM"};
    public selectedDistance="10";

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
        // private locationProvider:LocationProvider
      ) {
          this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);
          this.queryNearUrl = this.helperService.api + this.queryNearUrl;
          this.myIcon = new BMap.Icon("assets/icon/huowu.png", new BMap.Size(30, 30));
        }


    ionViewDidEnter() {
      // let map = this.map =
      //     new BMap.Map(
      //       this.map_container.nativeElement,
      //       {
      //         enableMapClick: true,//点击拖拽
      //         enableScrollWheelZoom: true,//启动滚轮放大缩小，默认禁用
      //         enableContinuousZoom: true //连续缩放效果，默认禁用
      //       }
      //     );//创建地图实例


      // //获取我的坐标
      // this.locationProvider.getLocations().then(
      //   (location)=>{
      //     let longitude = 0;
      //     let latitude = 0;
      //     if(!location[0]){//默认坐标
      //         longitude=104.074132;
      //         latitude=30.655826;
      //     }else {
      //       longitude = location[0].longitude;
      //       latitude = location[0].latitude;
      //     }

      //      //GPS坐标转换百度地图坐标；会出现跨越请求错误，APP运行正常的
      //     let url = this.gpsConvertBaiduUrl + "from=0&to=4&x="+ longitude + "longitude&y=" + latitude;
      //     this.http.get(url,this.helperService.getOptions()).subscribe(
      //       (data) => {
      //         let result = data.json();
      //         let x = base64js.Base64.decode(result.x); 
      //         let y = base64js.Base64.decode(result.y);  

      //         let point = new BMap.Point(x,y);//当前用户GPS获取的实时位置
      //         let marker = new BMap.Marker(point);
      //         this.map.addOverlay(marker);
      //         map.centerAndZoom(point, 12);//设置中心和地图显示级别
      //         map.addControl(new BMap.NavigationControl());//放大缩小按钮


      //         console.log(this.helperService.getOptions());
      //         //获取附近车辆，默认10KM
      //         this.http.post(this.queryNearUrl,{
      //           locationX:x,
      //           locationY:y,
      //           distance:this.distance
      //         },this.helperService.getOptions())
      //         .subscribe((data)=> {
      //             let result = data.json();
      //             if (result.code == '200') {
      //               console.log(result);
      //             }
      //           },
      //           (err)=> {
      //             console.log(err);
      //             this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
      //           });

      //       },(err) => {
      //         console.log(err);
      //       }
      //     )
          
      //   }
      // ).catch(
      //   (erro)=>{console.log("erro:"+erro)}
      // );


      this.map = new BMap.Map("map_container");
      let geolocation = new BMap.Geolocation(); 
      let $this = this;
      geolocation.getCurrentPosition(function(r){  
        if(this.getStatus() == BMAP_STATUS_SUCCESS){  
          console.log(r.point)
          $this.currentPoint = r.point;
          //默认查询
          $this.getNearCar(r.point.lng, r.point.lat, $this.selectedDistance);
        }
      });
    }

    getNearCar2(){
      this.getNearCar(this.currentPoint.lng, this.currentPoint.lat,this.selectedDistance);
    }

    //附近车辆
    getNearCar(x,y,distance){
        //获取附近车辆，默认10KM
        this.http.post(this.queryNearUrl,{
          settlementType:"ONLINE_PAYMENT",orderType:this.params.get('orderType'),
          locationX:x,
          locationY:y,
          distance:distance
        },this.helperService.getOptions())
        .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
              console.log(result);

              this.map.clearOverlays();
              let marker = new BMap.Marker(this.currentPoint);//当前用户GPS获取的实时位置
              this.map.addOverlay(marker);
              this.map.centerAndZoom(this.currentPoint, 14);//设置中心和地图显示级别

              result.data.forEach(element => {
                  let point1 = new BMap.Point(element.destX, element.destY);
                  let marker1 = new BMap.Marker(point1, { icon: this.myIcon });
                  this.markerArray.push(marker1);
              });

              this.markerArray.forEach(element => {
                this.map.addOverlay(element);
                element.addEventListener("click", function(){
                  console.log("1111111111111111")
                });
              });
            }
          },
          (err)=> {
            console.log(err);
            this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
          });
    }

    // //附近车辆
    // getNearCar2(){
    //   let point1 = new BMap.Point(104.059364,30.70484);//西南交通大学
    //   let point2 = new BMap.Point(104.080546,30.701653);//火车北站
    //   let point3 = new BMap.Point(104.019659,30.712331);//茶店子客运站
    //   let marker1 = this.marker = new BMap.Marker(point1, { icon: this.myIcon });
    //   let marker2 = this.marker = new BMap.Marker(point2, { icon: this.myIcon });
    //   let marker3 = this.marker = new BMap.Marker(point3, { icon: this.myIcon });
    //   this.map.addOverlay(marker1);
    //   this.map.addOverlay(marker2);
    //   this.map.addOverlay(marker3);
    //   this.map.centerAndZoom(new BMap.Point(104.074132,30.655826), 12);
    // }

    // //GPS坐标转换百度地图坐标；会出现跨越请求错误，APP运行正常的
    // convertBaiduByGPS(longitude,latitude){
    //   let url = this.gpsConvertBaiduUrl + "from=0&to=4&x="+ longitude + "longitude&y=" + latitude;
    //   this.http.get(url,this.helperService.getOptions()).subscribe(
    //     (data) => {
    //       let result = data.json();
    //       var result2 = base64js.Base64.decode(result.x); 
    //       var result3 = base64js.Base64.decode(result.y);  
    //     },(err) => {
    //       console.log(err);
    //     }
    //   )
    // }
}
