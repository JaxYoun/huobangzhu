package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2018/1/2.
 */
@AllArgsConstructor
@Getter
public enum RateType {

    CONSIGNOR("货主评价"),
    PROVIDER("承运人评价");

    private String name;

}
