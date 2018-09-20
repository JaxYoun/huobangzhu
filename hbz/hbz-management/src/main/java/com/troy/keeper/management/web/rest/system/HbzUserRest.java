package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzOrgDTO;
import com.troy.keeper.hbz.dto.HbzRoleDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.dto.HbzUserRegistryDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.po.HbzUserRegistry;
import com.troy.keeper.hbz.service.EntityService;
import com.troy.keeper.hbz.service.HbzOrgService;
import com.troy.keeper.hbz.service.HbzRoleService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.service.mapper.HbzUserRegistryMapper;
import com.troy.keeper.hbz.sys.ApplicationContextHolder;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.annotation.Config;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.management.utils.MapSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/12/29.
 */
@RestController
@RequestMapping("/api/user")
public class HbzUserRest {

    @Autowired
    EntityService entityService;

    @Config("hbz.user.default.password")
    private String defaultPassword;

    @Autowired
    HbzUserService users;

    @Autowired
    HbzRoleService hbzRoleService;

    /**
     * @param userInfo
     * @return
     */
    @Label("管理端 - 用户 - 添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseDTO addUser(@RequestBody HbzUserDTO userInfo) {
        String[] err = ValidationHelper.valid(userInfo, "platform_user_add");
        if (err.length > 0)
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证错误", err);
        HbzUserDTO user = new HbzUserDTO();
        HbzUserDTO existUser = users.findByLogin(userInfo.getLogin());
        if (existUser != null)
            return new ResponseDTO(Const.STATUS_ERROR, "用户已经存在");

        if (users.countByTelephone(userInfo.getTelephone()) > 0)
            return new ResponseDTO(Const.STATUS_ERROR, "手机号已被注册");

        if (StringHelper.notNullAndEmpty(userInfo.getEmail()))
            if (users.countByEmail(userInfo.getEmail()) > 0)
                return new ResponseDTO(Const.STATUS_ERROR, "邮件已经存在");

        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(userInfo, user, true);

        HbzOrgService orgService = ApplicationContextHolder.getService(HbzOrgService.class);
        if (userInfo.getOrgId() != null) {
            HbzOrgDTO userOrg = orgService.findById(userInfo.getOrgId());
            if (userOrg == null) return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证错误", "错误的机构ID");
            user.setOrg(userOrg);
            user.setOrgId(userOrg.getId());
            while (userOrg.getParent() != null) {
                userOrg = userOrg.getParent();
            }
            user.setEntId(userOrg.getId());
            user.setEnt(userOrg);
        }

        user.setPassword(defaultPassword);
        user.setActivated(true);
        user.setScore(300);
        user.setUserScore(300);
        user.setStarLevel(3);
        user.setUserStarLevel(3);
        user.setStatus("1");
        users.save(user);
        return new ResponseDTO(Const.STATUS_OK, "添加用户成功");
    }

    @Label("管理端 - 用户 - 修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseDTO updateUser(@RequestBody HbzUserDTO userInfo) {

        HbzUserDTO user = users.findById(userInfo.getId());
        if (user == null)
            return new ResponseDTO(Const.STATUS_ERROR, "非法用户id");

        HbzUserDTO existUser = users.findByLogin(userInfo.getLogin());
        if (existUser != null && !existUser.getId().equals(userInfo.getId()))
            return new ResponseDTO(Const.STATUS_ERROR, "用户已经存在");

        new Bean2Bean().copyProperties(userInfo, user, true);
        /**
         user.setLogin(userInfo.getLogin());
         if (StringHelper.notNullAndEmpty(user.getPassword()))
         user.setPassword(userInfo.getPassword());
         user.setEmail(userInfo.getEmail());
         user.setNickName(userInfo.getNickName());
         user.setSex(userInfo.getSex());
         */

        users.save(user);
        return new ResponseDTO(Const.STATUS_OK, "保存成功");
    }


