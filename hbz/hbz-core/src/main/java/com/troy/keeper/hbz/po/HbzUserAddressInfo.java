package com.troy.keeper.hbz.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 专线常用联系方法
 * Created by leecheng on 2017/10/27.
 */
@Setter
@Getter
@Entity
@Table(name = "hbz_user_address_info")
public class HbzUserAddressInfo extends BaseVersionLocked {

    @ManyToOne
    @JoinColumn(name = "user", columnDefinition = "bigint comment '用户id'")
    private HbzUser user;

    @ManyToOne
    @JoinColumn(name = "area", columnDefinition = "bigint comment '行政区域'")
    private HbzArea area;

    @Column(name = "sequenceId", columnDefinition = "int(11) comment '序号'")
    private Integer index;

    @Column(name = "type", columnDefinition = "int(11) comment '类型1、发货 2、收货'")
    private Integer type;

    @Column(name = "linker", columnDefinition = "varchar(64) comment '联系人'")
    private String linker;

    @Column(name = "telephone", columnDefinition = "varchar(32) comment '联系电话'")
    private String telephone;

    @Column(name = "destX", columnDefinition = "double comment '目标经度'")
    private Double destX;

    @Column(name = "destY", columnDefinition = "double comment '目标纬度'")
    private Double destY;

    @Column(name = "default_i", columnDefinition = "int comment '默认'")
    private Integer idefault;

    @Lob
    @Column(name = "destAddress", columnDefinition = "longtext comment '目标地址描述'")
    private String destAddress;

}
