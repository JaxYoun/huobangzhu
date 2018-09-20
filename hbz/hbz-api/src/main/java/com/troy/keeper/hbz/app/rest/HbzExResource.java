package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzExOrderMapDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.HbzExOrderDTO;
import com.troy.keeper.hbz.dto.HbzOrderRecordDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.TimeMillisFormater;
import com.troy.keeper.hbz.sys.annotation.Config;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.hbz.type.OrderTrans;
import com.troy.keeper.hbz.type.OrderType;
import com.troy.keeper.hbz.type.SettlementType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/11/30.
 */
@RestController
@RequestMapping("/api/order/ex")
public class HbzExResource {

    @Autowired
    private HbzExOrderService hbzExOrderService;

    @Autowired
    private HbzOrderService hbzOrderService;

    @Autowired
    private MapService map;

    @Autowired
    private HbzUserService hbzUserService;

    @Config("com.hbz.order.buy.limit")
    private Long limit;

    @Config("com.hbz.location.x.width")
    private Double xWidth;

    @Config("com.hbz.location.y.width")
    private Double yWidth;
    @Autowired
    HbzOrderRecordService hbzOrderRecordService;

    @Autowired
    private HbzFormulaService hbzFormulaService;

    @Autowired
    HbzWeightSectionService hbzWeightSectionService;

    @Config("com.hbz.price.ex.default.formula")
    private String defaultFormula;

    @Config("com.hbz.price.ex.remote.formula")
    private String remoteFormula;

    @Config("com.hbz.price.ex.specialadministrativeregion.formula")
    private String spec;

    @Config("com.hbz.price.ex.remote.area.code")
    private String remoteCode;

    @Config("com.hbz.price.ex.specialadministrativeregion.area.code")
    private String scode;

    @Autowired
    HbzAreaService areaService;

