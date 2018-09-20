package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzOrderDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.service.HbzOrderService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.hbz.type.OrderTrans;
import com.troy.keeper.hbz.type.OrderType;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by leecheng on 2018/2/28.
 */
@RestController
@RequestMapping("/api/statics")
public class HbzStaticsResource {

    @Autowired
    HbzOrderService hbzOrderService;

    @Autowired
    HbzUserService hbzUserService;

    @Label("Web端 - 订单统计 - 货主订单统计")
    @RequestMapping(value = "/consignor/order", method = RequestMethod.POST)
    public ResponseDTO countsByYearAndOrderTypes(@RequestBody ConsignorOrderStaticsRequestWrapper requestParameterWrapper) throws Throwable {
        String startTime = requestParameterWrapper.getYear() + "-01-01";
        Long start = new SimpleDateFormat("yyyy-MM-dd").parse(startTime).getTime();
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(start);

        Calendar endDate = Calendar.getInstance();
        endDate.setTimeInMillis(start);
        endDate.add(Calendar.YEAR, 1);

        Long startTimeMillis = startDate.getTimeInMillis();
        Long endTimeMillis = endDate.getTimeInMillis();

        HbzUserDTO user = hbzUserService.currentUser();

        List<Map<String, Object>> countMap = new LinkedList<>();

        int lastIndex = requestParameterWrapper.getOrderTypes().size();
        int[][] counts = new int[requestParameterWrapper.getOrderTypes().size() + 1][requestParameterWrapper.getOrderTranses().size()];
        for (int i = 0; i < requestParameterWrapper.getOrderTypes().size(); i++) {
            Map<String, Object> typeMap = new HashMap<>();
            OrderType orderType = requestParameterWrapper.getOrderTypes().get(i);
            typeMap.put("orderType", orderType);
            for (int j = 0; j < requestParameterWrapper.getOrderTranses().size(); j++) {
                OrderTrans orderTrans = requestParameterWrapper.getOrderTranses().get(j);
                HbzOrderDTO query = new HbzOrderDTO();
                query.setCreateUserId(user.getId());
                query.setCreateTimeGE(startTimeMillis);
                query.setCreateTimeLE(endTimeMillis);
                query.setStatus("1");
                query.setOrderType(orderType);
                query.setOrderTrans(orderTrans);
                counts[i][j] = Integer.valueOf(String.valueOf(hbzOrderService.count(query)));
                counts[lastIndex][j] += counts[i][j];
                typeMap.put(orderTrans.toString(), counts[i][j]);
            }
            countMap.add(typeMap);
        }
        Map<String, Object> all = new HashMap<>();
        all.put("orderType", "ALL");
        for (int j = 0; j < requestParameterWrapper.getOrderTranses().size(); j++) {
            OrderTrans orderTrans = requestParameterWrapper.getOrderTranses().get(j);
            all.put(orderTrans.toString(), counts[lastIndex][j]);
        }
        countMap.add(all);
        return new ResponseDTO(Const.STATUS_OK, "Ok!!", countMap);
    }

    @Label("Web端 - 订单统计 - 货运方数量统计")
    @RequestMapping(value = "/provider/order", method = RequestMethod.POST)
    public ResponseDTO provCountsByYearAndOrderTypes(@RequestBody ConsignorOrderStaticsRequestWrapper requestParameterWrapper) throws Throwable {
        String startTime = requestParameterWrapper.getYear() + "-01-01";
        Long start = new SimpleDateFormat("yyyy-MM-dd").parse(startTime).getTime();
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(start);

        Calendar endDate = Calendar.getInstance();
        endDate.setTimeInMillis(start);
        endDate.add(Calendar.YEAR, 1);

        Long startTimeMillis = startDate.getTimeInMillis();
        Long endTimeMillis = endDate.getTimeInMillis();

        HbzUserDTO user = hbzUserService.getAdministrator(hbzUserService.currentUser().getId());

        List<Map<String, Object>> countMap = new LinkedList<>();

        int lastIndex = requestParameterWrapper.getOrderTypes().size();
        int[][] counts = new int[requestParameterWrapper.getOrderTypes().size() + 1][requestParameterWrapper.getOrderTranses().size()];
        for (int i = 0; i < requestParameterWrapper.getOrderTypes().size(); i++) {
            Map<String, Object> typeMap = new HashMap<>();
            OrderType orderType = requestParameterWrapper.getOrderTypes().get(i);
            typeMap.put("orderType", orderType);
            for (int j = 0; j < requestParameterWrapper.getOrderTranses().size(); j++) {
                OrderTrans orderTrans = requestParameterWrapper.getOrderTranses().get(j);
                HbzOrderDTO query = new HbzOrderDTO();
                query.setTakeUserId(user.getId());
                query.setCreateTimeGE(startTimeMillis);
                query.setCreateTimeLE(endTimeMillis);
                query.setStatus("1");
                query.setOrderType(orderType);
                query.setOrderTrans(orderTrans);
                counts[i][j] = Integer.valueOf(String.valueOf(hbzOrderService.count(query)));
                counts[lastIndex][j] += counts[i][j];
                typeMap.put(orderTrans.toString(), counts[i][j]);
            }
            countMap.add(typeMap);
        }
        Map<String, Object> all = new HashMap<>();
        all.put("orderType", "ALL");
        for (int j = 0; j < requestParameterWrapper.getOrderTranses().size(); j++) {
            OrderTrans orderTrans = requestParameterWrapper.getOrderTranses().get(j);
            all.put(orderTrans.toString(), counts[lastIndex][j]);
        }
        countMap.add(all);
        return new ResponseDTO(Const.STATUS_OK, "Ok!!", countMap);
    }


}

@Data
class ConsignorOrderStaticsRequestWrapper {

    private String year;
    private List<OrderType> orderTypes;
    private List<OrderTrans> orderTranses;

}

@Data
class ProvOrderStaticsRequestWrapper {

    private String year;
    private List<OrderType> orderTypes;
    private List<OrderTrans> orderTranses;

}