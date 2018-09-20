package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzTransSize;

/**
 * Created by leecheng on 2017/12/6.
 */
public interface HbzTransSizeRepository extends BaseRepository<HbzTransSize, Long> {

    HbzTransSize findByTransSize(Double transSizes);

}
