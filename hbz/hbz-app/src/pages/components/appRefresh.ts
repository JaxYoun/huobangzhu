import { Component, Input } from '@angular/core';
import { ViewController } from "ionic-angular";

@Component({
 selector: 'app-refresh',
 templateUrl: './appRefresh.html'
})
export class AppRefresh {
  @Input() doRefresh;

  constructor(
    public viewCtrl: ViewController,
  ){

  }

}
