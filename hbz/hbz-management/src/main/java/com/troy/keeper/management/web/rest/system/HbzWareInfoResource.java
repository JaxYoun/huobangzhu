package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzWareInfoDTO;
import com.troy.keeper.hbz.dto.HbzWareTypeDTO;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.po.HbzWareInfo;
import com.troy.keeper.hbz.service.EntityService;
import com.troy.keeper.hbz.service.HbzWareInfoService;
import com.troy.keeper.hbz.service.HbzWareTypeService;
import com.troy.keeper.hbz.service.mapper.HbzWareInfoMapper;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.management.dto.HbzWareInfoMapDTO;
import com.troy.keeper.management.utils.MapSpec;
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
public class HbzWareInfoResource {

    @Autowired
    EntityService entityService;

    @Autowired
    HbzWareInfoMapper mapper;

    @Autowired
    HbzWareTypeService hbzWareTypeService;

    @Autowired
    HbzWareInfoService hbzWareInfoService;

    /**
     * 新建类型
     *
     * @param wareInfoMapDTO
     * @return
     */
    @RequestMapping(value = {"/wareInfo/new"}, method = RequestMethod.POST)
    public ResponseDTO newWareInfo(@RequestBody HbzWareInfoMapDTO wareInfoMapDTO) {
        String[] errs = ValidationHelper.valid(wareInfoMapDTO, "wareInfo_create");
        if (errs.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误", errs);
        }
        String wareNo = hbzWareInfoService.createNo(wareInfoMapDTO.getTypeId());
        HbzWareInfoDTO wareInfo = new HbzWareInfoDTO();
        new Bean2Bean().copyProperties(wareInfoMapDTO, wareInfo);
        wareInfo.setWareNo(wareNo);
        wareInfo.setStatus(Const.STATUS_ENABLED);
        if (wareInfo.getMarketAmount() == null) wareInfo.setMarketAmount(0.000000000);
        if (wareInfo.getAmount() == null) wareInfo.setAmount(0.0000000000);
        wareInfo = entityService.save(wareInfo, mapper);
        if (wareInfo != null) {
            Map<String, Object> hmap = new HashMap<>();
            hmap.put("id", wareInfo.getId());
            return new ResponseDTO(Const.STATUS_OK, "操作成功", hmap);
        }
        return new ResponseDTO(Const.STATUS_ERROR, "错误");
    }

