package com.troy.keeper.hbz.po;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/12/14.
 * 上架商品
 */
@Data
@Entity
@Table(name = "hbz_product")
public class HbzProduct extends BaseVersionLocked {

    @Column(columnDefinition = "varchar(64) comment '产品名称'")
    private String productName;

    @Column(columnDefinition = "varchar(32) comment '上传编号'")
    private String productNo;

    //0-停用1-启用
    @Column(name = "s", columnDefinition = "int")
    private Integer state;

    @Lob
    @Column(columnDefinition = "longtext comment '头图片'")
    private String header;

    @Lob
    @Column(columnDefinition = "longtext comment '图片'")
    private String img;

    @Column
    private Integer productStatus;

    @ManyToOne
    @JoinColumn(columnDefinition = "bigint comment '商品'")
    private HbzWareInfo ware;

    @Column(name = "seq_no", columnDefinition = "int comment '排序'")
    private Integer index;

    @Column(columnDefinition = "double comment '金额'")
    private Double amount;

    @Column(columnDefinition = "double comment '积分'")
    private Double score;

    @Column(columnDefinition = "int comment '库存数量'")
    private Integer total;

    @Column(name = "c_leave", columnDefinition = "int comment '余量'")
    private Integer leave;

    @Column(columnDefinition = "int comment '推荐标识'")
    private Integer recommend;

    @Lob
    @Column(columnDefinition = "longtext comment '备注'")
    private String message;

    @Lob
    @Column(columnDefinition = "longtext comment '摘要'")
    private String summary;

    @Lob
    @Column(columnDefinition = "longtext comment '详细'")
    private String content;


    @Column(columnDefinition = "varchar(100) comment '生产地址'")
    private String productionAddress;

}
