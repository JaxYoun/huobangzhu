package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.TimeLimit;
import lombok.*;
import lombok.extern.apachecommons.CommonsLog;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/10/24.
 */
//帮我买
@Setter
@Getter
@ToString
@CommonsLog
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hbz_buy_order")
@PrimaryKeyJoinColumn
public class HbzBuyOrder extends HbzOrder {

    @Column(columnDefinition = "varchar(1000) comment '关联图片'")
    private String relatedPictures;

    //配送地址
    @Column(columnDefinition = "varchar(128) comment '配送地址'")
    private String destAddress;

    //商品名
    @Column(columnDefinition = "varchar(32) comment '商品'")
    private String commodityName;

    //商品数量
    @Column(columnDefinition = "bigint comment '商品数量'")
    private Long commodityCount;

    @Lob
    @Column(columnDefinition = "longtext comment '详细需求'")
    private String buyNeedInfo;

    @Column(columnDefinition = "varchar(100) comment '联系人'")
    private String linker;

    @Column(columnDefinition = "varchar(100) comment '联系方法'")
    private String linkTelephone;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(1023) comment '配送限制'")
    private TimeLimit timeLimit;

    @Column(columnDefinition = "bigint comment '配送时间'")
    private Long startTime;

    @Column(columnDefinition = "bigint comment '超时'")
    private Long endTime;

    @Column(columnDefinition = "double comment '货物金额'")
    private Double commodityAmount;

    @Column(columnDefinition = "double comment '运输金额'")
    private Double remuneration;


}
