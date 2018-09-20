package com.troy.keeper.management.service;

import com.troy.keeper.hbz.po.HbzWarehouseAudit;
import com.troy.keeper.management.dto.WarehouseAuditDTO;
import com.troy.keeper.management.dto.WarehouseEarnestOrderDTO;
import com.troy.keeper.management.dto.WarehouseManageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Autohor: hecj
 * @Description: 仓储审核Service
 * @Date: Created in 11:43  2018/1/9.
 * @Midified By:
 */
public interface WarehouseAuditService {

    HbzWarehouseAudit findById(Long id);

    //审核列表查询
    Page<WarehouseAuditDTO> queryWarehouseAudit(WarehouseAuditDTO warehouseAuditDTO, Pageable pageable);
    //审核新增审核
    Boolean updateWarehouseAudit(WarehouseAuditDTO warehouseAuditDTO);
    //审核查询详情
    WarehouseAuditDTO queryWarehouseAuditDetail(WarehouseAuditDTO warehouseAuditDTO);
    //管理列表查询
    Page<WarehouseManageDTO> queryWarehouseManage(WarehouseManageDTO warehouseManageDTO, Pageable pageable);
    //管理查询详情
    WarehouseManageDTO queryWarehouseManageDetail(WarehouseManageDTO warehouseManageDTO);
    //管理新增审核
    Boolean updateWarehouseManage(WarehouseManageDTO warehouseManageDTO);
    //定时更新仓储过期
    void updateLifecycleTimer();
    //修改仓储过期
    Boolean updateLifecycleOverdue(WarehouseManageDTO warehouseManageDTO);
    //保证金分页列表查询
    Page<WarehouseEarnestOrderDTO>  queryWarehouseEarnestOrderByPage(WarehouseEarnestOrderDTO warehouseEarnestOrderDTO, Pageable pageable);
    //保证金详情查询
    WarehouseEarnestOrderDTO queryWarehouseEarnestOrderDetail(WarehouseEarnestOrderDTO warehouseEarnestOrderDTO);
    //申请退款（租赁诚意金）
    String refundWarehouseEarnestOrder(WarehouseEarnestOrderDTO warehouseEarnestOrderDTO);
}
