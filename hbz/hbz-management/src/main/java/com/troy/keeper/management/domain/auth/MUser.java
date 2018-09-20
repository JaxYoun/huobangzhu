package com.troy.keeper.management.domain.auth;

import com.troy.keeper.system.domain.SmUser;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * 后台管理 用户 实体类
 */
@Entity
@Table(name="m_user")
public class MUser extends SmUser {

    /**
     * 用户-数据权限组 关系表
     */
    @OneToMany(mappedBy = "smUser",fetch = FetchType.LAZY)
    private List<MUserDataAuthGroup> mUserDataAuthGroupList;

}
