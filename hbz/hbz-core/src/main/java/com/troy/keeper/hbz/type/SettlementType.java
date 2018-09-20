package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2017/11/8.
 */
@AllArgsConstructor
@Getter
public enum SettlementType {

    ONLINE_PAYMENT("在线支付"), MONTHLY_SETTLEMENT("月结"), LEVY_ONLINE_PAYMENT("车辆征集");

    private String name;

}