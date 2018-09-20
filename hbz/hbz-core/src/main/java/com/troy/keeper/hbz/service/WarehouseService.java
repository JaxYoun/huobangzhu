package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.CostStaticsDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.dto.WarehouseDTO;
import com.troy.keeper.hbz.dto.WarehouseEarnestOrderDTO;
import com.troy.keeper.hbz.po.Warehouse;
import com.troy.keeper.hbz.po.WarehouseEarnestOrder;
import com.troy.keeper.hbz.vo.WarehouseEarnestOrderVO;
import com.troy.keeper.hbz.vo.WarehouseVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2017/12/21 16:16
 */
public interface WarehouseService {

    /**
     * 创建一条仓储资讯
     *
     * @param warehouseDTO
     * @return
     */
    WarehouseVO createWarehouse(WarehouseDTO warehouseDTO);

    HbzUserDTO findByWarehourse(WarehouseDTO warehouseDTO);

    /**
     * 删除一条仓储资讯
     *
     * @param warehouseDTO
     * @return
     */
    boolean deleteWarehouse(WarehouseDTO warehouseDTO);

    /**
     * 获取仓储资讯详情
     *
     * @param warehouseDTO
     * @return
     */
    WarehouseVO getWarehouseDetail(WarehouseDTO warehouseDTO);

    /**
     * 修改仓储资讯
     *
     * @param warehouseDTO
     * @return
     */
    boolean updateWarehouse(WarehouseDTO warehouseDTO);

    /**
     * 根据id获取仓储
     *
     * @param warehouseDTO
     * @return
     */
    WarehouseVO getWarehouseById(WarehouseDTO warehouseDTO);

    /**
     * 分页条件查询我创建的仓储资讯
     *
     * @param warehouseDTO
     * @return
     */
    Page<WarehouseVO> getMyWarehouseListByPage(WarehouseDTO warehouseDTO, Pageable pageable);

    /**
     * 生成仓储租赁诚意金订单
     *
     * @param warehouseEarnestOrderDTO
     * @return
     */
    WarehouseEarnestOrderVO generateWarehouseEarnestOrder(WarehouseEarnestOrderDTO warehouseEarnestOrderDTO);

    /**
     * 分页条件查询我的仓储诚意金订单
     *
     * @param warehouseEarnestOrderDTO
     * @param pageable
     * @return
     */
    Page<WarehouseEarnestOrderVO> getMyWarehouseEarnestOrderByPage(WarehouseEarnestOrderDTO warehouseEarnestOrderDTO, Pageable pageable);

    /**
     * 获取仓储租赁诚意金详情
     *
     * @param warehouseEarnestOrderDTO
     * @return
     */
    WarehouseEarnestOrderVO getWarehouseEarnestOrderDetail(WarehouseEarnestOrderDTO warehouseEarnestOrderDTO);


    /**
     * 费用支出统计
     */
    Map<String,Double> cost(CostStaticsDTO costStaticsDTO);

    /**
     * 通过订单号查询仓储诚意金
     * @param orderNo
     * @return
     */
    WarehouseEarnestOrder findByOrderNo(String orderNo);

    /**
     * 修改仓储诚意金状态
     * @param warehouseOrder
     */
    void save(WarehouseEarnestOrder warehouseOrder);
}