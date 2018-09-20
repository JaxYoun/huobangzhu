package com.troy.keeper.management.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzFslOrder;
import com.troy.keeper.hbz.po.HbzLtlOrder;
import com.troy.keeper.hbz.po.HbzPersonDriverRegistry;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 李奥
 * @date 2018/2/5.
 */
public interface LtlManagementRepository extends BaseRepository<HbzLtlOrder, Long> {

    //根据接单人用户的id 查询用户的车辆信息
    @Query("select  hpdr  from  HbzPersonDriverRegistry hpdr where hpdr.id=?1")
    public HbzPersonDriverRegistry findHbzPersonDriverRegistry(Long id);


}
