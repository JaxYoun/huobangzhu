package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.*;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.po.HbzUserRegistry;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.service.mapper.HbzUserRegistryMapper;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.TimeMillisFormat;
import com.troy.keeper.hbz.type.RegistryCode;
import com.troy.keeper.hbz.type.RegistryProgress;
import com.troy.keeper.hbz.type.Role;
import com.troy.keeper.management.dto.*;
import com.troy.keeper.hbz.service.SitePushMessageService;
import com.troy.keeper.management.utils.CommonConstants;
import com.troy.keeper.management.utils.MapSpec;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2018/1/4.
 */
@RestController
@RequestMapping("/api/user/registry")
public class HbzUserRegRest {

    @Autowired
    HbzUserRegistryService hbzUserRegistryService;

    @Autowired
    HbzTransEnterpriseRegistryService hbzTransEnterpriseRegistryService;

    @Autowired
    HbzPersonConsignorRegistryService hbzPersonConsignorRegistryService;

    @Autowired
    HbzPersonDriverRegistryService hbzPersonDriverRegistryService;

    @Autowired
    HbzDeliveryBoyRegistrySerivce hbzDeliveryBoyRegistrySerivce;

    @Autowired
    HbzEnterpriseConsignorRegistryService hbzEnterpriseConsignorRegistryService;

    @Autowired
    EntityService entityService;

    @Autowired
    HbzUserRegistryMapper hbzUserRegistryMapper;

    @Autowired
    HbzRoleService hbzRoleService;

    @Autowired
    HbzUserService hbzUserService;

    @Autowired
    private HbzOrgService hbzOrgService;

    @Autowired
    SitePushMessageService sitePushMessageService;

    /**
     * 待认证列表
     *
     * @param registryDTO
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseDTO queryList(@RequestBody HbzUserRegistryMapDTO registryDTO) {
        HbzUserRegistryDTO q = new HbzUserRegistryDTO();
        new Bean2Bean()
                .addPropMapper(
                        new PropertyMapper<>("owerCreateTime", new TimeMillisFormat("yyyy-MM-dd")::parse)
                ).copyProperties(registryDTO, q);
        q.setStatus("1");
        List<HbzUserRegistryDTO> r = entityService.query(HbzUserRegistry.class, hbzUserRegistryMapper, q, Arrays.asList("id", "createdDate", "certificateNo", "certificateType", "registryCode", "registryProgress", "owerName", "user"));
        List<Map<String, Object>> show = r.stream().map(MapSpec::mapRegistry).collect(Collectors.toList());
        return new ResponseDTO(Const.STATUS_OK, "成功", show);
    }

    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public ResponseDTO queryPageList(@RequestBody HbzUserRegistryMapDTO registryDTO) {
        HbzUserRegistryDTO q = new HbzUserRegistryDTO();
        new Bean2Bean()
                .addPropMapper(
                        new PropertyMapper<>("owerCreateTime", new TimeMillisFormat("yyyy-MM-dd")::parse)
                ).copyProperties(registryDTO, q);
        q.setStatus("1");
        Page<HbzUserRegistryDTO> r = entityService.queryPage(HbzUserRegistry.class, hbzUserRegistryMapper, q.getPageRequest(), q, Arrays.asList("id", "createdDate", "certificateNo", "certificateType", "registryCode", "registryProgress", "owerName", "user"));
        Page<Map<String, Object>> show = r.map(MapSpec::mapRegistry);
        return new ResponseDTO(Const.STATUS_OK, "成功", show);
    }

    /**
     * 根据id获取详情
     *
     * @param registryDTO
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ResponseDTO ig(@RequestBody HbzUserRegistryMapDTO registryDTO) {
        HbzUserRegistryDTO registry = hbzUserRegistryService.findById(registryDTO.getId());
        Map map = new LinkedMap();
        switch (registry.getRegistryCode()) {
            case TransEnterprise: {
                map = MapSpec.mapTransEnterprise(hbzTransEnterpriseRegistryService.findById(registry.getId()));
            }
            break;
            case PersonConsignor: {
                map = MapSpec.mapPersonConsignor(hbzPersonConsignorRegistryService.findById(registry.getId()));
            }
            break;
            case PersonDriver: {
                map = MapSpec.mapPersonDriverRegistry(hbzPersonDriverRegistryService.findById(registry.getId()));
            }
            break;
            case DeliveryBoy: {
                map = MapSpec.mapA(hbzDeliveryBoyRegistrySerivce.findById(registry.getId()));
            }
            break;
            case EnterpriseConsignor: {
                map = MapSpec.mapEnterpriseConsignor(hbzEnterpriseConsignorRegistryService.findById(registry.getId()));
            }
            break;
        }
        return new ResponseDTO(Const.STATUS_OK, "成功", map);
    }

    /**
     * 保存
     *
     * @param registryDTO
     * @return
     */
    @RequestMapping(value = "/saveTransEnterprise", method = RequestMethod.POST)
    public ResponseDTO put(@RequestBody HbzTransEnterpriseRegistryMAPDTO registryDTO) {
        HbzTransEnterpriseRegistryDTO registry = hbzTransEnterpriseRegistryService.findById(registryDTO.getId());
        new Bean2Bean(
                new PropertyMapper<>("owerCreateTime", new TimeMillisFormat("yyyy-MM-dd")::parse)
        ).addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(registryDTO, registry);
        registry.setStatus("1");
        hbzTransEnterpriseRegistryService.save(registry);
        return new ResponseDTO(Const.STATUS_OK, "保存成功");
    }

