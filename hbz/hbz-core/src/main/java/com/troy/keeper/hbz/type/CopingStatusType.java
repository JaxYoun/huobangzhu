package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 李奥
 * @date 2018/1/22.
 */
@AllArgsConstructor
@Getter
public enum CopingStatusType {

    UNPAID("未付款"),
    PARTIAL_PAYMENT("部分付款"),
    PAYMENT_COMPLETED("完成付款");

    private String name;

}
