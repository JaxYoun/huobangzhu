package com.troy.keeper.hbz.vo;

import lombok.Data;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/22 16:56
 */
@Data
public class SmsRecordVO extends BasicVO {

    private String phoneNo;

    private String message;

    private String sendStatus;

}
