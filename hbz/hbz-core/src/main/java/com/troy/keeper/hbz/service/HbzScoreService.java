package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzScoreDTO;
import com.troy.keeper.hbz.po.HbzScore;

/**
 * Created by leecheng on 2017/12/19.
 */
public interface HbzScoreService extends BaseEntityService<HbzScore, HbzScoreDTO> {

    HbzScoreDTO attach(Long userId);

}
