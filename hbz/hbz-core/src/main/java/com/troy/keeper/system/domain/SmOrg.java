package com.troy.keeper.system.domain;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/5/25.
 */
@Data
@Entity
@Table(name = "sm_org")
@Inheritance(strategy = InheritanceType.JOINED)
public class SmOrg extends BaseAuditingEntity {



    // 父组织机构ID
    @Column(name = "p_id")
    private Long pId;
    // 层级关系标识
    @Column(name = "relationship", length = 200)
    private String relationship;
    // 名称
    @Column(name = "org_name", length = 50)
    private String orgName;
    // 顺序
    @Column(name = "order_code", length = 11)
    private Long orderCode;
    //组织机构级别
    @Column(name = "org_level",columnDefinition = "int comment '组织机构级别'")
    private Integer orgLevel;

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Long getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Long orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(Integer orgLevel) {
        this.orgLevel = orgLevel;
    }
}
