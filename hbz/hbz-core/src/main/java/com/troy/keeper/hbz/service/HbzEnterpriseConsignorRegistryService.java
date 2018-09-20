package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzEnterpriseConsignorRegistryDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.po.HbzEnterpriseConsignorRegistry;

/**
 * Created by leecheng on 2017/11/17.
 */
public interface HbzEnterpriseConsignorRegistryService extends BaseEntityService<HbzEnterpriseConsignorRegistry, HbzEnterpriseConsignorRegistryDTO> {
    HbzEnterpriseConsignorRegistryDTO find(HbzUserDTO user, boolean basic);
}
