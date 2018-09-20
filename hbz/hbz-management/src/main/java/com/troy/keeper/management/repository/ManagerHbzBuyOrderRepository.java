package com.troy.keeper.management.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzBuyOrder;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 李奥
 * @date 2017/12/11.  帮我买
 */
public interface ManagerHbzBuyOrderRepository extends BaseRepository<HbzBuyOrder, Long> {
   //通过订单id 查询订单详情
    @Query("select  hbo  from HbzBuyOrder hbo  where hbo.id=?1")
    public HbzBuyOrder findHbzBuyOrder(Long id);


}
