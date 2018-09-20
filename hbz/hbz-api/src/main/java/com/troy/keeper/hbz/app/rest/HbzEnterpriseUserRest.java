package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzOrgDTO;
import com.troy.keeper.hbz.dto.HbzRoleDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.service.EntityService;
import com.troy.keeper.hbz.service.HbzOrgService;
import com.troy.keeper.hbz.service.HbzRoleService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.sys.ApplicationContextHolder;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.annotation.Config;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.hbz.type.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2018/1/23.
 */
@RestController
@RequestMapping("/api/enterprise/user")
public class HbzEnterpriseUserRest {

    @Autowired
    EntityService entityService;

    @Config("hbz.user.default.password")
    private String defaultPassword;

    @Autowired
    HbzUserService users;

    @Autowired
    HbzRoleService hbzRoleService;

    @Autowired
    HbzOrgService hbzOrgService;

    @Autowired
    HbzUserService hbzUserService;

    /**
     * @param userInfo
     * @return
     */
    @Label("Web端 - 用户 - 添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseDTO add(@RequestBody HbzUserDTO userInfo) {
        HbzUserDTO current = users.currentUser();
        HbzOrgDTO ent = current.getEnt();

        if (ent == null) return new ResponseDTO(Const.STATUS_ERROR, "你不是企业用户");

        String[] err = ValidationHelper.valid(userInfo, "web_user_add");
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
        if (userInfo.getOrgId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误的机构id，机构id不能为空");
        }

        HbzOrgDTO userOrg = orgService.findById(userInfo.getOrgId());
        HbzOrgDTO userEnt = userOrg;
        while (userEnt.getParent() != null) userEnt = userEnt.getParent();

        if (ent.getId().equals(userEnt.getId())) {
            user.setOrg(userOrg);
            user.setOrgId(userOrg.getId());

            user.setEntId(userEnt.getId());
            user.setEnt(userEnt);

            user.setPassword(defaultPassword);
            user.setStarLevel(3);
            user.setUserStarLevel(3);
            user.setScore(300);
            user.setUserScore(300);
            user.setActivated(true);
            user.setStatus("1");
            user = users.save(user);

            if (userInfo.getRoleIds() != null) {
                if (userInfo.getRoleIds().stream().map(hbzRoleService::findById).filter(role -> !Arrays.asList(
                        Role.EnterpriseAssist,
                        Role.EnterpriseDriver,
                        Role.EnterpriseFinance
                ).contains(role.getRole())).count() > 0) {
                    return new ResponseDTO(Const.STATUS_ERROR, "不能指定助理、司机以外其它角色");
                }
                List<HbzRoleDTO> rs = userInfo.getRoleIds().stream().map(hbzRoleService::findById).filter(role -> Arrays.asList(
                        Role.EnterpriseAssist,
                        Role.EnterpriseDriver,
                        Role.EnterpriseFinance
                ).contains(role.getRole())).collect(Collectors.toList());
                users.setRoles(user, rs);
            }

            return new ResponseDTO(Const.STATUS_OK, "操作成功", MapSpec.mapUserRoleId(user));
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "机构id非法");
        }
    }

    @Label("Web端 - 用户 - 修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseDTO updateUser(@RequestBody HbzUserDTO userInfo) {
        HbzUserDTO current = users.currentUser();
        HbzUserDTO user = users.findById(userInfo.getId());
        if (user == null)
            return new ResponseDTO(Const.STATUS_ERROR, "非法用户id");

        if (current.getEnt().getId().equals(user.getEnt().getId())) {
            if (userInfo.getOrgId() == null)
                return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证错误", Arrays.asList("非法OrgId"));
            HbzOrgDTO org = hbzOrgService.findById(userInfo.getOrgId());
            if (org == null) return new ResponseDTO(Const.STATUS_ERROR, "非法的组织id");
            HbzOrgDTO ent = org;
            if (ent.getParent() != null) ent = ent.getParent();
            if (ent.getId().equals(current.getEnt().getId())) {
                HbzUserDTO existUser = users.findByLogin(userInfo.getLogin());
                if (existUser != null && !existUser.getId().equals(userInfo.getId()))
                    return new ResponseDTO(Const.STATUS_ERROR, "用户名已经存在");

                new Bean2Bean().copyProperties(userInfo, user, true);
                user = users.save(user);

                if (userInfo.getRoleIds() != null) {
                    if (userInfo.getRoleIds().stream().map(hbzRoleService::findById).filter(role -> !Arrays.asList(
                            Role.EnterpriseAssist,
                            Role.EnterpriseDriver,
                            Role.EnterpriseFinance
                    ).contains(role.getRole())).count() > 0) {
                        return new ResponseDTO(Const.STATUS_ERROR, "不能指定助理、司机以外其它角色");
                    }
                    List<HbzRoleDTO> rs = userInfo.getRoleIds().stream().map(hbzRoleService::findById).filter(role -> Arrays.asList(
                            Role.EnterpriseAssist,
                            Role.EnterpriseDriver,
                            Role.EnterpriseFinance
                    ).contains(role.getRole())).collect(Collectors.toList());
                    users.setRoles(user, rs);
                }

                return new ResponseDTO(Const.STATUS_OK, "保存成功", MapSpec.mapUserRoleId(user));
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, "你无法更改此用户，该用户不属于你所在的组织");
            }
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "你所在企业不存在该用户id[" + userInfo.getId() + "]");
        }
    }


    /**
     * @param userInfo
     * @return
     */
    @Label("Web端 - 用户 - 禁用")
    @RequestMapping(value = "/disable", method = RequestMethod.POST)
    public ResponseDTO disableUser(@RequestBody HbzUserDTO userInfo) {
        HbzUserDTO currentUser = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(userInfo, "web_user_disable");
        if (err.length > 0)
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证错误", err);
        //只能操作本部门的用户
        HbzUserDTO user = users.findById(userInfo.getId());
        if (user == null)
            return new ResponseDTO(Const.STATUS_ERROR, "用户标识不存在");
        HbzOrgDTO userEnt = user.getEnt();
        if (userEnt.getId().equals(currentUser.getEnt().getId())) {
            user.setActivated(false);
            users.save(user);
            return new ResponseDTO(Const.STATUS_OK, "成功");
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "你只能操所在企业员工");
        }
    }

    /**
     * @param userInfo
     * @return
     */
    @Label("Web端 - 用户 - 删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseDTO deleteUser(@RequestBody HbzUserDTO userInfo) {
        HbzUserDTO currentUser = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(userInfo, "web_user_delete");
        if (err.length > 0)
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证错误", err);
        HbzUserDTO user = users.findById(userInfo.getId());
        if (user == null)
            return new ResponseDTO(Const.STATUS_ERROR, "用户标识不存在");
        if (user.getEnt().getId().equals(currentUser.getEnt().getId())) {
            users.delete(user);
            return new ResponseDTO(Const.STATUS_OK, "成功");
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "你只能操作本企业员工");
        }
    }

    /**
     * @param userInfo
     * @return
     */
    @Label("Web端 - 用户 - 启用")
    @RequestMapping(value = "/enable", method = RequestMethod.POST)
    public ResponseDTO enable(@RequestBody HbzUserDTO userInfo) {
        String[] err = ValidationHelper.valid(userInfo, "web_user_enable");
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
    @Label("Web端 - 用户 - 重置密码")
    @RequestMapping(value = "/rest", method = RequestMethod.POST)
    public ResponseDTO rest(@RequestBody HbzUserDTO userInfo) {
        String[] err = ValidationHelper.valid(userInfo, "web_user_rest");
        if (err.length > 0)
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证错误", err);
        HbzUserDTO user = users.findById(userInfo.getId());
        if (user == null)
            return new ResponseDTO(Const.STATUS_ERROR, "用户标识不存在");
        user.setPassword(defaultPassword);
        users.save(user);
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }


    /**
     * @param userInfo
     * @return
     */
    @Label("Web端 - 授权管理 - 用户绑定角色")
    @RequestMapping(value = "/makeRoles", method = RequestMethod.POST)
    public ResponseDTO userMakeRoles(@RequestBody HbzUserDTO userInfo) {
        String[] err = ValidationHelper.valid(userInfo, "platform_user_role_link");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证失败", err);
        }
        HbzUserDTO user = users.findById(userInfo.getId());
        if (userInfo.getRoleIds().stream().map(hbzRoleService::findById).filter(role -> !Arrays.asList(Role.EnterpriseAssist, Role.EnterpriseDriver).contains(role.getRole())).count() > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "不能指定助理、司机以外其它角色");
        }
        List<HbzRoleDTO> rs = userInfo.getRoleIds().stream().map(hbzRoleService::findById).filter(role -> Arrays.asList(Role.EnterpriseAssist, Role.EnterpriseDriver).contains(role.getRole())).collect(Collectors.toList());
        users.setRoles(user, rs);
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }

    @Label("App端 - 企业用户 - 所有角色列表")
    @RequestMapping(value = "/availableRole", method = RequestMethod.POST)
    public ResponseDTO availableRoles(@RequestBody HbzUserDTO userInfo) {
        HbzRoleDTO roleQuery = new HbzRoleDTO();
        roleQuery.setStatus("1");
        roleQuery.setRoles(Arrays.asList(
                Role.EnterpriseAssist,
                Role.EnterpriseDriver,
                Role.EnterpriseFinance
        ));
        return new ResponseDTO(Const.STATUS_OK, "成功", hbzRoleService.query(roleQuery).stream().map(MapSpec::mapRole).collect(Collectors.toList()));
    }

    @Label("Web端 - 用户 - 查询接口")
    @RequestMapping(value = {"/query"}, method = RequestMethod.POST)
    public ResponseDTO userQuery(@RequestBody HbzUserDTO queryDTO) {
        HbzUserDTO user = users.currentUser();
        queryDTO.setEntId(user.getEnt().getId());
        queryDTO.setStatus("1");
        return new ResponseDTO(Const.STATUS_OK, "查询成功", users.query(queryDTO).stream().map(MapSpec::mapUserRoleId).collect(Collectors.toList()));
    }

    @Label("App端 - 用户 - 查询分页接口")
    @RequestMapping(value = {"/queryPage"}, method = RequestMethod.POST)
    public ResponseDTO pageQuery(@RequestBody HbzUserDTO queryDTO) {
        HbzUserDTO user = users.currentUser();
        queryDTO.setEntId(user.getEnt().getId());
        queryDTO.setStatus("1");
        return new ResponseDTO(Const.STATUS_OK, "查询成功", users.queryPage(queryDTO, queryDTO.getPageRequest()).map(MapSpec::mapUserRoleId));
    }

    /**
     * 查询用户角色
     *
     * @param roleDTO
     * @return
     */
    @RequestMapping(value = "/queryRoles", method = RequestMethod.POST)
    public ResponseDTO queryRoles(@RequestBody HbzRoleDTO roleDTO) {
        HbzUserDTO user = hbzUserService.currentUser();
        roleDTO.setUserEntId(user.getEntId());
        roleDTO.setStatus("1");
        List<HbzRoleDTO> roles = hbzRoleService.query(roleDTO);
        return new ResponseDTO(Const.STATUS_OK, "成功", roles.stream().map(MapSpec::mapRole).collect(Collectors.toList()));
    }

}
