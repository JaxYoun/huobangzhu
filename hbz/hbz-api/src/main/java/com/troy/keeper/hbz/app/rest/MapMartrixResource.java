package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.MapAddressDTO;
import com.troy.keeper.hbz.dto.MapRouteMatrixDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by leecheng on 2017/11/29.
 */
@RestController
@RequestMapping("/api/map")
public class MapMartrixResource {

    @Autowired
    private MapService service;

    /**
     * @param addr
     * @return
     */
    @RequestMapping(value = "/geo/convert", method = RequestMethod.POST)
    public ResponseDTO queryPointByAdd(@RequestBody MapAddressDTO addr) {
        if (StringHelper.notNullAndEmpty(addr.getAdd())) {
            Map<String, Object> add = service.getLocation(addr.getAdd());
            if (add != null) {
                return new ResponseDTO(Const.STATUS_OK, "成功", add);
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, "错误转换失败");
            }
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "地址为空");
        }
    }

    @RequestMapping(value = "/geo/info", method = RequestMethod.POST)
    public ResponseDTO queryAddByPoint(@RequestBody MapAddressDTO addr) {
        if (addr.getLng() != null && addr.getLat() != null) {
            Map<String, Object> add = service.getLocation(addr.getLng(), addr.getLat());
            if (add != null) {
                return new ResponseDTO(Const.STATUS_OK, "成功", add);
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, "错误转换失败");
            }
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "坐标为空");
        }
    }

    @RequestMapping(value = {"/routeMatrix"}, method = RequestMethod.POST)
    public ResponseDTO queryRoute(@RequestBody MapRouteMatrixDTO matrix) {
        Map<String, Object> ret = service.route(matrix);
        if (ret != null)
            return new ResponseDTO(Const.STATUS_OK, "成功", ret);
        return new ResponseDTO(Const.STATUS_ERROR, "错误");
    }

    @RequestMapping(value = {"/price"}, method = RequestMethod.POST)
    public ResponseDTO calculate() {
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }

}
