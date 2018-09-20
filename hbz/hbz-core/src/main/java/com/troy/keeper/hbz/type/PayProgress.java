package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2017/10/24.
 */
//在线支付进度
@AllArgsConstructor
@Getter
public enum PayProgress {

    NEW("新建支付单"),
    APP_PAY_SUCCESS("APP端支付成功"),
    APP_PAY_FAILURE("APP端支付失败"),
    SUCCESS("支付成功"),
    FAILURE("失败"),
    INVALID("订单失效"),
    CANCEL("已取消"),
    REFUNDED("已退款"),
    OTHER("其它");

//    WEB_NEW("Web端新建支付单"),
//    WEB_PAY_SUCCESS("Web端支付成功"),
//    WEB_PAY_FAILURE("Web端支付失败"),
//    WEB_SUCCESS("Web端支付成功"),
//    WEB_FAILURE("Web端失败"),
//    WEB_INVALID("Web端订单失效"),
//    WEB_CANCEL("Web端已取消"),
//    WEB_REFUNDED("Web端已退款"),
//    WEB_OTHER("Web端其它");

    private String name;


}