    /**
     * 保存
     *
     * @param registryDTO
     * @return
     */
    @RequestMapping(value = "/saveDeliveryBoy", method = RequestMethod.POST)
    public ResponseDTO put(@RequestBody HbzDeliveryBoyRegistryMapDTO registryDTO) {
        HbzDeliveryBoyRegistryDTO registry = hbzDeliveryBoyRegistrySerivce.findById(registryDTO.getId());
        new Bean2Bean(
                new PropertyMapper<>("owerCreateTime", new TimeMillisFormat("yyyy-MM-dd")::parse)
        ).addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(registryDTO, registry);
        registry.setStatus("1");
        hbzDeliveryBoyRegistrySerivce.save(registry);
        return new ResponseDTO(Const.STATUS_OK, "保存成功");
    }

    /**
     * 保存
     *
     * @param registryDTO
     * @return
     */
    @RequestMapping(value = "/savePersonConsignor", method = RequestMethod.POST)
    public ResponseDTO put(@RequestBody HbzPersonConsignorRegistryMapDTO registryDTO) {
        HbzPersonConsignorRegistryDTO registry = hbzPersonConsignorRegistryService.findById(registryDTO.getId());
        new Bean2Bean(
                new PropertyMapper<>("owerCreateTime", new TimeMillisFormat("yyyy-MM-dd")::parse)
        ).addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(registryDTO, registry);
        registry.setStatus("1");
        hbzPersonConsignorRegistryService.save(registry);
        return new ResponseDTO(Const.STATUS_OK, "保存成功");
    }


    /**
     * 保存
     *
     * @param registryDTO
     * @return
     */
    @RequestMapping(value = "/saveEnterpriseConsignor", method = RequestMethod.POST)
    public ResponseDTO put(@RequestBody HbzEnterpriseConsignorRegistryMapDTO registryDTO) {
        HbzEnterpriseConsignorRegistryDTO registry = hbzEnterpriseConsignorRegistryService.findById(registryDTO.getId());
        new Bean2Bean(
                new PropertyMapper<>("owerCreateTime", new TimeMillisFormat("yyyy-MM-dd")::parse)
        ).addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(registryDTO, registry);
        registry.setStatus("1");
        hbzEnterpriseConsignorRegistryService.save(registry);
        return new ResponseDTO(Const.STATUS_OK, "保存成功");
    }


