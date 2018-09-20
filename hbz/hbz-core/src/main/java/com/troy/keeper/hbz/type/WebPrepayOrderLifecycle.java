package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author：YangJx
 * @Description：扫码支付订单状态
 * @DateTime：2017/12/28 23:49
 */
@AllArgsConstructor
@Getter
public enum WebPrepayOrderLifecycle {

    NEW("新建"),
    PAID("已支付"),
    CANCELED("已取消"),
    EXPIRED("已过期"),
    FAILED("支付失败"),
    REFUND("已退款");

    private String name;

}
