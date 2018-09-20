/**
 * 使用说明
 * <city-picker></city-picker>
 *
 * 输入参数
 * address {object} 省市区县信息
 * label {string} 输入框标签名称
 * cityLevel {Number} 指定的城市级别
 * shorName {boolean} 是否只显示最后一级城市名称
 */

import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Storage } from '@ionic/storage';
import { ViewController, PopoverController, NavParams } from 'ionic-angular';
import { CityPickerContent } from './cityPickerContent';

@Component({
  selector: 'city-picker',
  templateUrl: './cityPicker.html',
})
export class CityPicker {
  @Input() address;
  @Input() label;
  @Input() shortName;
  @Input() cityLevel;
  @Output() change = new EventEmitter<any>();

  private text: string = '';

  constructor(
    public popoverCtrl: PopoverController,
    public viewCtrl: ViewController,
    public storage: Storage,
    public navParams: NavParams,
  ) {
    
  }
  ngOnInit(){
    this.text = this.address || '';
  }
  openModal() {
    let popover = this.popoverCtrl.create(
      CityPickerContent,
      {
        cityPicker: this,
        shortName: this.shortName,
        cityLevel: this.cityLevel,
      },
      { cssClass: 'city-picker-wrapper' },
    );
    popover.present();
  }
  // 获取目标完整中文地址
  getAreaString(item) {
    if (!item.id) {
      return '';
    }

    if (this.shortName) {
      return `${item.areaName}`;
    } else {
      return `${item.level === 3 ? item.parent.parent.areaName + '-' : ''}${
        item.parent.areaName
      }-${item.areaName}`;
    }
  }
  public onChange(data) {
    this.text = this.getAreaString(data);
    this.change.emit(data);
  }
}
