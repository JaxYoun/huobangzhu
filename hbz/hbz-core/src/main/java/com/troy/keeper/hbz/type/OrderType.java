package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2017/10/24.
 */
//订单类型
@AllArgsConstructor
@Getter
public enum OrderType {

    BUY("帮我买"), SEND("帮我送"), FSL("整车专线"), LTL("零担专线"), EX("快递"), FAC("找车"), S("积分订单");

    private String name;

}
