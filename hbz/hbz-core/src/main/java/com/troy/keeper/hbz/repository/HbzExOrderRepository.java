package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzExOrder;

/**
 * Created by leecheng on 2017/11/29.
 */
public interface HbzExOrderRepository extends BaseRepository<HbzExOrder, Long> {

    HbzExOrder findByOrderNo(String orderNo);

}
