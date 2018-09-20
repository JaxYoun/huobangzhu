package com.troy.keeper.hbz.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @Author：YangJx
 * @Description：站内推送消息实体类
 * @DateTime：2018/1/16 10:24
 */
@Data
@Entity
@Table(name = "hbz_site_push_message")
public class SitePushMessage extends BaseVersionLocked {

    @Column(columnDefinition = "char(10) comment '编号'")
    private String code;

    @Column(columnDefinition = "varchar(200) comment '标题'")
    private String title;

    @Column(columnDefinition = "int comment '消息类型'")
    private String messageType;

    @Column(columnDefinition = "int comment '接收者类型'")
    private String consumerType;

    @Column(columnDefinition = "bigint comment '发送时间'")
    private Long sendTime;

    @Column(columnDefinition = "varchar(11) comment '接收者电话号码'")
    private String consumerPhoneNo;

    @Column(columnDefinition = "char(1) comment '是否已发送，0：未发送，1：已发送'")
    private String ifSend;

    @Column(columnDefinition = "varchar(300) comment '图片路径'")
    private String imagePath;

    @Lob
    @Column(columnDefinition = "longtext comment '消息内容'")
    private String content;

    @Column(columnDefinition = "text comment '备注'")
    private String remark;

    @Column(columnDefinition = "text comment '接收平台类型'")
    private String receivePlatformType;

    @Column(columnDefinition = "text comment '发送方式'")
    private String pushType;

    @Column(columnDefinition = "varchar(200) comment '信息摘要'")
    private String summary;

}