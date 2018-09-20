package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzOrder;
import com.troy.keeper.hbz.type.OrderTrans;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by leecheng on 2017/10/24.
 */
public interface HbzOrderRepository extends BaseRepository<HbzOrder, Long> {

    HbzOrder findFirstByOrderNo(String orderNo);

    /**
     * 根据订单id更新订单流转状态
     *
     * @param orderTrans
     * @param id
     * @return
     */
    @Modifying
    @Query("update HbzOrder as t set t.orderTrans = ?1 where t.id = ?2")
    Integer updateOrderTransById(OrderTrans orderTrans, Long id);
    ///**
    // * 费用支出统计查询
    // */
    //@Query("select ho.orderType,sum(ho.amount) from HbzOrder ho where ho.createUser.id=?1  and (ho.orderTrans='PAID' or ho.orderTrans='LIQUIDATION_COMPLETED') and ho.createdDate >=?2 and ho.createdDate <=?3 group by ho.orderType")
    //List<Object[]> queryCostStatistics(Long currentId,Long startDate,Long endDate);
}
