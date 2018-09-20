package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.OrderTrans;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/11/3.
 */
//订单记录
@Getter
@Setter
@Entity
@Table(name = "hbz_order_rec")
public class HbzOrderRecord extends BaseVersionLocked {

    @ManyToOne
    @JoinColumn(name = "orderid", columnDefinition = "bigint comment '订单'")
    private HbzOrder order;

    @ManyToOne
    @JoinColumn(name = "userid", columnDefinition = "bigint comment '用户id'")
    private HbzUser user;

    @Column(columnDefinition = "varchar(67) comment '状态'")
    @Enumerated(EnumType.STRING)
    private OrderTrans orderTrans;

    @Column(columnDefinition = "bigint comment '时间'")
    private Long timeMillis;
}
