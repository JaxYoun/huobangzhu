package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.RefundStatus;
import lombok.Data;

import javax.persistence.*;

/**
 * 退款表
 * Created by leecheng on 2018/3/6.
 */
@Entity
@Table(name = "hbz_refund")
@Data
public class HbzRefund extends BaseVersionLocked {

    @ManyToOne
    @JoinColumn(name = "pay", columnDefinition = "bigint(11) comment '支付单'")
    private HbzPay pay;

    @ManyToOne
    @JoinColumn(name = "user", columnDefinition = "bigint comment '用户id'")
    private HbzUser user;

    @Column(columnDefinition = "varchar(100) comment '退款单号'")
    private String refundNo;

    @Column(unique = true, columnDefinition = "varchar(100) comment '请求单号'")
    private String requestNo;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '退款状态'")
    private RefundStatus refundStatus;

    @Column(columnDefinition = "int comment '对账单状态'")
    private Integer bill;

    @Column(columnDefinition = "bigint comment '创建时间unix时间戳'")
    private Long createTime;
}
