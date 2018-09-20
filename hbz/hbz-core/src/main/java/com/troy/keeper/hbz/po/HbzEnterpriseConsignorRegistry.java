package com.troy.keeper.hbz.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * Created by leecheng on 2017/11/6.
 */
//企业货主
@Getter
@Setter
@Entity
@DiscriminatorValue("EnterpriseConsignor")
public class HbzEnterpriseConsignorRegistry extends HbzUserRegistry {

    @Column(columnDefinition = "varchar(1000) comment '组织代码'")
    private String organizationCode;

    @Column(columnDefinition = "varchar(100) comment '企业名称'")
    private String organizationName;

    @Column(columnDefinition = "varchar(201) comment '开户行账号'")
    private String accountNo;

    @Column(columnDefinition = "varchar(20) comment '开户行'")
    private String bank;

    @Column(columnDefinition = "double comment '注册资金'")
    private Double registryMoney;

    @Column(columnDefinition = "varchar(555) comment '税号'")
    private String dutyParagraph;

    @Lob
    @Column(columnDefinition = "longtext comment '营业照'")
    private String businessLicense;

}
