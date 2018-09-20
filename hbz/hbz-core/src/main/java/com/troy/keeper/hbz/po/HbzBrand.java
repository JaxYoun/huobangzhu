package com.troy.keeper.hbz.po;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/12/13.
 * 品牌
 */

@Data
@Entity
@Table(name = "hbz_brand")
public class HbzBrand extends BaseVersionLocked {

    @Column(columnDefinition = "varchar(32) comment '编号'")
    private String brandNo;

    @Lob
    @Column(columnDefinition = "longtext comment '图片'")
    private String headerBit;

    @ManyToOne
    @JoinColumn(name = "area", columnDefinition = "bigint comment '区域编码'")
    private HbzArea area;

    @Column(columnDefinition = "varchar(32) comment '名称'")
    private String name;

    @Column(name = "seq", columnDefinition = "int comment '排序'")
    private Integer index;

    @Column(columnDefinition = "longtext comment '授权证书'")
    private String standard;

    @Column(columnDefinition = "varchar(511) comment '摘要'")
    private String summary;

    @Column(columnDefinition = "longtext comment '介绍'")
    private String introduce;
}
