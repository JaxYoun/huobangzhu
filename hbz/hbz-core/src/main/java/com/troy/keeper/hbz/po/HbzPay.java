package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.PayProgress;
import com.troy.keeper.hbz.type.PayType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/10/30.
 */
//支付信息
@Getter
@Setter
@Entity
@Table(name = "hbz_pay")
public class HbzPay extends BaseVersionLocked {

    //业务代码，标注业务
    @Column(columnDefinition = "varchar(16) comment '业务类'")
    @Enumerated(EnumType.STRING)
    private BusinessType businessType;

    //订单号--对应了具体业务订单号-----------------
    @Column(columnDefinition = "varchar(32) comment '业务号'")
    private String businessNo;

    //贸易订单号--发送到支付接口的订单号-----------
    @Column(columnDefinition = "varchar(1000) comment '支付订单号'")
    private String tradeNo;

    @Column(columnDefinition = "double comment '金额'")
    private Double fee;

    //被创建的订单号
    @Column(columnDefinition = "varchar(500) comment '支付机构订单号'")
    private String createdNo;

    //支付类型
    @Column(columnDefinition = "varchar(64) comment '支付类型'")
    @Enumerated(EnumType.STRING)
    private PayType payType;

    //支付进度
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '支付进度'")
    private PayProgress payProgress;

    //预支付单回执
    @Lob
    @Column(columnDefinition = "longtext comment '支付信息'")
    private String response;

    //支付详细信息
    @Column(columnDefinition = "longtext comment '详细信息'")
    @Lob
    private String details;

    @Column(columnDefinition = "int comment '兑账，二进制位状态：1、是否支付兑账，2、是否退款兑账'")
    private Integer bill;
}
