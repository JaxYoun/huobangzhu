package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.CostStaticsDTO;
import com.troy.keeper.hbz.service.HbzBondService;
import com.troy.keeper.hbz.service.HbzOrderService;
import com.troy.keeper.hbz.service.WarehouseService;
import com.troy.keeper.hbz.type.OrderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Autohor: hecj
 * @Description: 费用统计Resource
 * @Date: Created in 10:03  2018/2/28.
 * @Midified By:
 */
@RestController
@RequestMapping("/api/statics/consignor")
public class HbzCostStatisticsResource {

    @Autowired
    HbzOrderService hbzOrderService;

    @Autowired
    HbzBondService hbzBondService;

    @Autowired
    private WarehouseService warehouseService;

    /**
     * 费用支出统计
     */
    @RequestMapping(value = "/cost", method = RequestMethod.POST)
    public ResponseDTO cost(@RequestBody CostStaticsDTO costStaticsDTO) {
        try {
            Map<String,Double> map = new HashMap<>();
            //设置订单类型初始值
            Class<?> enumClass = OrderType.class;
            Method valuesMethod = enumClass.getMethod("values");
            Object[] values = (Object[]) valuesMethod.invoke(null);
            for (Object value : values) {
                String key = value.toString();
                map.put(key,0.0);
            }
            //设置保证金初始值
            map.put("BOND_FSL_C500",0.0);
            map.put("BOND_LTL_C200",0.0);
            map.put("BOND_SL_EC2000",0.0);
            //订单费用
            Map<String,Double> orderCosts = hbzOrderService.cost(costStaticsDTO);
            //保证金费用
            Map<String,Double> cautionMoneyCosts = hbzBondService.cost(costStaticsDTO);
            //仓储租赁诚意金
            Map<String,Double> wareHouse =  warehouseService.cost(costStaticsDTO);
            map.putAll(orderCosts);//从数据库查询结果有就覆盖初始值
            map.putAll(cautionMoneyCosts);
            map.putAll(wareHouse);
            return new ResponseDTO(Const.STATUS_OK, "查询成功", map);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseDTO(Const.STATUS_ERROR, "查询失败", null);
        }
    }

    /**
     * 费用收入统计
     */
    @RequestMapping(value = "/income", method = RequestMethod.POST)
    public ResponseDTO income(@RequestBody CostStaticsDTO costStaticsDTO) {
        try {
            Map<String,Double> map = new HashMap<>();
            //设置订单类型初始值
            Class<?> enumClass = OrderType.class;
            Method valuesMethod = enumClass.getMethod("values");
            Object[] values = (Object[]) valuesMethod.invoke(null);
            for (Object value : values) {
                String key = value.toString();
                map.put(key,0.0);
            }
            //设置保证金初始值
            map.put("BOND_FSL_D500",0.0);
            map.put("BOND_LTL_D200",0.0);
            map.put("BOND_SL_ED2000",0.0);
            //订单费用
            Map<String,Double> orderCosts = hbzOrderService.income(costStaticsDTO);
            //保证金费用
            Map<String,Double> cautionMoneyCosts = hbzBondService.income(costStaticsDTO);
            map.putAll(orderCosts);//从数据库查询结果有就覆盖初始值
            map.putAll(cautionMoneyCosts);
            return new ResponseDTO(Const.STATUS_OK, "查询成功", map);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseDTO(Const.STATUS_ERROR, "查询失败", null);
        }
    }

}
