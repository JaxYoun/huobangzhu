package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2017/10/30.
 */
@AllArgsConstructor
@Getter
public enum BusinessType {

    ORDER("订单支付"), COST("企业付款"), BOND("交纳保证金"), WORDER("支付仓储诚意金");

    private String name;

}
