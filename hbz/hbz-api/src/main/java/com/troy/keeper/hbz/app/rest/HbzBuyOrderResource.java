package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzBuyOrderMapDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzBuyOrderDTO;
import com.troy.keeper.hbz.dto.HbzOrderRecordDTO;
import com.troy.keeper.hbz.dto.HbzRateDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.TimeMillisFormater;
import com.troy.keeper.hbz.sys.annotation.Config;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.hbz.type.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/10/24.
 */
@CrossOrigin
@RestController
@RequestMapping({"/api/order/buy"})
public class HbzBuyOrderResource {

    public final static Log log = LogFactory.getLog(HbzBuyOrderResource.class);

    @Autowired
    HbzRateService rate;

    @Autowired
    private HbzBuyOrderServices hbzBuyOrderServices;

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

    @Label("App端 - 帮买 - 货主 - 建单")
    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    public ResponseDTO createOrder(@RequestBody HbzBuyOrderMapDTO hbzBuyOrderDTO) {
        if (hbzBuyOrderDTO.getId() != null) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "非法操作ID");
        } else {
            String[] errors = ValidationHelper.valid(hbzBuyOrderDTO, "buy_create");
            if (errors != null && errors.length > 0) {
                return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证失败", errors);
            } else {
                Long currentTimeMillis = System.currentTimeMillis();
                String orderNo = hbzOrderService.createNewOrderNo(OrderType.BUY);
                HbzBuyOrderDTO order = new HbzBuyOrderDTO();
                new Bean2Bean(
                        new PropertyMapper<>("startTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
                ).copyProperties(hbzBuyOrderDTO, order);
                if (StringHelper.isNullOREmpty(order.getDestInfo())) {
                    if (order.getDestX() != null && order.getDestY() != null) {
                        Map<String, Object> map = this.map.getLocation(order.getDestX(), order.getDestY());
                        String address = (String) map.get("formatted_address");
                        order.setDestInfo(address);
                    }
                }
                if (order.getDestX() != null && order.getDestY() != null) {
                    order.setDestArea(this.map.getAreaByLocation(order.getDestX(), order.getDestY()));
                }
                if (order.getOriginX() == null || order.getOriginY() == null) {
                    order.setOriginX(order.getDestX());
                    order.setOriginY(order.getDestY());
                }
                if (order.getOriginX() != null && order.getOriginY() != null) {
                    order.setOriginArea(this.map.getAreaByLocation(order.getOriginX(), order.getOriginY()));
                }
                if (StringHelper.isNullOREmpty(order.getOriginInfo())) {
                    if (order.getOriginX() != null && order.getOriginY() != null) {
                        Map<String, Object> map = this.map.getLocation(order.getOriginX(), order.getOriginY());
                        String address = (String) map.get("formatted_address");
                        order.setOriginInfo(address);
                    }
                }
                //设置帮买订单
                order.setOrderType(OrderType.BUY);
                //设置新建状态
                order.setOrderTrans(OrderTrans.NEW);
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
                HbzUserDTO user = hbzUserService.currentUser();
                order.setCreateUserId(user.getId());
                order.setCreateUser(user);
                order.setCreateTime(System.currentTimeMillis());
                //置未删除状态
                order.setStatus(Const.STATUS_ENABLED);
                order = hbzBuyOrderServices.save(order);
                if (order != null) {
                    hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), user, user.getId(), order.getOrderTrans(), System.currentTimeMillis()));
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

    @Label("App - 专线 - 帮买 - 发货方 - 删除订单")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseDTO deleteFslOrder(@RequestBody HbzBuyOrderMapDTO buyOrderMapDTO, HttpServletRequest request, HttpServletResponse response) {
        if (buyOrderMapDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "id为空，请提供订单id");
        }
        HbzBuyOrderDTO buy = hbzBuyOrderServices.findById(buyOrderMapDTO.getId());
        switch (buy.getOrderTrans()) {
            case NEW:
            case CONFIRMED:
                buy.setOrderTrans(OrderTrans.INVALID);
                buy.setStatus("0");
                hbzBuyOrderServices.save(buy);
                break;
            default:
                return new ResponseDTO(Const.STATUS_ERROR, "错误！此状态订单不能被删除");
        }
        return new ResponseDTO(Const.STATUS_OK, "操作成功！", null);
    }

    @Label("App端 - 帮买 - 货主 - 确认")
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public ResponseDTO confirmOrder(@RequestBody HbzBuyOrderMapDTO buyOrderMapDTO, HttpServletRequest request, HttpServletResponse response) {
        if (buyOrderMapDTO.getId() == null) {
            new ResponseDTO(Const.STATUS_ERROR, "订单标识为空");
        }
        String[] errors = ValidationHelper.valid(buyOrderMapDTO, "buy_confirm");
        if (errors != null && errors.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证失败", errors);
        }
        HbzBuyOrderDTO query = new HbzBuyOrderDTO();
        query.setId(buyOrderMapDTO.getId());
        HbzBuyOrderDTO order = hbzBuyOrderServices.get(query);

        if (order == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，该订单不存在");
        }
        if (!order.getCreateUser().getId().equals(hbzUserService.currentUser().getId())) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，不能修改非自己的订单");
        }

        if (order != null && order.getOrderTrans() == OrderTrans.NEW) {
            if (buyOrderMapDTO.getRemuneration() != null) {
                order.setOrderTrans(OrderTrans.CONFIRMED);
                order.setRemuneration(buyOrderMapDTO.getRemuneration());
                order.setAmount(Math.round(100D * (order.getRemuneration() + order.getCommodityAmount())) / 100D);
                if ((order = hbzBuyOrderServices.save(order)) != null) {
                    HbzUserDTO user = hbzUserService.currentUser();
                    hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), user, user.getId(), order.getOrderTrans(), System.currentTimeMillis()));
                    return new ResponseDTO(Const.STATUS_OK, "保存成功");
                }
            }
        }
        return new ResponseDTO(Const.STATUS_ERROR, "保存失败，状态错误", null);
    }

    @Label("App端 - 帮买 - 货主 - 我创建的订单查询")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseDTO queryFOrder(@RequestBody HbzBuyOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzBuyOrderDTO query = new HbzBuyOrderDTO();
        new Bean2Bean().addPropMapper(
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

        List<HbzBuyOrderDTO> list = hbzBuyOrderServices.query(query);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", list.stream().map(MapSpec::mapBuyOrder).collect(Collectors.toList()));
    }

    @Label("App端 - 帮买 - 货主 - 我创建的订单查询分页")
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public ResponseDTO pqueryFOrder(@RequestBody HbzBuyOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzBuyOrderDTO query = new HbzBuyOrderDTO();
        new Bean2Bean().addPropMapper(
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
        Page<HbzBuyOrderDTO> page = hbzBuyOrderServices.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功", page.map(MapSpec::mapBuyOrder));
    }

    @Label("App端 - 帮买 - 我接运的订单 - 查询")
    @RequestMapping(value = "/task/query", method = RequestMethod.POST)
    public ResponseDTO queryTaskOrder(@RequestBody HbzBuyOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzBuyOrderDTO query = new HbzBuyOrderDTO();
        Long user = hbzUserService.currentUser().getId();
        Long admin = hbzUserService.getAdministrator(user).getId();
        new Bean2Bean().addPropMapper(
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
        query.setTakeUserId(admin);
        query.setOrderTranses(Arrays.asList(OrderTrans.WAIT_TO_TAKE, OrderTrans.TRANSPORT, OrderTrans.WAIT_TO_CONFIRM, OrderTrans.WAIT_FOR_PAYMENT));
        query.setStatus(Const.STATUS_ENABLED);
        List<HbzBuyOrderDTO> list = hbzBuyOrderServices.query(query);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", list.stream().map(MapSpec::mapBuyOrder).collect(Collectors.toList()));
    }

    @Label("App端 - 帮买 - 我接运的订单 - 查询 - 分页")
    @RequestMapping(value = "/task/queryPage", method = RequestMethod.POST)
    public ResponseDTO pqueryTaskOrder(@RequestBody HbzBuyOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        Long user = hbzUserService.currentUser().getId();
        Long admin = hbzUserService.getAdministrator(user).getId();
        HbzBuyOrderDTO query = new HbzBuyOrderDTO();
        new Bean2Bean().addPropMapper(
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
        query.setTakeUserId(admin);
        query.setOrderTranses(Arrays.asList(OrderTrans.WAIT_TO_TAKE, OrderTrans.TRANSPORT, OrderTrans.WAIT_TO_CONFIRM, OrderTrans.WAIT_FOR_PAYMENT));
        query.setStatus(Const.STATUS_ENABLED);
        Page<HbzBuyOrderDTO> page = hbzBuyOrderServices.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功", page.map(MapSpec::mapBuyOrder));
    }

    @Label("App端 - 帮买 - 货运方 - 找货源")
    @RequestMapping(value = "/task/near/query", method = RequestMethod.POST)
    public ResponseDTO queryTaskOrderNear(@RequestBody HbzBuyOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzBuyOrderDTO query = new HbzBuyOrderDTO();
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("startTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),

                new PropertyMapper<>("endTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
        ).copyProperties(queryDTO, query);

        if (queryDTO.getLocationX() != null && queryDTO.getLocationY() != null) {
            Double distanceX = 1D / 111D * xWidth * Math.cos(Math.PI / 4 * queryDTO.getLocationY() / 90);
            Double distanceY = yWidth / 111D;
            if (queryDTO.getDistance() != null) {
                distanceX = 1D / 111D * queryDTO.getDistance() * Math.cos(Math.PI / 4 * queryDTO.getLocationY() / 90);
                distanceY = 1D / 111D * queryDTO.getDistance();
            }
            query.setDestXLE(queryDTO.getLocationX() + distanceX);
            query.setDestXGE(queryDTO.getLocationX() - distanceX);
            query.setDestYLE(queryDTO.getLocationY() + distanceY);
            query.setDestYGE(queryDTO.getLocationY() - distanceY);
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
        List<HbzBuyOrderDTO> list = hbzBuyOrderServices.query(query);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", list.stream().map(MapSpec::mapBuyOrder).collect(Collectors.toList()));
    }

    @Label("App端 - 帮买 - 货运方 - 货源查询分页")
    @RequestMapping(value = "/task/near/queryPage", method = RequestMethod.POST)
    public ResponseDTO queryTaskOrderPageI(@RequestBody HbzBuyOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzBuyOrderDTO query = new HbzBuyOrderDTO();
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("startTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("startTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),

                new PropertyMapper<>("endTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("endTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
        ).copyProperties(queryDTO, query);

        if (queryDTO.getLocationX() != null && queryDTO.getLocationY() != null) {
            Double distanceX = 1D / 111D * xWidth * Math.cos(Math.PI / 4 * queryDTO.getLocationY() / 90);
            Double distanceY = yWidth / 111D;
            if (queryDTO.getDistance() != null) {
                distanceX = 1D / 111D * queryDTO.getDistance() * Math.cos(Math.PI / 4 * queryDTO.getLocationY() / 90);
                distanceY = 1D / 111D * queryDTO.getDistance();
            }
            query.setDestXLE(queryDTO.getLocationX() + distanceX);
            query.setDestXGE(queryDTO.getLocationX() - distanceX);
            query.setDestYLE(queryDTO.getLocationY() + distanceY);
            query.setDestYGE(queryDTO.getLocationY() - distanceY);
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
        Page<HbzBuyOrderDTO> page = hbzBuyOrderServices.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功", page.map(MapSpec::mapBuyOrder));
    }

    @Label("App端 - 帮买 - 货运方 - 抢单")
    @RequestMapping(value = "/carry", method = RequestMethod.POST)
    public ResponseDTO carryOrder(@RequestBody HbzBuyOrderMapDTO map, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO user = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(map, "buy_get");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证失败", err);
        }
        HbzBuyOrderDTO query = new HbzBuyOrderDTO();
        query.setId(map.getId());
        HbzBuyOrderDTO order = hbzBuyOrderServices.get(query);
        // TODO 保证金判断，这个是帮我买的逻辑，一般就是判断是否有交纳保证金
        if (order.getOrderTrans() == OrderTrans.ORDER_TO_BE_RECEIVED) {
            HbzUserDTO ad = hbzUserService.getAdministrator(user.getId());
            order.setOrderTrans(OrderTrans.WAIT_TO_TAKE);
            order.setAgent(user);
            order.setAgentId(user.getId());
            order.setTakeUser(ad);
            order.setAgentTime(System.currentTimeMillis());
            order.setTakeUserId(ad.getId());
            order.setTakeTime(System.currentTimeMillis());
            if ((order = hbzBuyOrderServices.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), user, user.getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "抢单成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    @Label("App端 - 帮买 - 货运方 - 取货")
    @RequestMapping(value = "/take", method = RequestMethod.POST)
    public ResponseDTO takeOrder(@RequestBody HbzBuyOrderMapDTO map, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO u = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(map, "buy_get");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "操作失败", err);
        }
        HbzBuyOrderDTO query = new HbzBuyOrderDTO();
        query.setId(map.getId());
        HbzBuyOrderDTO order = hbzBuyOrderServices.get(query);
        if (order.getOrderTrans() == OrderTrans.WAIT_TO_TAKE) {
            order.setOrderTrans(OrderTrans.TRANSPORT);
            order.setDealUser(u);
            order.setDealUserId(u.getId());
            order.setDealTime(System.currentTimeMillis());
            if (u.getId().equals(order.getTakeUserId())) {
                if ((order = hbzBuyOrderServices.save(order)) != null) {
                    hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), u, u.getId(), order.getOrderTrans(), System.currentTimeMillis()));
                    return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
                } else
                    return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, "接单人不是取单人");
            }
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    @Label("App端 - 帮买 - 货运方 - 送达")
    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    public ResponseDTO comppleteOrder(@RequestBody HbzBuyOrderMapDTO mapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO u = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(mapDTO, "buy_get");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "操作失败", err);
        }
        HbzBuyOrderDTO query = new HbzBuyOrderDTO();
        query.setId(mapDTO.getId());
        HbzBuyOrderDTO order = hbzBuyOrderServices.get(query);
        if (order.getOrderTrans() == OrderTrans.TRANSPORT) {
            order.setCompleteImage(mapDTO.getCompleteImage());
            order.setOrderTrans(OrderTrans.WAIT_TO_CONFIRM);
            if ((order = hbzBuyOrderServices.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), u, u.getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
            }
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

    @Label("App端 - 帮买 - 发货方 - 收货")
    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public ResponseDTO receiveOrder(@RequestBody HbzBuyOrderMapDTO mapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO u = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(mapDTO, "buy_get");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "操作失败", err);
        }
        HbzBuyOrderDTO query = new HbzBuyOrderDTO();
        query.setId(mapDTO.getId());
        HbzBuyOrderDTO order = hbzBuyOrderServices.get(query);
        if (Arrays.asList(OrderTrans.WAIT_TO_CONFIRM, OrderTrans.TRANSPORT).contains(order.getOrderTrans())) {
            order.setOrderTrans(OrderTrans.PAID);
            if ((order = hbzBuyOrderServices.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), u, u.getId(), order.getOrderTrans(), System.currentTimeMillis()));

                HbzRateDTO consignorRate = new HbzRateDTO();
                consignorRate.setStatus("1");
                consignorRate.setUserId(order.getCreateUserId());
                consignorRate.setUser(order.getCreateUser());
                consignorRate.setOrder(order);
                consignorRate.setOrderId(order.getId());
                consignorRate.setType(RateType.CONSIGNOR);
                consignorRate.setStar(0);
                rate.save(consignorRate);

                HbzRateDTO tranRate = new HbzRateDTO();
                tranRate.setUser(order.getTakeUser());
                tranRate.setUserId(order.getTakeUserId());
                tranRate.setOrder(order);
                tranRate.setOrderId(order.getId());
                tranRate.setType(RateType.PROVIDER);
                tranRate.setStar(0);
                tranRate.setStatus("1");
                rate.save(tranRate);

                return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        }
    }

}
