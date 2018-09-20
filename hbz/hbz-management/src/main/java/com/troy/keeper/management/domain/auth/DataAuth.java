package com.troy.keeper.management.domain.auth;

import com.troy.keeper.monomer.security.domain.Authority;
import com.troy.keeper.system.domain.DataDictionary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 后台管理 数据权限 实体类
 */
@Entity
@Table(name = "m_data_auth")
public class DataAuth extends Authority {

    /**
     * 组织机构id
     */
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 业务数据类型
     */
    @Column(name = "biz_data_type")
    private DataDictionary biz_data_type;

    /**
     * 数据权限-增
     * 0：拒绝，1：允许
     */
    @Column(name = "operate_insert")
    private Integer operateInsert;

    /**
     * 数据权限-删
     * 0：拒绝，1：允许
     */
    @Column(name = "operate_delete")
    private Integer operateDelete;

    /**
     * 数据权限-改
     * 0：拒绝，1：允许
     */
    @Column(name = "operate_update")
    private Integer operateUpdate;

    /**
     * 数据权限-查
     * 0：拒绝，1：允许
     */
    @Column(name = "operate_select")
    private Integer operateSelect;

    /**
     * 数据权限-执行其他操作
     * 0：拒绝，1：允许
     */
    @Column(name = "operate_execute")
    private Integer operateExecute;


    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public DataDictionary getBiz_data_type() {
        return biz_data_type;
    }

    public void setBiz_data_type(DataDictionary biz_data_type) {
        this.biz_data_type = biz_data_type;
    }

    public Integer getOperateInsert() {
        return operateInsert;
    }

    public void setOperateInsert(Integer operateInsert) {
        this.operateInsert = operateInsert;
    }

    public Integer getOperateDelete() {
        return operateDelete;
    }

    public void setOperateDelete(Integer operateDelete) {
        this.operateDelete = operateDelete;
    }

    public Integer getOperateUpdate() {
        return operateUpdate;
    }

    public void setOperateUpdate(Integer operateUpdate) {
        this.operateUpdate = operateUpdate;
    }

    public Integer getOperateSelect() {
        return operateSelect;
    }

    public void setOperateSelect(Integer operateSelect) {
        this.operateSelect = operateSelect;
    }

    public Integer getOperateExecute() {
        return operateExecute;
    }

    public void setOperateExecute(Integer operateExecute) {
        this.operateExecute = operateExecute;
    }
}
