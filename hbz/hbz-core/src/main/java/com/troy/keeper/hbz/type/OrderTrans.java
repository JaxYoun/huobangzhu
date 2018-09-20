package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2017/11/1.
 */
@AllArgsConstructor
@Getter
public enum OrderTrans {

    //NEW("新建"),
    //CONFIRMED("已确认"),
    //ORDER_TO_BE_RECEIVED("待接订单"),
    //LOCKED_ORDER("锁定待确认"),
    //LOCKED_ORDER_DRIVER("待司机确认"),
    //WAIT_TO_TAKE("待取货"),
    //TRANSPORT("取货运输中"),
    //WAIT_TO_CONFIRM("收货待确认"),
    //WAIT_FOR_PAYMENT("待在线支付"),
    //WAIT_FOR_PAYMENT_OFFLINE("线下支付中"),
    //PAID("已付款"),
    //LIQUIDATION_COMPLETED("已结算"),
    //OVER_TIME("订单超时"),
    //WAITE_TO_REFUNDDE("等待退款"),
    //APPLY_FOR_REFUND("退款中"),
    //REFUND_FINISHT("退款完成"),
    //INVALID("作废");

    NEW("新建"),
    CONFIRMED("已确认"),
    ORDER_TO_BE_RECEIVED("待接订单"),
    LOCKED_ORDER("订单锁定待货主确认"),
    LOCKED_ORDER_DRIVER("订单锁定待司机确认"),
    WAIT_TO_TAKE("已接单"),
    OVER_TIME("订单超时"),
    TRANSPORT("运输中"),
    WAIT_TO_CONFIRM("已收货，待付货款"),
    WAIT_FOR_PAYMENT_OFFLINE("线下支付中"),
    WAIT_FOR_PAYMENT("待付款"),
    PAID("已付款"),
    LIQUIDATION_COMPLETED("已清算"),
    APPLY_FOR_REFUND("退款中"),
    WAITE_TO_REFUNDDE("等待退款"),
    REFUND_FINISHT("退款完成"),
    INVALID("作废");
    private String name;

}
