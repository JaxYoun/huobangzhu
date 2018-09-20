package com.troy.keeper.hbz.app.rest.web;

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
import com.troy.keeper.hbz.type.OrderTrans;
import com.troy.keeper.hbz.type.OrderType;
import com.troy.keeper.hbz.type.SettlementType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Author：YangJx
 * @Description：快递业务控制层
 * @DateTime：2017/12/5 14:48
 */
@RestController
@RequestMapping("/api/web/express")
public class WebExpressResource {

    @Autowired
    private HbzExOrderService hbzExOrderService;

    @Autowired
    private HbzOrderService hbzOrderService;

    @Autowired
    private MapService map;

    @Autowired
    private HbzUserService hbzUserService;

    @Autowired
    HbzAreaService areaService;

    @Config("com.hbz.order.buy.limit")
    private Long limit;

    @Config("com.hbz.location.x.width")
    private Double xWidth;

    @Config("com.hbz.location.y.width")
    private Double yWidth;
    @Autowired
    HbzOrderRecordService hbzOrderRecordService;

    /**
     * 货主 创建快递
     *
     * @param orderMapDTO
     * @return
     */
    @PostMapping("/createExpress")
    public ResponseDTO createExpress(@RequestBody HbzExOrderMapDTO orderMapDTO) {
        if (orderMapDTO.getId() != null) {
            return new ResponseDTO<>(Const.STATUS_VALIDATION_ERROR, "非法操作!");
        } else {
            String[] errors = ValidationHelper.valid(orderMapDTO, "ex_order_create");
            if (errors != null && errors.length > 0) {
                return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证失败!", errors);
            } else {
                String expressOrderNo = hbzOrderService.createNewOrderNo(OrderType.EX);
                HbzExOrderDTO expressDTO = new HbzExOrderDTO();
                new Bean2Bean(new PropertyMapper<>("takeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)).copyProperties(orderMapDTO, expressDTO);

                //封装发货地址
                if (StringHelper.isNullOREmpty(expressDTO.getOriginInfo())) {
                    if (expressDTO.getOriginX() != null && expressDTO.getOriginY() != null) {
                        Map<String, Object> map = this.map.getLocation(expressDTO.getOriginX(), expressDTO.getOriginY());
                        String address = (String) map.get("formatted_address");
                        expressDTO.setOriginInfo(address);
                    }
                }

                //封装收货地址
                if (StringHelper.isNullOREmpty(expressDTO.getDestInfo())) {
                    if (expressDTO.getDestX() != null && expressDTO.getDestY() != null) {
                        Map<String, Object> map = this.map.getLocation(expressDTO.getDestX(), expressDTO.getDestY());
                        System.out.println();
                        String address = (String) map.get("formatted_address");
                        expressDTO.setDestInfo(address);
                    }
                }

                //设置订单类型 快递
                expressDTO.setOrderType(OrderType.EX);

                //设置为新建状态、支付方式
                expressDTO.setOrderTrans(OrderTrans.NEW);
                expressDTO.setSettlementType(SettlementType.ONLINE_PAYMENT);

                //设置业务订单号
                expressDTO.setOrderNo(expressOrderNo);
                expressDTO.setCreateUserId(hbzUserService.currentUser().getId());

                //址区
                HbzAreaDTO originArea = areaService.findByOutCode(orderMapDTO.getOriginAreaCode());
                HbzAreaDTO destArea = areaService.findByOutCode(orderMapDTO.getDestAreaCode());
                expressDTO.setOriginArea(originArea);

                expressDTO.setOriginAreaId(originArea.getId());
                expressDTO.setDestArea(destArea);
                expressDTO.setDestAreaId(destArea.getId());

                //置未删除状态
                expressDTO.setStatus(Const.STATUS_ENABLED);
                if ((expressDTO = hbzExOrderService.save(expressDTO)) != null) {
                    hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(expressDTO, expressDTO.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), expressDTO.getOrderTrans(), System.currentTimeMillis()));
                    Map<String, Object> id = new HashMap<>();
                    System.out.println();
                    id.put("id", expressDTO.getId());
                    id.put("orderNo", expressDTO.getOrderNo());
                    return new ResponseDTO(Const.STATUS_OK, "保存成功", id);
                } else {
                    System.out.println();
                    return new ResponseDTO(Const.STATUS_ERROR, "保存失败", null);
                }
            }
        }
    }

