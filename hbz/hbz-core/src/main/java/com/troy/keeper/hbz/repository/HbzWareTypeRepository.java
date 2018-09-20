package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzWareType;

/**
 * Created by leecheng on 2017/12/12.
 */
public interface HbzWareTypeRepository extends BaseRepository<HbzWareType, Long> {

    Long countByTypeNo(String typeNo);

}
