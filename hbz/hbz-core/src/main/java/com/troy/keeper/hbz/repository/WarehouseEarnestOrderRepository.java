package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.WarehouseEarnestOrder;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2017/12/29 14:14
 */
public interface WarehouseEarnestOrderRepository extends BaseRepository<WarehouseEarnestOrder, Long> {
    ///**
    // * 费用支出统计查询
    // */
    //@Query("select sum(hw.earnestPrice) from WarehouseEarnestOrder hw where hw.createUser.id=?1 and hw.payStatus='PAID'")
    //Double queryCostStatistics(Long currentId);

    @Query("select we from WarehouseEarnestOrder we where we.orderNo=?1")
    WarehouseEarnestOrder findByOrderNo(String orderNo);
}
