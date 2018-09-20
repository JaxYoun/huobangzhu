package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.TimeLimit;
import lombok.*;
import lombok.extern.apachecommons.CommonsLog;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/10/24.
 */
//帮送
@Setter
@Getter
@ToString
@CommonsLog
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hbz_send_order")
@PrimaryKeyJoinColumn
public class HbzSendOrder extends HbzOrder {

    @Column(columnDefinition = "varchar(1000) comment '关联图片'")
    private String relatedPictures;

    @Column(columnDefinition = "double comment '质量'")
    private Double commodityWeight;

    @Column(columnDefinition = "double comment '货物体积'")
    private Double commodityVolume;

    //配送地址
    @Column(columnDefinition = "varchar(128) comment '配送地址'")
    private String destAddress;

    //配送地址
    @Column(columnDefinition = "varchar(128) comment '货物地址'")
    private String originAddress;

    //货物品描述
    @Lob
    @Column(columnDefinition = "longtext comment '描述'")
    private String commodityDesc;

    @Column(columnDefinition = "varchar(32) comment '商品'")
    private String commodityName;

    @Column(columnDefinition = "varchar(100) comment '取货联系人'")
    private String originLinker;

    @Column(columnDefinition = "varchar(100) comment '联系方法'")
    private String originLinkTelephone;

    @Column(columnDefinition = "varchar(100) comment '送货联系人'")
    private String linker;

    @Column(columnDefinition = "varchar(100) comment '联系方法'")
    private String linkTelephone;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(1023) comment '配送限制'")
    private TimeLimit timeLimit;

    @Column(columnDefinition = "int comment '取货'")
    private Integer takeLimit;

    @Column(columnDefinition = "bigint comment '取货时间'")
    private Long orderTakeTime;

    @Column(columnDefinition = "bigint comment '开始配送时间'")
    private Long startTime;

    @Column(columnDefinition = "bigint comment '结束配送'")
    private Long endTime;

}
