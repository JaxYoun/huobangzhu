package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzWareTypeDTO;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.po.HbzWareType;
import com.troy.keeper.hbz.service.EntityService;
import com.troy.keeper.hbz.service.HbzWareTypeService;
import com.troy.keeper.hbz.service.mapper.HbzWareTypeMapper;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.management.dto.HbzWareTypeMapDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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
public class HbzWareTypeResource {

    @Autowired
    EntityService entityService;

    @Autowired
    HbzWareTypeMapper hbzWareTypeMapper;

    @Autowired
    HbzWareTypeService hbzWareTypeService;

    /**
     * 新增商品类型
     *
     * @param wareTypeMapDTO
     * @return
     */
    @RequestMapping(value = {"/wareType/new"}, method = RequestMethod.POST)
    public ResponseDTO newWareType(@RequestBody HbzWareTypeMapDTO wareTypeMapDTO) {
        String[] errs = ValidationHelper.valid(wareTypeMapDTO, "wareType_create");
        if (errs.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误", errs);
        }
        String typeNo = hbzWareTypeService.createTypeNo(wareTypeMapDTO.getParentId());
        int level = 1;
        if (wareTypeMapDTO.getParentId() != null) {
            HbzWareTypeDTO q = new HbzWareTypeDTO();
            q.setId(wareTypeMapDTO.getParentId());
            HbzWareTypeDTO parent = hbzWareTypeService.get(q);
            if (parent != null) {
                level = parent.getLevel() + 1;
            }
        }
        HbzWareTypeDTO wareType = new HbzWareTypeDTO();
        new Bean2Bean().copyProperties(wareTypeMapDTO, wareType);
        wareType.setTypeNo(typeNo);
        wareType.setLevel(level);
        wareType.setStatus(Const.STATUS_ENABLED);
        wareType = entityService.save(wareType, hbzWareTypeMapper);
        if (wareType != null) {
            Map<String, Object> hmap = new HashMap<>();
            hmap.put("id", wareType.getId());
            return new ResponseDTO(Const.STATUS_OK, "操作成功", hmap);
        }
        return new ResponseDTO(Const.STATUS_ERROR, "错误");
    }

    /**
     * 更新商品类型
     *
     * @param wareTypeMAPDTO
     * @return
     */
    @RequestMapping(value = {"/wareType/merge"}, method = RequestMethod.POST)
    public ResponseDTO mergeWareType(@RequestBody HbzWareTypeMapDTO wareTypeMAPDTO) {
        String[] errs = ValidationHelper.valid(wareTypeMAPDTO, "wareType_update");
        if (errs.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误", errs);
        }
        HbzWareTypeDTO wareType = entityService.get(HbzWareType.class, hbzWareTypeMapper, wareTypeMAPDTO.getId());
        if (wareType == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误");
        }
        String typeNo = wareType.getTypeNo();
        int level = 1;
        if (wareTypeMAPDTO.getParentId() != null) {
            HbzWareTypeDTO q = new HbzWareTypeDTO();
            q.setId(wareTypeMAPDTO.getParentId());
            HbzWareTypeDTO parent = hbzWareTypeService.get(q);
            if (parent != null) {
                level = parent.getLevel() + 1;
            }
        }
        new Bean2Bean().addExcludeProp("id").copyProperties(wareTypeMAPDTO, wareType);
        wareType.setStatus(Const.STATUS_ENABLED);
        wareType.setLevel(level);
        wareType.setTypeNo(typeNo);
        wareType = entityService.save(wareType, hbzWareTypeMapper);
        if (wareType != null) {
            Map<String, Object> hmap = new HashMap<>();
            hmap.put("id", wareType.getId());
            return new ResponseDTO(Const.STATUS_OK, "操作成功", hmap);
        }
        return new ResponseDTO(Const.STATUS_ERROR, "错误");
    }


