package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzWareTypeDTO;
import com.troy.keeper.hbz.po.HbzWareType;

import java.util.List;

/**
 * Created by leecheng on 2017/12/18.
 */
public interface HbzWareTypeService extends BaseEntityService<HbzWareType, HbzWareTypeDTO> {

    List<HbzWareTypeDTO> querySub(Long id);

    String createTypeNo(Long parentId);

}
