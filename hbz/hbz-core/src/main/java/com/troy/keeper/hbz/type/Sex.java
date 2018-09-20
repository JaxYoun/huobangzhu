package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2017/10/25.
 */
@AllArgsConstructor
@Getter
public enum Sex {

    Male("男"), Female("女"), Other("未知");
    
    private String value;


}
