package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2017/11/23.
 */
@AllArgsConstructor
@Getter
public enum TimeLimit {

    Immediately("立即"), Assign("指派");

    private String name;


}
