package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2017/10/16.
 */
@AllArgsConstructor
@Getter
public enum Role {

    DeliveryBoy("配送员"),
    Consignor("货主"),
    EnterpriseConsignor("企业货主"),
    EnterpriseAdmin("货运公司"),
    EnterpriseFinance("企业财务"),
    EnterpriseDriver("企业运输司机"),
    EnterpriseAssist("企业助理"),
    PersonDriver("个人车主");

    private String name;

}
