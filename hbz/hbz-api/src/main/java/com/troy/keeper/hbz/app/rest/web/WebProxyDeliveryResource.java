package com.troy.keeper.hbz.app.rest.web;

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
import com.troy.keeper.hbz.type.OrderTrans;
import com.troy.keeper.hbz.type.OrderType;
import com.troy.keeper.hbz.type.SettlementType;
import com.troy.keeper.hbz.type.TimeLimit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Author：YangJx
 * @Description：帮我送
 * @DateTime：2017/12/11 15:45
 */
@Slf4j
@RestController
@RequestMapping("/api/web/proxyDelivery")
public class WebProxyDeliveryResource {

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

    /**
     * 货主 创建帮我送订单
     *
     * @param par
     * @return
     */
    @PostMapping("/createProxyDelivery")
    public ResponseDTO createProxyDelivery(@RequestBody HbzSendOrderMapDTO par) {
        if (par.getId() != null) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "非法操作！");
        } else {
            String[] errors = ValidationHelper.valid(par, "web_send_create");
            if (errors != null && errors.length > 0) {
                return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证失败！", errors);
            } else {
                Long currentTimeMillis = System.currentTimeMillis();
                String orderNo = hbzOrderService.createNewOrderNo(OrderType.SEND);
                HbzSendOrderDTO order = new HbzSendOrderDTO();
                new Bean2Bean(
                        new PropertyMapper<>("startTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                        new PropertyMapper<>("takeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
                ).copyProperties(par, order);
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
                order.setStatus(Const.STATUS_ENABLED);
                if ((order = hbzSendOrderService.save(order)) != null) {
                    hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), order.getOrderTrans(), System.currentTimeMillis()));
                    Map<String, Object> id = new HashMap<>();
                    id.put("id", order.getId());
                    id.put("orderNo", order.getOrderNo());
                    return new ResponseDTO(Const.STATUS_OK, "保存成功!", id);
                } else {
                    return new ResponseDTO(Const.STATUS_ERROR, "保存失败!", null);
                }
            }
        }
    }

    /**
     * 计算价格 TODO 待确定
     *
     * @param od
     * @return
     */
    @PostMapping("/computePrice")
    public ResponseDTO computePrice(@RequestBody HbzSendOrderMapDTO od) {
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
        Double ta = service.calculate(LONG_PRICE, var);
        return new ResponseDTO(Const.STATUS_OK, "计算成功!", ta);
    }

    /**
     * 货主 确认帮我送订单
     *
     * @param hbzSendOrderMapDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/confirmProxyDelivery")
    public ResponseDTO confirmProxyDelivery(@RequestBody HbzSendOrderMapDTO hbzSendOrderMapDTO, HttpServletRequest request, HttpServletResponse response) {
        if (hbzSendOrderMapDTO.getId() == null) {
            new ResponseDTO(Const.STATUS_ERROR, "订单标识为空!");
        }
        HbzSendOrderDTO query = new HbzSendOrderDTO();
        query.setId(hbzSendOrderMapDTO.getId());
        HbzSendOrderDTO order = hbzSendOrderService.get(query);
        if (order == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，该订单不存在!");
        }
        if (!order.getCreateUser().getId().equals(hbzUserService.currentUser().getId())) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，不能修改非自己的订单!");
        }
        if (order != null && order.getOrderTrans() == OrderTrans.NEW) {
            order.setOrderTrans(OrderTrans.CONFIRMED);
            if ((order = hbzSendOrderService.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "保存成功!");
            }
        }
        return new ResponseDTO(Const.STATUS_ERROR, "保存失败，状态错误!", null);
    }

    /**
     * 货主 删除帮我送订单
     *
     * @param hbzSendOrderMapDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/deleteProxyDelivery")
    public ResponseDTO deleteProxyDelivery(@RequestBody HbzSendOrderMapDTO hbzSendOrderMapDTO, HttpServletRequest request, HttpServletResponse response) {
        if (hbzSendOrderMapDTO.getId() == null) {
            new ResponseDTO(Const.STATUS_ERROR, "订单id不能为空!");
        }
        HbzSendOrderDTO query = new HbzSendOrderDTO();
        query.setId(hbzSendOrderMapDTO.getId());
        HbzSendOrderDTO order = hbzSendOrderService.get(query);
        if (order == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，该订单不存在!");
        }
        if (!order.getCreateUser().getId().equals(hbzUserService.currentUser().getId())) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，不能删除非自己的订单!");
        }
        if(order.getOrderTrans() != OrderTrans.NEW) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，只能删除新建订单!");
        }

        if (order != null && order.getOrderTrans() == OrderTrans.NEW) {
            order.setStatus(Const.STATUS_DISABLED);
            if ((order = hbzSendOrderService.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "删除成功!");
            }
        }
        return new ResponseDTO(Const.STATUS_ERROR, "保存失败，状态错误!", null);
    }

    /**
     * 货主 分页查询我创建的帮我送订单
     *
     * @param queryDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/queryMyProxyDeliveryByPage")
    public ResponseDTO queryMyProxyDeliveryByPage(@RequestBody HbzSendOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzSendOrderDTO query = new HbzSendOrderDTO();

        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("takeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),

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
        return new ResponseDTO(Const.STATUS_OK, "查询成功!", page.map(MapSpec::mapSendOrder));
    }

    /**
     * 车主 分页条件查询我接运的帮送订单
     *
     * @param queryDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/queryMyTakenProxyDeliveryByPage")
    public ResponseDTO queryMyTakenProxyDeliveryByPage(@RequestBody HbzSendOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzSendOrderDTO query = new HbzSendOrderDTO();

        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("takeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),

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

        query.setTakeUserId(hbzUserService.currentUser().getId());
        query.setOrderTranses(Arrays.asList(OrderTrans.WAIT_TO_TAKE, OrderTrans.TRANSPORT, OrderTrans.WAIT_TO_CONFIRM, OrderTrans.WAIT_FOR_PAYMENT));
        query.setStatus(Const.STATUS_ENABLED);

        Page<HbzSendOrderDTO> page = hbzSendOrderService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功!", page.map(MapSpec::mapSendOrder));
    }

    /**
     * 车主 分页查询可接帮送订单
     *
     * @param queryDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/queryAvailableProxyDeliveryByPage")
    public ResponseDTO queryAvailableProxyDeliveryByPage(@RequestBody HbzSendOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzSendOrderDTO query = new HbzSendOrderDTO();
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("takeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),

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
            query.setDestYLE(queryDTO.getLocationY() + distanceY);
            query.setDestYGE(queryDTO.getLocationY() - distanceY);
        } else if (queryDTO.getLocationX() != null || queryDTO.getLocationY() != null) {
            return new ResponseDTO(Const.STATUS_ERROR, "坐标必须传两个!");
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
        return new ResponseDTO(Const.STATUS_OK, "查询成功!", page.map(MapSpec::mapSendOrder));
    }

    /**
     * 车主 接帮送订单
     *
     * @param map
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/takeProxyDelivery")
    public ResponseDTO takeProxyDelivery(@RequestBody HbzSendOrderMapDTO map, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO user = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(map, "send_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败！", err);
        }
        HbzSendOrderDTO query = new HbzSendOrderDTO();
        query.setId(map.getId());
        HbzSendOrderDTO order = hbzSendOrderService.get(query);
        if (order.getOrderTrans() == OrderTrans.ORDER_TO_BE_RECEIVED) {
            order.setOrderTrans(OrderTrans.WAIT_TO_TAKE);
            order.setTakeUser(user);
            order.setTakeUserId(user.getId());
            if ((order = hbzSendOrderService.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "抢单成功!", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败!", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单!", null);
        }
    }

    /**
     * 车主 帮送订单揽货
     *
     * @param map
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/pickUpProxyDelivery")
    public ResponseDTO pickUpProxyDelivery(@RequestBody HbzSendOrderMapDTO map, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO u = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(map, "send_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败!", err);
        }
        HbzSendOrderDTO query = new HbzSendOrderDTO();
        query.setId(map.getId());
        HbzSendOrderDTO order = hbzSendOrderService.get(query);
        if (order.getOrderTrans() == OrderTrans.WAIT_TO_TAKE) {
            order.setOrderTrans(OrderTrans.TRANSPORT);
            order.setDealUser(u);
            order.setDealUserId(u.getId());
            if ((order = hbzSendOrderService.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "操作成功!", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败!", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单!", null);
        }
    }

    /**
     * 车主 帮送订单签收
     *
     * @param mapDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/completeProxyDelivery")
    public ResponseDTO completeProxyDelivery(@RequestBody HbzSendOrderMapDTO mapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO u = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(mapDTO, "send_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败!", err);
        }
        HbzSendOrderDTO query = new HbzSendOrderDTO();
        query.setId(mapDTO.getId());
        HbzSendOrderDTO order = hbzSendOrderService.get(query);
        if (order.getOrderTrans() == OrderTrans.TRANSPORT) {
            order.setOrderTrans(OrderTrans.WAIT_TO_CONFIRM);
            if ((order = hbzSendOrderService.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "操作成功!", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败！", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单！", null);
        }
    }

    /**
     * 查询帮送订单详情
     *
     * @param hbzSendOrderMapDTO
     * @return
     */
    @PostMapping("/queryProxyDeliveryDetail")
    public ResponseDTO queryProxyDeliveryDetail(@RequestBody HbzSendOrderMapDTO hbzSendOrderMapDTO) {
        /*String[] err = ValidationHelper.valid(hbzSendOrderMapDTO, "web_send_confirm");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败！", err);
        }*/
        if(hbzSendOrderMapDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "id不能为空！", null);
        }
        HbzSendOrderDTO hbzSendOrderDTO = new HbzSendOrderDTO();
        hbzSendOrderDTO.setId(hbzSendOrderMapDTO.getId());
        HbzSendOrderDTO hbzSendOrderDTOFromDB = hbzSendOrderService.getSendOrderDetail(hbzSendOrderDTO);
        return new ResponseDTO(Const.STATUS_OK, "查询成功！", hbzSendOrderDTOFromDB);
    }
}
