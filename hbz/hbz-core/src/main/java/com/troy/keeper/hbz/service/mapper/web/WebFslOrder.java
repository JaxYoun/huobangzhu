package com.troy.keeper.hbz.service.mapper.web;

import com.troy.keeper.hbz.po.HbzOrder;
import com.troy.keeper.hbz.type.CommodityClass;
import com.troy.keeper.hbz.type.TransType;
import com.troy.keeper.hbz.type.VolumeUnit;
import com.troy.keeper.hbz.type.WeightUnit;
import lombok.*;

import javax.persistence.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "web_fsl_order")
public class WebFslOrder extends HbzOrder {

    @Column(columnDefinition = "varchar(255) comment '货物名称'")
    private String commodityName;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '货物分类'")
    private CommodityClass commodityClass;

    @Column(columnDefinition = "long comment '质量'")
    private Double commodityWeight;

    @Column(columnDefinition = "long comment '体积'")
    private Double commodityVolume;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '质量单元'")
    private WeightUnit weightUnit;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '体积单位'")
    private VolumeUnit volumeUnit;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '车辆类型要求'")
    private TransType transType;

    //最大载重
    @Column(columnDefinition = "double comment '最大载重'")
    private Double maxLoad;

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

    public Long getOrderTakeStart() {
        return orderTakeStart;
    }
    public void setOrderTakeStart(Long orderTakeStart) {
        this.orderTakeStart = orderTakeStart;
    }
}