    /**
     * @param userInfo
     * @return
     */
    @Label("管理端 - 用户 - 禁用")
    @RequestMapping(value = "/disable", method = RequestMethod.POST)
    public ResponseDTO disableUser(@RequestBody HbzUserDTO userInfo) {
        String[] err = ValidationHelper.valid(userInfo, "platform_user_disable");
        if (err.length > 0)
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证错误", err);

        HbzUserDTO user = users.findById(userInfo.getId());
        if (user == null)
            return new ResponseDTO(Const.STATUS_ERROR, "用户标识不存在");

        user.setActivated(false);
        users.save(user);
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }


    /**
     * @param userInfo
     * @return
     */
    @Label("管理端 - 用户 - 启用")
    @RequestMapping(value = "/enable", method = RequestMethod.POST)
    public ResponseDTO enable(@RequestBody HbzUserDTO userInfo) {
        String[] err = ValidationHelper.valid(userInfo, "platform_user_enable");
        if (err.length > 0)
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证错误", err);

        HbzUserDTO user = users.findById(userInfo.getId());
        if (user == null)
            return new ResponseDTO(Const.STATUS_ERROR, "用户标识不存在");

        user.setActivated(true);
        users.save(user);
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }

    /**
     * @param userInfo
     * @return
     */
    @Label("管理端 - 授权管理 - 用户绑定角色")
    @RequestMapping(value = "/makeRoles", method = RequestMethod.POST)
    public ResponseDTO userMakeRoles(@RequestBody HbzUserDTO userInfo) {
        String[] err = ValidationHelper.valid(userInfo, "platform_user_role_link");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证失败", err);
        }
        HbzUserDTO user = users.findById(userInfo.getId());
        List<HbzRoleDTO> rs = userInfo.getRoleIds().stream().map(hbzRoleService::findById).collect(Collectors.toList());
        users.setRoles(user, rs);
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }


    @Label("管理端 - 用户 - 查询接口")
    @RequestMapping(value = {"/query"}, method = RequestMethod.POST)
    public ResponseDTO userQuery(@RequestBody HbzUserDTO queryDTO) {
        queryDTO.setStatus("1");
        return new ResponseDTO(Const.STATUS_OK, "查询成功", users.query(queryDTO).stream().map(user -> {
            Map<String, Object> userMap = MapSpec.mapUser(user);
            Map<String, Object> r = new HashMap<>();
            r.put("status", "1");
            r.put("user.id", user.getId());
            HbzUserRegistryMapper hbzUserRegistryMapper = ApplicationContextHolder.getService(HbzUserRegistryMapper.class);
            List<String> columns = Arrays.asList("id", "registryCode", "registryProgress");
            List<HbzUserRegistryDTO> registries = entityService.query(HbzUserRegistry.class, hbzUserRegistryMapper, r, columns);
            userMap.put("registries", registries.stream().map(MapSpec::mapRegistry).collect(Collectors.toList()));
            userMap.put("activated", user.getActivated());
            return userMap;
        }).collect(Collectors.toList()));
    }

    @Label("管理端 - 用户 - 查询分页接口")
    @RequestMapping(value = {"/queryPage"}, method = RequestMethod.POST)
    public ResponseDTO pageQuery(@RequestBody HbzUserDTO queryDTO) {
        queryDTO.setStatus("1");
        return new ResponseDTO(Const.STATUS_OK, "查询成功", users.queryPage(queryDTO, queryDTO.getPageRequest()).map(user -> {
            Map<String, Object> userMap = MapSpec.mapUser(user);
            Map<String, Object> r = new HashMap<>();
            r.put("status", "1");
            r.put("user.id", user.getId());
            HbzUserRegistryMapper hbzUserRegistryMapper = ApplicationContextHolder.getService(HbzUserRegistryMapper.class);
            List<String> columns = Arrays.asList("id", "registryCode", "registryProgress");
            List<HbzUserRegistryDTO> registries = entityService.query(HbzUserRegistry.class, hbzUserRegistryMapper, r, columns);
            userMap.put("registries", registries.stream().map(MapSpec::mapRegistry).collect(Collectors.toList()));
            userMap.put("activated", user.getActivated());
            return userMap;
        }));
    }

}
