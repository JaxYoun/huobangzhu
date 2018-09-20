package com.troy.keeper.hbz.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 李奥
 * @date 2017/12/22.   货物信息 管理
 */

@Getter
@Setter
@Entity
@Table(name = "commodity_information")
public class CommodityInformation  extends  BaseVersionLocked  {

    //货物编号
    @Column(columnDefinition = "varchar(32) comment '货物编号'")
    private  String  commodityNumber;

    //货物名称
    @Column(columnDefinition = "varchar(32) comment '货物名称'")
    private String commodityName;

    //规格
    @Column(columnDefinition = "long comment '规格'")
    private  Double specification;

    //包装单位
    @Column(columnDefinition = "varchar(32) comment '包装单位'")
    private  String packageUnit;

    //体积
    @Column(columnDefinition = "long comment '体积'")
    private Double  volume;

    //重量
    @Column(columnDefinition = "long comment '重量'")
    private Double   weight;

    //价格
    @Column(columnDefinition = "double comment '价格'")
    private Double price;

    //条码
    @Column(columnDefinition = "varchar(32) comment '条码'")
    private String barcode;

    //货物名称简拼
    @Column(columnDefinition = "varchar(10) comment '货物名称简拼'")
    private  String  jianpin;

    //计费重量
    @Column(columnDefinition = "varchar(32) comment '计费重量'")
    private  String  billingWeight;











}
