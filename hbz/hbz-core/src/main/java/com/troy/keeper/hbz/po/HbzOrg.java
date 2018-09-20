package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.OrgType;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by leecheng on 2017/10/30. 组织机构
 */
@Data
@Entity
@Table(name = "hbz_org")
public class HbzOrg extends BaseVersionLocked {

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '企业部门'")
    private OrgType orgType;

    @Column(columnDefinition = "varchar(64) comment '企业部门名称'")
    private String organizationName;

    @ManyToOne
    @JoinColumn(name = "parentOrg", columnDefinition = "bigint comment '父部门、公司'")
    private HbzOrg parent;

    @OneToMany
    @JoinColumn(name = "parentOrg", columnDefinition = "bigint comment '子部门'")
    private List<HbzOrg> subOrg;

    @Column(columnDefinition = "varchar(100) comment '公司地址'")
    private String address;

    @Column(columnDefinition = "varchar(10) comment '联系人'")
    private String linkMan;

    @Column(columnDefinition = "varchar(20) comment '联系电话'")
    private String telephone;

    @Column(columnDefinition = "varchar(200) comment '公司描述'")
    private String description;

}
