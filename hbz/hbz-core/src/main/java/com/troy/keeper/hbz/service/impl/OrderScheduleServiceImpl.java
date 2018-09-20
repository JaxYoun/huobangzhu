package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzFslOrderDTO;
import com.troy.keeper.hbz.dto.HbzLtlOrderDTO;
import com.troy.keeper.hbz.dto.HbzOrderDTO;
import com.troy.keeper.hbz.dto.HbzOrderRecordDTO;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.type.BizCode;
import com.troy.keeper.hbz.type.OrderTrans;
import com.troy.keeper.hbz.type.OrderType;
import com.troy.keeper.hbz.type.SettlementType;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

/**
 * Created by leecheng on 2017/11/14.
 */
@Service("OrderScheduleService")
@Transactional
@CommonsLog
public class OrderScheduleServiceImpl implements OrderScheduleService {

    @Autowired
    HbzFslOrderService hbzFslOrderService;

    @Autowired
    HbzLtlOrderService hbzLtlOrderService;

    @Autowired
    HbzOrderService hbzOrderService;

    @Autowired
    HbzPledgeService hbzPledgeService;

    @Autowired
    HbzOrderRecordService hbzOrderRecordService;

    public void run() {
        try {
            HbzFslOrderDTO q = new HbzFslOrderDTO();
            q.setOrderTakeStartLT(System.currentTimeMillis());
            q.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);
            q.setStatus(Const.STATUS_ENABLED);
            List<HbzFslOrderDTO> orders = hbzFslOrderService.query(q);
            orders.stream().forEach(order -> {
                order.setOrderTrans(OrderTrans.OVER_TIME);
                hbzFslOrderService.save(order);
            });
        } catch (Exception e) {
            log.error(e);
        }
        try {
            HbzLtlOrderDTO q = new HbzLtlOrderDTO();
            q.setOrderTakeStartLT(System.currentTimeMillis());
            q.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);
            q.setStatus(Const.STATUS_ENABLED);
            List<HbzLtlOrderDTO> orders = hbzLtlOrderService.query(q);
            orders.stream().forEach(order -> {
                order.setOrderTrans(OrderTrans.OVER_TIME);
                hbzLtlOrderService.save(order);
            });
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public void s() {
        Long currentTime = System.currentTimeMillis();
        HbzOrderDTO q = new HbzOrderDTO();
        q.setOrderTypes(Arrays.asList(OrderType.FSL, OrderType.LTL));
        q.setStatus("1");
        q.setSettlementType(SettlementType.ONLINE_PAYMENT);
        q.setOrderTranses(Arrays.asList(OrderTrans.LOCKED_ORDER));
        q.setTakeTimeLE(currentTime - 20L * 60L * 1000L);
        List<HbzOrderDTO> orders = hbzOrderService.query(q);
        orders.forEach(order -> {
            switch (order.getOrderType()) {
                case LTL:
                    //解除司机的保证金，订单变为等待接单
                    hbzPledgeService.unPledge(order.getOrderNo(), BizCode.ORDER, "BOND_LTL", "D200");
                    order.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);
                    hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), null, null, order.getOrderTrans(), System.currentTimeMillis()));
                    order.setTakeTime(null);
                    order.setTakeUser(null);
                    order.setTakeUserId(null);
                    order.setAgentTime(null);
                    order.setAgent(null);
                    order.setAgentId(null);
                    hbzOrderService.save(order);
                    break;
                case FSL:
                    //解除司机的保证金，订单变为等待接单
                    hbzPledgeService.unPledge(order.getOrderNo(), BizCode.ORDER, "BOND_FSL", "D500");
                    order.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);
                    hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), null, null, order.getOrderTrans(), System.currentTimeMillis()));
                    order.setTakeTime(null);
                    order.setTakeUser(null);
                    order.setTakeUserId(null);
                    order.setAgentTime(null);
                    order.setAgent(null);
                    order.setAgentId(null);
                    hbzOrderService.save(order);
                    break;
            }
        });
    }
}
