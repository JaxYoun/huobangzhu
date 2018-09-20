package com.troy.keeper.hbz.po;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/12/12.
 * 商品类型
 */
@Data
@Entity
@Table(name = "hbz_wareType")
public class HbzWareType extends BaseVersionLocked {

    @ManyToOne
    @JoinColumn(name = "parentWareType", columnDefinition = "bigint comment '父类型'")
    private HbzWareType parent;

    @Column(columnDefinition = "varchar(64) comment '类别编码'")
    private String typeNo;

    @Column(name = "typeName", columnDefinition = "varchar(100) comment '类型名称'")
    private String name;

    @Column(columnDefinition = "int comment '类型级别'")
    private Integer level;

    @Lob
    @Column(columnDefinition = "longtext comment ''")
    private String headerBit;
}
