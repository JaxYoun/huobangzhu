package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author：YangJx
 * @Description：WEB端需要登录才能访问的功能模块
 * @DateTime：2017/11/27 10:16
 */
@AllArgsConstructor
@Getter
public enum WebModule {

    CONSIGNOR_ENTERPRISE("企业货主"),
    TRANSPORT_ENTERPRISE("运输企业"),
    PERSONAL_CENTER("个人中心"),
    WAREHOUSE_CENTER("仓储中心");

    private String name;

}
