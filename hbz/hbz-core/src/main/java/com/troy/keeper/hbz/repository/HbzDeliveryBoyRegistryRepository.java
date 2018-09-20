package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzDeliveryBoyRegistry;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by leecheng on 2017/11/17.
 */
public interface HbzDeliveryBoyRegistryRepository extends BaseRepository<HbzDeliveryBoyRegistry, Long> {
    @Query("select hr from HbzDeliveryBoyRegistry hr where hr.user.id=?1  and hr.status=1")
    HbzDeliveryBoyRegistry findByUserId(Long userId);
}
