package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzMenuDTO;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.service.HbzMenuService;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.management.utils.MapSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * Created by leecheng on 2018/1/8.
 */
@RestController
@RequestMapping("/api/menu")
public class HbzMenuRes {

    @Autowired
    HbzMenuService hbzMenuService;

    /**
     * @param menuDTO
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseDTO add(@RequestBody HbzMenuDTO menuDTO) {
        String[] err = ValidationHelper.valid(menuDTO, "menu_ad");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证错误", err);
        }
        HbzMenuDTO menu = new HbzMenuDTO();
        new Bean2Bean().copyProperties(menuDTO, menu);
        menu.setStatus("1");
        hbzMenuService.save(menu);
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }

    /**
     * @param menuDTO
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseDTO up(@RequestBody HbzMenuDTO menuDTO) {
        String[] err = ValidationHelper.valid(menuDTO, "menu_up");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证错误", err);
        }
        HbzMenuDTO menu = hbzMenuService.get(menuDTO);
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(menuDTO, menu);
        menu.setStatus("1");
        hbzMenuService.save(menu);
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }

    /**
     * @param menuDTO
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseDTO delete(@RequestBody HbzMenuDTO menuDTO) {
        String[] err = ValidationHelper.valid(menuDTO, "menu_r");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证错误", err);
        }
        HbzMenuDTO menu = hbzMenuService.get(menuDTO);
        hbzMenuService.delete(menu);
        return new ResponseDTO(Const.STATUS_OK, "成功");
    }

    /**
     * @param menuDTO
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseDTO query(@RequestBody HbzMenuDTO menuDTO) {
        HbzMenuDTO menu = new HbzMenuDTO();
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(menuDTO, menu);
        menu.setStatus("1");
        return new ResponseDTO(Const.STATUS_OK, "成功", hbzMenuService.query(menu).stream().map(MapSpec::mapMenu).collect(Collectors.toList()));
    }

    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public ResponseDTO queryPage(@RequestBody HbzMenuDTO menuDTO) {
        HbzMenuDTO menu = new HbzMenuDTO();
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(menuDTO, menu);
        menu.setStatus("1");
        return new ResponseDTO(Const.STATUS_OK, "成功", hbzMenuService.queryPage(menu, menu.getPageRequest()).map(MapSpec::mapMenu));
    }

}
