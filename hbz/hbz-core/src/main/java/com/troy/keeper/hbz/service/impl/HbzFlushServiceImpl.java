package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.hbz.dto.HbzBuyOrderDTO;
import com.troy.keeper.hbz.dto.HbzSendOrderDTO;
import com.troy.keeper.hbz.service.HbzBuyOrderServices;
import com.troy.keeper.hbz.service.HbzFlushService;
import com.troy.keeper.hbz.service.HbzRefundService;
import com.troy.keeper.hbz.service.HbzSendOrderService;
import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.OrderTrans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by leecheng on 2018/3/14.
 */
@Service
public class HbzFlushServiceImpl implements HbzFlushService {

    @Autowired
    HbzBuyOrderServices hbzBuyOrderService;

    @Autowired
    HbzSendOrderService hbzSendOrderService;

    @Autowired
    HbzRefundService hbzRefundService;

    public void auto() {
        try {
            HbzSendOrderDTO query = new HbzSendOrderDTO();
            query.setStatus("1");
            query.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);
            query.setEndTimeLE(System.currentTimeMillis());
            List<HbzSendOrderDTO> orders = hbzSendOrderService.query(query);
            orders.forEach(order -> {
                order.setOrderTrans(OrderTrans.OVER_TIME);
                hbzSendOrderService.save(order);
                hbzRefundService.refundImmediate(order.getOrderNo(), BusinessType.ORDER, null);
                order.setOrderTrans(OrderTrans.APPLY_FOR_REFUND);
                hbzSendOrderService.save(order);
            });
        } catch (Exception e) {

        }
        try {
            HbzBuyOrderDTO query = new HbzBuyOrderDTO();
            query.setStatus("1");
            query.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);
            query.setEndTimeLE(System.currentTimeMillis());
            List<HbzBuyOrderDTO> orders = hbzBuyOrderService.query(query);
            orders.forEach(order -> {
                order.setOrderTrans(OrderTrans.OVER_TIME);
                hbzBuyOrderService.save(order);
                hbzRefundService.refundImmediate(order.getOrderNo(), BusinessType.ORDER, null);
                order.setOrderTrans(OrderTrans.APPLY_FOR_REFUND);
                hbzBuyOrderService.save(order);
            });
        } catch (Exception e) {

        }
    }

}
