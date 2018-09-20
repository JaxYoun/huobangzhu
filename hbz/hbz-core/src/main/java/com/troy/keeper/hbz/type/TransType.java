package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2017/11/8.
 */
@AllArgsConstructor
@Getter
public enum TransType {

    UNLIMITED("不限"),
    FLAT("平板"),
    GAOLAN("高栏"),
    VAN("厢式"),
    HIGH_LOW_PLATE("高低板"),
    HEAT_PRESERVATION("保温"),
    COLD_STORAGE("冷冻"),
    DANGEROUS_GOODS("危险品"),
    DUMP("自卸"),
    INTHE_CARD("中卡"),
    BREAD("面包车"),
    QUILT_CAR("棉被车");

    private String name;

}
