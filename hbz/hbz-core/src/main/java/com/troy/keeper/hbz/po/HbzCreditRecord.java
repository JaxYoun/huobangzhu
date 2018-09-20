package com.troy.keeper.hbz.po;

import lombok.Data;

import javax.persistence.*;

/**
 * 信誉记录
 * Created by leecheng on 2018/1/19.
 */
@Data
@Entity
@Table(name = "hbz_credit_rec")
public class HbzCreditRecord extends BaseVersionLocked {

    @Column(columnDefinition = "varchar(32) comment '类型'")
    private String type;

    @Column(columnDefinition = "varchar(32) comment '记录编号'")
    private String recNo;

    @ManyToOne
    @JoinColumn(name = "user", columnDefinition = "bigint comment '用户'")
    private HbzUser user;

    @Column(name = "delta")
    private Integer delta;

    @Column(columnDefinition = "varchar(16) comment '类型'")
    private String action;

    @Column(columnDefinition = "varchar(32) comment '适用业务'")
    private String adjustType;

    @Column(columnDefinition = "varchar(1024) comment '详细信息'")
    private String msg;

    @Column(name = "x_time", columnDefinition = "bigint comment '时间'")
    private Long time;

}
