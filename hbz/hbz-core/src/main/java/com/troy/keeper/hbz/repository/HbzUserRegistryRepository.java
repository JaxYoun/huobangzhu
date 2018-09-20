package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzUserRegistry;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by leecheng on 2017/10/25.
 */
public interface HbzUserRegistryRepository extends BaseRepository<HbzUserRegistry, Long> {

    @Query("select hr from HbzUserRegistry hr where hr.createdBy=?1 and hr.registryCode='PersonDriver' and hr.status=1")
    HbzUserRegistry findByCreatedBy(Long id);
}
