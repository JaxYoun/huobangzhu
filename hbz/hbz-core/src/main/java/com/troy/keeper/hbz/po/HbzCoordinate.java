package com.troy.keeper.hbz.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/10/27.
 */
//用户位置
@Getter
@Setter
@Entity
@Table(name = "hbz_coordinate")
public class HbzCoordinate extends BaseVersionLocked {

    @ManyToOne
    @JoinColumn(name = "user", columnDefinition = "bigint comment '用户'")
    private HbzUser user;

    @Column(columnDefinition = "double comment '经'")
    private Double pointX;

    @Column(columnDefinition = "double comment '纬度'")
    private Double pointY;

    @Column(columnDefinition = "bigint comment '时间'")
    private Long syncMillis;

    @ManyToOne()
    @JoinColumn()
    private HbzArea area;
}
