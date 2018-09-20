package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leecheng on 2018/1/3.
 */
@AllArgsConstructor
@Getter
public enum ReimburseProgress {

    NotBeginning("未发起"),
    AlreadyStarted("已发起"),
    Disagree("退款失败"),
    End("退款成功");

    private String name;

}
