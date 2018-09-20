package com.troy.keeper.hbz.dto;

import lombok.Data;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/19 9:07
 */
@Data
public class SitePushMessageRecordDTO extends BaseDTO {

    private Long sitePushMessageId;

    private Long consumerId;

    private String ifRead;

    private String title;

    private String messageType;

    private String consumerType;

}
