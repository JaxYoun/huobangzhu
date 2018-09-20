package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.HbzCoordinateDTO;
import com.troy.keeper.hbz.dto.HbzOrderDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.po.HbzCoordinate;
import com.troy.keeper.hbz.po.HbzOrder;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.service.mapper.HbzAreaMapper;
import com.troy.keeper.hbz.service.mapper.HbzOrderMapper;
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
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2018/2/27.
 */
@RestController
@RequestMapping("/api/statics")
public class StaticsRest {

    @Autowired
    HbzAreaService hbzAreaService;

    @Autowired
    HbzOrderService hbzOrderService;

    @Autowired
    HbzAreaMapper hbzAreaMapper;

    @Autowired
    EntityService entityService;

    @Autowired
    HbzOrderMapper hbzOrderMapper;

    @Autowired
    HbzCoordinateService hbzCoordinateService;

    @Autowired
    MapService mapService;

    @RequestMapping(value = "/orderCountsByArea")
    public ResponseDTO staticsByArea(@RequestBody OrderCountsByAreaRequestWrapper wrapper) {

        HbzAreaDTO a_query = new HbzAreaDTO();
        a_query.setParentCode(wrapper.getAreaCode());
        a_query.setStatus("1");
        List<HbzAreaDTO> areas = hbzAreaService.query(a_query);

        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < areas.size(); i++) {
            HbzAreaDTO area = areas.get(i);
            Map<String, Object> as = new HashMap<>();
            as.put("areaCode", area.getOutCode());
            as.put("areaName", area.getAreaName());
            as.put("level", area.getLevel());
            List<Object> orderCounts = new ArrayList<>();
            for (int j = 0; j < wrapper.getOrderTypes().size(); j++) {
                OrderType orderType = wrapper.getOrderTypes().get(j);
                HbzOrderDTO query = new HbzOrderDTO();
                query.setStatus("1");
                query.setOrderType(orderType);
                if (area.getLevel().intValue() == 0)
                    query.setOriginAreaCodeLIKE("______");
                else if (area.getLevel().intValue() == 1)
                    query.setOriginAreaCodeLIKE(area.getOutCode().substring(0, 2) + "____");
                else if (area.getLevel().intValue() == 2)
                    query.setOriginAreaCodeLIKE(area.getOutCode().substring(0, 4) + "__");
                else if (area.getLevel().intValue() == 3)
                    query.setOriginAreaCode(area.getOutCode());
                Long count = hbzOrderService.count(query);
                orderCounts.add(count);
            }
            as.put("orderCounts", orderCounts);
            list.add(as);
        }
        return new ResponseDTO(Const.STATUS_OK, "Ok!", list);
    }

    @RequestMapping(value = "/orderDistributionByArea")
    public ResponseDTO distributionByArea(@RequestBody OrderCountsByAreaRequestWrapper wrapper) {
        HbzAreaDTO area = hbzAreaService.findByOutCode(wrapper.getAreaCode());
        HbzOrderDTO query = new HbzOrderDTO();
        query.setStatus("1");
        query.setOrderTrans(OrderTrans.TRANSPORT);
        query.setOrderTypes(wrapper.getOrderTypes());
        if (area.getLevel().intValue() == 0)
            query.setOriginAreaCodeLIKE("______");
        else if (area.getLevel().intValue() == 1)
            query.setOriginAreaCodeLIKE(area.getOutCode().substring(0, 2) + "____");
        else if (area.getLevel().intValue() == 2)
            query.setOriginAreaCodeLIKE(area.getOutCode().substring(0, 4) + "__");
        else if (area.getLevel().intValue() == 3)
            query.setOriginAreaCode(area.getOutCode());
        List<HbzOrderDTO> orders = entityService.query(HbzOrder.class, hbzOrderMapper, query, Arrays.asList("id", "orderType", "createUser"));
        List<Map<String, Object>> list = orders.stream().map(order -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", order.getId());
            HbzUserDTO user = order.getCreateUser();
            map.put("orderType", order.getOrderType());
            HbzCoordinateDTO coordinate = hbzCoordinateService.findByUser(user);
            if (coordinate != null) {
                Map<String, Object> add = new HashMap<>();
                add.put("lng", coordinate.getPointX());
                add.put("lat", coordinate.getPointY());
                add.put("add", mapService.getLocationAddress(coordinate.getPointX(), coordinate.getPointY()));
                map.put("add", add);
            }
            return map;
        }).filter(map -> map.get("add") != null).collect(Collectors.toList());
        return new ResponseDTO(Const.STATUS_OK, "Ok!", list);
    }

    @Label("管理端 - 订单表达式 - 可视化分析")
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public ResponseDTO countsByYearAndOrderTypes(@RequestBody OrderStaticsRequestWrapper requestParameterWrapper) throws Throwable {
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
                query.setStatus("1");
                query.setOrderType(orderType);
                query.setOrderTrans(orderTrans);
                counts[i][j] = Integer.parseInt(String.valueOf(hbzOrderService.count(query)));
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

    @Label("管理端 - 订单表达式 - 可视化分析")
    @RequestMapping(value = "/orderbyreal", method = RequestMethod.POST)
    public ResponseDTO countsByReal(@RequestBody OrderCountsByRealStaticsRequestWrapper requestParameterWrapper) throws Throwable {
        Map<String, Long> counts = new HashMap<>();
        Long currentTimestamp = System.currentTimeMillis();
        Long lastFiveMinute = currentTimestamp - 5L * 60L * 1000L;
        for (OrderType orderType : requestParameterWrapper.getOrderTypes()) {
            HbzOrderDTO query = new HbzOrderDTO();
            query.setStatus("1");
            query.setOrderType(orderType);
            query.setCreateTimeLE(currentTimestamp);
            query.setCreateTimeGE(lastFiveMinute);
            Long count = hbzOrderService.count(query);
            counts.put(orderType.toString(), count);
        }
        return new ResponseDTO(Const.STATUS_OK, "Ok!!", counts);
    }
}

@Data
class OrderStaticsRequestWrapper {

    private List<OrderType> orderTypes;
    private List<OrderTrans> orderTranses;

}

@Data
class OrderCountsByRealStaticsRequestWrapper {

    private String areaCode;
    private List<OrderType> orderTypes;

}

@Data
class OrderCountsByAreaRequestWrapper {

    private String areaCode;

    private List<OrderType> orderTypes;

}
