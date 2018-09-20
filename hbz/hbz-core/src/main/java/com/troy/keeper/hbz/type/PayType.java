package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2017/10/24.
 */
//一级支付分类和二级分类枚举
@AllArgsConstructor
@Getter
public enum PayType {

    Wechat("微信"), Alipay("支付宝"), Union("银联"),
    WebWechat("Web端微信"), WebAlipay("Web端支付宝"), WebUnion("Web端银联");

    private String name;

}
