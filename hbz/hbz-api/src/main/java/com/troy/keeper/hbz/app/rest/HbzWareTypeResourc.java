package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzWareTypeMapDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzWareTypeDTO;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.po.HbzWareType;
import com.troy.keeper.hbz.service.EntityService;
import com.troy.keeper.hbz.service.HbzWareTypeService;
import com.troy.keeper.hbz.service.mapper.HbzWareTypeMapper;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/12/12.
 */
@RestController
@RequestMapping("/api")
public class HbzWareTypeResourc {

    @Autowired
    HbzWareTypeService hbzWareTypeService;

    @Autowired
    EntityService entityService;

    @Autowired
    HbzWareTypeMapper hbzWareTypeMapper;


    /**
     * @param wareTypeMapDTO
     * @return
     */
    @RequestMapping(value = {"/wareType/attach"}, method = RequestMethod.POST)
    public ResponseDTO attachWareType(@RequestBody HbzWareTypeMapDTO wareTypeMapDTO) {
        String[] errs = ValidationHelper.valid(wareTypeMapDTO, "wareType_attach");
        if (errs.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误", errs);
        }
        HbzWareTypeDTO wareType = entityService.get(HbzWareType.class, hbzWareTypeMapper, wareTypeMapDTO.getId());
        if (wareType == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误");
        }
        return new ResponseDTO(Const.STATUS_OK, "操作成功", Optional.of(wareType).map(MapSpec::mapWareType).get());
    }

    /**
     * 查询列表
     *
     * @param wareTypeMapDTO
     * @return
     */
    @RequestMapping(value = {"/wareType/query"}, method = RequestMethod.POST)
    public ResponseDTO queryWareType(@RequestBody HbzWareTypeMapDTO wareTypeMapDTO) {
        HbzWareTypeDTO hbzWareTypeDTO = new HbzWareTypeDTO();
        new Bean2Bean().copyProperties(wareTypeMapDTO, hbzWareTypeDTO);
        hbzWareTypeDTO.setStatus("1");
        List<HbzWareTypeDTO> list = entityService.query(HbzWareType.class, hbzWareTypeMapper::map, hbzWareTypeDTO);
        return new ResponseDTO(Const.STATUS_OK, "操作成功", list.stream().map(MapSpec::mapWareType).collect(Collectors.toList()));
    }

    private List<Map<String, Object>> putWithout(List<HbzWareTypeDTO> wareTypes) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < wareTypes.size(); i++) {
            HbzWareTypeDTO wt = wareTypes.get(i);
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", wt.getName());
            map.put("id", wt.getId());
            map.put("headerBit", wt.getHeaderBit());
            map.put("parentId", wt.getParentId());
            map.put("typeNo", wt.getTypeNo());
            map.put("level", wt.getLevel());
            list.add(map);
        }
        return list;
    }

    /**
     * 树查询
     */
    @RequestMapping(value = {"/wareType/top"}, method = RequestMethod.POST)
    public ResponseDTO topWareType(@RequestBody HbzWareTypeMapDTO wareTypeMapDTO) {
        Map<String, Object> queryTop = new LinkedHashMap<>();
        if (wareTypeMapDTO.getParentId() == null) {
            queryTop.put("parent isNull", "true");
        } else {
            queryTop.put("parent.id", wareTypeMapDTO.getParentId());
        }
        queryTop.put("status", "1");
        List<HbzWareTypeDTO> list = entityService.query(HbzWareType.class, hbzWareTypeMapper, queryTop);
        return new ResponseDTO(Const.STATUS_OK, "操作成功", putWithout(list));
    }

    /**
     * 分页
     *
     * @param wareTypeMapDTO
     * @return
     */
    @RequestMapping(value = {"/wareType/queryPage"}, method = RequestMethod.POST)
    public ResponseDTO queryPageWareType(@RequestBody HbzWareTypeMapDTO wareTypeMapDTO) {
        HbzWareTypeDTO hbzWareTypeDTO = new HbzWareTypeDTO();
        new Bean2Bean().copyProperties(wareTypeMapDTO, hbzWareTypeDTO);
        hbzWareTypeDTO.setStatus("1");
        Page<HbzWareTypeDTO> page = entityService.queryPage(HbzWareType.class, hbzWareTypeMapper::map, hbzWareTypeDTO.getPageRequest(), hbzWareTypeDTO);
        return new ResponseDTO(Const.STATUS_OK, "操作成功", page.map(MapSpec::mapWareType));
    }

    /**
     * 树查询
     */
    @RequestMapping(value = {"/wareType/all"}, method = RequestMethod.POST)
    public ResponseDTO allWareType(@RequestBody HbzWareTypeMapDTO wareTypeMapDTO) {
        Map<String, Object> queryTop = new LinkedHashMap<>();
        if (wareTypeMapDTO.getParentId() == null) {
            queryTop.put("parent isNull", "true");
        } else {
            queryTop.put("parent.id", wareTypeMapDTO.getParentId());
        }
        queryTop.put("status", "1");
        List<HbzWareTypeDTO> list = entityService.query(HbzWareType.class, hbzWareTypeMapper, queryTop);
        return new ResponseDTO(Const.STATUS_OK, "操作成功", put(list));
    }

    private List<Map<String, Object>> put(List<HbzWareTypeDTO> wareTypes) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < wareTypes.size(); i++) {
            HbzWareTypeDTO wt = wareTypes.get(i);
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", wt.getName());
            map.put("id", wt.getId());
            map.put("headerBit", wt.getHeaderBit());
            map.put("parentId", wt.getParentId());
            map.put("typeNo", wt.getTypeNo());
            map.put("level", wt.getLevel());

            Map<String, Object> querySubWareType = new HashMap<>();
            querySubWareType.put("status", Const.STATUS_ENABLED);
            querySubWareType.put("parent.id", wt.getId());
            List<HbzWareTypeDTO> sub = entityService.query(HbzWareType.class, hbzWareTypeMapper, querySubWareType);
            if (sub != null && !(sub.size() == 0)) {
                map.put("subs", put(sub));
            }
            list.add(map);
        }
        return list;
    }

}
