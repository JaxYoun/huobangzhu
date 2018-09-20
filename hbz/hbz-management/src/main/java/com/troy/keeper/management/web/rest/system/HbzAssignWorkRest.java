package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.HbzAssignWorkDTO;
import com.troy.keeper.hbz.dto.SmOrgDTO;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.service.HbzAreaService;
import com.troy.keeper.hbz.service.HbzAssignWorkService;
import com.troy.keeper.hbz.service.HbzSmOrgService;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.TimeMillisFormat;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.management.dto.HbzAssignWorkMapDTO;
import com.troy.keeper.management.utils.MapSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2018/1/18.
 */
@RestController
@RequestMapping("/api/assignWork")
public class HbzAssignWorkRest {

    @Autowired
    HbzAssignWorkService hbzAssignWorkService;

    @Autowired
    HbzAreaService hbzAreaService;

    @Autowired
    HbzSmOrgService hbzSmOrgService;

    @Label("管理端 - 快递派单 - 添加线下运输作业")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseDTO add(@RequestBody HbzAssignWorkMapDTO assignWorkDTO) {
        HbzAssignWorkDTO assignWork = new HbzAssignWorkDTO();
        String[] er = ValidationHelper.valid(assignWorkDTO, "create_assign_work");
        if (er.length > 0)
            return new ResponseDTO(Const.STATUS_ERROR, "验证失败", er);
        assignWork.setWorkNo(hbzAssignWorkService.createWorkNo());
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("assignTime", new TimeMillisFormat("yyyy-MM-dd")::parse)
        ).copyProperties(assignWorkDTO, assignWork, true);
        if (assignWorkDTO.getOriginAreaCode() != null) {
            HbzAreaDTO area = hbzAreaService.findByOutCode(assignWorkDTO.getOriginAreaCode());
            if (area != null) {
                assignWork.setOriginAreaId(area.getId());
                assignWork.setOriginArea(area);
            }
        }
        if (assignWorkDTO.getDestAreaCode() != null) {
            HbzAreaDTO area = hbzAreaService.findByOutCode(assignWorkDTO.getDestAreaCode());
            if (area != null) {
                assignWork.setDestAreaId(area.getId());
                assignWork.setDestArea(area);
            }
        }
        assignWork.setWorkStatus("1");
        assignWork.setStatus("1");
        assignWork = hbzAssignWorkService.save(assignWork);
        return new ResponseDTO(Const.STATUS_OK, "保存成功", MapSpec.mapAssignWork(assignWork));
    }

    @Label("管理端 - 快递派单 - 更新线下运输作业详细信息")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseDTO update(@RequestBody HbzAssignWorkMapDTO assignWorkDTO) {
        HbzAssignWorkDTO assignWork = hbzAssignWorkService.findById(assignWorkDTO.getId());
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("assignTime", new TimeMillisFormat("yyyy-MM-dd")::parse)
        ).copyProperties(assignWorkDTO, assignWork, true);
        if (assignWorkDTO.getOriginAreaCode() != null) {
            HbzAreaDTO area = hbzAreaService.findByOutCode(assignWorkDTO.getOriginAreaCode());
            if (area != null) {
                assignWork.setOriginAreaId(area.getId());
                assignWork.setOriginArea(area);
            }
        }
        if (assignWorkDTO.getDestAreaCode() != null) {
            HbzAreaDTO area = hbzAreaService.findByOutCode(assignWorkDTO.getDestAreaCode());
            if (area != null) {
                assignWork.setDestAreaId(area.getId());
                assignWork.setDestArea(area);
            }
        }
        assignWork.setStatus("1");
        hbzAssignWorkService.save(assignWork);
        return new ResponseDTO(Const.STATUS_OK, "更新成功");
    }

    @Label("管理端 - 快递派单 - 删除线下运输作业详细信息")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseDTO delete(@RequestBody HbzAssignWorkMapDTO assignWorkMap) {
        HbzAssignWorkDTO assignWork = hbzAssignWorkService.findById(assignWorkMap.getId());
        hbzAssignWorkService.delete(assignWork);
        return new ResponseDTO(Const.STATUS_OK, "删除成功");
    }

    @Label("管理端 - 快递派单 - 查询线下运输作业详细信息")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseDTO query(@RequestBody HbzAssignWorkMapDTO assignWorkMap) {
        HbzAssignWorkDTO assignWork = new HbzAssignWorkDTO();
        String[] er = ValidationHelper.valid(assignWorkMap, "create_assign_work");
        if (er.length > 0)
            return new ResponseDTO(Const.STATUS_ERROR, "验证失败", er);
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("assignTime", new TimeMillisFormat("yyyy-MM-dd")::parse)
        ).copyProperties(assignWorkMap, assignWork);
        if(assignWorkMap.getPlatformOrganizationId()!=null){
            LinkedList<Long> orgIds = new LinkedList<>();
            LinkedList<Long> requires = new LinkedList<>();
            requires.add(assignWorkMap.getPlatformOrganizationId());
            while(requires.size()>0){
                Long currentId = requires.removeFirst();
                Map<String,Object> query = new HashMap<>();
                query.put("status","1");
                query.put("pId",currentId);
                List<SmOrgDTO> subs = hbzSmOrgService.query(query);
                List<Long> subids = subs.stream().map(SmOrgDTO::getId).collect(Collectors.toList());
                if(subids.size()>0) requires.addAll(subids);
                orgIds.add(currentId);
            }
            if(orgIds.size()>0){
                assignWork.setPlatformOrganizationId(null);
                assignWork.setPlatformOrganizationIds(orgIds);
            }
        }
        assignWork.setStatus("1");
        List<HbzAssignWorkDTO> list = hbzAssignWorkService.query(assignWork);
        return new ResponseDTO(Const.STATUS_OK, "操作成功", list.stream().map(MapSpec::mapAssignWork).collect(Collectors.toList()));
    }

    @Label("管理端 - 快递派单 - 分页查询线下运输作业详细信息")
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public ResponseDTO queryPage(@RequestBody HbzAssignWorkMapDTO assignWorkMap) {
        HbzAssignWorkDTO assignWork = new HbzAssignWorkDTO();
        String[] er = ValidationHelper.valid(assignWorkMap, "create_assign_work");
        if (er.length > 0)
            return new ResponseDTO(Const.STATUS_ERROR, "验证失败", er);
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("assignTime", new TimeMillisFormat("yyyy-MM-dd")::parse)
        ).copyProperties(assignWorkMap, assignWork);
        if(assignWorkMap.getPlatformOrganizationId()!=null){
            LinkedList<Long> orgIds = new LinkedList<>();
            LinkedList<Long> requires = new LinkedList<>();
            requires.add(assignWorkMap.getPlatformOrganizationId());
            while(requires.size()>0){
                Long currentId = requires.removeFirst();
                Map<String,Object> query = new HashMap<>();
                query.put("status","1");
                query.put("pId",currentId);
                List<SmOrgDTO> subs = hbzSmOrgService.query(query);
                List<Long> subids = subs.stream().map(SmOrgDTO::getId).collect(Collectors.toList());
                if(subids.size()>0) requires.addAll(subids);
                orgIds.add(currentId);
            }
            if(orgIds.size()>0){
                assignWork.setPlatformOrganizationId(null);
                assignWork.setPlatformOrganizationIds(orgIds);
            }
        }
        assignWork.setStatus("1");
        Page<HbzAssignWorkDTO> page = hbzAssignWorkService.queryPage(assignWork, assignWork.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "操作成功", page.map(MapSpec::mapAssignWork));
    }

}
