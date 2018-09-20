package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzAuthDTO;
import com.troy.keeper.hbz.dto.HbzMenuDTO;
import com.troy.keeper.hbz.dto.HbzRoleDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.po.HbzAuth;
import com.troy.keeper.hbz.po.HbzMenu;
import com.troy.keeper.hbz.po.HbzRole;
import com.troy.keeper.hbz.po.HbzUser;
import com.troy.keeper.hbz.repository.HbzAuthRepository;
import com.troy.keeper.hbz.repository.HbzMenuRepository;
import com.troy.keeper.hbz.repository.HbzRoleRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.service.HbzRoleService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzRoleMapper;
import com.troy.keeper.hbz.service.mapper.HbzUserMapper;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/10/16.
 */
@CommonsLog
@Service
@Transactional
public class HbzRoleServiceImpl extends BaseEntityServiceImpl<HbzRole, HbzRoleDTO> implements HbzRoleService {

    @Autowired
    HbzMenuRepository hbzMenuRepository;

    @Autowired
    private HbzRoleRepository hbzRoleRepository;

    @Autowired
    private HbzRoleMapper hbzRoleMapper;

    @Autowired
    private HbzUserMapper hbzUserMapper;

    @Autowired
    private HbzUserRepository hbzUserRepository;

    @Autowired
    HbzAuthRepository hbzAuthRepository;

    @Override
    public BaseMapper<HbzRole, HbzRoleDTO> getMapper() {
        return hbzRoleMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzRoleRepository;
    }


    @Override
    public boolean addUsers(HbzRoleDTO hbzRole, List<HbzUserDTO> users) {
        HbzRole hr = hbzRoleRepository.findOne(hbzRole.getId());
        if (hr == null) {
            return false;
        } else {
            if (hr.getUsers() == null) {
                hr.setUsers(new ArrayList<>());
            }
            if (users == null) {
                return true;
            } else {
                hr.getUsers().addAll(users.stream().map(hbzUserMapper::map).collect(Collectors.toList()));
                hbzRoleRepository.save(hr);
                return true;
            }
        }
    }

    @Override
    public boolean setUsers(HbzRoleDTO hbzRole, List<HbzUserDTO> users) {
        HbzRole hr = hbzRoleRepository.findOne(hbzRole.getId());
        if (hr == null) {
            return false;
        } else {
            if (users == null || users.size() == 0) {
                return true;
            } else {
                if (users.stream().filter(user -> user.getId() == null).count() > 0) {
                    return false;
                } else {
                    hr.setUsers(users.stream().map(hbzUserMapper::map).collect(Collectors.toList()));
                    hbzRoleRepository.save(hr);
                    return true;
                }
            }
        }
    }

    @Override
    public boolean removeUsers(HbzRoleDTO hbzRole, List<HbzUserDTO> users) {
        HbzRole hr = hbzRoleRepository.findOne(hbzRole.getId());
        if (hr == null) {
            return false;
        } else {
            if (users == null || users.size() == 0) {
                return true;
            } else {
                List<HbzUser> userList = hr.getUsers();
                List<HbzUser> filteredusers = userList.stream().map(HbzUser::getId).filter(id -> !(users.stream().map(HbzUserDTO::getId).collect(Collectors.toList()).contains(id))).map(hbzUserRepository::findOne).collect(Collectors.toList());
                hr.setUsers(filteredusers);
                hbzRoleRepository.save(hr);
                return true;
            }
        }
    }

    @Override
    public boolean setAuths(@NotNull HbzRoleDTO role, @NotNull List<HbzAuthDTO> auths) {
        try {
            HbzRole roleE = hbzRoleRepository.findOne(role.getId());
            List<HbzAuth> authsE = auths.stream().map(HbzAuthDTO::getId).map(hbzAuthRepository::findOne).collect(Collectors.toList());
            roleE.setAuths(authsE);
            hbzRoleRepository.save(roleE);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean setMenus(HbzRoleDTO role, List<HbzMenuDTO> menus) {
        try {
            HbzRole role1 = hbzRoleRepository.findOne(role.getId());
            List<HbzMenu> menus1 = menus.stream().map(HbzMenuDTO::getId).map(hbzMenuRepository::findOne).collect(Collectors.toList());
            role1.setMenus(menus1);
            hbzRoleRepository.save(role1);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
