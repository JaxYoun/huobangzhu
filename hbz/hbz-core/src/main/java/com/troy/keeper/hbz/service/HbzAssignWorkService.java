package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzAssignWorkDTO;
import com.troy.keeper.hbz.po.HbzAssignWork;

/**
 * Created by leecheng on 2018/1/18.
 */
public interface HbzAssignWorkService extends BaseEntityService<HbzAssignWork, HbzAssignWorkDTO> {

    String createWorkNo();

}
