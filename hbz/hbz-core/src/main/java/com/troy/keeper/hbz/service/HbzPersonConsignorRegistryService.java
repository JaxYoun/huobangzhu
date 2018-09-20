package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzPersonConsignorRegistryDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.po.HbzPersonConsignorRegistry;

/**
 * Created by leecheng on 2017/11/16.
 */
public interface HbzPersonConsignorRegistryService extends BaseEntityService<HbzPersonConsignorRegistry, HbzPersonConsignorRegistryDTO> {
    HbzPersonConsignorRegistryDTO findUser(HbzUserDTO user, boolean basic);
}
