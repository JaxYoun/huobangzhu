package com.troy.keeper.hbz.po;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/12/15.
 */
@Data
@Entity
@Table(name = "hbz_recommendProduct")
public class HbzRecommendProduct extends BaseVersionLocked {

    @Column(name = "c_name", columnDefinition = "varchar(64) comment '名称'")
    private String recommendName;

    @ManyToOne
    @JoinColumn(columnDefinition = "bigint comment '产品id'")
    HbzProduct product;

    @Column(name = "s")
    private Integer state;

    @Column(name = "order_No", columnDefinition = "int comment '排序'")
    Integer index;

    @Column(name = "c_utype", columnDefinition = "varchar(10) comment '类型'")
    private String useType;

    @Column(columnDefinition = "varchar(1000) comment '简历'")
    String summary;

    @Lob
    @Column(columnDefinition = "longtext comment '推荐图片'")
    private String headerBit;

    //介绍
    @Column(columnDefinition = "longtext")
    @Lob
    String introduce;
}
