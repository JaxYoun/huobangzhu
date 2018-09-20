package com.troy.keeper.hbz.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author：YangJx
 * @Description：短信发送记录-实体类
 * @DateTime：2018/1/16 14:58
 */
@Data
@Entity
@Table(name = "hbz_sms_record")
public class SmsRecord extends BaseVersionLocked {

    @Column(columnDefinition = "varchar(11) comment '电话号码'")
    private String phoneNo;

    @Column(columnDefinition = "varchar(500) comment '短信内容'")
    private String message;

    @Column(columnDefinition = "char(1) comment '发送状态，0：失败，1：成功'")
    private String sendStatus;

}
