package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzBrandDTO;
import com.troy.keeper.hbz.po.HbzBrand;

/**
 * Created by leecheng on 2017/12/28.
 */
public interface HbzBrandService extends BaseEntityService<HbzBrand,HbzBrandDTO> {

    String createBrandNo();

}
