package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.RechargePriceDTO;
import com.troy.keeper.hbz.type.Role;

import java.util.List;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2017/12/21 10:39
 */
public interface RechargePriceService {

    List<RechargePriceDTO> getRechargePriceListByUserRole(List<Role> roleList, String status);

}
