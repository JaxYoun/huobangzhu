package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.TransType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Created by leecheng on 2017/12/5.
 */
//司机专线信息
@Getter
@Setter
@Entity
@Table(name = "hbz_driverLi")
public class HbzDriverLine extends BaseVersionLocked {

    @ManyToOne
    @JoinColumn(columnDefinition = "bigint comment '用户'")
    private HbzUser user;

    @ManyToOne
    @JoinColumn(columnDefinition = "bigint comment '发货区域'")
    private HbzArea originArea;

    @ManyToOne
    @JoinColumn(columnDefinition = "bigint comment '终止区域'")
    private HbzArea destArea;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '车辆类型'")
    private TransType transType;

    @Column(columnDefinition = "double comment '载重'")
    private Double maxLoad;

    @ManyToMany
    @JoinTable(name = "hbz_dvl_ts", joinColumns = {@JoinColumn(name = "dvl_id")}, inverseJoinColumns = {@JoinColumn(name = "s_id")})
    private List<HbzTransSize> transSizes;

    @Column(columnDefinition = "double comment '单价'")
    private Double unitPrices;

}
