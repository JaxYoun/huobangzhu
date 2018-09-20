package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzRefund;

import java.util.List;

/**
 * Created by leecheng on 2018/3/6.
 */
public interface HbzRefundRepo extends BaseRepository<HbzRefund, Long> {

    List<HbzRefund> findByRefundNo(String refundNo);

    HbzRefund findByRequestNo(String requestNo);

}
