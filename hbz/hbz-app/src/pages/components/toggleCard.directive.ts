import {Directive, ElementRef, Renderer2, Input, Output, EventEmitter} from '@angular/core';

@Directive({
  selector: '[toggleCard]',
})
export class ToggleCardDirective {
  private _render:any;
  private _domElem:any;
  private _isShow:boolean;
  private _header:any;
  private _button:any;
  private _text:string;
  private _textWrap:any;

  @Input() showItem:string;
  @Input() canLeave:boolean;
  @Output() change =  new EventEmitter<any>();

  constructor(el: ElementRef, renderer: Renderer2) {
    this._render = renderer;
    this._domElem = el.nativeElement;
    this._render.addClass(this._domElem, 'hide-content');
    this._isShow = false;
    this._text = '点击输入';
    // 添加确认按钮
    this._button = this._render.createElement('button');
    this._button.className = 'confirm';
    this._button.innerHTML = '确认';
    this._button.addEventListener('click', ()=>{
      this.toggle(true);
    });
    this._render.appendChild(this._domElem, this._button);

    // 创建文字显示标签
    this._textWrap = this._render.createElement('em');
    this._textWrap.className = 'header-text';
    this._textWrap.innerHTML = this._text;
  }


  ngOnInit(){
    // console.log(this.showItem);
  }
  ngOnChanges() {
    if(this.canLeave){
      this._textWrap.innerHTML = this.showItem || this._text;
      this._render.addClass(this._domElem, 'hide-content');
      this._render.removeClass(this._domElem, 'show-content');
      this._isShow = false;
    }
  }
  ngAfterViewInit():void{
    this._header = this._domElem.querySelector('.card-header');
    // 添加文字显示标签
    this._render.appendChild(this._header, this._textWrap);
    // 标题注册事件
    this._header.addEventListener('click',()=>{
      this.toggle(false);
    });
  }

  toggle(confirm:boolean):void{
    if(!this._isShow){
      this._render.removeClass(this._domElem, 'hide-content');
      this._render.addClass(this._domElem, 'show-content');
      this._isShow = true;
      this._textWrap.innerHTML = '';
    }else{
      this._textWrap.innerHTML = this._text;
      this._render.addClass(this._domElem, 'hide-content');
      this._render.removeClass(this._domElem, 'show-content');
      this._isShow = false;
    }
    if(confirm){
      this._textWrap.innerHTML = this.showItem || this._text;
    }
    this.change.emit(!this._isShow);
  }

}
