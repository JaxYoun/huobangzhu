package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 李奥   服务方式
 * @date 2017/12/29.
 */
@AllArgsConstructor
@Getter
public enum ServiceMethodType {

    A("自提"),
    B("送货"),
    C("转提"),
    D("转送");

    private String name;



}
