package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzPersonDriverRegistry;
import com.troy.keeper.hbz.po.HbzTransEnterpriseRegistry;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by leecheng on 2017/11/6.
 */
public interface HbzPersonDriverRegistryRepository extends BaseRepository<HbzPersonDriverRegistry, Long> {

    @Query("select hr from HbzPersonDriverRegistry hr where hr.user.id=?1  and hr.status=1")
    HbzPersonDriverRegistry findByUserId(Long userId);

}
