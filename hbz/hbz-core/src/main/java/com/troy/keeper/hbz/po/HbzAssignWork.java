package com.troy.keeper.hbz.po;

import lombok.Data;

import javax.persistence.*;

/**
 * 指派给网点的任务
 * Created by leecheng on 2018/1/17.
 */
@Data
@Entity
@Table(name = "hbz_assignWork")
public class HbzAssignWork extends BaseVersionLocked {

    @Column(columnDefinition = "varchar(32) comment '作业编号'")
    private String workNo;

    @Column(columnDefinition = "varchar(32) comment '平台单号'")
    private String platformNo;

    @Column(columnDefinition = "double comment '预估价格'")
    private Double expectedAmount;

    @ManyToOne
    @JoinColumn(name = "platformOrganizationId", columnDefinition = "bigint comment '处理网点'")
    private HbzPlatformOrganization platformOrganization;

    @ManyToOne
    @JoinColumn(name = "originAreaId", columnDefinition = "bigint comment '原始发货地址'")
    private HbzArea originArea;

    @ManyToOne
    @JoinColumn(name = "destareaid", columnDefinition = "bigint comment '收货地址'")
    private HbzArea destArea;

    @Column(name = "classification", columnDefinition = "varchar(128) comment '货物类型'")
    private String classification;

    @Column(name = "send_person", columnDefinition = "varchar(32) comment '发件人'")
    private String sendUser;

    @Column(name = "send_person_phone",columnDefinition = "varchar(64) comment '发送人电话'")
    private String sendPersonPhone;

    @Column(name = "receive_person",columnDefinition = "varchar(32) comment '收货人'")
    private String receivePerson;

    @Column(name = "receive_person_phone",columnDefinition = "varchar(32) comment '收货人电话'")
    private String receivePersonPhone;

    @Column(name = "weight", columnDefinition = "double comment '质量'")
    private Double weight;

    @Column(name = "volume", columnDefinition = "double comment '体积'")
    private Double volume;

    @Column(name = "assignTime", columnDefinition = "bigint comment '派单时间'")
    private Long assignTime;

    @Column(name = "logisticsNo",columnDefinition = "varchar(64) comment '物流编号'")
    private String logisticsNo;

    @Column(columnDefinition = "varchar(32) comment '状态 0 - 未接单 1 - 已接单 '")
    private String workStatus;
}
