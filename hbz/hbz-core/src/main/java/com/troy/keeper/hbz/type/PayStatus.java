package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2017/11/2.
 */
@AllArgsConstructor
@Getter
public enum PayStatus {

    UN_PAY("未支付"), HAVE_PAY("已支付");

    private String name;


}
