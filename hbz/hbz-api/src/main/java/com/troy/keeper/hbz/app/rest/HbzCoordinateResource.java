package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.HbzCoordinateDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.service.HbzAreaService;
import com.troy.keeper.hbz.service.HbzCoordinateService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.service.MapService;
import com.troy.keeper.hbz.service.wrapper.AddInfo;
import com.troy.keeper.hbz.sys.ApplicationContextHolder;
import com.troy.keeper.hbz.sys.annotation.Label;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by leecheng on 2017/10/27.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/user/app/coordinate")
public class HbzCoordinateResource {

    @Autowired
    private HbzCoordinateService hbzCoordinateService;

    @Autowired
    HbzAreaService hbzAreaService;

    @Label("App端 - 用户 - 位置 - 位置同步")
    @RequestMapping(value = "/sync", method = RequestMethod.POST)
    public ResponseDTO sync(@RequestBody HbzCoordinateDTO hbzCoordinateDTO) {
        HbzUserService hbzUserService = ApplicationContextHolder.getService(HbzUserService.class);
        MapService mapService = ApplicationContextHolder.getService(MapService.class);
        HbzUserDTO user = hbzUserService.currentUser();
        if (user == null) {
            return new ResponseDTO("500", "用户未指定");
        }
        if (hbzCoordinateDTO.getId() != null) {
            return new ResponseDTO("500", "不能手动指定标识");
        } else {
            String[] errors = ValidationHelper.valid(hbzCoordinateDTO, "sync");
            if (null != errors && errors.length > 0) {
                return new ResponseDTO(Const.STATUS_ERROR, "验证失败", errors);
            } else {
                //删除过去该用户的坐标数据
                HbzCoordinateDTO query = new HbzCoordinateDTO();
                query.setStatus("1");
                query.setUserId(user.getId());
                List<HbzCoordinateDTO> coordinateDTOS = hbzCoordinateService.query(query);
                coordinateDTOS.forEach(hbzCoordinateService::delete);

                AddInfo addInfo = mapService.getLocationX(hbzCoordinateDTO.getPointX(), hbzCoordinateDTO.getPointY());


                //新增用户坐标
                HbzCoordinateDTO hbzCoordinate = new HbzCoordinateDTO();
                BeanUtils.copyProperties(hbzCoordinateDTO, hbzCoordinate);
                hbzCoordinate.setUserId(SecurityUtils.getCurrentUserId());
                hbzCoordinate.setSyncMillis(System.currentTimeMillis());
                hbzCoordinate.setStatus(Const.STATUS_ENABLED);
                hbzCoordinate = hbzCoordinateService.save(hbzCoordinate);
                if (hbzCoordinate != null) {
                    if (addInfo != null) {
                        HbzAreaDTO area = hbzAreaService.findByOutCode(addInfo.getCityCode());
                        if (area != null) {
                            hbzCoordinate.setAreaId(area.getId());
                            hbzCoordinateService.save(hbzCoordinate);
                        }
                    }
                    return new ResponseDTO(Const.STATUS_OK, "保存成功", null);
                } else {
                    return new ResponseDTO(Const.STATUS_ERROR, "保存失败", null);
                }
            }
        }
    }
}
