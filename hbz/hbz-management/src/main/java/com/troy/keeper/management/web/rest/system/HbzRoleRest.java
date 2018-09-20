package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzRoleDTO;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.service.HbzAuthService;
import com.troy.keeper.hbz.service.HbzMenuService;
import com.troy.keeper.hbz.service.HbzRoleService;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.management.utils.MapSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * Created by leecheng on 2018/1/5.
 */
@RestController
@RequestMapping("/api/role")
public class HbzRoleRest {

    @Autowired
    HbzRoleService roles;
    @Autowired
    HbzAuthService hbzAuthService;
    @Autowired
    HbzMenuService menus;

    /**
     * @param roleDTO
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseDTO addRole(@RequestBody HbzRoleDTO roleDTO) {
        String[] err = ValidationHelper.valid(roleDTO, "role_add");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证失败", err);
        }
        HbzRoleDTO role = new HbzRoleDTO();
        new Bean2Bean().copyProperties(roleDTO, role);
        role.setStatus("1");
        roles.save(role);
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }

    /**
     * @param roleDTO
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseDTO update(@RequestBody HbzRoleDTO roleDTO) {
        HbzRoleDTO role = roles.findById(roleDTO.getId());
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(roleDTO, role, true);
        roles.save(role);
        role.setStatus("1");
        roles.save(role);
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }


    /**
     * @param roleDTO
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseDTO delete(@RequestBody HbzRoleDTO roleDTO) {
        String[] err = ValidationHelper.valid(roleDTO, "role_r");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证失败", err);
        }
        HbzRoleDTO role = roles.findById(roleDTO.getId());
        roles.delete(role);
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }

    /**
     * 查询角色
     *
     * @param roleDTO
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseDTO query(@RequestBody HbzRoleDTO roleDTO) {
        roleDTO.setStatus("1");
        return new ResponseDTO(Const.STATUS_OK, "成功", roles.query(roleDTO).stream().map(MapSpec::mapRole).collect(Collectors.toList()));
    }

    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public ResponseDTO pageQuery(@RequestBody HbzRoleDTO roleDTO) {
        roleDTO.setStatus("1");
        return new ResponseDTO(Const.STATUS_OK, "成功", roles.queryPage(roleDTO, roleDTO.getPageRequest()).map(MapSpec::mapRole));
    }

    /**
     * 绑定权限
     *
     * @param roleDTO
     * @return
     */
    @RequestMapping(value = "/makeAuthorities", method = RequestMethod.POST)
    public ResponseDTO auths(@RequestBody HbzRoleDTO roleDTO) {
        String[] err = ValidationHelper.valid(roleDTO, "role_auth");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证失败", err);
        }
        HbzRoleDTO role = roles.findById(roleDTO.getId());
        roles.setAuths(role, roleDTO.getAuthIds().stream().map(hbzAuthService::findById).collect(Collectors.toList()));
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }


    /**
     * 绑定菜单
     *
     * @param roleDTO
     * @return
     */
    @RequestMapping(value = "/makeMenus", method = RequestMethod.POST)
    public ResponseDTO menus(@RequestBody HbzRoleDTO roleDTO) {
        String[] err = ValidationHelper.valid(roleDTO, "role_menu");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证失败", err);
        }
        HbzRoleDTO role = roles.findById(roleDTO.getId());
        roles.setMenus(role, roleDTO.getMenuIds().stream().map(menus::findById).collect(Collectors.toList()));
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }
}
