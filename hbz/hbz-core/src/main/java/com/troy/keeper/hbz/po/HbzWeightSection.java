package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.CommodityClass;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/12/6.
 */
@Data
@Entity
@Table(name = "hbz_weigthSection")
public class HbzWeightSection extends BaseVersionLocked {

    @Column(columnDefinition = "varchar(10) comment '货物种类'")
    @Enumerated(EnumType.STRING)
    private CommodityClass commodityClass;

    @Column(name = "seqNo", columnDefinition = "int comment '排序字段'")
    private Integer index;

    @Column(columnDefinition = "varchar(32) comment '区间开始'")
    private Double leftBoundary;

    @Column(columnDefinition = "varchar(16) comment '区间结束'")
    private Double rightBoundary;

    //计算公式
    @Column(columnDefinition = "varchar(32) comment '公式'")
    private String formulaKey;
}
