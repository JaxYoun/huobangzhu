package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzScoreChange;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by leecheng on 2018/1/22.
 */
public interface HbzScoreChangeRepository extends BaseRepository<HbzScoreChange, Long> {

    Long countByRecNo(String recNo);


    @Query("select sum(cr.delta) from HbzScoreChange cr where cr.user.id=?1 and cr.delta < 0")
    Integer countByDelta(Long currentId);
}
