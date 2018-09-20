package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzDriverLineMapDto;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.HbzDriverLineDTO;
import com.troy.keeper.hbz.dto.HbzTransSizeDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.service.HbzAreaService;
import com.troy.keeper.hbz.service.HbzDriverLineService;
import com.troy.keeper.hbz.service.HbzTransSizeService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.annotation.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/12/6.
 */
@RestController
@RequestMapping("/api/driverLine")
public class HbzDriverLineMgrResource {

    @Autowired
    HbzDriverLineService hbzDriverLineService;
    @Autowired
    HbzAreaService hbzAreaService;
    @Autowired
    HbzTransSizeService hbzTransSizeService;
    @Autowired
    HbzUserService hbzUserService;

    @Label("App端 - 货运方 - 运力发布 - 创建")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseDTO createDriverLi(@RequestBody HbzDriverLineMapDto mapDto) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzDriverLineDTO driverLine = new HbzDriverLineDTO();
        new Bean2Bean().copyProperties(mapDto, driverLine);
        HbzAreaDTO origin = hbzAreaService.findByOutCode(mapDto.getOriginAreaCode());
        HbzAreaDTO dest = hbzAreaService.findByOutCode(mapDto.getDestAreaCode());
        driverLine.setStatus(Const.STATUS_ENABLED);
        driverLine.setOriginArea(origin);
        driverLine.setDestArea(dest);
        driverLine.setUser(user);
        driverLine.setUserId(user.getId());
        driverLine = hbzDriverLineService.save(driverLine);
        if (driverLine != null) {
            if (mapDto.getTransSizes() == null) mapDto.setTransSizes(new ArrayList<>());
            List<HbzTransSizeDTO> transSizes = mapDto.getTransSizes().stream().map(hbzTransSizeService::findByTransSize).collect(Collectors.toList());
            hbzDriverLineService.bindTransSizes(driverLine, transSizes);
            return new ResponseDTO(Const.STATUS_OK, "保存成功");
        }
        return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
    }

    @Label("App端 - 货运方 - 运力发布 - 更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseDTO updateDriverLi(@RequestBody HbzDriverLineMapDto mapDto) {
        HbzUserDTO user = hbzUserService.currentUser();
        if (mapDto.getId() == null) return new ResponseDTO(Const.STATUS_ERROR, "标识为空");
        HbzDriverLineDTO driverLine = new HbzDriverLineDTO();
        driverLine.setId(mapDto.getId());
        driverLine = hbzDriverLineService.get(driverLine);
        if (driverLine == null || driverLine.getStatus().equals(Const.STATUS_DISABLED))
            return new ResponseDTO(Const.STATUS_ERROR, "标识错误");
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(mapDto, driverLine);
        HbzAreaDTO origin = hbzAreaService.findByOutCode(mapDto.getOriginAreaCode());
        HbzAreaDTO dest = hbzAreaService.findByOutCode(mapDto.getDestAreaCode());
        driverLine.setStatus(Const.STATUS_ENABLED);
        driverLine.setOriginArea(origin);
        driverLine.setDestArea(dest);
        driverLine.setUser(user);
        driverLine.setUserId(user.getId());
        driverLine = hbzDriverLineService.save(driverLine);
        if (driverLine != null) {
            if (mapDto.getTransSizes() == null) mapDto.setTransSizes(new ArrayList<>());
            List<HbzTransSizeDTO> transSizes = mapDto.getTransSizes().stream().map(hbzTransSizeService::findByTransSize).collect(Collectors.toList());
            hbzDriverLineService.bindTransSizes(driverLine, transSizes);
            return new ResponseDTO(Const.STATUS_OK, "保存成功");
        }
        return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
    }

    @Label("App端 - 货运方 - 运力发布 - 删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseDTO deleteDriverLi(@RequestBody HbzDriverLineMapDto mapDto) {
        if (mapDto.getId() == null) return new ResponseDTO(Const.STATUS_ERROR, "标识为空");
        HbzDriverLineDTO driverLine = new HbzDriverLineDTO();
        driverLine.setId(mapDto.getId());
        hbzDriverLineService.delete(driverLine);
        return new ResponseDTO(Const.STATUS_OK, "保存成功");
    }

    @Label("App端 - 运力发布 - 查询")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseDTO queryDriverLi(@RequestBody HbzDriverLineMapDto mapDto) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzDriverLineDTO query = new HbzDriverLineDTO();
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(mapDto, query);
        query.setStatus(Const.STATUS_ENABLED);
        List<HbzDriverLineDTO> rs = hbzDriverLineService.query(query);
        return new ResponseDTO(Const.STATUS_OK, "操作成功", rs.stream().map(MapSpec::mapDriverLineDTO).collect(Collectors.toList()));
    }

    @Label("App端 - 运力发布 - 分页搜索")
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public ResponseDTO queryDrpiverLi(@RequestBody HbzDriverLineMapDto mapDto) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzDriverLineDTO query = new HbzDriverLineDTO();
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(mapDto, query);
        query.setStatus(Const.STATUS_ENABLED);
        Page<HbzDriverLineDTO> rs = hbzDriverLineService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "操作成功", rs.map(MapSpec::mapDriverLineDTO));
    }
}
