package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.ExpressCompanyType;
import lombok.Data;

import javax.persistence.*;

/**
 * 积分商城快递记录
 * Created by leecheng on 2018/1/5.
 */
@Data
@Entity
@Table(name = "hbz_ex_invoice")
public class HbzExpressInvoice extends BaseVersionLocked {

    @Column(columnDefinition = "varchar(64) comment '业务订单号'")
    private String orderNo;

    @Column(columnDefinition = "varchar(32) comment '业务订单类型'")
    private String orderType;

    @Column(columnDefinition = "bigint comment '发货时间'")
    private Long sendTime;

    //快递公司
    @Enumerated(EnumType.STRING)
    private ExpressCompanyType expressCompanyType;

    @Column(columnDefinition = "varchar(100) comment '快递号'")
    private String exNo;

}
