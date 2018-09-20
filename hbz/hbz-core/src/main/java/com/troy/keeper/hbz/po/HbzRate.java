package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.RateType;
import lombok.Data;

import javax.persistence.*;

/**
 * 订单评分
 * Created by leecheng on 2017/12/28.
 */
@Data
@Entity
public class HbzRate extends BaseVersionLocked {

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) comment '评价类型'")
    private RateType type;

    @ManyToOne
    @JoinColumn(name = "orderId", columnDefinition = "bigint comment '订单'")
    private HbzOrder order;

    @ManyToOne
    @JoinColumn(name = "user", columnDefinition = "bigint comment '评论用户'")
    private HbzUser user;

    @Column(columnDefinition = "int comment '星级'")
    private Integer star;

    @Lob
    @Column(name = "my_l_message", columnDefinition = "longtext comment '评语'")
    private String comment;

}
