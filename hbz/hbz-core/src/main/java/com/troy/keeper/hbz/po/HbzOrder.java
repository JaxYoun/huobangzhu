package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.OrderTrans;
import com.troy.keeper.hbz.type.OrderType;
import com.troy.keeper.hbz.type.SettlementType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/10/24.
 */
//订单
@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class HbzOrder extends BaseVersionLocked {

    //订单号
    @Column(unique = true, columnDefinition = "varchar(4000) comment '订单号'")
    private String orderNo;

    //取货地址X
    @Column(columnDefinition = "double comment '货源坐标经度'")
    private Double originX;

    //取货地址Y
    @Column(columnDefinition = "double comment '货源坐标纬度'")
    private Double originY;

    //原始地址
    @Column(columnDefinition = "varchar(300) comment '货源地址'")
    private String originInfo;

    //目标经度
    @Column(columnDefinition = "double comment '目的坐标经度'")
    private Double destX;

    //目标纬度
    @Column(columnDefinition = "double comment '目的坐标纬度'")
    private Double destY;

    @Column(columnDefinition = "varchar(4000) comment '目的地址'")
    private String destInfo;

    @Column(columnDefinition = "double comment '订单金额'")
    private Double amount;

    @Column(columnDefinition = "double comment '预估价格'")
    private Double expectedPrice;

    //订单类型
    @Column(nullable = false, columnDefinition = "varchar(32) comment '订单类型'")
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Column(nullable = false, columnDefinition = "varchar(32) comment '订单状态'")
    @Enumerated(EnumType.STRING)
    private OrderTrans orderTrans;

    @Column(columnDefinition = "varchar(33) comment '结算类型'")
    @Enumerated(EnumType.STRING)
    private SettlementType settlementType;

    //建订单时间
    @Column(columnDefinition = "bigint comment '创建时间'")
    private Long createTime;

    //创建人
    @ManyToOne
    @JoinColumn(name = "createUser", columnDefinition = "bigint comment '下单者'")
    private HbzUser createUser;

    @Column(columnDefinition = "int comment '线下处理标识'")
    private Integer offlineProcess;

    @Lob
    @Column(columnDefinition = "longtext comment '完成拍照'")
    private String completeImage;

    @Column(columnDefinition = "bigint comment '接单时间'")
    Long takeTime;

    @ManyToOne
    @JoinColumn(name = "takeUser", columnDefinition = "bigint comment '接单用户'")
    private HbzUser takeUser;

    @Column(columnDefinition = "bigint comment '取货时间'")
    private Long dealTime;

    @ManyToOne
    @JoinColumn(name = "dealUser", columnDefinition = "bigint comment '配送人'")
    private HbzUser dealUser;

    @ManyToOne
    @JoinColumn(name = "agent", columnDefinition = "bigint comment '代理'")
    private HbzUser agent;

    @Column(columnDefinition = "bigint comment '接手时间'")
    private Long agentTime;

    @ManyToOne
    @JoinColumn(name = "originArea", columnDefinition = "bigint comment '起始地区'")
    private HbzArea originArea;

    @ManyToOne
    @JoinColumn(name = "destArea", columnDefinition = "bigint comment '送货地区'")
    private HbzArea destArea;

}
