package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzTransEnterpriseRegistry;
import com.troy.keeper.hbz.po.HbzUserRegistry;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by leecheng on 2017/11/20.
 */
public interface HbzTransEnterpriseRegistryRepository extends BaseRepository<HbzTransEnterpriseRegistry, Long> {

    @Query("select hr from HbzTransEnterpriseRegistry hr where hr.user.id=?1 and hr.status=1")
    HbzTransEnterpriseRegistry findByUserId(Long userId);

}
