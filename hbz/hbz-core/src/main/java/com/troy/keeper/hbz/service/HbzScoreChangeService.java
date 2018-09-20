package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzScoreChangeDTO;
import com.troy.keeper.hbz.po.HbzScoreChange;

/**
 * Created by leecheng on 2018/1/22.
 */
public interface HbzScoreChangeService extends BaseEntityService<HbzScoreChange, HbzScoreChangeDTO> {

    String createNo();

    Integer countByDelta(Long currentId);
}
