package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 李奥  在途状态
 * @date 2018/1/9.
 */
@AllArgsConstructor
@Getter
public enum TransitState {

    NORMAL("正常"),
    DELAY("延误"),
    MALFUNCTION("故障"),
    PERFECTION("完成");

    private String name;

}
