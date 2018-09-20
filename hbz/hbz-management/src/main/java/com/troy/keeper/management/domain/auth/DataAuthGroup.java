package com.troy.keeper.management.domain.auth;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;

import javax.persistence.*;
import java.util.List;

/**
 * 后台管理 数据权限组 实体类
 */
@Entity
@Table(name = "m_data_auth_group")
public class DataAuthGroup extends BaseAuditingEntity {

    @Column(name = "m_data_auth_group_name")
    private String dataAuthGroupName;

    @OneToMany(fetch = FetchType.LAZY)
    private List<DataAuth> dataAuthList;

}
