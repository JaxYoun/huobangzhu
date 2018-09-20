package com.troy.keeper.management.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzSendOrder;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 李奥   帮我送
 * @date 2017/12/18.
 */
public interface HelpSendRepository  extends BaseRepository<HbzSendOrder, Long> {

//根据订单id 查询帮我送订单的 基本信息
    @Query("select   hs   from HbzSendOrder  hs  where  hs.id=?1")
    public HbzSendOrder findHbzSendOrder(Long  id);







}
