package com.troy.keeper.hbz.vo;

import lombok.Data;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/18 10:42
 */
@Data
public class SitePushMessageVO extends BasicVO {

    private String code;

    private String title;

    private DictionaryVO messageType;

    private DictionaryVO consumerType;

    private Long sendTime;

    private String consumerPhoneNo;

    private String ifSend;

    private String imagePath;

    private String content;

    private String remark;

    private DictionaryVO receivePlatformType;

    private DictionaryVO pushType;

    private String summary;

    private String formattedCreateDate;

    private String formattedUpdateDate;

    private String formattedSendTime;

}
