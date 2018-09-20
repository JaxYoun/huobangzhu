package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzAssignWork;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by leecheng on 2018/1/18.
 */
public interface HbzAssignWorkRepository extends BaseRepository<HbzAssignWork, Long> {

    Long countByWorkNo(String workNo);

    @Query("select hw from HbzAssignWork hw where hw.id=?1")
    HbzAssignWork findById(Long id);

}
