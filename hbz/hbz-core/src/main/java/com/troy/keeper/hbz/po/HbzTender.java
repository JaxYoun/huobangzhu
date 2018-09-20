package com.troy.keeper.hbz.po;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/11/21.
 */
//招标条件
@Getter
@Setter
@Entity
@Table(name = "hbz_tender")
public class HbzTender extends BaseVersionLocked {

    @ManyToOne
    @JoinColumn(name = "ord", columnDefinition = "bigint comment '订单'")
    private HbzOrder order;

    @Column(columnDefinition = "double comment '注册资金'")
    private Double registryMoney;

    @Column(columnDefinition = "int comment '运送者类型'")
    private Integer need;

    @Column(columnDefinition = "double comment '保证金'")
    private Double bond;

    @Column(columnDefinition = "int comment '星级'")
    private Integer starLevel;
}
