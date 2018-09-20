package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzTender;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by leecheng on 2017/11/21.
 */
public interface HbzTenderRepository extends BaseRepository<HbzTender, Long> {

    @Query("select t from HbzTender t where t.order.id = ?1")
    HbzTender findByOrderId(Long id);

}
