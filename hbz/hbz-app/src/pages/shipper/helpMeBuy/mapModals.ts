import { Component,ViewChild, ElementRef} from '@angular/core';
import {Platform, NavController, ToastController,AlertController, LoadingController, ModalController, NavParams, ViewController } from 'ionic-angular';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../../app/services/help.service';
import { Config } from '../../../app/config';
// import { LocationProvider } from '../../../providers/LocationProvider';
// import * as base64js from "js-base64";

declare var BMap;
declare var BMAP_STATUS_SUCCESS;
declare var BMAP_ANIMATION_BOUNCE;
// declare var BMAP_ANCHOR_BOTTOM_LEFT;//	表示控件定位于地图的左下角
// declare var BMAP_NAVIGATION_CONTROL_LARGE;//表示显示完整的平移缩放控件

@Component({
  selector: 'mapModals',
  templateUrl: 'mapModals.html'
})
export class HelpMeBuyMapModals {
  public character; // 当前模态框信息
  public historyModal:object = {};  // 记录上一次模态框信息
  public helperService:HelperService;


  // private queryNearUrl: string = "/api/order/fsl/task/near/query";//附近货源-找货源
  // private convertBaiduUrl = "/api/map/geo/info";//接口封装百度地图逆地址解析

  @ViewChild('map') map_container: ElementRef;
  map: any;//地图对象
  marker: any;//标记
  marker2: any;//标记
  myIcon: any;
  myIcon2: any;

  public obj:any={
    "destX":"",//经度
    "destY":"",//纬度
    // "destInfo":"",//坐标中文名称，联系地址
    "destAddress":"",//详细地址
    "linker":"",//联系人
    "linkTelephone":""//联系电话
    };

  distance=10;
  public inputAddress:string="";
  public myValue="";

