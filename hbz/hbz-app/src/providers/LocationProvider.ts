import { Injectable, NgZone} from '@angular/core';
import { BackgroundGeolocation } from 'ionic-native';

@Injectable()
export class LocationProvider {

public message: any = "I'm new here";
public latitude:number = 0.0;
public longitude:number = 0.0;
private zone: NgZone;

constructor() {
   this.initialiseLocationManager();
}

initialiseLocationManager() {

  let config = {
          desiredAccuracy: 1,
          stationaryRadius: 1,
          distanceFilter: 1,
          interval:1000,
          activityType:'AutomotiveNavigation',
          notificationTitle:'货帮主定位服务',
          notificationText:'后台运行中',
          debug: false,
          stopOnTerminate: true,
  };

  BackgroundGeolocation.configure(
    ()=>{
      console.log(this.locationUpdated);
    },
    this.error,
    config)
}

private locationUpdated(location) {
   console.log('******* NEW LOCATION ********');
   this.zone.run(() => {
     this.latitude = location.latitude;
     this.longitude = location.longitude;
 });

 BackgroundGeolocation.finish(); // FOR IOS ONLY
}

private error(error) {
   console.log('BackgroundGeolocation ERROR');
}

public startTracking() {
  BackgroundGeolocation.start();
}

public stopTracking() {
  BackgroundGeolocation.stop();
}

public getLocations(){
  return BackgroundGeolocation.getLocations();
}

public getValidLocations(){
  return BackgroundGeolocation.getValidLocations();
}
}