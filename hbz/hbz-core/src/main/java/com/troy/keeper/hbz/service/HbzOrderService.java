package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.CostStaticsDTO;
import com.troy.keeper.hbz.dto.HbzOrderDTO;
import com.troy.keeper.hbz.po.HbzOrder;
import com.troy.keeper.hbz.type.OrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by leecheng on 2017/10/24.
 */
public interface HbzOrderService extends BaseEntityService<HbzOrder, HbzOrderDTO> {

    String createNewOrderNo(OrderType orderType);

    Map<String, Object> completeOrder(String orderNo);

    HbzOrderDTO findByOrderNo(String orderNo);

    Page<HbzOrderDTO> getSpecialLineGoodsListByPage(HbzOrderDTO hbzOrderDTO, Pageable pageable) throws Exception;

    HbzOrderDTO getSpecialLineGoodsListDetailById(HbzOrderDTO hbzOrderDTO);

    /**
     * 更新订单流转状态
     *
     * @param hbzOrderDTO
     * @return
     */
    boolean updateOrderTrans(HbzOrderDTO hbzOrderDTO);

    /**
     * 费用支出统计
     */
    Map<String,Double> cost(CostStaticsDTO costStaticsDTO);
    /**
     * 费用收入统计
     */
    Map<String,Double> income(CostStaticsDTO costStaticsDTO);
}
