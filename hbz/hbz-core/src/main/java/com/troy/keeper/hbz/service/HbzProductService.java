package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzProductDTO;
import com.troy.keeper.hbz.po.HbzProduct;

/**
 * Created by leecheng on 2017/12/29.
 */
public interface HbzProductService extends BaseEntityService<HbzProduct, HbzProductDTO> {

    String createProductNo();

}
