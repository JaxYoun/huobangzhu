package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.entity.Account;
import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzMenuDTO;
import com.troy.keeper.hbz.dto.HbzRoleDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.po.HbzMenu;
import com.troy.keeper.hbz.po.HbzOrg;
import com.troy.keeper.hbz.po.HbzRole;
import com.troy.keeper.hbz.po.HbzUser;
import com.troy.keeper.hbz.repository.HbzMenuRepository;
import com.troy.keeper.hbz.repository.HbzRoleRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzRoleMapper;
import com.troy.keeper.hbz.service.mapper.HbzUserMapper;
import com.troy.keeper.hbz.sys.QueryBuilder;
import com.troy.keeper.hbz.type.RequireAuthEnum;
import com.troy.keeper.hbz.type.Role;
import com.troy.keeper.hbz.type.WebModule;
import com.troy.keeper.hbz.vo.HbzMenuVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/10/16.
 */
@Service
@Transactional
public class HbzUserServiceImpl extends BaseEntityServiceImpl<HbzUser, HbzUserDTO> implements HbzUserService {

    @Override
    public Long countByLogin(String log) {
        return hbzUserRepository.countByLogin(log);
    }

    @Override
    public Long countByEmail(String e) {
        return hbzUserRepository.countByEmail(e);
    }

    @Override
    public Long countByTelephone(String tel) {
        return hbzUserRepository.countByTelephone(tel);
    }

    @Autowired
    private HbzUserRepository hbzUserRepository;

    @Autowired
    private HbzUserMapper hbzUserMapper;

    @Autowired
    private HbzRoleMapper hbzRoleMapper;

    @Autowired
    private HbzRoleRepository hbzRoleRepository;

    @Autowired
    private HbzMenuRepository hbzMenuRepository;

    @Override
    public BaseMapper<HbzUser, HbzUserDTO> getMapper() {
        return hbzUserMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzUserRepository;
    }

    @Override
    public HbzUserDTO findByLogin(String login) {
        return getMapper().map(hbzUserRepository.findByLogin(login));
    }

    @Override
    public HbzUserDTO currentUser() {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return hbzUserMapper.map(hbzUserRepository.findByLogin(account.getUsername()));
    }

    @Override
    public HbzUserDTO findUser(Long id) {
        HbzUserDTO userId = new HbzUserDTO();
        userId.setId(id);
        return get(userId);
    }

    @Override
    public boolean haveRole(Long userId, Role role) {
        HbzUserDTO queryUser = new HbzUserDTO();
        queryUser.setId(userId);
        HbzUserDTO user = get(queryUser);
        List<HbzRoleDTO> roles = queryRoles(userId);
        Long roleCount = roles.stream().map(HbzRoleDTO::getRole).filter(rol -> rol == role).count();
        return roleCount > 0;
    }

    @Override
    public HbzUserDTO getAdministrator(Long userId) {
        HbzUser user = hbzUserRepository.findOne(userId);
        List<HbzRole> roles = user.getRoles();
        for (HbzRole role : roles) {
            Role roleType = role.getRole();
            if (roleType == Role.EnterpriseAssist) {
                HbzOrg org = user.getEnt();

                Map<String, Object> queryUser = new LinkedHashMap<>();
                queryUser.put("status =", "1");
                queryUser.put("ent.id =", org.getId());
                queryUser.put("roles.role =", Role.EnterpriseAdmin);

                List<HbzUser> users = hbzUserRepository.findAll(QueryBuilder.buildQuery(queryUser));
                if (users == null) {
                    throw new IllegalStateException("用户[" + user.getLogin() + "]没有对应的企业账号");
                } else if (users.size() != 1) {
                    throw new IllegalStateException("用户[" + user.getLogin() + "]含有超过一个企业账号");
                } else {
                    HbzUser ad = users.get(0);
                    return hbzUserMapper.map(ad);
                }
            }
        }
        return hbzUserMapper.map(user);
    }

    @Override
    public boolean addRoles(HbzUserDTO hbzUser, List<HbzRoleDTO> roles) {
        HbzUser hu = hbzUserRepository.findOne(hbzUser.getId());
        if (hu == null) {
            return false;
        } else {
            if (hu.getRoles() == null) {
                hu.setRoles(new ArrayList<>());
            }
            if (roles == null) {
                return true;
            } else {
                hu.getRoles().addAll(roles.stream().map(hbzRoleMapper::map).collect(Collectors.toList()));
                hbzUserRepository.save(hu);
                return true;
            }
        }
    }

    @Override
    public boolean setRoles(HbzUserDTO hbzUser, List<HbzRoleDTO> roles) {
        HbzUser hu = hbzUserRepository.findOne(hbzUser.getId());
        if (hu == null) {
            return false;
        } else {
            if (roles == null || roles.size() == 0) {
                return true;
            } else {
                if (roles.stream().filter(role -> role.getId() == null).count() > 0) {
                    return false;
                } else {
                    hu.setRoles(roles.stream().map(hbzRoleMapper::map).collect(Collectors.toList()));
                    hbzUserRepository.save(hu);
                    return true;
                }
            }
        }
    }

