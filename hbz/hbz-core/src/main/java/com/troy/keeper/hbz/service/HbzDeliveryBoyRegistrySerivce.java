package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzDeliveryBoyRegistryDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.po.HbzDeliveryBoyRegistry;

/**
 * Created by leecheng on 2017/11/17.
 */
public interface HbzDeliveryBoyRegistrySerivce extends BaseEntityService<HbzDeliveryBoyRegistry, HbzDeliveryBoyRegistryDTO> {

    HbzDeliveryBoyRegistryDTO findHbzDeliveryBoyRegistryByUser(HbzUserDTO user,boolean isBasic);

}
