package com.troy.keeper.hbz.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 李奥   费用表
 * @date 2017/12/29.
 */
@Getter
@Setter
@Entity
@Table(name = "fee_schedule")
public class FeeSchedule extends BaseVersionLocked {


    //运费
    @Column(columnDefinition = "double comment '运费'")
    private Double shippingCosts;


    //提货费
    @Column(columnDefinition = "double comment '提货费'")
    private Double deliveryFee;

    //送货费
    @Column(columnDefinition = "double comment '送货费'")
    private Double deliveryCharges;

    //保费
    @Column(columnDefinition = "double comment '保费'")
    private Double premium;

    //包装费
    @Column(columnDefinition = "double comment '包装费'")
    private Double packagingFee;

    //其他费用
    @Column(columnDefinition = "double comment '其他费用'")
    private Double otherFee;

    //付款方式   数据字典
    @Column(columnDefinition = "varchar(10) comment '付款方式'")
    private String paymentMethod;
//    private  String  paymentMethodName;

    //总运费
    @Column(columnDefinition = "double comment '总运费'")
    private Double fotalFee;


}
