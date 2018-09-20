package com.troy.keeper.hbz.po;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/12/12.
 * 商品
 */
@Data
@Entity
@Table(name = "hbz_ware_information")
public class HbzWareInfo extends BaseVersionLocked {

    @ManyToOne
    @JoinColumn(name = "type", columnDefinition = "bigint comment '商品类'")
    private HbzWareType type;

    @Column(columnDefinition = "varchar(60) comment '商品编号'")
    private String wareNo;

    @Column(columnDefinition = "varchar(128) comment '名称'")
    private String name;

    @Column(columnDefinition = "double comment '行情价'")
    private Double marketAmount;

    @Column(columnDefinition = "double comment '金额'")
    private Double amount;

    //0-停用1-启用
    @Column(name = "s", columnDefinition = "int")
    private Integer state;

    @ManyToOne
    @JoinColumn(columnDefinition = "bigint comment '公司'")
    private HbzBrand brand;

    @Column(columnDefinition = "double comment '积分'")
    private Double score;

    @Lob
    @Column(columnDefinition = "longtext comment '头图片'")
    private String header;

    @Lob
    @Column(columnDefinition = "longtext comment '图片'")
    private String img;

    @Column(columnDefinition = "varchar(64) comment '规格'")
    private String specifications;

    @Column(columnDefinition = "varchar(200) comment '摘要'")
    private String summary;

    @Lob
    @Column(columnDefinition = "longtext comment '详细信息'")
    private String introduction;

    @Column(columnDefinition = "varchar(100) comment '生产地址'")
    private String productionAddress;

}
