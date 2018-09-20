package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzPay;

/**
 * Created by leecheng on 2017/10/30.
 */
public interface HbzPayRepository extends BaseRepository<HbzPay, Long> {

    HbzPay findByTradeNo(String tradeNo);

    HbzPay findByBusinessNo(String buinessNo);

}