    /**
     * 保存
     *
     * @param registryDTO
     * @return
     */
    @RequestMapping(value = "/savePersonDriver", method = RequestMethod.POST)
    public ResponseDTO put(@RequestBody HbzPersonDriverRegistryMapDTO registryDTO) {
        HbzPersonDriverRegistryDTO registry = hbzPersonDriverRegistryService.findById(registryDTO.getId());
        new Bean2Bean(
                new PropertyMapper<>("owerCreateTime", new TimeMillisFormat("yyyy-MM-dd")::parse),
                new PropertyMapper<>("drivingValidity", new TimeMillisFormat("yyyy-MM-dd")::parse)
        ).addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(registryDTO, registry);
        registry.setStatus("1");
        hbzPersonDriverRegistryService.save(registry);
        return new ResponseDTO(Const.STATUS_OK, "保存成功");
    }

    /**
     * 根据id获取详情
     *
     * @param registryDTO
     * @return
     */
    @RequestMapping(value = "/put", method = RequestMethod.POST)
    public ResponseDTO put(@RequestBody HbzUserRegistryMapDTO registryDTO) {
        HbzUserRegistryDTO registry = hbzUserRegistryService.findById(registryDTO.getId());
        Role roleType = null;
        if (registry == null) return new ResponseDTO(Const.STATUS_ERROR, "非法标识");
        if (registryDTO.getRegistryProgress() == null) return new ResponseDTO(Const.STATUS_ERROR, "非法项目");
        if (registry.getRegistryCode() == RegistryCode.TransEnterprise) {
            roleType = Role.EnterpriseAdmin;
        } else if (registry.getRegistryCode() == RegistryCode.PersonDriver) {
            roleType = Role.PersonDriver;
        } else if (registry.getRegistryCode() == RegistryCode.PersonConsignor) {
            roleType = Role.Consignor;
        } else if (registry.getRegistryCode() == RegistryCode.EnterpriseConsignor) {
            roleType = Role.EnterpriseConsignor;
        } else if (registry.getRegistryCode() == RegistryCode.DeliveryBoy) {
            roleType = Role.DeliveryBoy;
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "不支持的资质");
        }
        Role type = roleType;
        HbzRoleDTO queryTypedRole = new HbzRoleDTO();
        queryTypedRole.setStatus("1");
        queryTypedRole.setRole(type);
        List<HbzRoleDTO> roleList = hbzRoleService.query(queryTypedRole);
        switch (registryDTO.getRegistryProgress()) {
            case REGISTRYED: {
                registry.setRegistryProgress(RegistryProgress.REGISTRYED);
                hbzUserRegistryService.save(registry);
                HbzUserDTO user = registry.getUser();
                List<HbzRoleDTO> userRole = hbzUserService.queryRoles(user.getId());
                List<Long> ids = userRole.stream().map(HbzRoleDTO::getId).collect(Collectors.toList());
                List<Long> asids = roleList.stream().map(HbzRoleDTO::getId).collect(Collectors.toList());
                List<Long> crids = asids.stream().filter(id -> !ids.contains(id)).collect(Collectors.toList());
                ids.addAll(crids);
                List<HbzRoleDTO> ur = ids.stream().map(hbzRoleService::findById).collect(Collectors.toList());
                hbzUserService.setRoles(user, ur);

                //为企业用户绑定公司对象 yjx
                if (registry.getRegistryCode() == RegistryCode.EnterpriseConsignor || registry.getRegistryCode() == RegistryCode.TransEnterprise) {
                    HbzOrgDTO ent = user.getEnt();
                    if (ent == null) {
                        HbzOrgDTO hbzOrgDTO = new HbzOrgDTO();
                        if (registry.getRegistryCode() == RegistryCode.TransEnterprise)
                            hbzOrgDTO.setOrganizationName(hbzTransEnterpriseRegistryService.findById(registry.getId()).getOrganizationName());
                        else if (registry.getRegistryCode() == RegistryCode.EnterpriseConsignor)
                            hbzOrgDTO.setOrganizationName(hbzEnterpriseConsignorRegistryService.findById(registry.getId()).getOrganizationName());
                        hbzOrgDTO = this.hbzOrgService.save(hbzOrgDTO);
                        user.setEnt(hbzOrgDTO);
                        user.setOrg(hbzOrgDTO);
                        this.hbzUserService.save(user);
                    } else {
                        if (registry.getRegistryCode() == RegistryCode.TransEnterprise)
                            ent.setOrganizationName(hbzTransEnterpriseRegistryService.findById(registry.getId()).getOrganizationName());
                        else if (registry.getRegistryCode() == RegistryCode.EnterpriseConsignor)
                            ent.setOrganizationName(hbzEnterpriseConsignorRegistryService.findById(registry.getId()).getOrganizationName());
                        this.hbzOrgService.save(ent);
                    }
                }
                String role = roleList.stream().map(HbzRoleDTO::getRoleName).reduce((a, b) -> "[" + a + "]" + "," + "[" + b + "]").get();
                sitePushMessageService.sendMessageImmediately(Arrays.asList(user),
                        "信息审核",
                        "认证通知消息", "您" + role + "的身份已认证成功，请重新登录系统使身份生效。");
                return new ResponseDTO(Const.STATUS_OK, "成功");
            }
            case ERR_REGISTER: {
                registry.setRegistryProgress(RegistryProgress.ERR_REGISTER);
                hbzUserRegistryService.save(registry);
                HbzUserDTO user = registry.getUser();
                List<Long> haveRoleIds = hbzUserService.queryRoles(user.getId()).stream().map(HbzRoleDTO::getId).collect(Collectors.toList());
                List<Long> exceptRoles = roleList.stream().map(HbzRoleDTO::getId).collect(Collectors.toList());
                List<Long> nrs = haveRoleIds.stream().filter(id -> !(exceptRoles.contains(id))).collect(Collectors.toList());
                List<HbzRoleDTO> rs = nrs.stream().map(hbzRoleService::findById).collect(Collectors.toList());
                hbzUserService.setRoles(user, rs);
                String role = roleList.stream().map(HbzRoleDTO::getRoleName).reduce((a, b) -> "[" + a + "]" + "," + "[" + b + "]").get();
                sitePushMessageService.sendMessageImmediately(Arrays.asList(user),
                        "信息审核",
                        "认证通知消息", "您" + role + "的身份未通过认证，请在平台认证中查看详情。");
                return new ResponseDTO(Const.STATUS_OK, "成功");
            }
        }
        return new ResponseDTO(Const.STATUS_ERROR, "不支持的类型");
    }

    /**
     * @param registryDTO
     * @return
     */
    @RequestMapping(value = "/queryAll", method = RequestMethod.POST)
    public ResponseDTO query(@RequestBody HbzUserRegistryMapDTO registryDTO) {
        HbzUserRegistryDTO q = new HbzUserRegistryDTO();
        new Bean2Bean().addExcludeProp(CommonConstants.commonIgnores)
                .addPropMapper(
                        new PropertyMapper<>("owerCreateTime", new TimeMillisFormat("yyyy-MM-dd")::parse)
                ).copyProperties(registryDTO, q);
        q.setStatus("1");
        List<HbzUserRegistryDTO> r = hbzUserRegistryService.query(q);
        List<Map<String, Object>> show = r.stream().map(ur -> {
            switch (ur.getRegistryCode()) {
                case TransEnterprise: {
                    return MapSpec.mapTransEnterprise(hbzTransEnterpriseRegistryService.findById(ur.getId()));
                }
                case PersonConsignor: {
                    return MapSpec.mapPersonConsignor(hbzPersonConsignorRegistryService.findById(ur.getId()));
                }
                case PersonDriver: {
                    return MapSpec.mapPersonDriverRegistry(hbzPersonDriverRegistryService.findById(ur.getId()));
                }
                case DeliveryBoy: {
                    return MapSpec.mapA(hbzDeliveryBoyRegistrySerivce.findById(ur.getId()));
                }
                case EnterpriseConsignor: {
                    return MapSpec.mapEnterpriseConsignor(hbzEnterpriseConsignorRegistryService.findById(ur.getId()));
                }
            }
            return null;
        }).collect(Collectors.toList());
        return new ResponseDTO(Const.STATUS_OK, "成功", show);
    }


    @RequestMapping(value = "/queryAllPage", method = RequestMethod.POST)
    public ResponseDTO queryPage(@RequestBody HbzUserRegistryMapDTO registryDTO) {
        HbzUserRegistryDTO q = new HbzUserRegistryDTO();
        new Bean2Bean().addExcludeProp(CommonConstants.commonIgnores)
                .addPropMapper(
                        new PropertyMapper<>("owerCreateTime", new TimeMillisFormat("yyyy-MM-dd")::parse)
                ).copyProperties(registryDTO, q);
        q.setStatus("1");
        Page<HbzUserRegistryDTO> r = hbzUserRegistryService.queryPage(q, q.getPageRequest());
        Page<Map<String, Object>> show = r.map(ur -> {
            switch (ur.getRegistryCode()) {
                case TransEnterprise: {
                    return MapSpec.mapTransEnterprise(hbzTransEnterpriseRegistryService.findById(ur.getId()));
                }
                case PersonConsignor: {
                    return MapSpec.mapPersonConsignor(hbzPersonConsignorRegistryService.findById(ur.getId()));
                }
                case PersonDriver: {
                    return MapSpec.mapPersonDriverRegistry(hbzPersonDriverRegistryService.findById(ur.getId()));
                }
                case DeliveryBoy: {
                    return MapSpec.mapA(hbzDeliveryBoyRegistrySerivce.findById(ur.getId()));
                }
                case EnterpriseConsignor: {
                    return MapSpec.mapEnterpriseConsignor(hbzEnterpriseConsignorRegistryService.findById(ur.getId()));
                }
            }
            return null;
        });
        return new ResponseDTO(Const.STATUS_OK, "成功", show);
    }

    /**
     * 企业注册查询
     *
     * @param registryDTO
     * @return
     */
    @RequestMapping(value = "/transEnterprise/query", method = RequestMethod.POST)
    public ResponseDTO queryTransEnterprise(@RequestBody HbzTransEnterpriseRegistryMAPDTO registryDTO) {
        HbzTransEnterpriseRegistryDTO q = new HbzTransEnterpriseRegistryDTO();
        new Bean2Bean().addExcludeProp(CommonConstants.commonIgnores)
                .addPropMapper(
                        new PropertyMapper<>("owerCreateTime", new TimeMillisFormat("yyyy-MM-dd")::parse)
                ).copyProperties(registryDTO, q);
        q.setStatus("1");
        List<HbzTransEnterpriseRegistryDTO> r = hbzTransEnterpriseRegistryService.query(q);
        List<Map<String, Object>> show = r.stream().map(MapSpec::mapTransEnterprise).collect(Collectors.toList());
        return new ResponseDTO(Const.STATUS_OK, "成功", show);
    }

    @RequestMapping(value = "/transEnterprise/queryPage", method = RequestMethod.POST)
    public ResponseDTO querypTransEnterprise(@RequestBody HbzTransEnterpriseRegistryMAPDTO registryDTO) {
        HbzTransEnterpriseRegistryDTO q = new HbzTransEnterpriseRegistryDTO();
        new Bean2Bean().addExcludeProp(CommonConstants.commonIgnores)
                .addPropMapper(
                        new PropertyMapper<>("owerCreateTime", new TimeMillisFormat("yyyy-MM-dd")::parse)
                ).copyProperties(registryDTO, q);
        q.setStatus("1");
        Page<HbzTransEnterpriseRegistryDTO> r = hbzTransEnterpriseRegistryService.queryPage(q, q.getPageRequest());
        Page<Map<String, Object>> show = r.map(MapSpec::mapTransEnterprise);
        return new ResponseDTO(Const.STATUS_OK, "成功", show);
    }


}
