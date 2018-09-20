package com.troy.keeper.hbz.vo;

import lombok.Data;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/18 16:52
 */
@Data
public class SitePushMessageRecoredVO extends BasicVO {

    private HbzUserVO consumer;

    private SitePushMessageVO sitePushMessage;

    private String ifRead;

    private Long readTime;

    private String formatedCreateDate;

    private String formattedUpdateDate;

    private String formatedReadTime;

}
