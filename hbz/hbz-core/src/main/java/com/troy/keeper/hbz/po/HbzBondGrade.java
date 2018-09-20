package com.troy.keeper.hbz.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by leecheng on 2017/12/26.
 */
@Data
@Entity
@Table(name = "hbz_bondGrade")
public class HbzBondGrade extends BaseVersionLocked {

    @Column(columnDefinition = "varchar(16) comment '保存金业务类型'")
    private String bondType;

    //档次编码
    @Column(columnDefinition = "varchar(32) comment '档次编码'")
    private String grade;

    //序号
    @Column(name = "seq", columnDefinition = "int comment '排序'")
    private Integer index;

    @Column(columnDefinition = "int comment '是否支付多'")
    private Integer multi;

    //保证金名称
    @Column(columnDefinition = "varchar(1000) comment '名称'")
    private String name;

    //保证金金额
    @Column(columnDefinition = "double comment '金额'")
    private Double total;

}
