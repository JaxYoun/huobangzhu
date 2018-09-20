package com.troy.keeper.hbz.app.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import com.troy.keeper.hbz.type.SmsSource;
import lombok.Data;

/**
 * Created by leecheng on 2017/10/25.
 */
@Data
public class SmsVO {

    @ValueFormat(validations = {
            @Validation(use = "sms_send", format = ValidConstants.REGEX, msg = "手机号{fieldName}不规范", conf = "(\\+86)?\\d{8,15}")
    })
    private String phone;

    private String msg;

    private SmsSource source;
}
