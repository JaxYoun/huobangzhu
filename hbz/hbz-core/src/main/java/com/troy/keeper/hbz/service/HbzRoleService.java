package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzAuthDTO;
import com.troy.keeper.hbz.dto.HbzMenuDTO;
import com.troy.keeper.hbz.dto.HbzRoleDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.po.HbzRole;

import java.util.List;

/**
 * Created by leecheng on 2017/10/16.
 */
public interface HbzRoleService extends BaseEntityService<HbzRole, HbzRoleDTO> {

    public boolean addUsers(HbzRoleDTO hbzRole, List<HbzUserDTO> users);

    public boolean setUsers(HbzRoleDTO hbzRole, List<HbzUserDTO> users);

    public boolean removeUsers(HbzRoleDTO hbzRole, List<HbzUserDTO> users);

    public boolean setAuths(HbzRoleDTO role, List<HbzAuthDTO> auths);

    public boolean setMenus(HbzRoleDTO role, List<HbzMenuDTO> menus);

}
