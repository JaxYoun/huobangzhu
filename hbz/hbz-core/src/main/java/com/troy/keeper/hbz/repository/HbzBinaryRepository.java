package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzBinary;

/**
 * Created by leecheng on 2017/12/18.
 */
public interface HbzBinaryRepository extends BaseRepository<HbzBinary, Long> {

    HbzBinary findByKey(String key);

}
