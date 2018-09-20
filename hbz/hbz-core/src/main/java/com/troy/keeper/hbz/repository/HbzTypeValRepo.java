package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzTypeVal;

import java.util.List;

/**
 * Created by leecheng on 2017/11/22.
 */
public interface HbzTypeValRepo extends BaseRepository<HbzTypeVal, Long> {

    List<HbzTypeVal> getAllByTypeAndLanguage(String type, String language);

    HbzTypeVal getByTypeAndValAndLanguage(String type, String val, String language);

    /**
     * 通过key-value获取字典对象
     *
     * @param type
     * @param val
     * @return
     */
    HbzTypeVal getByTypeAndVal(String type, String val);
}
