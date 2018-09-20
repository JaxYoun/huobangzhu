package com.troy.keeper.hbz.app.rest.web;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzRoleDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.dto.RechargePriceDTO;
import com.troy.keeper.hbz.service.HbzRoleService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.service.RechargePriceService;
import com.troy.keeper.hbz.type.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author：YangJx
 * @Description：保证金类型及价格
 * @DateTime：2017/12/21 10:48
 */
@Slf4j
@RestController
@RequestMapping("/api/web/rechargePrice")
public class WebRechargePriceResource {

    @Autowired
    RechargePriceService rechargePriceService;

    @Autowired
    HbzRoleService roleService;

    @Autowired
    HbzUserService hbzUserService;

    /**
     * 根据当前用户身份获取保证金类型及价格
     *
     * @return
     */
    @PostMapping("/getRechargePriceListByUserRole")
    public ResponseDTO getRechargePriceListByUserRole() {
        HbzUserDTO currentUser = hbzUserService.currentUser();
        Map<String, Object> query = new HashMap<>();
        query.put("users.id", currentUser.getId());
        query.put("status", Const.STATUS_ENABLED);
        List<HbzRoleDTO> roleDTOList = roleService.query(query);
        List<Role> roleList = roleDTOList.stream().map(HbzRoleDTO::getRole).collect(Collectors.toList());

        List<RechargePriceDTO> rechargePriceDTOList = rechargePriceService.getRechargePriceListByUserRole(roleList, Const.STATUS_ENABLED);
        if (rechargePriceDTOList == null || rechargePriceDTOList.size() == 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "失败！", null);
        } else {
            return new ResponseDTO(Const.STATUS_OK, "成功！", rechargePriceDTOList);
        }
    }

}