  constructor(
    public params: NavParams,
    public viewCtrl: ViewController,
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
    this.myIcon = new BMap.Icon("assets/icon/marker1.png", new BMap.Size(30, 30));
    this.myIcon2 = new BMap.Icon("assets/icon/marker2.png", new BMap.Size(30, 30));
    this.helperService = new HelperService(nav,alertCtrl,loadingCtrl,toastCtrl,storage,http,config);

    // this.convertBaiduUrl = this.helperService.api + this.convertBaiduUrl;
    // 设置模态框信息
    this.character = { title: '地图', visible: false }
    this.character.visible = true;
    this.historyModal = this.character;

    this.obj = params.get("obj");
  }



  
  ionViewDidEnter() {
    this.map = new BMap.Map("map_container");
    let geolocation = new BMap.Geolocation();  
    let $this = this;

    if(this.obj.destX == "" && this.obj.destY == ""){
      //当前用户GPS获取的实时位置
      geolocation.getCurrentPosition(function(r){  
        if(this.getStatus() == BMAP_STATUS_SUCCESS){  
          $this.marker = new BMap.Marker(r.point,{icon: $this.myIcon2});  
          $this.map.addOverlay($this.marker);
  
          $this.marker2 = new BMap.Marker(r.point,{icon: $this.myIcon});
          // $this.marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画 
          $this.map.addOverlay($this.marker2);
  
          $this.map.centerAndZoom(r.point, 15);//设置中心和地图显示级别
  
          $this.getNearAddress(r.point);
        }
      });
    }else{
      //保存的历史地理位置
      var p = new BMap.Point(this.obj.destX, this.obj.destY);
      $this.marker = new BMap.Marker(p,{icon: $this.myIcon2});  //当前用户GPS获取的实时位置
      $this.map.addOverlay($this.marker);

      $this.marker2 = new BMap.Marker(p,{icon: $this.myIcon});
      // $this.marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画 
      $this.map.addOverlay($this.marker2);

      $this.map.centerAndZoom(p, 15);//设置中心和地图显示级别

      $this.getNearAddress(p);
    }
    
    
    // 添加定位控件
    let geolocationControl = new BMap.GeolocationControl();
    geolocationControl.addEventListener("locationSuccess", function(e){
      // 定位成功事件
      $this.map.clearOverlays();
      $this.marker2 = new BMap.Marker(e.point,{icon: $this.myIcon});
      $this.marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画 
      $this.map.addOverlay($this.marker2);
      
      $this.marker = new BMap.Marker(e.point,{icon: $this.myIcon2});
      $this.map.addOverlay($this.marker);

      $this.getNearAddress(e.point);
    });
    geolocationControl.addEventListener("locationError",function(e){
      // 定位失败事件
      alert(e.message);
    });
    this.map.addControl(geolocationControl);   //定位控件，针对移动端开发，默认位于地图左下方。
    this.map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
    this.map.addEventListener("dragstart", function(e){
      $this.map.removeOverlay($this.marker2);//清除指定位置覆盖物
      document.getElementById('r-center').style.display = '';
    });
    this.map.addEventListener("dragend", function(e){
      document.getElementById('r-center').style.display = 'none';
      var p = new BMap.Point($this.map.getCenter().lng, $this.map.getCenter().lat);//百度地图中心点
      $this.marker2 = new BMap.Marker(p,{icon: $this.myIcon});
      $this.map.addOverlay($this.marker2);
      $this.getNearAddress(p);
    });

    //搜索框自动搜索
    let ac = new BMap.Autocomplete({
      "input":"searchInput",
      "location":$this.map
    });
    ac.setInputValue($this.inputAddress);
    ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
      let str = "";
      let _value = e.fromitem.value;
      let value = "";
      if (e.fromitem.index > -1) {
          value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
      }    
      str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;
      if (e.toitem.index > -1) {
          _value = e.toitem.value;
          value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
      }    
        str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
        document.getElementById("searchResultPanel").innerHTML = str;
      });
      ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
        let _value = e.item.value;
        $this.myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        document.getElementById("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + $this.myValue;
        $this.setPlace();
      });
  }

  
  setPlace(){  
    let $this = this;
      function myFun(){  
          var pp = ls.getResults().getPoi(0).point;    //获取第一个智能搜索的结果  
          $this.map.centerAndZoom(pp, 15);  
          $this.map.clearOverlays();    //清除地图上所有覆盖物  
          $this.marker2 = new BMap.Marker(pp,{icon: $this.myIcon});
          $this.map.addOverlay($this.marker2);
          $this.getNearAddress(pp);
      }  
      var ls = new BMap.LocalSearch(this.map, { //智能搜索  
        onSearchComplete: myFun  
      });  
      ls.search(this.inputAddress);  
  }  

  //获取当前坐标附近位置
  getNearAddress(p){
    let $this = this;
    var mOption = {  
      poiRadius : 1000,           //半径为r米内的POI,  
      numPois : 5             //最多只有12个 系统决定的  
    }  
    var geoc = new BMap.Geocoder();
    geoc.getLocation(p, function (rs) {
      var allPois = rs.surroundingPois; //获取全部POI(半径R的范围 最多12个点)  
      if(allPois==null || allPois==""){  
          return;  
      }
      var s = "<ul>"; 
      for (var i = 0; i < allPois.length; i ++){  

        s += "<li id='poi" + i +"' data-title='"+ allPois[i].title 
            +"' data-lng='"+ allPois[i].point.lng +"'  data-lat='"+ allPois[i].point.lat 
            +"'  (click)='selectAddress()'>" + allPois[i].title + "<br>" 
            + allPois[i].address + "</li>"; 


        // //百度地图逆地址解析获取code
        // // http://api.map.baidu.com/geocoder?location=30.679935,104.067921&output=json&key=6eea93095ae93db2c77be9ac910ff311
        // let longitude = allPois[i].point.lat + "," + allPois[i].point.lng;
        // let geocoderUrl = "http://api.map.baidu.com/geocoder?location=" + longitude + "&output=json&key=6eea93095ae93db2c77be9ac910ff311"; 
        // let headers = new Headers({ "myHeader": "myValue" });
        //   headers.append("Access-Control-Allow-Origin", "*");
        //   headers.append("Accept", "application/json");
        // let options = new RequestOptions({ headers: headers});
        // $this.http.get(geocoderUrl,options).subscribe(
          //   (data) => {
            //     console.log(data);
            //   }
            // )     


          //   // 调用后台接口获取百度逆地址解析
          //   $this.http.post($this.convertBaiduUrl,{lng:allPois[i].point.lng,lat:allPois[i].point.lat},$this.helperService.getOptions()).subscribe(
          //     (data) => {
          //       let result = data.json();
          //       s += "<li id='poi" + i +"' data-title='"+ allPois[i].title 
          //           +"' data-lng='"+ allPois[i].point.lng +"'  data-lat='"+ allPois[i].point.lat 
          //           +"' data-cityCode='"+ result.data.cityCode
          //           +"'  (click)='selectAddress()'>" + allPois[i].title + "<br>" 
          //           + allPois[i].address + "</li>"; 
          //   }
          // )     
      }
      s += "</ul>";
      document.getElementById("r-result").innerHTML = s;  
      for (var j = 0; j < allPois.length; j ++){   
        document.getElementById("poi"+j).addEventListener('click',function(item){
          $this.dismiss(this.getAttribute("data-title"),
              this.getAttribute("data-lng"),
              this.getAttribute("data-lat")
              // this.getAttribute("data-cityCode")
            );
        })
      }
    },mOption);
  }

  // 关闭模态框
  dismiss(title,lng,lat) {
    this.viewCtrl.dismiss({title:title,lng:lng,lat:lat});
  }
  // // 关闭模态框
  // dismiss(title,lng,lat,cityCode) {
  //   this.viewCtrl.dismiss({title:title,lng:lng,lat:lat,cityCode});
  // }



}