    /**
     * 货主 确认快递
     *
     * @param orderMapDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/confirmExpress")
    public ResponseDTO confirmExpress(@RequestBody HbzExOrderMapDTO orderMapDTO, HttpServletRequest request, HttpServletResponse response) {
        if (orderMapDTO.getId() == null) {
            new ResponseDTO(Const.STATUS_ERROR, "订单标识为空!");
        }
        String[] errors = ValidationHelper.valid(orderMapDTO, "ex_ord_confirm");
        if (errors != null && errors.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证失败", errors);
        }
        HbzExOrderDTO query = new HbzExOrderDTO();
        query.setId(orderMapDTO.getId());
        HbzExOrderDTO order = hbzExOrderService.get(query);
        if (order == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，该订单不存在!");
        }
        if (!order.getCreateUser().getId().equals(hbzUserService.currentUser().getId())) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误，不能修改非自己的订单!");
        }
        if (order != null && order.getOrderTrans() == OrderTrans.NEW) {
            order.setOrderTrans(OrderTrans.CONFIRMED);
            if ((order = hbzExOrderService.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "保存成功!");
            }
        }
        return new ResponseDTO(Const.STATUS_ERROR, "保存失败，状态错误", null);
    }

    /**
     * 货主 分页查询当前用户发布的快递
     * @param hbzExOrderMapDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/queryMyExpressByPage")
    public ResponseDTO queryMyExpressByPage(@RequestBody HbzExOrderMapDTO hbzExOrderMapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzExOrderDTO hbzExOrderDTO = new HbzExOrderDTO();
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("takeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
        ).copyProperties(hbzExOrderMapDTO, hbzExOrderDTO);

        hbzExOrderDTO.setCreateUserId(hbzUserService.currentUser().getId());
        hbzExOrderDTO.setStatus(Const.STATUS_ENABLED);
        Page<HbzExOrderDTO> page = hbzExOrderService.queryPage(hbzExOrderDTO, hbzExOrderDTO.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功!", page.map(MapSpec::maperEx));
    }

    //快递详情查询 TODO

    /**
     * 司机 分页查询可接快递
     * @param hbzExOrderMapDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/queryAvailableExpressByPage")
    public ResponseDTO queryAvailableExpressByPage(@RequestBody HbzExOrderMapDTO hbzExOrderMapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzExOrderDTO hbzExOrderDTO = new HbzExOrderDTO();
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("takeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
        ).copyProperties(hbzExOrderMapDTO, hbzExOrderDTO);

        if (hbzExOrderMapDTO.getLocationX() != null && hbzExOrderMapDTO.getLocationY() != null) {
            Double distanceX = 1D / 111D * xWidth * Math.cos(Math.PI / 4 * hbzExOrderMapDTO.getLocationY() / 90);
            Double distanceY = yWidth / 111D;
            if (hbzExOrderMapDTO.getDistance() != null) {
                distanceX = 1D / 111D * hbzExOrderMapDTO.getDistance() * Math.cos(Math.PI / 4 * 1 * hbzExOrderMapDTO.getLocationY() / 90);
                distanceY = 1D / 111D * hbzExOrderMapDTO.getDistance();
            }
            hbzExOrderDTO.setDestXLE(hbzExOrderMapDTO.getLocationX() + distanceX);
            hbzExOrderDTO.setDestXGE(hbzExOrderMapDTO.getLocationX() - distanceX);
            hbzExOrderDTO.setDestYLE(hbzExOrderMapDTO.getLocationY() + distanceY);
            hbzExOrderDTO.setDestYGE(hbzExOrderMapDTO.getLocationY() - distanceY);
        } else if (hbzExOrderMapDTO.getLocationX() != null || hbzExOrderMapDTO.getLocationY() != null) {
            return new ResponseDTO(Const.STATUS_ERROR, "坐标必须传两个!");
        }

        //订单必须是待接运
        hbzExOrderDTO.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);

        List<String[]> sorts = new ArrayList<>();
        sorts.add(new String[]{"lastUpdatedDate", "DESC"});
        hbzExOrderDTO.setSorts(sorts);
        //订单是正常状态
        hbzExOrderDTO.setStatus(Const.STATUS_ENABLED);
        Page<HbzExOrderDTO> page = hbzExOrderService.queryPage(hbzExOrderDTO, hbzExOrderDTO.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功", page.map(MapSpec::maperEx));
    }

    //收货整车专线订单 -- 司机端

    //司机 抢线订单

    /**
     * 司机 快递接单（抢单或者接受月结指派）
     * @param hbzExOrderMapDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/takeExpress")
    public ResponseDTO takeExpress(@RequestBody HbzExOrderMapDTO hbzExOrderMapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO user = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(hbzExOrderMapDTO, "hbz_exorder_get");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败!", err);
        }
        HbzExOrderDTO hbzExOrderDTO = new HbzExOrderDTO();
        hbzExOrderDTO.setId(hbzExOrderMapDTO.getId());
        HbzExOrderDTO hbzExOrderDTOFromDB = hbzExOrderService.get(hbzExOrderDTO);
        if (hbzExOrderDTOFromDB.getOrderTrans() == OrderTrans.ORDER_TO_BE_RECEIVED) {
            hbzExOrderDTOFromDB.setOrderTrans(OrderTrans.WAIT_TO_TAKE);
            hbzExOrderDTOFromDB.setTakeUser(user);
            hbzExOrderDTOFromDB.setTakeUserId(user.getId());
            if ((hbzExOrderDTOFromDB = hbzExOrderService.save(hbzExOrderDTOFromDB)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(hbzExOrderDTOFromDB, hbzExOrderDTOFromDB.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), hbzExOrderDTOFromDB.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "抢单成功!", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败!", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单!", null);
        }
    }

    /**
     * 司机 我接单的快递分页查询
     * @param queryDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/queryMyTakenExpressByPage")
    public ResponseDTO queryMyTakenExpressByPage(@RequestBody HbzExOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzExOrderDTO query = new HbzExOrderDTO();
        new Bean2Bean().addPropMapper(
                new PropertyMapper<>("takeTime", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeLT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeLE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeGT", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse),
                new PropertyMapper<>("takeTimeGE", new TimeMillisFormater("yyyy-MM-dd HH:mm")::parse)
        ).copyProperties(queryDTO, query);
        query.setTakeUserId(hbzUserService.currentUser().getId());
        query.setOrderTranses(Arrays.asList(OrderTrans.WAIT_TO_TAKE, OrderTrans.TRANSPORT, OrderTrans.WAIT_TO_CONFIRM, OrderTrans.WAIT_FOR_PAYMENT));
        query.setStatus(Const.STATUS_ENABLED);
        Page<HbzExOrderDTO> page = hbzExOrderService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功!", page.map(MapSpec::maperEx));
    }

    /**
     * 司机 快递接运（即抢单或接单完成后实地装货）
     * @param hbzExOrderMapDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/pickuExpress")
    public ResponseDTO pickuExpress(@RequestBody HbzExOrderMapDTO hbzExOrderMapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO u = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(hbzExOrderMapDTO, "buy_get");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败!", err);
        }
        HbzExOrderDTO hbzExOrderDTO = new HbzExOrderDTO();
        hbzExOrderDTO.setId(hbzExOrderMapDTO.getId());
        HbzExOrderDTO order = hbzExOrderService.get(hbzExOrderDTO);
        if (order.getOrderTrans() == OrderTrans.WAIT_TO_TAKE) {
            order.setOrderTrans(OrderTrans.TRANSPORT);
            order.setDealUser(u);
            order.setDealUserId(u.getId());
            if ((order = hbzExOrderService.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "操作成功!", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败!", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单!", null);
        }
    }

    /**
     * 司机 快递已送达
     * @param mapDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/completeExpress")
    public ResponseDTO completeExpress(@RequestBody HbzExOrderMapDTO mapDTO, HttpServletRequest request, HttpServletResponse response) {
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
                return new ResponseDTO(Const.STATUS_OK, "操作成功!", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败!", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单!", null);
        }
    }

    /**
     * 货主 确认快递已送达
     * @param mapDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/confirmExpressArrived")
    public ResponseDTO confirmExpressArrived(@RequestBody HbzExOrderMapDTO mapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO u = hbzUserService.currentUser();
        String[] err = ValidationHelper.valid(mapDTO, "buy_get");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败!", err);
        }
        HbzExOrderDTO query = new HbzExOrderDTO();
        query.setId(mapDTO.getId());
        HbzExOrderDTO order = hbzExOrderService.get(query);
        if (order.getOrderTrans() == OrderTrans.WAIT_TO_CONFIRM) {
            order.setOrderTrans(OrderTrans.WAIT_FOR_PAYMENT);
            if ((order = hbzExOrderService.save(order)) != null) {
                hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), order.getOrderTrans(), System.currentTimeMillis()));
                return new ResponseDTO(Const.STATUS_OK, "操作成功!", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败!", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单!", null);
        }
    }

    /**
     * 查询快递详情
     * @param mapDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/queryExpressDetail")
    public ResponseDTO queryExpressDetail(@RequestBody HbzExOrderMapDTO mapDTO, HttpServletRequest request, HttpServletResponse response) {
        String[] err = ValidationHelper.valid(mapDTO, "buy_get");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "参数错误!", err);
        }
        return new ResponseDTO(Const.STATUS_OK, "成功！", this.hbzExOrderService.findById(mapDTO.getId()));
    }

}
