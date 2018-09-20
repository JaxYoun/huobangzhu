package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2017/10/25.
 */
@AllArgsConstructor
@Getter
public enum RegistryCode {

    PersonDriver("个人司机"),
    TransEnterprise("运输企业"),
    PersonConsignor("个人货主"),
    EnterpriseConsignor("企业货主"),
    DeliveryBoy("配送员");

    private String name;

}
