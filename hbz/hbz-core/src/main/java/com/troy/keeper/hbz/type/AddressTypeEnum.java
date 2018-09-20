package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author：YangJx
 * @Description：地址类型
 * @DateTime：2018/1/10 11:50
 */
@AllArgsConstructor
@Getter
public enum AddressTypeEnum {
    ORIGIN("发货地"),
    DEST("收货地");

    private String name;

}
