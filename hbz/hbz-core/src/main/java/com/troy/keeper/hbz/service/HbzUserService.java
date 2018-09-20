package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzRoleDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.po.HbzUser;
import com.troy.keeper.hbz.type.Role;

import java.util.List;
import java.util.Map;

/**
 * Created by leecheng on 2017/10/13.
 */
public interface HbzUserService extends BaseEntityService<HbzUser, HbzUserDTO> {

    public Long countByLogin(String log);

    public Long countByEmail(String e);

    public Long countByTelephone(String tel);

    public HbzUserDTO currentUser();

    public HbzUserDTO getAdministrator(Long userId);

    public HbzUserDTO findUser(Long id);

    public HbzUserDTO findByLogin(String login);

    public boolean addRoles(HbzUserDTO hbzUser, List<HbzRoleDTO> roles);

    public boolean setRoles(HbzUserDTO hbzUser, List<HbzRoleDTO> roles);

    public boolean removeRoles(HbzUserDTO hbzUser, List<HbzRoleDTO> roles);

    public List<HbzRoleDTO> queryRoles(Long userId);

    public boolean haveRole(Long userId, Role role);

    /**
     * 根据用户id、角色类型获取用户菜单
     *
     * @param id
     * @return
     */
    Map<String, Object> getMenuListByUserId(Long id);

    /**
     * 检查电话号码是否可用
     *
     * @param phoneNo
     * @return
     */
    boolean checkActiveUserPhone(String phoneNo);

    /**
     * 修改个人信息
     *
     * @param id
     * @param hbzUserDTO
     * @return
     */
    boolean updateHbzUser(Long id, HbzUserDTO hbzUserDTO);
}