    @Label("App端 - 快递 - 货主 - 创建")
    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    public ResponseDTO createOrder(@RequestBody HbzExOrderMapDTO orderMapDTO) {
        if (orderMapDTO.getId() != null) {
            return new ResponseDTO<>(Const.STATUS_VALIDATION_ERROR, "非法操作");
        } else {
            String[] errors = ValidationHelper.valid(orderMapDTO, "ex_order_create");
            if (errors != null && errors.length > 0) {
                return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证失败", errors);
            } else {
                String orderNo = hbzOrderService.createNewOrderNo(OrderType.EX);
                HbzExOrderDTO order = new HbzExOrderDTO();
                new Bean2Bean(
                        new PropertyMapper<>("orderTakeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
                ).copyProperties(orderMapDTO, order);
                if (StringHelper.isNullOREmpty(order.getOriginInfo())) {
                    if (order.getOriginX() != null && order.getOriginY() != null) {
                        Map<String, Object> map = this.map.getLocation(order.getOriginX(), order.getOriginY());
                        String address = (String) map.get("formatted_address");
                        order.setOriginInfo(address);
                    }
                }
                if (StringHelper.isNullOREmpty(order.getDestInfo())) {
                    if (order.getDestX() != null && order.getDestY() != null) {
                        Map<String, Object> map = this.map.getLocation(order.getDestX(), order.getDestY());
                        String address = (String) map.get("formatted_address");
                        order.setDestInfo(address);
                    }
                }
                //设置帮买订单
                order.setOrderType(OrderType.EX);
                //设置新建状态
                //order.setOrderTrans(OrderTrans.NEW);
                //设置确认状态
                order.setOrderTrans(OrderTrans.CONFIRMED);

                order.setSettlementType(SettlementType.ONLINE_PAYMENT);
                //设置业务订单号
                order.setOrderNo(orderNo);
                order.setCreateUserId(hbzUserService.currentUser().getId());
                //址区
                HbzAreaDTO originArea = areaService.findByOutCode(orderMapDTO.getOriginAreaCode());
                HbzAreaDTO destArea = areaService.findByOutCode(orderMapDTO.getDestAreaCode());
                order.setOriginArea(originArea);
                order.setOriginAreaId(originArea.getId());
                order.setDestArea(destArea);
                order.setDestAreaId(destArea.getId());

                order.setCreateTime(System.currentTimeMillis());

                //置未删除状态
                order.setStatus(Const.STATUS_ENABLED);
                if ((order = hbzExOrderService.save(order)) != null) {
                    hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), order.getOrderTrans(), System.currentTimeMillis()));
                    Map<String, Object> id = new HashMap<>();
                    id.put("id", order.getId());
                    id.put("orderNo", order.getOrderNo());
                    return new ResponseDTO(Const.STATUS_OK, "保存成功", id);
                } else {
                    return new ResponseDTO(Const.STATUS_ERROR, "保存失败", null);
                }
            }
        }
    }

    @Label("App - 快递 - 删除")
    @RequestMapping(value={"/delete"})
    public ResponseDTO delete(@RequestBody HbzExOrderMapDTO exOrderMapDTO){
        HbzExOrderDTO ex = hbzExOrderService.findById(exOrderMapDTO.getId());
        if(Arrays.asList(OrderTrans.NEW,OrderTrans.CONFIRMED).contains(ex.getOrderTrans())) {
            hbzExOrderService.delete(ex);
            return new ResponseDTO(Const.STATUS_OK, "操作成功");
        }else{
            return new ResponseDTO(Const.STATUS_ERROR, "操作失敗，無法更改這種狀態的快遞");
        }
    }

    @Deprecated
    //确认货主
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public ResponseDTO confirmOrder(@RequestBody HbzExOrderMapDTO orderMapDTO, HttpServletRequest request, HttpServletResponse response) {
        if (orderMapDTO.getId() == null) {
            new ResponseDTO(Const.STATUS_ERROR, "订单标识为空");
        }
        String[] errors = ValidationHelper.valid(orderMapDTO, "ex_ord_confirm");
        if (errors != null && errors.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证失败", errors);
        }
        HbzExOrderDTO query = new HbzExOrderDTO();
        query.setId(orderMapDTO.getId());
        HbzExOrderDTO order = hbzExOrderService.get(query);
        if (order == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，该订单不存在");
        }
        if (!order.getCreateUser().getId().equals(hbzUserService.currentUser().getId())) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，不能修改非自己的订单");
        }
        if (order != null && order.getOrderTrans() == OrderTrans.NEW) {
            order.setOrderTrans(OrderTrans.CONFIRMED);
            if ((order = hbzExOrderService.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "保存成功");
            }
        }
        return new ResponseDTO(Const.STATUS_ERROR, "保存失败，状态错误", null);
    }

    //TODO 价格计算，需要定需求修改

    /**
     * @param od
     * @return
     */
    @RequestMapping(value = "/price/compute", method = RequestMethod.POST)
    public ResponseDTO price(@RequestBody HbzExOrderMapDTO od) {
        HbzAreaDTO origin = areaService.findByOutCode(od.getOriginAreaCode());
        HbzAreaDTO dest = areaService.findByOutCode(od.getDestAreaCode());
        if (origin == null || dest == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "区域码有错误");
        }
        HbzAreaDTO originlv1area = origin;
        HbzAreaDTO destlv1area = dest;
        if (originlv1area.getLevel() < 1) {
            return new ResponseDTO(Const.STATUS_ERROR, "货物地址非市级地址");
        }
        if (destlv1area.getLevel() < 1) {
            return new ResponseDTO(Const.STATUS_ERROR, "目的地址非地级地址");
        }
        while (originlv1area.getLevel() > 1) {
            originlv1area = originlv1area.getParent();
        }
        while (destlv1area.getLevel() > 1) {
            destlv1area = destlv1area.getParent();
        }
        String formulak;
        if (Arrays.asList(scode.split("\\,")).contains(originlv1area.getOutCode()) || Arrays.asList(scode.split("\\,")).contains(destlv1area.getOutCode())) {
            formulak = spec;
        } else if (Arrays.asList(remoteCode.split("\\,")).contains(originlv1area.getOutCode()) || Arrays.asList(remoteCode.split("\\,")).contains(destlv1area.getOutCode())) {
            formulak = remoteFormula;
        } else {
            formulak = defaultFormula;
        }
        if (formulak != null) {
            Double s = 0D;
            if (originlv1area.getId().equals(destlv1area.getId())) {
                s = 1D;
            }
            Map<String, Double> var = new LinkedHashMap<>();
            var.put("s", s);
            var.put("w", od.getCommodityWeight());
            var.put("v", od.getCommodityVolume());
            Double result = hbzFormulaService.calculate(formulak, var);
            try {
                return new ResponseDTO(Const.STATUS_OK, "成功", new DecimalFormat("#.00").format(result));
            } catch (Exception e) {
                return new ResponseDTO(Const.STATUS_ERROR, "失败", e.getMessage());
            }
        }
        return new ResponseDTO(Const.STATUS_ERROR, "计算失败");
    }

    //查询我创建的订单 -- 货主端
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseDTO queryMyOrder(@RequestBody HbzExOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzExOrderDTO query = new HbzExOrderDTO();
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("orderTakeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
        ).copyProperties(queryDTO, query);
        query.setCreateUserId(hbzUserService.currentUser().getId());
        query.setStatus(Const.STATUS_ENABLED);
        List<HbzExOrderDTO> list = hbzExOrderService.query(query);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", list.stream().map(MapSpec::maperEx).collect(Collectors.toList()));
    }

    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public ResponseDTO queryMyOrderPage(@RequestBody HbzExOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzExOrderDTO query = new HbzExOrderDTO();
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("orderTakeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
        ).copyProperties(queryDTO, query);
        query.setCreateUserId(hbzUserService.currentUser().getId());
        query.setStatus(Const.STATUS_ENABLED);
        Page<HbzExOrderDTO> page = hbzExOrderService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功", page.map(MapSpec::maperEx));
    }

    @Deprecated
    //查询我抢的订单 -- 快递
    @RequestMapping(value = "/task/query", method = RequestMethod.POST)
    public ResponseDTO queryTaskOrder(@RequestBody HbzExOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzExOrderDTO query = new HbzExOrderDTO();
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("orderTakeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
        ).copyProperties(queryDTO, query);
        query.setTakeUserId(hbzUserService.currentUser().getId());
        query.setOrderTranses(Arrays.asList(OrderTrans.WAIT_TO_TAKE, OrderTrans.TRANSPORT, OrderTrans.WAIT_TO_CONFIRM, OrderTrans.WAIT_FOR_PAYMENT));
        query.setStatus(Const.STATUS_ENABLED);
        List<HbzExOrderDTO> list = hbzExOrderService.query(query);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", list.stream().map(MapSpec::maperEx).collect(Collectors.toList()));
    }

    @Deprecated
    @RequestMapping(value = "/task/queryPage", method = RequestMethod.POST)
    public ResponseDTO queryPAGETaskOrder(@RequestBody HbzExOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzExOrderDTO query = new HbzExOrderDTO();
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("orderTakeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
        ).copyProperties(queryDTO, query);
        query.setTakeUserId(hbzUserService.currentUser().getId());
        query.setOrderTranses(Arrays.asList(OrderTrans.WAIT_TO_TAKE, OrderTrans.TRANSPORT, OrderTrans.WAIT_TO_CONFIRM, OrderTrans.WAIT_FOR_PAYMENT));
        query.setStatus(Const.STATUS_ENABLED);
        Page<HbzExOrderDTO> page = hbzExOrderService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功", page.map(MapSpec::maperEx));
    }

    @Deprecated
    //抢线订单 -- 司机端
    @RequestMapping(value = "/carry", method = RequestMethod.POST)
    public ResponseDTO carryOrder(@RequestBody HbzExOrderMapDTO map, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO user = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(map, "hbz_exorder_get");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzExOrderDTO query = new HbzExOrderDTO();
        query.setId(map.getId());
        HbzExOrderDTO order = hbzExOrderService.get(query);
        if (order.getOrderTrans() == OrderTrans.ORDER_TO_BE_RECEIVED) {
            order.setOrderTrans(OrderTrans.WAIT_TO_TAKE);
            order.setTakeUser(user);
            order.setTakeUserId(user.getId());
            if ((order = hbzExOrderService.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "抢单成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    @Deprecated
    //收货整车专线订单 -- 司机端
    @RequestMapping(value = "/take", method = RequestMethod.POST)
    public ResponseDTO takeOrder(@RequestBody HbzExOrderMapDTO map, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO u = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(map, "buy_get");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzExOrderDTO query = new HbzExOrderDTO();
        query.setId(map.getId());
        HbzExOrderDTO order = hbzExOrderService.get(query);
        if (order.getOrderTrans() == OrderTrans.WAIT_TO_TAKE) {
            order.setOrderTrans(OrderTrans.TRANSPORT);
            order.setDealUser(u);
            order.setDealUserId(u.getId());
            if ((order = hbzExOrderService.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    @Deprecated
    //已送达订单 -- 司机端
    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    public ResponseDTO comppleteOrder(@RequestBody HbzExOrderMapDTO mapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO u = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(mapDTO, "buy_get");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzExOrderDTO query = new HbzExOrderDTO();
        query.setId(mapDTO.getId());
        HbzExOrderDTO order = hbzExOrderService.get(query);
        if (order.getOrderTrans() == OrderTrans.TRANSPORT) {
            order.setOrderTrans(OrderTrans.WAIT_TO_CONFIRM);
            if ((order = hbzExOrderService.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    @Deprecated
    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public ResponseDTO receiveOrder(@RequestBody HbzExOrderMapDTO mapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO u = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(mapDTO, "buy_get");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzExOrderDTO query = new HbzExOrderDTO();
        query.setId(mapDTO.getId());
        HbzExOrderDTO order = hbzExOrderService.get(query);
        if (order.getOrderTrans() == OrderTrans.WAIT_TO_CONFIRM) {
            order.setOrderTrans(OrderTrans.WAIT_FOR_PAYMENT);
            if ((order = hbzExOrderService.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

}
