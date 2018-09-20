package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzScoreOrder;

import java.util.List;

/**
 * Created by leecheng on 2017/12/19.
 */
public interface HbzScoreOrderRepository extends BaseRepository<HbzScoreOrder, Long> {

    List<HbzScoreOrder> findByOrderNo(String orderNo);

}
