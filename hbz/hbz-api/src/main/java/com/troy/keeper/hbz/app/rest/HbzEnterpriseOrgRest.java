package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzOrgDTO;
import com.troy.keeper.hbz.dto.HbzRoleDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.service.HbzOrgService;
import com.troy.keeper.hbz.service.HbzRoleService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.hbz.type.OrgType;
import com.troy.keeper.hbz.type.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2018/1/24.
 */
@RestController
@RequestMapping("/api/enterprise/org")
public class HbzEnterpriseOrgRest {

    @Autowired
    HbzUserService hbzUserService;

    @Autowired
    HbzRoleService hbzRoleService;

    @Autowired
    HbzOrgService hbzOrgService;

    /**
     * @param orgInfo
     * @return
     */
    @Label("Web端 - 机构 - 添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseDTO add(@RequestBody HbzOrgDTO orgInfo) {
        String[] err = ValidationHelper.valid(orgInfo, "org_add");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证失败", err);
        }
        //先验证角色是否含有企业管理员
        HbzUserDTO currentUser = hbzUserService.currentUser();
        HbzRoleDTO query = new HbzRoleDTO();
        query.setUserId(currentUser.getId());
        query.setRole(Role.EnterpriseAdmin);
        query.setStatus("1");
        List<HbzRoleDTO> adminRole = hbzRoleService.query(query);
        if (adminRole != null && adminRole.size() > 0) {
            //添加顶级机构
            if (orgInfo.getParentId() == null) {
                //先检查该用户是否含有顶级机构
                if (currentUser.getEnt() != null || currentUser.getOrg() != null) {
                    return new ResponseDTO(Const.STATUS_ERROR, "你已经有所属的顶级机构，无法再次添加顶级机构，但是可以修改");
                } else {
                    HbzOrgDTO enterprise = new HbzOrgDTO();
                    new Bean2Bean().copyProperties(orgInfo, enterprise);
                    enterprise.setOrgType(OrgType.Enterprise);
                    enterprise.setStatus("1");
                    enterprise = hbzOrgService.save(enterprise);
                    currentUser.setEnt(enterprise);
                    currentUser.setEntId(enterprise.getId());
                    currentUser.setOrg(enterprise);
                    currentUser.setOrgId(enterprise.getId());
                    hbzUserService.save(currentUser);
                    return new ResponseDTO(Const.STATUS_OK, "操作成功", MapSpec.mapOrg(enterprise));
                }
            } else {
                HbzOrgDTO org = hbzOrgService.findById(orgInfo.getParentId());
                if (org == null) {
                    return new ResponseDTO(Const.STATUS_ERROR, "不存在的所属机构id");
                } else {
                    HbzOrgDTO ent = org;
                    while (ent.getParent() != null) {
                        ent = ent.getParent();
                    }
                    if (currentUser.getEnt() == null || currentUser.getOrg() == null) {
                        return new ResponseDTO(Const.STATUS_ERROR, "对不起，你还没有顶级组织机构");
                    } else {
                        if (ent.getId().equals(currentUser.getEnt().getId())) {
                            HbzOrgDTO newOrg = new HbzOrgDTO();
                            new Bean2Bean().copyProperties(orgInfo, newOrg);
                            newOrg.setOrgType(OrgType.EnDepartment);
                            newOrg.setStatus("1");
                            newOrg = hbzOrgService.save(newOrg);
                            return new ResponseDTO(Const.STATUS_OK, "操作成功", MapSpec.mapOrg(newOrg));
                        } else {
                            return new ResponseDTO(Const.STATUS_ERROR, "对不起，你只能添加你所有企业的部门");
                        }
                    }
                }
            }
        } else {
            return new ResponseDTO(Const.STATUS_OK, "对不起，你不是企业管理员，无法进行此操作");
        }
    }

    @Label("Web端 - 机构 - 更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseDTO update(@RequestBody HbzOrgDTO orgInfo) {
        String[] err = ValidationHelper.valid(orgInfo, "org_update");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证失败", err);
        }
        //先验证角色是否含有企业管理员
        HbzUserDTO currentUser = hbzUserService.currentUser();
        HbzRoleDTO query = new HbzRoleDTO();
        query.setUserId(currentUser.getId());
        query.setRole(Role.EnterpriseAdmin);
        query.setStatus("1");
        List<HbzRoleDTO> adminRole = hbzRoleService.query(query);
        if (adminRole != null && adminRole.size() > 0) {
            if (orgInfo.getParentId() != null) {
                HbzOrgDTO inputParentOrg = hbzOrgService.findById(orgInfo.getParentId());
                HbzOrgDTO inputParentEnt = inputParentOrg;
                HbzOrgDTO inputOrg = hbzOrgService.findById(orgInfo.getId());
                HbzOrgDTO inputEnt = inputOrg;
                while (inputEnt.getParentId() != null) inputEnt = inputEnt.getParent();
                while (inputParentEnt.getParent() != null) inputParentEnt = inputParentEnt.getParent();
                if (currentUser.getEnt() == null || currentUser.getOrg() == null) {
                    return new ResponseDTO(Const.STATUS_ERROR, "用户数据非法,没有所属机构");
                } else {
                    if (currentUser.getEnt().getId().equals(inputParentEnt.getId())) {
                        if (currentUser.getEnt().getId().equals(inputEnt.getId())) {
                            new Bean2Bean().copyProperties(orgInfo, inputOrg, true);
                            inputOrg = hbzOrgService.save(inputOrg);
                            return new ResponseDTO(Const.STATUS_OK, "成功", MapSpec.mapOrg(inputOrg));
                        } else {
                            return new ResponseDTO(Const.STATUS_ERROR, "不能更改其它公司部门");
                        }
                    } else {
                        return new ResponseDTO(Const.STATUS_ERROR, "无法把当前部门修改为非法的部门id");
                    }
                }
            } else {
                HbzOrgDTO inputOrg = hbzOrgService.findById(orgInfo.getId());
                HbzOrgDTO inputEnt = inputOrg;
                while (inputEnt.getParentId() != null) inputEnt = inputEnt.getParent();
                if (inputOrg.getParent() == null) {
                    if (currentUser.getEnt().getId().equals(inputOrg.getId())) {
                        new Bean2Bean().copyProperties(orgInfo, inputOrg, true);
                        inputOrg = hbzOrgService.save(inputOrg);
                        return new ResponseDTO(Const.STATUS_OK, "成功", MapSpec.mapOrg(inputOrg));
                    } else {
                        return new ResponseDTO(Const.STATUS_ERROR, "不能更改其它公司部门");
                    }
                } else {
                    return new ResponseDTO(Const.STATUS_ERROR, "无法把子部门移为顶级部门");
                }
            }
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "你不是企业管理员");
        }
    }

    @Label("Web端 - 机构 - 删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseDTO delete(@RequestBody HbzOrgDTO orgInfo) {
        HbzOrgDTO inputOrg = hbzOrgService.findById(orgInfo.getId());
        hbzOrgService.delete(inputOrg);
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }

    @Label("Web端 - 机构 - 查询")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseDTO query(@RequestBody HbzOrgDTO queryInfo) {
        HbzOrgDTO query = new HbzOrgDTO();
        new Bean2Bean().copyProperties(queryInfo, query, true);
        if (query.getParentId() != null) {
            HbzOrgDTO p = hbzOrgService.findById(query.getParentId());
            while (p.getParent() != null) p = p.getParent();
            if (!hbzUserService.currentUser().getEnt().getId().equals(p.getId())) {
                return new ResponseDTO(Const.STATUS_ERROR, "parentId非法");
            }
        }
        query.setStatus("1");
        List<HbzOrgDTO> orgs = hbzOrgService.query(query);
        return new ResponseDTO(Const.STATUS_OK, "成功", orgs.stream().map(MapSpec::mapOrg).collect(Collectors.toList()));
    }

    @Label("Web端 - 机构 - 分页查询")
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public ResponseDTO page(@RequestBody HbzOrgDTO queryInfo) {
        HbzOrgDTO query = new HbzOrgDTO();
        new Bean2Bean().copyProperties(queryInfo, query, true);
        if (query.getParentId() != null) {
            HbzOrgDTO p = hbzOrgService.findById(query.getParentId());
            while (p.getParent() != null) p = p.getParent();
            if (!hbzUserService.currentUser().getEnt().getId().equals(p.getId())) {
                return new ResponseDTO(Const.STATUS_ERROR, "parentId非法");
            }
        }
        query.setStatus("1");
        Page<HbzOrgDTO> orgs = hbzOrgService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "成功", orgs.map(MapSpec::mapOrg));
    }

    @Label("App端 - 企业组织机构 - 组织机构树")
    @RequestMapping(value = {"/all"}, method = RequestMethod.POST)
    public ResponseDTO all(@RequestBody HbzOrgDTO hbzOrgDTO) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzUserDTO admin = hbzUserService.getAdministrator(user.getId());
        HbzOrgDTO ent = admin.getEnt();

        Map<String, Object> map = MapSpec.mapOrgWithoutParent(ent);

        Map<String, Object> querySubOrgs = new HashMap<>();
        querySubOrgs.put("status", Const.STATUS_ENABLED);
        querySubOrgs.put("parent.id", ent.getId());
        List<HbzOrgDTO> sub = hbzOrgService.query(querySubOrgs);
        if (sub != null && !(sub.size() == 0)) {
            map.put("subs", put(sub));
        }

        return new ResponseDTO(Const.STATUS_OK, "操作成功", map);
    }

    private List<Map<String, Object>> put(List<HbzOrgDTO> orgs) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < orgs.size(); i++) {
            HbzOrgDTO org = orgs.get(i);
            Map<String, Object> map = MapSpec.mapOrgWithoutParent(org);

            Map<String, Object> querySubOrgs = new HashMap<>();
            querySubOrgs.put("status", Const.STATUS_ENABLED);
            querySubOrgs.put("parent.id", org.getId());
            List<HbzOrgDTO> sub = hbzOrgService.query(querySubOrgs);
            if (sub != null && !(sub.size() == 0)) {
                map.put("subs", put(sub));
            }
            list.add(map);
        }
        return list;
    }


    @Label("App端 - 企业组织机构 - 组织机构树V1")
    @RequestMapping(value = {"/tree"}, method = RequestMethod.POST)
    public ResponseDTO tree(@RequestBody HbzOrgDTO hbzOrgDTO) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzUserDTO admin = hbzUserService.getAdministrator(user.getId());
        HbzOrgDTO ent = admin.getEnt();

        Map<String, Object> map = MapSpec.mapOrgWithoutParent(ent);
        map.put("key", ent.getId());
        Map<String, Object> querySubOrgs = new HashMap<>();
        querySubOrgs.put("status", Const.STATUS_ENABLED);
        querySubOrgs.put("parent.id", ent.getId());
        List<HbzOrgDTO> sub = hbzOrgService.query(querySubOrgs);
        if (sub != null && !(sub.size() == 0)) {
            map.put("children", set(sub));
        }

        return new ResponseDTO(Const.STATUS_OK, "操作成功", map);
    }

    private List<Map<String, Object>> set(List<HbzOrgDTO> orgs) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < orgs.size(); i++) {
            HbzOrgDTO org = orgs.get(i);
            Map<String, Object> map = MapSpec.mapOrgWithoutParent(org);
            map.put("key", org.getId());
            Map<String, Object> querySubOrgs = new HashMap<>();
            querySubOrgs.put("status", Const.STATUS_ENABLED);
            querySubOrgs.put("parent.id", org.getId());
            List<HbzOrgDTO> sub = hbzOrgService.query(querySubOrgs);
            if (sub != null && !(sub.size() == 0)) {
                map.put("children", set(sub));
            }
            list.add(map);
        }
        return list;
    }
}
