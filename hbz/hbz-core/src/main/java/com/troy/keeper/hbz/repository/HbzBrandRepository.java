package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzBrand;

/**
 * Created by leecheng on 2017/11/8.
 */
public interface HbzBrandRepository extends BaseRepository<HbzBrand, Long> {

    Long countByBrandNo(String brandNo);

}
