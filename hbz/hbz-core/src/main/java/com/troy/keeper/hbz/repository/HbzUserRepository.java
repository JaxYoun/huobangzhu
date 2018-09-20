package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzRole;
import com.troy.keeper.hbz.po.HbzUser;

import java.util.List;

/**
 * Created by leecheng on 2017/10/12.
 */
public interface HbzUserRepository extends BaseRepository<HbzUser, Long> {

    HbzUser findByLogin(String username);

    Long countByLogin(String userName);

    Long countByTelephone(String te);

    Long countByEmail(String e);

    /**
     * 获取全部注册用户
     *
     * @param actived
     * @param status
     * @return
     */
    List<HbzUser> getDistinctByActivatedAndStatus(boolean actived, String status);

    /**
     * 根据电话号码获取单个有效用户
     *
     * @param actived
     * @param status
     * @param telephone
     * @return
     */
    List<HbzUser> getHbzUserByActivatedAndStatusAndTelephone(Boolean actived, String status, String telephone);

    /**
     * 根据角色获取有效用户
     *
     * @param roleList
     * @param actived
     * @param status
     * @return
     */
    List<HbzUser> getDistinctByRolesInAndActivatedAndStatus(List<HbzRole> roleList, Boolean actived, String status);
}
