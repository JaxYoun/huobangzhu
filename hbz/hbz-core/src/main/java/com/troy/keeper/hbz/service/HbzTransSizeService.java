package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzTransSizeDTO;
import com.troy.keeper.hbz.po.HbzTransSize;

/**
 * Created by leecheng on 2017/12/6.
 */
public interface HbzTransSizeService extends BaseEntityService<HbzTransSize, HbzTransSizeDTO> {

    HbzTransSizeDTO findByTransSize(Double transSize);

}
