import { Component } from '@angular/core';
import { Storage } from '@ionic/storage';
import { ViewController, NavParams } from 'ionic-angular';
import { CityPicker } from './cityPicker';
@Component({
  templateUrl: './cityPickerContent.html',
})
export class CityPickerContent {
  private cityPicker: CityPicker;
  public shortName: Boolean;
  public cityLevel: Number;
  public text: String = '';
  public areaJsonArray: Array<any> = [];
  public currentArray: Array<any> = [];
  public defaultAreaArray: Array<any> = [];
  public level1: Object = {};
  public level2: Object = {};
  public level3: Object = {};
  public municipalities: Array<string> = [
    '北京市',
    '重庆市',
    '上海市',
    '天津市',
  ];
  constructor(
    public storage: Storage,
    public viewCtrl: ViewController,
    private navParams: NavParams,
  ) {
    this.cityPicker = navParams.get('cityPicker');
    // 初始加载省级、直辖市
    this.storage.get('areaJson').then(data => {
      this.areaJsonArray = data;
      this.currentArray = data.filter(item => item.level === 1);
      this.defaultAreaArray = this.currentArray;
    });
  }
  ngOnInit() {
    const parmas = this.navParams.data
    if (parmas) {
      this.shortName = parmas.shortName;
      this.cityLevel = parmas.cityLevel;
    }
  }


  // 根据目标筛选子级城市
  filterArea(target) {
    return this.areaJsonArray.filter(item => target.id === item.parentId);
  }
  // 重置到省级、直辖市
  resetArea() {
    this.currentArray = this.defaultAreaArray;
  }

  // 处理返回结果
  // 关闭弹出层、重置城市数据、返回结果
  hanldeResult(item) {
    this.currentArray = this.defaultAreaArray;
    this.level1 = {};
    this.level2 = {};
    this.cityPicker.onChange(item);
    this.viewCtrl.dismiss();
  }

  // 处理城市点击事件
  handleClick(item) {
    // 省级、直辖市
    if (item.level === 1) {
      //处理指定了城市级别cityLevel的情况
      switch (this.cityLevel) {
        case 1:
          this.hanldeResult(item);
          return;
        case 2:
          const currentCity = this.municipalities.find(
            n => n === item.areaName,
          );
          if (currentCity) {
            this.hanldeResult(item);
            return;
          }
          break;

        default:
          break;
      }
      this.currentArray = this.filterArea(item);
      this.level1 = item;
      return;
    }

    // 市别
    if (item.level === 2) {
      // 处理指定了城市级别city level === 2
      if (this.cityLevel === 2) {
        this.hanldeResult(item);
        return;
      }
      let cityAarray = this.filterArea(item);
      if (cityAarray.length > 0) {
        this.currentArray = cityAarray;
        this.level2 = item;
        return;
      }
    }
    this.hanldeResult(item);
  }
}
