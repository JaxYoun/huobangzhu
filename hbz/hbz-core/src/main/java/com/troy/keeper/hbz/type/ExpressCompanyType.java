package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 李奥
 * @date 2017/12/20.
 */
@AllArgsConstructor
@Getter
public enum ExpressCompanyType {

    shunfeng("顺丰"),
    yuantong("圆通"),
    shengtong("申通"),
    ziyou("自有");


    private String name;

}