    /**
     * 删除商品类型
     *
     * @param wareTypeMapDTO
     * @return
     */
    @RequestMapping(value = {"/wareType/delete"}, method = RequestMethod.POST)
    public ResponseDTO deleteWareType(@RequestBody HbzWareTypeMapDTO wareTypeMapDTO) {
        String[] errs = ValidationHelper.valid(wareTypeMapDTO, "wareType_delete");
        if (errs.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误", errs);
        }
        HbzWareTypeDTO wareType = entityService.get(HbzWareType.class, hbzWareTypeMapper, wareTypeMapDTO.getId());
        if (wareType == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误");
        }
        LinkedList<HbzWareTypeDTO> deleteList = new LinkedList<>();
        deleteList.add(wareType);
        while (deleteList.size() > 0) {
            HbzWareTypeDTO current = deleteList.removeFirst();
            Map<String, Object> querySubWareType = new HashMap<>();
            querySubWareType.put("status", Const.STATUS_ENABLED);
            querySubWareType.put("parent.id", current.getId());
            List<HbzWareTypeDTO> sub = entityService.query(HbzWareType.class, hbzWareTypeMapper, querySubWareType);
            if (sub != null) deleteList.addAll(sub);
            current.setStatus(Const.STATUS_DISABLED);
            entityService.save(current, hbzWareTypeMapper);
        }
        Map<String, Object> hmap = new HashMap<>();
        hmap.put("code", wareType.getId());
        return new ResponseDTO(Const.STATUS_OK, "操作成功", hmap);
    }

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
        return new ResponseDTO(Const.STATUS_OK, "操作成功", Optional.of(wareType).map(com.troy.keeper.management.utils.MapSpec::mapWareType).get());
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
        List<HbzWareTypeDTO> list = entityService.query(HbzWareType.class, hbzWareTypeMapper::map, hbzWareTypeDTO, new Sort.Order(Sort.Direction.DESC, "createdDate"));
        return new ResponseDTO(Const.STATUS_OK, "操作成功", list.stream().map(com.troy.keeper.management.utils.MapSpec::mapWareType).collect(Collectors.toList()));
    }

    private List<Map<String, Object>> render(List<HbzWareTypeDTO> wareTypes) {
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
            queryTop.put("parent isNull", "");
        } else {
            queryTop.put("parent.id", wareTypeMapDTO.getParentId());
        }
        queryTop.put("status", "1");
        List<HbzWareTypeDTO> list = entityService.query(HbzWareType.class, hbzWareTypeMapper, queryTop);
        return new ResponseDTO(Const.STATUS_OK, "操作成功", render(list));
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
        Page<HbzWareTypeDTO> page = entityService.queryPage(HbzWareType.class, hbzWareTypeMapper::map, hbzWareTypeDTO.getPageRequest(), hbzWareTypeDTO, new Sort.Order(Sort.Direction.DESC, "createdDate"));
        return new ResponseDTO(Const.STATUS_OK, "操作成功", page.map(com.troy.keeper.management.utils.MapSpec::mapWareType));
    }

    /**
     * 树查询
     */
    @RequestMapping(value = {"/wareType/all"}, method = RequestMethod.POST)
    public ResponseDTO allWareType(@RequestBody HbzWareTypeMapDTO wareTypeMapDTO) {
        Map<String, Object> queryTop = new LinkedHashMap<>();
        if (wareTypeMapDTO.getParentId() == null) {
            queryTop.put("parent isNull", "true13579");
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
            map.put("label", wt.getName());
            map.put("id", wt.getId());
            map.put("value", wt.getId());
            map.put("headerBit", wt.getHeaderBit());
            map.put("pid", wt.getParentId());
            map.put("parentId", wt.getParentId());
            map.put("typeNo", wt.getTypeNo());
            map.put("level", wt.getLevel());

            Map<String, Object> querySubWareType = new HashMap<>();
            querySubWareType.put("status", Const.STATUS_ENABLED);
            querySubWareType.put("parent.id", wt.getId());
            List<HbzWareTypeDTO> sub = entityService.query(HbzWareType.class, hbzWareTypeMapper, querySubWareType);
            if (sub != null && !(sub.size() == 0)) {
                map.put("children", put(sub));
                map.put("subs", put(sub));
            }
            list.add(map);
        }
        return list;
    }

}
