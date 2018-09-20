package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzOrderDTO;
import com.troy.keeper.hbz.dto.HbzRateDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.service.HbzOrderService;
import com.troy.keeper.hbz.service.HbzRateService;
import com.troy.keeper.hbz.service.HbzScoreService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.hbz.type.OrderTrans;
import com.troy.keeper.hbz.type.RateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by leecheng on 2018/1/29.
 */
@RestController
@RequestMapping("/api/biz")
public class HbzBizSource {

    @Autowired
    HbzUserService hbzUserService;

    @Autowired
    HbzOrderService hbzOrderService;

    @Autowired
    HbzRateService hbzRateService;

    @Autowired
    HbzScoreService hbzScoreService;

    /**
     * type，我的身份,consignor我是货主，provider我是车主
     *
     * @param type
     * @return
     */
    @Label("App端 - 首页 - 待办事项")
    @RequestMapping(value = "/{type}/statics", method = RequestMethod.POST)
    public ResponseDTO statics(@PathVariable(name = "type") String type) {
        HbzUserDTO user = hbzUserService.currentUser();                     //个人
        HbzUserDTO admin = hbzUserService.getAdministrator(user.getId());   //所属企业

        //1、查询我创建的订单等待收货数量
        HbzOrderDTO orderWaitConfirmQuery = new HbzOrderDTO();
        orderWaitConfirmQuery.setStatus("1");
        orderWaitConfirmQuery.setOrderTrans(OrderTrans.WAIT_TO_CONFIRM);
        switch (type) {
            case "consignor":
                orderWaitConfirmQuery.setCreateUserId(user.getId());
                break;
            case "provider":
                orderWaitConfirmQuery.setTakeUserId(admin.getId());
                break;
        }
        Long wait_receive = hbzOrderService.count(orderWaitConfirmQuery);

        //2、等待评价
        HbzRateDTO rateQuery = new HbzRateDTO();
        rateQuery.setStatus("1");
        rateQuery.setStarLE(0);
        switch (type) {
            case "consignor":   //我是货主，等我对车主的评价数量
                rateQuery.setUserId(user.getId());
                rateQuery.setType(RateType.CONSIGNOR);
                break;
            case "provider":      //我是车主，我对货主的评价数据
                rateQuery.setUserId(admin.getId());
                rateQuery.setType(RateType.PROVIDER);
                break;
        }
        Long waite_rate = hbzRateService.count(rateQuery);

        //3、我是货主，我的运输中订单数量
        HbzOrderDTO orderTransportQuery = new HbzOrderDTO();
        orderTransportQuery.setStatus("1");
        orderTransportQuery.setOrderTrans(OrderTrans.TRANSPORT);
        switch (type) {
            case "consignor":           //我是货主，等待我确认订单数量
                orderTransportQuery.setCreateUserId(user.getId());
                break;
            case "provider":            //我是司机没有等待我确认的订单
                orderTransportQuery.setTakeUserId(admin.getId());
                break;
        }
        Long transport_count = hbzOrderService.count(orderTransportQuery);

        //4、待付款
        HbzOrderDTO waitPaymentOrderQuery = new HbzOrderDTO();
        waitPaymentOrderQuery.setStatus("1");
        waitPaymentOrderQuery.setOrderTrans(OrderTrans.WAIT_FOR_PAYMENT);
        switch (type) {
            case "consignor":           //我是货主，等待我付款的订单数量
                waitPaymentOrderQuery.setCreateUserId(user.getId());
                break;
            case "provider":            //我是司机，等待付款给我所属的企业的订单数量
                waitPaymentOrderQuery.setTakeUserId(admin.getId());
                break;
        }
        Long wait_payment_online = hbzOrderService.count(waitPaymentOrderQuery);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("wait_receive", wait_receive);
        map.put("waite_rate", waite_rate);
        map.put("transport_count", transport_count);
        map.put("wait_paymentonline", wait_payment_online);
        return new ResponseDTO(Const.STATUS_OK, "ok", map);
    }

}
