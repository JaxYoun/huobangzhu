package com.troy.keeper.hbz.repository;

import com.troy.keeper.hbz.dto.CostStaticsDTO;

import java.util.List;

/**
 * @Autohor: hecj
 * @Description: 使用实体查询费用统计repository
 * @Date: Created in 9:54  2018/3/1.
 * @Midified By:
 */
public interface CostStaticsRepository {
    //货主订单支出统计
    List<Object[]> queryOrderCostStatistics(Long currentId, CostStaticsDTO costStaticsDTO);
    //货主保证金支出统计
    List<Object[]> queryBondCostStatistics(Long currentId, CostStaticsDTO costStaticsDTO);
    //仓储支出
    Double queryWarehouseCostStatistics(Long currentId, CostStaticsDTO costStaticsDTO);
    //车主订单支出统计
    List<Object[]> queryOrderIncomeStatistics(Long currentId, CostStaticsDTO costStaticsDTO);
    //车主保证金收入统计
    List<Object[]> queryBondIncomeStatistics(Long currentId, CostStaticsDTO costStaticsDTO);
}
