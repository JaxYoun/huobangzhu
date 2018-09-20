package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzPersonDriverRegistryDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.po.HbzPersonDriverRegistry;

/**
 * Created by leecheng on 2017/11/6.
 */
public interface HbzPersonDriverRegistryService extends BaseEntityService<HbzPersonDriverRegistry, HbzPersonDriverRegistryDTO> {

    HbzPersonDriverRegistryDTO find(HbzUserDTO user, boolean basic);

}
