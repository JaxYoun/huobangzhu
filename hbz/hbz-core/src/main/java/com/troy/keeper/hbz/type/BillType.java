package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2018/3/8.
 */
@AllArgsConstructor
@Getter
public enum BillType {
    PAY("支付"),
    REFUND("退钱");
    private String name;
}
