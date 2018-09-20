package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.HbzPlatformOrganizationDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.dto.SmOrgDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.sys.ApplicationContextHolder;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.management.dto.HbzPlatformOrganizationMapDTO;
import com.troy.keeper.management.utils.MapSpec;
import com.troy.keeper.system.service.SmOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2018/1/16.
 */
@RestController
@RequestMapping({"/api/organization", "/api/sm/org"})
public class HbzPlatformOrganizationResource {

    @Autowired
    HbzPlatformOrganizationService hbzPlatformOrganizationService;

    @Autowired
    SmOrgService smOrgService;

    @Autowired
    HbzSmOrgService hbzSmOrgService;

    @Autowired
    HbzUserService hbzUserService;

    @Autowired
    HbzAreaService hbzAreaService;

    @Label("管理端 - 网点管理 -添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseDTO add(@RequestBody HbzPlatformOrganizationMapDTO platformOrganizationDTO) {
        MapService map = ApplicationContextHolder.getService(MapService.class);
        HbzPlatformOrganizationDTO hbzPlatformOrganization = new HbzPlatformOrganizationDTO();
        new Bean2Bean().copyProperties(platformOrganizationDTO, hbzPlatformOrganization);
        if (hbzPlatformOrganization.getOrderCode() == null) {
            hbzPlatformOrganization.setOrderCode(0L);
        }
        if (StringHelper.notNullAndEmpty(platformOrganizationDTO.getAddressAreaCode())) {
            HbzAreaDTO area = hbzAreaService.findByOutCode(platformOrganizationDTO.getAddressAreaCode());
            if (area != null) {
                hbzPlatformOrganization.setAddressArea(area);
                hbzPlatformOrganization.setAddressAreaId(area.getId());
            }
        }
        if (platformOrganizationDTO.getParentId() != null) {
            hbzPlatformOrganization.setPId(platformOrganizationDTO.getParentId());
            String relationship = hbzPlatformOrganizationService.getRelationship(platformOrganizationDTO.getParentId());
            if (relationship != null) hbzPlatformOrganization.setRelationship(relationship);
        } else {
            hbzPlatformOrganization.setRelationship("1");
        }
        hbzPlatformOrganization.setAddress(map.getLocationAddress(hbzPlatformOrganization.getLng(), hbzPlatformOrganization.getLat()));
        hbzPlatformOrganization.setStatus("1");
        hbzPlatformOrganizationService.save(hbzPlatformOrganization);
        return new ResponseDTO(Const.STATUS_OK, "操作OK");
    }

    @Label("管理端 - 网点管理 - 编辑")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseDTO update(@RequestBody HbzPlatformOrganizationMapDTO platformOrganizationDTO) {
        MapService map = ApplicationContextHolder.getService(MapService.class);
        HbzPlatformOrganizationDTO hbzPlatformOrganization = hbzPlatformOrganizationService.findById(platformOrganizationDTO.getId());
        new Bean2Bean().copyProperties(platformOrganizationDTO, hbzPlatformOrganization, true);
        if (StringHelper.notNullAndEmpty(platformOrganizationDTO.getAddressAreaCode())) {
            HbzAreaDTO area = hbzAreaService.findByOutCode(platformOrganizationDTO.getAddressAreaCode());
            if (area != null) {
                hbzPlatformOrganization.setAddressArea(area);
                hbzPlatformOrganization.setAddressAreaId(area.getId());
            }
        }
        if (platformOrganizationDTO.getParentId() != null) {
            hbzPlatformOrganization.setPId(platformOrganizationDTO.getParentId());
            String relationship = hbzPlatformOrganizationService.getRelationship(platformOrganizationDTO.getParentId());
            if (relationship != null) hbzPlatformOrganization.setRelationship(relationship);
        } else {
            hbzPlatformOrganization.setRelationship("1");
        }
        hbzPlatformOrganization.setAddress(map.getLocationAddress(hbzPlatformOrganization.getLng(), hbzPlatformOrganization.getLat()));
        hbzPlatformOrganizationService.save(hbzPlatformOrganization);
        return new ResponseDTO(Const.STATUS_OK, "操作OK");
    }

    @Label("管理端 - 网点管理 - 绑定服务区域")
    @RequestMapping(value = "/setarea", method = RequestMethod.POST)
    public ResponseDTO setServiceArea(@RequestBody HbzPlatformOrganizationMapDTO platformOrganizationDTO) {
        HbzPlatformOrganizationDTO hbzPlatformOrganization = hbzPlatformOrganizationService.findById(platformOrganizationDTO.getId());
        List<HbzAreaDTO> areas = platformOrganizationDTO.getAreaIds().stream().map(hbzAreaService::findById).collect(Collectors.toList());
        hbzPlatformOrganizationService.setArea(hbzPlatformOrganization, areas);
        return new ResponseDTO(Const.STATUS_OK, "操作OK");
    }

    @Label("管理端 - 网点管理 - 删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseDTO delete(@RequestBody HbzPlatformOrganizationMapDTO platformOrganizationDTO) {
        HbzPlatformOrganizationDTO hbzPlatformOrganization = hbzPlatformOrganizationService.findById(platformOrganizationDTO.getId());
        LinkedList<HbzPlatformOrganizationDTO> list = new LinkedList<>();
        list.addLast(hbzPlatformOrganization);
        Long departmentSize = 0L;
        Long userSize = 0L;
        while (list.size() > 0) {
            HbzPlatformOrganizationDTO current = list.removeFirst();
            departmentSize++;

            HbzPlatformOrganizationDTO query = new HbzPlatformOrganizationDTO();
            query.setStatus("1");
            query.setPId(current.getId());
            List<HbzPlatformOrganizationDTO> subs = hbzPlatformOrganizationService.query(query);
            list.addAll(subs);

            HbzUserDTO queryUser = new HbzUserDTO();
            queryUser.setStatus("1");
            queryUser.setOrgId(current.getId());
            userSize += hbzUserService.count(queryUser);
        }
        if (departmentSize > 2L || userSize > 0L) {
            return new ResponseDTO(Const.STATUS_ERROR, "存在[" + (departmentSize - 1L) + "]个子部门，存在[" + userSize + "]个用户");
        }
        hbzPlatformOrganizationService.delete(hbzPlatformOrganization);
        return new ResponseDTO(Const.STATUS_OK, "操作OK");
    }

