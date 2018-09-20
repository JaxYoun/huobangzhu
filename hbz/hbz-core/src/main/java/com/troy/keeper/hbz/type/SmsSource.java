package com.troy.keeper.hbz.type;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum SmsSource {

    REGISTER("注册"),
    FORGET_PASSWORD("忘记密码");

    private String name;

}
