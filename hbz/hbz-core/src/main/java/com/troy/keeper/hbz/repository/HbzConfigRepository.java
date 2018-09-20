package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzConfig;

/**
 * Created by leecheng on 2017/10/19.
 */
public interface HbzConfigRepository extends BaseRepository<HbzConfig, Long> {

    HbzConfig findByKey(String k);

}