    @Label("管理端 - 网点管理 - 查询")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseDTO query(@RequestBody HbzPlatformOrganizationMapDTO platformOrganizationDTO) {
        HbzPlatformOrganizationDTO hbzPlatformOrganization = new HbzPlatformOrganizationDTO();
        new Bean2Bean().copyProperties(platformOrganizationDTO, hbzPlatformOrganization);
        hbzPlatformOrganization.setStatus("1");
        if (platformOrganizationDTO.getParentId() != null) {
            hbzPlatformOrganization.setPId(platformOrganizationDTO.getParentId());
        }
        if (platformOrganizationDTO.getAreaId() != null) {
            HbzAreaDTO area = hbzAreaService.findById(platformOrganizationDTO.getAreaId());
            if (area != null && area.getStatus().equals("1")) {
                List<Long> areaIds = new ArrayList<>();
                HbzAreaDTO current = area;
                while (current != null) {
                    areaIds.add(current.getId());
                    current = current.getParent();
                }
                if (areaIds != null && areaIds.size() > 0) {
                    hbzPlatformOrganization.setAreaIds(areaIds);
                }
            }
        }
        List<HbzPlatformOrganizationDTO> list = hbzPlatformOrganizationService.query(hbzPlatformOrganization);
        return new ResponseDTO(Const.STATUS_OK, "操作OK", list.stream().map(MapSpec::mapOrganization).collect(Collectors.toList()));
    }

    @Label("管理端 - 网点管理 - 分页查询")
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public ResponseDTO queryPage(@RequestBody HbzPlatformOrganizationMapDTO platformOrganizationDTO) {
        HbzPlatformOrganizationDTO hbzPlatformOrganization = new HbzPlatformOrganizationDTO();
        new Bean2Bean().copyProperties(platformOrganizationDTO, hbzPlatformOrganization);
        hbzPlatformOrganization.setStatus("1");
        if (platformOrganizationDTO.getParentId() != null) {
            hbzPlatformOrganization.setPId(platformOrganizationDTO.getParentId());
        }
        if (platformOrganizationDTO.getAreaId() != null) {
            HbzAreaDTO area = hbzAreaService.findById(platformOrganizationDTO.getAreaId());
            if (area != null && area.getStatus().equals("1")) {
                List<Long> areaIds = new ArrayList<>();
                HbzAreaDTO current = area;
                while (current != null) {
                    areaIds.add(current.getId());
                    current = current.getParent();
                }
                if (areaIds != null && areaIds.size() > 0) {
                    hbzPlatformOrganization.setAreaIds(areaIds);
                }
            }
        }
        Page<HbzPlatformOrganizationDTO> page = hbzPlatformOrganizationService.queryPage(hbzPlatformOrganization, hbzPlatformOrganization.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "操作OK", page.map(MapSpec::mapOrganization));
    }


    @Label("管理端 - 组织机构网点 - 带树查询")
    @RequestMapping(value = {"/tree"}, method = RequestMethod.POST)
    public ResponseDTO treeOrganizations(@RequestBody HbzPlatformOrganizationMapDTO hbzPlatformOrganizationMapDTO) {
        Map<String, Object> queryTop = new LinkedHashMap<>();
        if (hbzPlatformOrganizationMapDTO.getParentId() == null) {
            queryTop.put("pId isNull", "True");
        } else {
            queryTop.put("pId =", hbzPlatformOrganizationMapDTO.getParentId());
        }
        queryTop.put("status", "1");
        List<SmOrgDTO> list = hbzSmOrgService.query(queryTop);
        List<Map<String, Object>> roots = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            SmOrgDTO sm = list.get(i);
            Map<String, Object> map = put(sm);
            roots.add(map);
        }
        return new ResponseDTO(Const.STATUS_OK, "查询成功！", roots);
    }

    public Map<String, Object> put(SmOrgDTO smOrgDTO) {
        Map<String, Object> map = new HashMap<>();
        map.put("value", smOrgDTO.getId());
        map.put("pId", smOrgDTO.getPId());
        map.put("orderCode", smOrgDTO.getOrderCode());
        map.put("orgName", smOrgDTO.getOrgName());
        map.put("label", smOrgDTO.getOrgName());
        map.put("relationship", smOrgDTO.getRelationship());
        map.put("id", smOrgDTO.getId());
        map.put("key", smOrgDTO.getId());

        Map<String, Object> querySubParameter = new HashMap<>();
        querySubParameter.put("pId", smOrgDTO.getId());
        querySubParameter.put("status", "1");
        List<SmOrgDTO> sms = hbzSmOrgService.query(querySubParameter);
        List<Map<String, Object>> maps = new LinkedList<>();
        for (SmOrgDTO sm : sms) {
            maps.add(put(sm));
        }
        if (maps.size() > 0) {
            map.put("sub", maps);
            map.put("children", maps);
        }
        HbzPlatformOrganizationDTO platformOrganization = hbzPlatformOrganizationService.findById(smOrgDTO.getId());
        map.put("isPlatform", false);
        if (platformOrganization != null)
            map.put("isPlatform", true);

        return map;
    }
}
