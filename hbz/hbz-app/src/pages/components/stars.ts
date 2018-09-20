/**
 * 使用说明
 * <star-level></star-level>
 *
 * 输入参数
 * starLevel {Number} 星星等级
 *selectable {boolean} 启用选择星星等级
 *
 *
 *
 * 输出参数
 * change($event) {function}
 * $event：星星等级
 */


import { Component, Input, Output, EventEmitter } from '@angular/core';


@Component({
 selector: 'star-level',
 templateUrl: './stars.html'
})
export class StarLevel {
  @Input() starLevel;
  @Input() selectable;
  @Output() change =  new EventEmitter<any>();

  selectStar(i) {
    if(!this.selectable){
      return
    }
    if(i===1 && this.starLevel === 1){
      i = 0
    }
    this.starLevel = i;
    this.change.emit(i);
  }
}
