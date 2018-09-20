package com.troy.keeper.hbz.app.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 密码DTO，主要用于密码修改
 */
@Data
@Getter
@Setter
@EqualsAndHashCode
public class PasswordDTO {

    /**
     * 验证码
     */
    private String authCode;

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;

}
