package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.ReceiveAccountType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/11/6.
 */
//运输企业资质注册
@Getter
@Setter
@Entity
@DiscriminatorValue(value = "TransEnterprise")
public class HbzTransEnterpriseRegistry extends HbzUserRegistry {

    @Enumerated(EnumType.STRING)
    @Column(name = "receiveAccountType", columnDefinition = "varchar(32) comment '收款账号类型'")
    private ReceiveAccountType receiveAccountType;

    @Column(name = "receiveAccount", length = 255)
    private String receiveAccount;

    @Column(columnDefinition = "varchar(1000) comment '组织代码'")
    private String organizationCode;

    @Column(columnDefinition = "varchar(100) comment '企业名称'")
    private String organizationName;

    @Column(columnDefinition = "varchar(201) comment '开户行账号'")
    private String accountNo;

    @Column(columnDefinition = "double comment '注册资金'")
    private Double registryMoney;

    @Column(columnDefinition = "varchar(20) comment '开户行'")
    private String bank;

    @Column(columnDefinition = "varchar(555) comment '税号'")
    private String dutyParagraph;

    @Lob
    @Column(columnDefinition = "longtext comment '营业照'")
    private String businessLicense;
}
