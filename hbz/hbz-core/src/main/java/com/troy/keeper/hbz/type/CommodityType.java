package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2017/11/8.
 */
@AllArgsConstructor
@Getter
public enum CommodityType {

    Light("轻货"), Heavy("重货"), Other("其它");

    private String name;


}
