package com.troy.keeper.hbz.po;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author：YangJx
 * @Description：站内消息推送记录
 * @DateTime：2018/1/16 11:46
 */
@Data
@Entity
@Table(name = "hbz_site_push_message_record")
public class SitePushMessageRecord extends BaseVersionLocked {
    /**
     * 接收者
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private HbzUser consumer;

    /**
     * 被推送消息
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private SitePushMessage sitePushMessage;

    @Column(columnDefinition = "char(1) comment '是否已阅读，0：未阅读，1：已阅读'")
    private String ifRead;

    @Column(columnDefinition = "varchar(64) comment '接收者电话号码'")
    private String phoneNo;

    @Column(columnDefinition = "bigint(20) comment '阅读时间'")
    private Long readTime;

}
