package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzRecharge;

/**
 * Created by leecheng on 2017/12/4.
 */
public interface HbzRechargeRepository extends BaseRepository<HbzRecharge, Long> {

    Long countByChargeNo(String chargeNo);

    HbzRecharge findByChargeNo(String chargeNo);

}
