package com.troy.keeper.hbz.dto;

import lombok.Data;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/22 16:55
 */
@Data
public class SmsRecordDTO extends BaseDTO {

    private String phoneNo;

    private String message;

    private String sendStatus;

}
