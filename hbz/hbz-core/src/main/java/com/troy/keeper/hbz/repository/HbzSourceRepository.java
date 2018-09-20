package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzSource;

/**
 * Created by leecheng on 2018/1/11.
 */
public interface HbzSourceRepository extends BaseRepository<HbzSource, Long> {

    Long countByLabel(String label);

    Long countBySrc(String src);

}
