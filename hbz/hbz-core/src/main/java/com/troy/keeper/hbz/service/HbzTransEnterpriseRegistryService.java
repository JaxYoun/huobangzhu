package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzTransEnterpriseRegistryDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.po.HbzTransEnterpriseRegistry;

/**
 * Created by leecheng on 2017/11/20.
 */
public interface HbzTransEnterpriseRegistryService extends BaseEntityService<HbzTransEnterpriseRegistry, HbzTransEnterpriseRegistryDTO> {

    HbzTransEnterpriseRegistryDTO findTransEnterpriseRegistry(HbzUserDTO user,boolean basic);

}
