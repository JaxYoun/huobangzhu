package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author：YangJx
 * @Description：仓储租赁诚意金订单支付状态枚举类
 * @DateTime：2018/1/5 16:22
 */
@AllArgsConstructor
@Getter
public enum WarehouseEarnestPayStatusEnum {

    NEW("未支付"),
    PAID("已支付"),
    ACCEPTED("已受理"),
    REFUSED("拒绝受理"),
    REFUND("已退款");

    private String name;

}
