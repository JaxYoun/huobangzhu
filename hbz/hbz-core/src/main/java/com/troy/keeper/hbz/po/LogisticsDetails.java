package com.troy.keeper.hbz.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author 李奥
 * @date 2017/12/21.
 */
@Getter
@Setter
@Entity
@Table(name = "logistics_details")
public class LogisticsDetails extends BaseVersionLocked {

    //当前物流信息
    @Column(columnDefinition = "varchar(50) comment '当前物流信息'")
    private String commodityDesc;

    //发件时间
    @Column(columnDefinition = "long comment '事件时间'")
    private Long sendTime;

    //关联快递
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hbz_expressPieces_id")
    private HbzExpressPieces hbzExpressPieces;

}
