package com.troy.keeper.management.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzWarehouseAudit;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

/**
 * @Autohor: hecj
 * @Description: 仓储审核Repository
 * @Date: Created in 13:55  2018/1/9.
 * @Midified By:
 */
@Component("WarehouseAuditRepositoryMan")
public interface WarehouseAuditRepository  extends BaseRepository<HbzWarehouseAudit, Long> {
    //根据id 查询该记录详细信息
    @Query("select hwa from HbzWarehouseAudit hwa where hwa.id=?1")
    HbzWarehouseAudit findById(Long id);
}
