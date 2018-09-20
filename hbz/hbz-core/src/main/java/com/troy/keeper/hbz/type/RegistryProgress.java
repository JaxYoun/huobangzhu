package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by leechen on 2017/11/6.
 */
@AllArgsConstructor
@Getter
public enum RegistryProgress {

    UN_DO("未注册"),
    UN_REGISTER("审核中"),
    REGISTRYED("已注册"),
    ERR_REGISTER("注册失败");

    private String name;

}
