package com.troy.keeper.hbz.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 可计算的Script公式
 * Created by leecheng on 2017/12/4.
 */
//公式
@Entity
@Setter
@Getter
@Table(name = "hbz_for_mula")
public class HbzFormula extends BaseVersionLocked {

    @Column(columnDefinition = "varchar(32) comment '规则编号'")
    private String formulaNo;

    @Column(columnDefinition = "varchar(32) comment '公式类型'")
    private String formulaType;

    @Column(columnDefinition = "varchar(32) comment '适用业务'")
    private String adjustType;

    @Column(columnDefinition = "varchar(32) comment '公式状态'")
    private String formulaState;

    @Column(name = "cn")
    private String name;

    @Column(columnDefinition = "varchar(32) comment '公式Key'")
    private String formulaKey;

    @Column(columnDefinition = "varchar(64) comment '公式名称'")
    private String formulaDesc;

    @Lob
    @Column(columnDefinition = "longtext comment '公式内容'")
    private String formula;

    //公式图像
    @Column(columnDefinition = "longtext comment '公式图像'")
    @Lob
    private String formulaImage;

    @Lob
    @Column(name = "comment_f",columnDefinition = "longtext comment '备注'")
    private String comments;

}
