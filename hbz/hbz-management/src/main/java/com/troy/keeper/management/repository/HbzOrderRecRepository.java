package com.troy.keeper.management.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzOrderRecord;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 李奥
 * @date 2018/3/14.
 */
public interface HbzOrderRecRepository extends BaseRepository<HbzOrderRecord, Long> {


    @Query(" select  hor   from HbzOrderRecord hor where hor.order.id=?1 and hor.status='1'")
    public List<HbzOrderRecord>  findOrderRec(Long orderId);




}
