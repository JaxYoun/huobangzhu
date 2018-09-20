package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2018/1/31.
 */
@AllArgsConstructor
@Getter
public enum BizCode {

    ORDER("订单"),
    Tender("投标");

    private String name;

}
