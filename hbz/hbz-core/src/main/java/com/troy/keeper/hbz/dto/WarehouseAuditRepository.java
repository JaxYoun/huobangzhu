package com.troy.keeper.hbz.dto;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzWarehouseAudit;
import com.troy.keeper.hbz.po.Warehouse;
import org.springframework.data.jpa.repository.Query;

/**
 * @Autohor: hecj
 * @Description: 仓储审核Repository
 * @Date: Created in 13:55  2018/1/9.
 * @Midified By:
 */
public interface WarehouseAuditRepository extends BaseRepository<HbzWarehouseAudit, Long> {
    //根据id 查询该记录详细信息
    @Query("select hwa from HbzWarehouseAudit hwa where hwa.id=?1")
    HbzWarehouseAudit findById(Long id);

    @Query("select hwa from HbzWarehouseAudit hwa where hwa.warehouse.id=?1")
    HbzWarehouseAudit findByWarehouseId(Long warehouseId);
}
