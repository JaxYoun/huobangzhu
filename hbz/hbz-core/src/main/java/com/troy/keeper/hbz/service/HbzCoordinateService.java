package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzCoordinateDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.po.HbzCoordinate;

/**
 * Created by leecheng on 2017/10/27.
 */
public interface HbzCoordinateService extends BaseEntityService<HbzCoordinate, HbzCoordinateDTO> {

    HbzCoordinateDTO findByUser(HbzUserDTO user);

}
