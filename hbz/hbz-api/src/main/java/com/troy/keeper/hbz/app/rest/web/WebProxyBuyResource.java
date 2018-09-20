package com.troy.keeper.hbz.app.rest.web;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzBuyOrderMapDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzBuyOrderDTO;
import com.troy.keeper.hbz.dto.HbzOrderRecordDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Author：YangJx
 * @Description：帮我买
 * @DateTime：2017/12/11 15:44
 */
@Slf4j
@RestController
@RequestMapping("/api/web/proxyBuy")
public class WebProxyBuyResource {

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

    /**
     * 货主 创建帮我买订单
     *
     * @param hbzBuyOrderDTO
     * @return
     */
    @PostMapping("/createProxyBuy")
    public ResponseDTO createProxyBuy(@RequestBody HbzBuyOrderMapDTO hbzBuyOrderDTO) {
        if (hbzBuyOrderDTO.getId() != null) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "非法操作!");
        } else {
            String[] errors = ValidationHelper.valid(hbzBuyOrderDTO, "web_buy_create");
            if (errors != null && errors.length > 0) {
                return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证失败!", errors);
            } else {
                Long currentTimeMillis = System.currentTimeMillis();
                String orderNo = hbzOrderService.createNewOrderNo(OrderType.BUY);
                HbzBuyOrderDTO order = new HbzBuyOrderDTO();
                new Bean2Bean(
                        new PropertyMapper<>("startTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
                ).copyProperties(hbzBuyOrderDTO, order);
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
                //置未删除状态
                order.setStatus(Const.STATUS_ENABLED);
                order = hbzBuyOrderServices.save(order);
                if (order != null) {
                    hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), user, user.getId(), order.getOrderTrans(), System.currentTimeMillis()));
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
     * 货主 确认帮买订单
     *
     * @param buyOrderMapDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/confirmProxyBuy")
    public ResponseDTO confirmProxyBuy(@RequestBody HbzBuyOrderMapDTO buyOrderMapDTO, HttpServletRequest request, HttpServletResponse response) {
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
            return new ResponseDTO(Const.STATUS_ERROR, "错误，该订单不存在!");
        }
        if (!order.getCreateUser().getId().equals(hbzUserService.currentUser().getId())) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，不能修改非自己的订单!");
        }

        if (order != null && order.getOrderTrans() == OrderTrans.NEW) {
            if (buyOrderMapDTO.getRemuneration() != null) {
                order.setOrderTrans(OrderTrans.CONFIRMED);
                order.setRemuneration(buyOrderMapDTO.getRemuneration());
                order.setAmount(order.getRemuneration() + order.getCommodityAmount());
                if ((order = hbzBuyOrderServices.save(order)) != null) {
                    HbzUserDTO user = hbzUserService.currentUser();
                    hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), user, user.getId(), order.getOrderTrans(), System.currentTimeMillis()));
                    return new ResponseDTO(Const.STATUS_OK, "保存成功!");
                }
            }
        }
        return new ResponseDTO(Const.STATUS_ERROR, "保存失败，状态错误!", null);
    }

    /**
     * 货主 删除帮买订单
     *
     * @param buyOrderMapDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/deleteProxyBuy")
    public ResponseDTO deleteProxyBuy(@RequestBody HbzBuyOrderMapDTO buyOrderMapDTO, HttpServletRequest request, HttpServletResponse response) {
        if (buyOrderMapDTO.getId() == null) {
            new ResponseDTO(Const.STATUS_ERROR, "订单id不能为空");
        }
        HbzBuyOrderDTO query = new HbzBuyOrderDTO();
        query.setId(buyOrderMapDTO.getId());
        HbzBuyOrderDTO order = hbzBuyOrderServices.get(query);

        if (order == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，该订单不存在!");
        }
        if (!order.getCreateUser().getId().equals(hbzUserService.currentUser().getId())) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，不能删除非自己的订单!");
        }
        if (order.getOrderTrans() != OrderTrans.NEW) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，只能删除新建订单!");
        }

        if (order != null && order.getOrderTrans() == OrderTrans.NEW) {
            if (buyOrderMapDTO.getRemuneration() != null) {
                order.setStatus(Const.STATUS_DISABLED);
                if ((order = hbzBuyOrderServices.save(order)) != null) {
                    HbzUserDTO user = hbzUserService.currentUser();
                    hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), user, user.getId(), order.getOrderTrans(), System.currentTimeMillis()));
                    return new ResponseDTO(Const.STATUS_OK, "删除成功!");
                }
            }
        }
        return new ResponseDTO(Const.STATUS_ERROR, "保存失败，状态错误!", null);
    }

    /**
     * 货主 分页查询我创建的帮买订单
     *
     * @param queryDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/queryMyProxyBuyByPage")
    public ResponseDTO queryMyProxyBuyPage(@RequestBody HbzBuyOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
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
        return new ResponseDTO(Const.STATUS_OK, "查询成功!", page.map(MapSpec::mapBuyOrder));
    }

    /**
     * 车主 分页查询可接运帮买订单
     *
     * @param queryDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/queryAvailableProxyBuyByPage")
    public ResponseDTO queryAvailableProxyBuyByPage(@RequestBody HbzBuyOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
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
        Page<HbzBuyOrderDTO> page = hbzBuyOrderServices.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功!", page.map(MapSpec::mapBuyOrder));
    }

    /**
     * 车主 接运帮买订单
     *
     * @param map
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/takeProxyBuy")
    public ResponseDTO takeProxyBuy(@RequestBody HbzBuyOrderMapDTO map, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO user = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(map, "buy_get");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败!", err);
        }
        HbzBuyOrderDTO query = new HbzBuyOrderDTO();
        query.setId(map.getId());
        HbzBuyOrderDTO order = hbzBuyOrderServices.get(query);
        if (order.getOrderTrans() == OrderTrans.ORDER_TO_BE_RECEIVED) {
            order.setOrderTrans(OrderTrans.WAIT_TO_TAKE);
            order.setTakeUser(user);
            order.setTakeUserId(user.getId());
            if ((order = hbzBuyOrderServices.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), user, user.getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "抢单成功!", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败!", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单!", null);
        }
    }

    /**
     * 车主 揽货帮买订单
     *
     * @param map
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/pickupProxyBuy")
    public ResponseDTO pickupProxyBuy(@RequestBody HbzBuyOrderMapDTO map, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO u = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(map, "buy_get");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败!", err);
        }
        HbzBuyOrderDTO query = new HbzBuyOrderDTO();
        query.setId(map.getId());
        HbzBuyOrderDTO order = hbzBuyOrderServices.get(query);
        if (order.getOrderTrans() == OrderTrans.WAIT_TO_TAKE) {
            order.setOrderTrans(OrderTrans.TRANSPORT);
            order.setDealUser(u);
            //TODO 判断是否是接单人

            order.setDealUserId(u.getId());
            if ((order = hbzBuyOrderServices.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), u, u.getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "操作成功!", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败!", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单!", null);
        }
    }

    /**
     * 车主 订单送达
     *
     * @param mapDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/completeProxyBuy")
    public ResponseDTO completeProxyBuy(@RequestBody HbzBuyOrderMapDTO mapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO u = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(mapDTO, "buy_get");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", err);
        }
        HbzBuyOrderDTO query = new HbzBuyOrderDTO();
        query.setId(mapDTO.getId());
        HbzBuyOrderDTO order = hbzBuyOrderServices.get(query);
        if (order.getOrderTrans() == OrderTrans.TRANSPORT) {
            order.setOrderTrans(OrderTrans.WAIT_TO_CONFIRM);
            if ((order = hbzBuyOrderServices.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), u, u.getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "操作成功!", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败!", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单!", null);
        }
    }

    /**
     * 货主 确认帮买订单完成
     *
     * @param mapDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/conformReceiveProxyBuy")
    public ResponseDTO conformReceiveProxyBuy(@RequestBody HbzBuyOrderMapDTO mapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO u = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(mapDTO, "buy_get");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败!", err);
        }
        HbzBuyOrderDTO query = new HbzBuyOrderDTO();
        query.setId(mapDTO.getId());
        HbzBuyOrderDTO order = hbzBuyOrderServices.get(query);
        if (order.getOrderTrans() == OrderTrans.WAIT_TO_CONFIRM) {
            order.setOrderTrans(OrderTrans.WAIT_FOR_PAYMENT);
            if ((order = hbzBuyOrderServices.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), u, u.getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "操作成功!", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败!", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单!", null);
        }
    }

    /**
     * 查询帮我买订单详情
     *
     * @param hbzBuyOrderMapDTO
     * @return
     */
    @PostMapping("/getProxyBuyDetail")
    public ResponseDTO getProxyBuyDetail(@RequestBody HbzBuyOrderMapDTO hbzBuyOrderMapDTO) {
        String[] err = ValidationHelper.valid(hbzBuyOrderMapDTO, "buy_get");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败!", err);
        }

        HbzBuyOrderDTO hbzBuyOrderDTO = new HbzBuyOrderDTO();
        hbzBuyOrderDTO.setId(hbzBuyOrderMapDTO.getId());

        HbzBuyOrderDTO hbzBuyOrderDTOFromDB = this.hbzBuyOrderServices.getHbzBuyOrderDetail(hbzBuyOrderDTO);
        return new ResponseDTO(Const.STATUS_OK, "查询成功！", hbzBuyOrderDTOFromDB);
    }

    /**
     * 车主 分页条件查询我接的帮买订单
     * @param queryDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/queryMyTakenProxyBuyByPage")
    public ResponseDTO queryMyTakenProxyBuyByPage(@RequestBody HbzBuyOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzBuyOrderDTO hbzBuyOrderDTO = new HbzBuyOrderDTO();
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
        ).copyProperties(queryDTO, hbzBuyOrderDTO);

        hbzBuyOrderDTO.setTakeUserId(hbzUserService.currentUser().getId());
        hbzBuyOrderDTO.setOrderTranses(Arrays.asList(OrderTrans.WAIT_TO_TAKE, OrderTrans.TRANSPORT, OrderTrans.WAIT_TO_CONFIRM, OrderTrans.WAIT_FOR_PAYMENT));
        hbzBuyOrderDTO.setStatus(Const.STATUS_ENABLED);
        Page<HbzBuyOrderDTO> page = hbzBuyOrderServices.queryPage(hbzBuyOrderDTO, hbzBuyOrderDTO.getPageRequest());

        return new ResponseDTO(Const.STATUS_OK, "查询成功", page.map(MapSpec::mapBuyOrder));
    }

}
