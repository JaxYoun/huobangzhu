import {Component,ViewChild} from '@angular/core';
import {Platform} from 'ionic-angular';
import {Tabs} from 'ionic-angular';
import {HomePage} from '../home/home';
import {ShipperPage} from '../shipper/shipper';
import {Driver} from '../driver/driver';
import {PersonCenter} from '../person/personCenter';
import {Warehouse} from '../warehouse/warehouse/warehouse';
import {BackButtonService} from '../../providers/BackButtonService';

@Component({
    selector: 'page-tabs',
    templateUrl: 'tabs.html'
  })

export class TabsPage {

  @ViewChild('mainTabs') tabRef:Tabs;

  private tabBadge = 8;

  private tab1Root:any;
  private tab2Root:any;
  private tab3Root:any;
  private tab4Root:any;
  private tab5Root:any;

  constructor(public backButtonService: BackButtonService, private platform: Platform) {
    this.tab1Root = HomePage;
    this.tab2Root = ShipperPage;
    this.tab3Root = Driver;
    this.tab4Root = Warehouse;
    this.tab5Root = PersonCenter;

    platform.ready().then(() => {
      this.backButtonService.registerBackButtonAction(this.tabRef);
    });
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad tabsPage');
  }

  ionViewDidEnter() {
  }

  onChange(ev){
    // var index = ev.index;
    // if(index > 0){
    //   this.tabBadge = 0;
    // }
  }

}
