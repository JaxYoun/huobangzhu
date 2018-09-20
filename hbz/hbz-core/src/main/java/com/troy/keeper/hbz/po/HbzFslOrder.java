package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/11/8.
 */
//整车专线订单
@Getter
@Setter
@ToString
@Entity
@Table(name = "hbz_fslOrder")
public class HbzFslOrder extends HbzOrder {

    @Column(columnDefinition = "varchar(1000) comment '关联图片'")
    private String relatedPictures;

    @Column(columnDefinition = "varchar(4000) comment '车长'")
    private String transLen;

    @Column(columnDefinition = "varchar(255) comment '货物名称'")
    private String commodityName;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(16) comment '分类'")
    private CommodityType commodityType;

    @Column(columnDefinition = "long comment '质量'")
    private Double commodityWeight;

    @Column(columnDefinition = "long comment '体积'")
    private Double commodityVolume;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '质量单元'")
    private WeightUnit weightUnit;

    @Lob
    @Column(columnDefinition = "longtext comment '货物描述'")
    private String commodityDescribe;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '体积单位'")
    private VolumeUnit volumeUnit;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '车辆类型要求'")
    private TransType transType;

    //最大载重
    @Column(columnDefinition = "double comment '最大载重'")
    private Double maxLoad;

    @Column(columnDefinition = "double comment '单价'")
    private Double unitPrice;

    @Lob
    @Column(columnDefinition = "longtext comment '原地址'")
    private String originAddress;

    @Lob
    @Column(columnDefinition = "longtext comment '目的地说明'")
    private String destAddress;

    @Column(columnDefinition = "bigint comment '接运时间'")
    private Long orderTakeStart;

    @Column(columnDefinition = "varchar(16) comment '货主电话'")
    private String linkTelephone;

    @Column(columnDefinition = "varchar(16) comment '联系人'")
    private String linkMan;

    @Column(columnDefinition = "varchar(16) comment '目标联系人'")
    private String destLinker;

    @Column(columnDefinition = "varchar(16) comment '收货电话'")
    private String destTelephone;

    @Column(columnDefinition = "bigint comment '送达时间限制'")
    private Long destlimit;

    @Lob
    @Column(columnDefinition = "longtext comment '补充说明'")
    private String linkRemark;

}
