package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2017/11/10.
 */
@AllArgsConstructor
@Getter
public enum WeightUnit {

    T("吨"),Kg("千克");

    private String name;

}
