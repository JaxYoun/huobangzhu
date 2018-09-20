package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzAuth;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by leecheng on 2017/10/13.
 */
public interface HbzAuthRepository extends BaseRepository<HbzAuth, Long> {

    @Query("select a from HbzAuth a where a.status = '1'")
    List<HbzAuth> findAvilable();

}
