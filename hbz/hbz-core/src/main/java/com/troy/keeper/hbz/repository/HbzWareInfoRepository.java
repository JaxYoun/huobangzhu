package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzWareInfo;

/**
 * Created by leecheng on 2017/12/12.
 */
public interface HbzWareInfoRepository extends BaseRepository<HbzWareInfo, Long> {

    Long countByWareNo(String wareNo);

}
