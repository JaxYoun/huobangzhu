package com.troy.keeper.hbz.po;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/12/18.
 */
@Data
@Entity
@Table(name = "hbz_score_order")
public class HbzScoreOrder extends BaseVersionLocked {

    @ManyToOne
    @JoinColumn(columnDefinition = "bigint comment '区域'")
    private HbzArea area;

    @ManyToOne
    @JoinColumn(columnDefinition = "bigint comment '用户'")
    private HbzUser user;

    @Column
    String orderNo;

    @Column
    Long scoreTime;

    @ManyToOne
    @JoinColumn(name = "productId", columnDefinition = "bigint comment '商品'")
    private HbzProduct product;

    @Column(columnDefinition = "varchar(200) comment '配送目的地址'")
    private String destAddr;

    @Column(columnDefinition = "varchar(8) comment '目的联系人'")
    private String link;

    @Column(columnDefinition = "varchar(18) comment '电话'")
    private String telephone;

    @Column(name = "s", columnDefinition = "int comment '状态1、未发货；2、已发货；3、已收货；'")
    private Integer state;

}
