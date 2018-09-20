package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.PayType;
import com.troy.keeper.hbz.type.ReimburseProgress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * 业务退款单号
 * Created by leecheng on 2018/1/3.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hbz_reimburse")
public class HbzReimburse extends BaseVersionLocked {

    @Column(columnDefinition = "varchar(16) comment '业务类'")
    @Enumerated(EnumType.STRING)
    private BusinessType businessType;

    @Column(columnDefinition = "varchar(32) comment '业务号'")
    private String businessNo;

    @Column(columnDefinition = "varchar(1000) comment '支付订单号'")
    private String tradeNo;

    @Column(columnDefinition = "double comment '金额'")
    private Double fee;

    @Column(columnDefinition = "varchar(500) comment '退款单号'")
    private String reimburseNo;

    @Column(columnDefinition = "varchar(64) comment '支付类型'")
    @Enumerated(EnumType.STRING)
    private PayType payType;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '进度'")
    private ReimburseProgress reProgress;

}
