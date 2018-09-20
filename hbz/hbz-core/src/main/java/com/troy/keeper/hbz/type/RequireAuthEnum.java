package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2017/12/28 11:33
 */
@AllArgsConstructor
@Getter
public enum RequireAuthEnum {

    YES("需要认证"),
    NO("不需要认证");

    private String name;

}
