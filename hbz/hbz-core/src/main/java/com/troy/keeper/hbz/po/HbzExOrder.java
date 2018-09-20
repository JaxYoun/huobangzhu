package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.CommodityClass;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/11/29.
 */
//快递
@Getter
@Setter
@Entity
@Table(name = "hbz_exOrder")
public class HbzExOrder extends HbzOrder {
    @Column(columnDefinition = "varchar(1000) comment '关联图片'")
    private String relatedPictures;
    @Column(columnDefinition = "varchar(64) comment '货物名称'")
    private String commodityName;

    @Column(columnDefinition = "varchar(32) comment '发货人'")
    private String originLinker;

    @Column(columnDefinition = "varchar(16) comment '发货电话'")
    private String originTelephone;

    @Column(columnDefinition = "varchar(100) comment '取货地址'")
    private String originAddr;

    @Column(columnDefinition = "varchar(100) comment '送货地址'")
    private String destAddr;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(16) comment '货物类型'")
    private CommodityClass commodityClass;

    @Column(columnDefinition = "double comment '货物质量'")
    private Double commodityWeight;

    @Column(columnDefinition = "double comment '货物体积'")
    private Double commodityVolume;

    @Column(columnDefinition = "long comment '取货时间'")
    private Long orderTakeTime;

    @Lob
    @Column(columnDefinition = "longtext comment '详细信息'")
    private String commodityDesc;

    @Column(columnDefinition = "varchar(32) comment '联系人'")
    private String linker;

    @Column(columnDefinition = "varchar(18) comment '手机'")
    private String telephone;

    //与派件表一对队以 的关系
    @OneToOne(mappedBy = "hbzExOrder")
    private HbzExpressPieces hbzExpressPieces;

}
