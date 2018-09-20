package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzSendOrderMapDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.*;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.TimeMillisFormater;
import com.troy.keeper.hbz.sys.annotation.Config;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.hbz.type.*;
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
 * Created by leecheng on 2017/11/28.
 */
@RestController
@RequestMapping("/api/order/send")
public class HbzSendOrderResource {

    @Autowired
    private HbzSendOrderService hbzSendOrderService;

    @Autowired
    private HbzOrderService hbzOrderService;

    @Autowired
    private HbzUserService hbzUserService;

    @Config("com.hbz.order.buy.limit")
    private Long limit;

    @Config("com.hbz.location.x.width")
    private Double xWidth;

    @Config("com.hbz.location.y.width")
    private Double yWidth;

    @Autowired
    private MapService map;

    @Autowired
    private HbzOrderRecordService hbzOrderRecordService;

    @Config("com.hbz.long.prc")
    private String LONG_PRICE = null;

    @Autowired
    protected HbzFormulaService service;

    @Autowired
    HbzRateService rate;

    @Label("App端 - 帮送 - 货主 - 创建")
    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    public ResponseDTO createOrder(@RequestBody HbzSendOrderMapDTO par) {
        if (par.getId() != null) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "非法操作");
        } else {
            String[] errors = ValidationHelper.valid(par, "send_create");
            if (errors != null && errors.length > 0) {
                return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证失败", errors);
            } else {
                Long currentTimeMillis = System.currentTimeMillis();
                String orderNo = hbzOrderService.createNewOrderNo(OrderType.SEND);
                HbzSendOrderDTO order = new HbzSendOrderDTO();
                new Bean2Bean(
                        new PropertyMapper<>("startTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                        new PropertyMapper<>("orderTakeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
                ).copyProperties(par, order);
                if (StringHelper.isNullOREmpty(order.getOriginInfo())) {
                    if (order.getOriginX() != null && order.getOriginY() != null) {
                        Map<String, Object> map = this.map.getLocation(order.getOriginX(), order.getOriginY());
                        String address = (String) map.get("formatted_address");
                        order.setOriginArea(this.map.getAreaByLocation(order.getOriginX(), order.getOriginY()));
                        order.setOriginInfo(address);
                    }
                }

                if (order.getOriginX() != null && order.getOriginY() != null) {
                    order.setOriginArea(this.map.getAreaByLocation(order.getOriginX(), order.getOriginY()));
                }

                if (StringHelper.isNullOREmpty(order.getDestInfo())) {
                    if (order.getDestX() != null && order.getDestY() != null) {
                        Map<String, Object> map = this.map.getLocation(order.getDestX(), order.getDestY());
                        String address = (String) map.get("formatted_address");
                        order.setDestArea(this.map.getAreaByLocation(order.getDestX(), order.getDestY()));
                        order.setDestInfo(address);
                    }
                }

                if (order.getDestX() != null && order.getDestY() != null) {
                    order.setDestArea(this.map.getAreaByLocation(order.getDestX(), order.getDestY()));
                }

                //设置帮买订单
                order.setOrderType(OrderType.SEND);
                //设置新建状态
                order.setOrderTrans(OrderTrans.NEW);
                //在线支付
                order.setSettlementType(SettlementType.ONLINE_PAYMENT);
                //设置业务订单号
                order.setOrderNo(orderNo);
                //设置开始时间
                if (order.getTimeLimit() == TimeLimit.Immediately) {
                    order.setStartTime(currentTimeMillis);
                }
                //设置过期时间
                order.setEndTime(order.getStartTime() + limit);
                //设置待支付
                order.setCreateUserId(hbzUserService.currentUser().getId());
                //置未删除状态
                order.setCreateTime(System.currentTimeMillis());
                order.setStatus(Const.STATUS_ENABLED);
                if ((order = hbzSendOrderService.save(order)) != null) {
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


    //TODO 价格计算，需要定需求修改

    /**
     * @param od
     * @return
     */
    @Label("App端 - 帮送 - 计价")
    @RequestMapping(value = "/price/compute", method = RequestMethod.POST)
    public ResponseDTO price(@RequestBody HbzSendOrderMapDTO od) {
        MapRouteMatrixDTO route = new MapRouteMatrixDTO();

        MapAddressDTO originAdd = new MapAddressDTO();
        originAdd.setLng(od.getOriginX());
        originAdd.setLat(od.getOriginY());

        MapAddressDTO destAdd = new MapAddressDTO();
        destAdd.setLng(od.getDestX());
        destAdd.setLat(od.getDestY());

        route.setTactics(13);
        route.setOperator("driving");
        route.setOrigins(Arrays.asList(originAdd));
        route.setDestinations(Arrays.asList(destAdd));

        Map<String, Object> calc = map.route(route);
        List<Map<String, Object>> result = (List) calc.get("result");
        Map<String, Object> it = result.get(0);
        Double distance = Double.valueOf((String) it.get("distance"));

        Map<String, Double> var = new HashMap<>();
        var.put("d", distance);
        try {
            Double ta = service.calculate(LONG_PRICE, var);
            return new ResponseDTO(Const.STATUS_OK, "计算成功", new DecimalFormat("#.##").format(ta));
        } catch (Throwable e) {
            return new ResponseDTO(Const.STATUS_ERROR, "计算失败", e.getMessage());
        }
    }
    @Label("App - 专线 - 帮送 - 发货方 - 删除订单")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseDTO deleteFslOrder(@RequestBody HbzSendOrderMapDTO sendOrderMapDTO, HttpServletRequest request, HttpServletResponse response) {
        if (sendOrderMapDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "id为空，请提供订单id");
        }
        HbzSendOrderDTO send = hbzSendOrderService.findById(sendOrderMapDTO.getId());
        switch (send.getOrderTrans()) {
            case NEW:
            case CONFIRMED:
                send.setOrderTrans(OrderTrans.INVALID);
                send.setStatus("0");
                hbzSendOrderService.save(send);
                break;
            default:
                return new ResponseDTO(Const.STATUS_ERROR, "错误！此状态订单不能被删除");
        }
        return new ResponseDTO(Const.STATUS_OK, "操作成功！", null);
    }
    //确认整车专线订单 -- 货主端
    @Label("App端 - 帮送 - 货主 - 提交")
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public ResponseDTO confirmOrder(@RequestBody HbzSendOrderMapDTO hbzSendOrderMapDTO, HttpServletRequest request, HttpServletResponse response) {
        if (hbzSendOrderMapDTO.getId() == null) {
            new ResponseDTO(Const.STATUS_ERROR, "订单标识为空");
        }
        HbzSendOrderDTO query = new HbzSendOrderDTO();
        query.setId(hbzSendOrderMapDTO.getId());
        HbzSendOrderDTO order = hbzSendOrderService.get(query);
        if (order == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，该订单不存在");
        }
        if (!order.getCreateUser().getId().equals(hbzUserService.currentUser().getId())) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，不能修改非自己的订单");
        }
        if (order != null && order.getOrderTrans() == OrderTrans.NEW) {
            order.setAmount(hbzSendOrderMapDTO.getAmount());
            order.setOrderTrans(OrderTrans.CONFIRMED);
            if ((order = hbzSendOrderService.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "保存成功");
            }
        }
        return new ResponseDTO(Const.STATUS_ERROR, "保存失败，状态错误", null);
    }

    @Label("App端 - 帮送 - 货主 - 创建查询")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseDTO queryFOrder(@RequestBody HbzSendOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzSendOrderDTO query = new HbzSendOrderDTO();

        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("orderTakeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),

                new PropertyMapper<>("startTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),

                new PropertyMapper<>("endTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
        ).copyProperties(queryDTO, query);

        query.setCreateUserId(hbzUserService.currentUser().getId());
        query.setStatus(Const.STATUS_ENABLED);

        List<HbzSendOrderDTO> list = hbzSendOrderService.query(query);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", list.stream().map(MapSpec::mapSendOrder).collect(Collectors.toList()));
    }

    @Label("App端 - 帮送 - 货主 - 创建查询分页")
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public ResponseDTO pqueryFOrder(@RequestBody HbzSendOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzSendOrderDTO query = new HbzSendOrderDTO();

        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("orderTakeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),

                new PropertyMapper<>("startTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),

                new PropertyMapper<>("endTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
        ).copyProperties(queryDTO, query);

        query.setCreateUserId(hbzUserService.currentUser().getId());
        query.setStatus(Const.STATUS_ENABLED);

        Page<HbzSendOrderDTO> page = hbzSendOrderService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功", page.map(MapSpec::mapSendOrder));
    }

    @Label("App端 - 帮送 - 司机 - 我接运的订单查询")
    @RequestMapping(value = "/task/query", method = RequestMethod.POST)
    public ResponseDTO queryTaskOrder(@RequestBody HbzSendOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzSendOrderDTO query = new HbzSendOrderDTO();

        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("orderTakeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),

                new PropertyMapper<>("startTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),

                new PropertyMapper<>("endTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
        ).copyProperties(queryDTO, query);

        query.setTakeUserId(hbzUserService.getAdministrator(hbzUserService.currentUser().getId()).getId());
        query.setOrderTranses(Arrays.asList(OrderTrans.WAIT_TO_TAKE, OrderTrans.TRANSPORT, OrderTrans.WAIT_TO_CONFIRM, OrderTrans.WAIT_FOR_PAYMENT));
        query.setStatus(Const.STATUS_ENABLED);

        List<HbzSendOrderDTO> list = hbzSendOrderService.query(query);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", list.stream().map(MapSpec::mapSendOrder).collect(Collectors.toList()));
    }

    @Label("App端 - 帮送 - 货主 - 我创建的订单查询分页")
    @RequestMapping(value = "/task/queryPage", method = RequestMethod.POST)
    public ResponseDTO pqueryTaskOrder(@RequestBody HbzSendOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzSendOrderDTO query = new HbzSendOrderDTO();

        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("orderTakeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),

                new PropertyMapper<>("startTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),

                new PropertyMapper<>("endTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
        ).copyProperties(queryDTO, query);

        query.setTakeUserId(hbzUserService.getAdministrator(hbzUserService.currentUser().getId()).getId());
        query.setOrderTranses(Arrays.asList(OrderTrans.WAIT_TO_TAKE, OrderTrans.TRANSPORT, OrderTrans.WAIT_TO_CONFIRM, OrderTrans.WAIT_FOR_PAYMENT));
        query.setStatus(Const.STATUS_ENABLED);

        Page<HbzSendOrderDTO> page = hbzSendOrderService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功", page.map(MapSpec::mapSendOrder));
    }

    @Label("App端 - 帮送 - 司机 - 货源查询")
    @RequestMapping(value = "/task/near/query", method = RequestMethod.POST)
    public ResponseDTO queryTaskOrderNear(@RequestBody HbzSendOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzSendOrderDTO query = new HbzSendOrderDTO();
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("orderTakeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),

                new PropertyMapper<>("startTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),

                new PropertyMapper<>("endTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
        ).copyProperties(queryDTO, query);

        if (queryDTO.getLocationX() != null && queryDTO.getLocationY() != null) {
            Double distanceX = 1D / 111D * xWidth * Math.cos(Math.PI / 4 * queryDTO.getLocationY() / 90);
            Double distanceY = yWidth / 111D;
            if (queryDTO.getDistance() != null) {
                distanceX = 1D / 111D * queryDTO.getDistance() * Math.cos(Math.PI / 4 * queryDTO.getLocationY() / 90);
                distanceY = 1D / 111D * queryDTO.getDistance();
            }
            query.setOriginXLE(queryDTO.getLocationX() + distanceX);
            query.setOriginXGE(queryDTO.getLocationX() - distanceX);
            query.setOriginYLE(queryDTO.getLocationY() + distanceY);
            query.setOriginYGE(queryDTO.getLocationY() - distanceY);
        } else if (queryDTO.getLocationX() != null || queryDTO.getLocationY() != null) {
            return new ResponseDTO(Const.STATUS_ERROR, "坐标必须传两个");
        }

        //订单必须是待接运
        query.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);
        query.setStartTimeLE(System.currentTimeMillis());
        query.setEndTimeGE(System.currentTimeMillis());
        List<String[]> sorts = new ArrayList<>();
        sorts.add(new String[]{"lastUpdatedDate", "DESC"});
        query.setSorts(sorts);
        //订单是正常状态
        query.setStatus(Const.STATUS_ENABLED);
        List<HbzSendOrderDTO> list = hbzSendOrderService.query(query);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", list.stream().map(MapSpec::mapSendOrder).collect(Collectors.toList()));
    }

    @Label("App端 - 帮送 - 司机 - 货源分页查询")
    @RequestMapping(value = "/task/near/queryPage", method = RequestMethod.POST)
    public ResponseDTO queryTaskOrderPageI(@RequestBody HbzSendOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzSendOrderDTO query = new HbzSendOrderDTO();
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("orderTakeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("orderTakeTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),

                new PropertyMapper<>("startTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),

                new PropertyMapper<>("endTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
        ).copyProperties(queryDTO, query);

        if (queryDTO.getLocationX() != null && queryDTO.getLocationY() != null) {
            Double distanceX = 1D / 111D * xWidth * Math.cos(Math.PI / 4 * queryDTO.getLocationY() / 90);
            Double distanceY = yWidth / 111D;
            if (queryDTO.getDistance() != null) {
                distanceX = 1D / 111D * queryDTO.getDistance() * Math.cos(Math.PI / 4 * queryDTO.getLocationY() / 90);
                distanceY = 1D / 111D * queryDTO.getDistance();
            }
            query.setOriginXLE(queryDTO.getLocationX() + distanceX);
            query.setOriginXGE(queryDTO.getLocationX() - distanceX);
            query.setOriginYLE(queryDTO.getLocationY() + distanceY);
            query.setOriginYGE(queryDTO.getLocationY() - distanceY);
        } else if (queryDTO.getLocationX() != null || queryDTO.getLocationY() != null) {
            return new ResponseDTO(Const.STATUS_ERROR, "坐标必须传两个");
        }

        //订单必须是待接运
        query.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);
        query.setStartTimeLE(System.currentTimeMillis());
        query.setEndTimeGE(System.currentTimeMillis());

        List<String[]> sorts = new ArrayList<>();
        sorts.add(new String[]{"lastUpdatedDate", "DESC"});
        query.setSorts(sorts);
        //订单是正常状态
        query.setStatus(Const.STATUS_ENABLED);
        Page<HbzSendOrderDTO> page = hbzSendOrderService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功", page.map(MapSpec::mapSendOrder));
    }

    @Label("App端 - 帮送 - 司机 - 抢单")
    @RequestMapping(value = "/carry", method = RequestMethod.POST)
    public ResponseDTO carryOrder(@RequestBody HbzSendOrderMapDTO map, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO user = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(map, "send_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzSendOrderDTO query = new HbzSendOrderDTO();
        query.setId(map.getId());
        HbzSendOrderDTO order = hbzSendOrderService.get(query);
        if (order.getOrderTrans() == OrderTrans.ORDER_TO_BE_RECEIVED) {
            HbzUserDTO ad = hbzUserService.getAdministrator(user.getId());
            order.setAgent(user);
            order.setAgentId(user.getId());
            order.setOrderTrans(OrderTrans.WAIT_TO_TAKE);
            order.setTakeUser(ad);
            order.setTakeUserId(ad.getId());
            order.setAgentTime(System.currentTimeMillis());
            order.setTakeTime(System.currentTimeMillis());
            if ((order = hbzSendOrderService.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "抢单成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    @Label("App端 - 帮送 - 司机 - 接运")
    @RequestMapping(value = "/take", method = RequestMethod.POST)
    public ResponseDTO takeOrder(@RequestBody HbzSendOrderMapDTO map, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO u = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(map, "send_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzSendOrderDTO query = new HbzSendOrderDTO();
        query.setId(map.getId());
        HbzSendOrderDTO order = hbzSendOrderService.get(query);
        if (order.getOrderTrans() == OrderTrans.WAIT_TO_TAKE) {
            order.setDealTime(System.currentTimeMillis());
            order.setOrderTrans(OrderTrans.TRANSPORT);
            order.setDealUser(u);
            order.setDealUserId(u.getId());
            if ((order = hbzSendOrderService.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }


    @Label("App端 - 帮送 - 司机 - 送到")
    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    public ResponseDTO completeOrder(@RequestBody HbzSendOrderMapDTO mapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO u = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(mapDTO, "send_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzSendOrderDTO query = new HbzSendOrderDTO();
        query.setId(mapDTO.getId());
        HbzSendOrderDTO order = hbzSendOrderService.get(query);
        if (order.getOrderTrans() == OrderTrans.TRANSPORT) {
            order.setCompleteImage(mapDTO.getCompleteImage());
            order.setOrderTrans(OrderTrans.WAIT_TO_CONFIRM);
            if ((order = hbzSendOrderService.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    @Label("App端 - 帮送 - 货主 - 收货")
    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public ResponseDTO receiveOrder(@RequestBody HbzSendOrderMapDTO mapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO u = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(mapDTO, "send_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzSendOrderDTO query = new HbzSendOrderDTO();
        query.setId(mapDTO.getId());
        HbzSendOrderDTO ord = hbzSendOrderService.get(query);
        if (Arrays.asList(OrderTrans.WAIT_TO_CONFIRM, OrderTrans.TRANSPORT).contains(ord.getOrderTrans())) {
            ord.setOrderTrans(OrderTrans.PAID);
            if ((ord = hbzSendOrderService.save(ord)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(ord, ord.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), ord.getOrderTrans(), System.currentTimeMillis()));
                HbzRateDTO consignorRate = new HbzRateDTO();
                consignorRate.setUserId(ord.getCreateUserId());
                consignorRate.setUser(ord.getCreateUser());
                consignorRate.setOrder(ord);
                consignorRate.setOrderId(ord.getId());
                consignorRate.setType(RateType.CONSIGNOR);
                consignorRate.setStar(0);
                consignorRate.setStatus("1");
                rate.save(consignorRate);

                HbzRateDTO tranRate = new HbzRateDTO();
                tranRate.setUser(ord.getTakeUser());
                tranRate.setUserId(ord.getTakeUserId());
                tranRate.setOrder(ord);
                tranRate.setOrderId(ord.getId());
                tranRate.setStatus("1");
                tranRate.setType(RateType.PROVIDER);
                tranRate.setStar(0);
                rate.save(tranRate);
                return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

}
