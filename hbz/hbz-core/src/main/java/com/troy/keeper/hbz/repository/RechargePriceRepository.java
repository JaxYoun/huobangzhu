package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.RechargePrice;
import com.troy.keeper.hbz.type.Role;

import java.util.List;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2017/12/21 10:30extends BaseRepository<HbzWareType, Long>
 */
public interface RechargePriceRepository extends BaseRepository<RechargePrice, Long> {

    List<RechargePrice> getRechargePriceByRoleInAndStatus(List<Role> roleList, String status);

}
