package com.troy.keeper.hbz.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/11/8.
 */
//地理区域
@Getter
@Setter
@ToString
@Entity
@Table(name = "hbzArea")
public class HbzArea extends BaseVersionLocked {

    @Column(name = "out_code", columnDefinition = "varchar(32) comment '百度编码'")
    private String outCode;

    @Column(name = "area_name", columnDefinition = "varchar(32) comment '区域名称'")
    private String areaName;

    @Column(name = "area_code", columnDefinition = "varchar(20) comment '地区编码'")
    private String areaCode;

    @Column(name = "jian_pin", columnDefinition = "varchar(20) comment '简拼'")
    private String jianPin;

    @Column(name = "pin_yin", columnDefinition = "varchar(20) comment '拼音'")
    private String pinYin;

    @Column(name = "level", columnDefinition = "int comment '城市级别'")
    private Integer level;

    @Column(name = "seq_no", columnDefinition = "int comment '排序'")
    private Integer index;

    @ManyToOne
    @JoinColumn(name = "parent_id", columnDefinition = "bigint comment '所属区域id'")
    private HbzArea parent;

}
