package com.troy.keeper.hbz.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by leecheng on 2017/11/27.
 */
@Getter
@Setter
public class AuthCode implements Serializable {

    private String authCode;
    private Long createTime;

    public AuthCode(String authCode, Long createTime) {
        this.authCode = authCode;
        this.createTime = createTime;
    }
}