    /**
     * 更新
     *
     * @param wareInfoMapDTO
     * @return
     */
    @RequestMapping(value = {"/wareInfo/update"}, method = RequestMethod.POST)
    public ResponseDTO updateWareInfo(@RequestBody HbzWareInfoMapDTO wareInfoMapDTO) {
        String[] errs = ValidationHelper.valid(wareInfoMapDTO, "wareInfo_update");
        if (errs.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误", errs);
        }
        HbzWareInfoDTO wareInfo = entityService.get(HbzWareInfo.class, mapper, wareInfoMapDTO.getId());
        if (wareInfo == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "数据不合法");
        }
        String wareNo = wareInfo.getWareNo();
        new Bean2Bean().addExcludeProp(Const.ID_FIELDS).copyProperties(wareInfoMapDTO, wareInfo);
        wareInfo.setWareNo(wareNo);
        wareInfo.setStatus(Const.STATUS_ENABLED);
        wareInfo = entityService.save(wareInfo, mapper);
        if (wareInfo != null) {
            Map<String, Object> hmap = new HashMap<>();
            hmap.put("id", wareInfo.getId());
            return new ResponseDTO(Const.STATUS_OK, "操作成功", hmap);
        }
        return new ResponseDTO(Const.STATUS_ERROR, "错误");
    }

    /**
     * 加商品
     *
     * @param wareInfoMapDTO xyz
     * @return
     */
    @RequestMapping(value = {"/wareInfo/attach"}, method = RequestMethod.POST)
    public ResponseDTO attachWareInfo(@RequestBody HbzWareInfoMapDTO wareInfoMapDTO) {
        String[] errs = ValidationHelper.valid(wareInfoMapDTO, "wareInfo_attach");
        if (errs.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误", errs);
        }
        HbzWareInfoDTO wareInfo = entityService.get(HbzWareInfo.class, mapper, wareInfoMapDTO.getId());
        if (wareInfo == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "数据不合法");
        }
        return new ResponseDTO(Const.STATUS_OK, "操作成功", Optional.of(wareInfo).map(MapSpec::mapWareInfo));
    }

    /**
     * 删除
     *
     * @param wareInfoMapDTO xyz
     * @return
     */
    @RequestMapping(value = {"/wareInfo/delete"}, method = RequestMethod.POST)
    public ResponseDTO deleteWareInfo(@RequestBody HbzWareInfoMapDTO wareInfoMapDTO) {
        String[] errs = ValidationHelper.valid(wareInfoMapDTO, "wareInfo_delete");
        if (errs.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误", errs);
        }
        HbzWareInfoDTO wareInfo = entityService.get(HbzWareInfo.class, mapper, wareInfoMapDTO.getId());
        if (wareInfo == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "数据不合法");
        }
        wareInfo.setStatus(Const.STATUS_DISABLED);
        wareInfo = entityService.save(wareInfo, mapper);
        if (wareInfo != null) {
            Map<String, Object> hmap = new HashMap<>();
            hmap.put("id", wareInfo.getId());
            return new ResponseDTO(Const.STATUS_OK, "操作成功", hmap);
        }
        return new ResponseDTO(Const.STATUS_ERROR, "错误");
    }


    /**
     * 查询商品
     *
     * @param wareInfoMapDTO
     * @return
     */
    @RequestMapping(value = {"/wareInfo/query"}, method = RequestMethod.POST)
    public ResponseDTO query(@RequestBody HbzWareInfoMapDTO wareInfoMapDTO) {
        HbzWareInfoDTO wareInfoDTO = new HbzWareInfoDTO();
        new Bean2Bean().copyProperties(wareInfoMapDTO, wareInfoDTO);
        List<String[]> ord = new ArrayList<>();
        ord.add(new String[]{"createdDate", "desc"});
        wareInfoDTO.setSorts(ord);
        if (wareInfoDTO.getTypeId() != null) {
            List<Long> ids = hbzWareTypeService.querySub(wareInfoDTO.getTypeId()).stream().map(HbzWareTypeDTO::getId).collect(Collectors.toList());
            if (ids.size() > 0) {
                wareInfoDTO.setTypeId(null);
                wareInfoDTO.setTypeIds(ids);
            }
        }

        wareInfoDTO.setStatus("1");
        List<HbzWareInfoDTO> list = entityService.query(HbzWareInfo.class, mapper, wareInfoDTO, new Sort.Order(Sort.Direction.DESC, "createdDate"));
        return new ResponseDTO(Const.STATUS_OK, "操作成功", list.stream().map(MapSpec::mapWareInfo).collect(Collectors.toList()));
    }

    @RequestMapping(value = {"/wareInfo/queryPage"}, method = RequestMethod.POST)
    public ResponseDTO queryPage(@RequestBody HbzWareInfoMapDTO wareInfoMapDTO) {
        HbzWareInfoDTO wareInfoDTO = new HbzWareInfoDTO();
        new Bean2Bean().copyProperties(wareInfoMapDTO, wareInfoDTO);
        List<String[]> ord = new ArrayList<>();
        ord.add(new String[]{"createdDate", "desc"});
        wareInfoDTO.setSorts(ord);
        if (wareInfoDTO.getTypeId() != null) {
            List<Long> ids = hbzWareTypeService.querySub(wareInfoDTO.getTypeId()).stream().map(HbzWareTypeDTO::getId).collect(Collectors.toList());
            if (ids.size() > 0) {
                wareInfoDTO.setTypeId(null);
                wareInfoDTO.setTypeIds(ids);
            }
        }

        wareInfoDTO.setStatus("1");
        Page<HbzWareInfoDTO> page = entityService.queryPage(HbzWareInfo.class, mapper, wareInfoDTO.getPageRequest(), wareInfoDTO, new Sort.Order(Sort.Direction.DESC, "createdDate"));
        return new ResponseDTO(Const.STATUS_OK, "操作成功", page.map(MapSpec::mapWareInfo));
    }
}
