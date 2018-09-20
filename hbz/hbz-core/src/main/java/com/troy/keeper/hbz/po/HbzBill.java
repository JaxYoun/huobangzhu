package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.BillType;
import com.troy.keeper.hbz.type.PayType;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by leecheng on 2018/3/8.
 */
@Data
@Entity
@Table(name = "hbz_bill")
public class HbzBill extends BaseVersionLocked {

    @Enumerated(EnumType.STRING)
    @Column(name = "bt", columnDefinition = "varchar(32) comment '支付、退款'")
    private BillType billType;

    //外部订单号
    @Column(name = "tn", columnDefinition = "varchar(125)")
    private String tradeNo;

    @Column(columnDefinition = "varchar(100) comment '退款请求号'")
    private String requestNo;

    @Column(name = "bill_date", columnDefinition = "bigint comment '账单日期'")
    private Long date;

    @Lob
    @Column(name = "bill_content", columnDefinition = "longtext comment '账单内容'")
    private String billContent;

    @Column(name = "pay_type", columnDefinition = "varchar(10) comment '支付类型'")
    @Enumerated(EnumType.STRING)
    private PayType payType;

}
