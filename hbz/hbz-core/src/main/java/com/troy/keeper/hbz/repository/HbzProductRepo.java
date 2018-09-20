package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzProduct;

/**
 * Created by leecheng on 2017/12/14.
 */
public interface HbzProductRepo extends BaseRepository<HbzProduct, Long> {

    Long countAllByProductNo(String abcd);

}
