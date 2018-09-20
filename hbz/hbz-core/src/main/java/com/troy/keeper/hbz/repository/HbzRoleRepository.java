package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzRole;
import com.troy.keeper.hbz.type.Role;

import java.util.List;

/**
 * Created by leecheng on 2017/10/16.
 */
public interface HbzRoleRepository extends BaseRepository<HbzRole, Long> {

    /**
     * 根据角色类型列表查询角色列表
     *
     * @param roleList
     * @param status
     * @return
     */
    List<HbzRole> getDistinctByRoleInAndStatus(List<Role> roleList, String status);

}
