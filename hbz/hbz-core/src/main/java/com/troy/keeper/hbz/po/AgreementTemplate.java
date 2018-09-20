package com.troy.keeper.hbz.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author：YangJx
 * @Description：合同模板实体类
 * @DateTime：2018/1/30 13:59
 */
@Data
@Entity
@Table(name = "hbz_agreement_template")
public class AgreementTemplate extends BaseVersionLocked {

    @Column(columnDefinition = "varchar(100) comment '合同模板名称'")
    private String name;

    @Column(columnDefinition = "char(2) comment '合同模板类型'")
    private String type;

    @Column(columnDefinition = "varchar(200) comment '描述'")
    private String description;

    @Column(columnDefinition = "char(1) comment '是否可用，0：已禁用，1：可用'")
    private String isEnable;

    @Column(columnDefinition = "varchar(300) comment '模板内容附件路径'")
    private String attachPath;

    @Column(columnDefinition = "varchar(200) comment '备注'")
    private String remark;

    @Column(columnDefinition = "char(10) comment '模板编号'")
    private String code;

}