    @Override
    public boolean removeRoles(HbzUserDTO hbzUser, List<HbzRoleDTO> roles) {
        HbzUser hu = hbzUserRepository.findOne(hbzUser.getId());
        if (hu == null) {
            return false;
        } else {
            if (roles == null || roles.size() == 0) {
                return true;
            } else {
                List<HbzRole> roleList = hu.getRoles();
                List<HbzRole> filteredroles = roleList.stream().map(HbzRole::getId).filter(id -> !(roles.stream().map(HbzRoleDTO::getId).collect(Collectors.toList()).contains(id))).map(hbzRoleRepository::findOne).collect(Collectors.toList());
                hu.setRoles(filteredroles);
                hbzUserRepository.save(hu);
                return true;
            }
        }
    }

    @Override
    public List<HbzRoleDTO> queryRoles(Long userId) {
        return hbzUserRepository.findOne(userId).getRoles().stream().map(hbzRoleMapper::map).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getMenuListByUserId(Long userId) {
        Map<String, Object> resultMap = new HashMap<>();
        //查询不需要授权菜单列表
        Map<String, Object> publicQueryParame = new HashMap<>();
        publicQueryParame.put("requireAuth = ", RequireAuthEnum.NO);
        publicQueryParame.put("status = ", Const.STATUS_ENABLED);
        List<HbzMenu> publicMenuList = hbzMenuRepository.findAll(QueryBuilder.buildQuery(publicQueryParame));

        //查询需要授权菜单列表
        List<HbzMenu> menus = null;
        if(userId != null) {
            HbzUser hbzUser = hbzUserRepository.findOne(userId);  //从数据库获取用户详情
            if(hbzUser != null) {
                HbzUserDTO hbzUserDTO = new HbzUserDTO();
                BeanUtils.copyProperties(hbzUser, hbzUserDTO, "password");
                resultMap.put("user", hbzUserDTO);

                List<Long> hbzRoleIdList = hbzUser.getRoles().stream().map(HbzRole::getId).collect(Collectors.toList());
                if (hbzRoleIdList.size() > 0) {
                    Map<String, Object> queryParm = new HashMap<>();
                    queryParm.put("hbzRoleList.id in", hbzRoleIdList);
                    menus = hbzMenuRepository.findAll(QueryBuilder.buildQuery(queryParm));
                    publicMenuList.addAll(menus);
                }
            }
        }

        //转换为VO
        List<HbzMenuVO> hbzMenuVOList = publicMenuList.stream().distinct().map(menu -> {
            HbzMenuVO hbzMenuVO = new HbzMenuVO();
            BeanUtils.copyProperties(menu, hbzMenuVO);
            if (menu.getParent() != null) {
                hbzMenuVO.setParentId(menu.getParent().getId());
            }
            return hbzMenuVO;
        }).collect(Collectors.toList());

        //根据模块编码分组
        List<HbzMenuVO> TransEnterpriseMenuList = new ArrayList<>();
        List<HbzMenuVO> EnterpriseConsignorMenuList = new ArrayList<>();
        List<HbzMenuVO> EnterpriseStorageCenterMenuList = new ArrayList<>();
        List<HbzMenuVO> EnterprisePersonalCenterMenuList = new ArrayList<>();
        hbzMenuVOList.stream().forEach(it -> {
            if (it != null) {
                if (it.getWebModule() == WebModule.CONSIGNOR_ENTERPRISE) {
                    EnterpriseConsignorMenuList.add(it);
                } else if (it.getWebModule() == WebModule.TRANSPORT_ENTERPRISE) {
                    TransEnterpriseMenuList.add(it);
                } else if (it.getWebModule() == WebModule.PERSONAL_CENTER) {
                    EnterprisePersonalCenterMenuList.add(it);
                } else if (it.getWebModule() == WebModule.WAREHOUSE_CENTER) {
                    EnterpriseStorageCenterMenuList.add(it);
                }
            }
        });

        resultMap.put("EnterpriseConsignorMenuList", EnterpriseConsignorMenuList.stream().distinct().collect(Collectors.toList()));
        resultMap.put("EnterpriseDriverMenuList", TransEnterpriseMenuList.stream().distinct().collect(Collectors.toList()));
        resultMap.put("EnterprisePersonalCenterMenuList", EnterprisePersonalCenterMenuList.stream().distinct().collect(Collectors.toList()));
        resultMap.put("EnterpriseStorageCenterMenuList", EnterpriseStorageCenterMenuList.stream().distinct().collect(Collectors.toList()));
        return resultMap;
    }

    /**
     * 检查电话号码是否可用
     *
     * @param phoneNo
     * @return
     */
    @Override
    public boolean checkActiveUserPhone(String phoneNo) {
        return this.hbzUserRepository.getHbzUserByActivatedAndStatusAndTelephone(true, Const.STATUS_ENABLED, phoneNo).size() == 1;
    }

    /**
     * 修改个人信息
     *
     * @param id
     * @param hbzUserDTO
     * @return
     */
    @Override
    public boolean updateHbzUser(Long id, HbzUserDTO hbzUserDTO) {
        HbzUser hbzUser = hbzUserRepository.findOne(id);
        hbzUser.setImageUrl(hbzUserDTO.getImageUrl());
        return hbzUserRepository.save(hbzUser) != null;
    }

    public static void main(String[] args) {
        List<String> stringList0 = new ArrayList<>();
        stringList0.add("0");
        stringList0.add("00");
        List<String> stringList1 = new ArrayList<>();
        stringList1.add("1");
        stringList1.add("11");
        stringList0.addAll(stringList1);
        System.out.println(stringList0);
    }
}