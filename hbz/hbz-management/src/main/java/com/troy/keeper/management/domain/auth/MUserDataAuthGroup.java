package com.troy.keeper.management.domain.auth;

import com.troy.keeper.core.base.entity.BaseEntity;
import com.troy.keeper.system.domain.SmUser;

import javax.persistence.*;

/**
 * 后台管理 用户-数据权限组 实体类
 */
@Entity
@Table(name = "m_user_data_auth_group")
public class MUserDataAuthGroup extends BaseEntity {

    @Column(name = "m_user_id")
    private Long mUserId;

    @Column(name = "data_auth_group_id")
    private Long dataAuthGroupId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "m_user_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
    private SmUser smUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "data_auth_group_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
    private DataAuthGroup dataAuthGroup;

    public Long getMUserId() {
        return mUserId;
    }
    public void setMUserId(Long mUserId) {
        this.mUserId = mUserId;
    }
    public Long getDataAuthGroupId() {
        return dataAuthGroupId;
    }
    public void setDataAuthGroupId(Long dataAuthGroupId) {
        this.dataAuthGroupId = dataAuthGroupId;
    }
    public SmUser getSmUser() {
        return smUser;
    }
    public void setSmUser(SmUser smUser) {
        this.smUser = smUser;
    }
    public DataAuthGroup getDataAuthGroup() {
        return dataAuthGroup;
    }
    public void setDataAuthGroup(DataAuthGroup dataAuthGroup) {
        this.dataAuthGroup = dataAuthGroup;
    }
}
