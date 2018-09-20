package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2017/11/8.
 */
@AllArgsConstructor
@Getter
public enum CommodityClass {

    DEFAULT("默认"),Article("文件"),Parcel("包裹");

    private String name;


}
