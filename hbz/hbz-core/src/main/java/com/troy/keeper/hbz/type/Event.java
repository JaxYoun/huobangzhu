package com.troy.keeper.hbz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by lee on 2017/10/17.
 */
@AllArgsConstructor
@Getter
public enum Event {

    URI_REQUEST("URI请求"),
    METHOD_REQUEST("方法调用"),
    AUTH_FAILURE("认证失败"),
    LOGOUT_SUCCESS("注销"),
    AUTH_SUCCESS_EVENT("认证成功"),
    AUTH_DENIED("拒绝访问"),
    UN_AUTH("没认证");

    private String name;

}
