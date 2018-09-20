package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2018/3/6.
 */
@AllArgsConstructor
@Getter
public enum RefundStatus {

    NEW("新建"),
    SUBMIT("已申请"),
    REFUSE("已拒绝"),
    REFUNDING("退款中"),
    SUCCESS("退款成功"),
    FAILURE("退款失败");

    private String name;

}
