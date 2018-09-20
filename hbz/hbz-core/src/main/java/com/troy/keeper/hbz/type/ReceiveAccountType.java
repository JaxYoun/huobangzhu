package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 收款账号类型
 * Created by leecheng on 2018/3/5.
 */
@AllArgsConstructor
@Getter
public enum ReceiveAccountType {

    Wechat("微信"),
    Alipay("支付宝");

    private String name;


}
