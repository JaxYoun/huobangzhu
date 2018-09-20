package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 李奥  货物运输的状态
 * @date 2018/1/4.
 */
@AllArgsConstructor
@Getter
public enum ShippingStatus {

    NEW("新建"),
    START("已发车"),
    ALL_START("全部装车"),
    SECTION_START("部分装车"),
    DURING_SHIPPING("运输中"),
    PARTUNLOADING("部分卸货"),
    DISCHARGE("已卸货"),
    OUTSOURCING("已分包"),
    ISOK("已完成"),
    SIGN("已签收");

    private String name;

}
