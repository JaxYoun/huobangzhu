package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.ExpressCompanyType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * @author 李奥   快递派件表
 * @date 2017/12/20.
 */
@Getter
@Setter
@Entity
@Table(name = "hbz_expressPieces")
public class HbzExpressPieces extends BaseVersionLocked {

    //快递公司名称
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '快递公司名称'")
    private ExpressCompanyType expressCompanyType;

    //快递单号
    @Column(columnDefinition = "varchar(32) comment '快递单号'")
    private String trackingNumber;

    //发件时间
    @Column(columnDefinition = "long comment '发件时间'")
    private Long sendTime;

    //与快递表对应的关系属性
    @OneToOne
    @JoinColumn(name = "hbz_exOrder_id")
    private HbzExOrder hbzExOrder;

    //
    @OneToMany(mappedBy = "hbzExpressPieces")
    private List<LogisticsDetails> logisticsDetailsList;

}
