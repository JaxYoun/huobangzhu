package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.OrderType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/11/30.
 */
//联系人收货人
@Getter
@Setter
@Entity
@Table(name = "hbz_linkInfo")
public class HbzLinkInfo extends BaseVersionLocked {

    @ManyToOne
    @JoinColumn(name = "area", columnDefinition = "bigint comment '行政区域'")
    private HbzArea area;

    @Column(columnDefinition = "varchar(5) comment '地点'")
    private String type;

    @Column(columnDefinition = "int comment '收发类型'")
    private Integer usein;

    @Column(name = "index_seq", columnDefinition = "int comment '排序'")
    private Integer index;

    @Column(columnDefinition = "int comment '默认'")
    private Integer idefault;

    @Column(columnDefinition = "varchar(17) comment '姓名'")
    private String linker;

    @Column(columnDefinition = "varchar(16) comment '电话'")
    private String linkTelephone;

    @Column(columnDefinition = "double comment '经度'")
    private Double lng;

    @Column(columnDefinition = "double comment '纬度'")
    private Double lat;

    @Column(columnDefinition = "varchar(1055) comment '地址描述'")
    private String location;

    @Column(columnDefinition = "varchar(4000) comment '门牌号'")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(128) comment '订单类型'")
    private OrderType orderType;

    @ManyToOne
    @JoinColumn(columnDefinition = "bigint comment '用户'")
    private HbzUser user;

}
