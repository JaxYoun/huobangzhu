package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.TakeType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/11/21.
 */
//投标信息
@Getter
@Setter
@Entity
@Table(name = "hbz_takerInfo")
public class HbzTakerInfo extends BaseVersionLocked {

    @Column(columnDefinition = "double comment '报价'")
    private Double offer;

    @ManyToOne
    @JoinColumn(name = "orderId", columnDefinition = "bigint comment '订单'")
    private HbzOrder order;

    @ManyToOne
    @JoinColumn(name = "user", columnDefinition = "bigint comment '用户'")
    private HbzUser user;

    @ManyToOne
    @JoinColumn(name = "agent", columnDefinition = "bigint comment '用户'")
    private HbzUser agent;

    @Column
    @Enumerated(EnumType.STRING)
    private TakeType takeType;

}
