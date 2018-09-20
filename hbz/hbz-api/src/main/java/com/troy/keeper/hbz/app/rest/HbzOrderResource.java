package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzOrderMapDTO;
import com.troy.keeper.hbz.app.dto.HbzOrderRecordMapDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzCoordinateDTO;
import com.troy.keeper.hbz.dto.HbzOrderDTO;
import com.troy.keeper.hbz.dto.HbzOrderRecordDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.po.HbzOrderRecord;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.service.mapper.HbzOrderMapper;
import com.troy.keeper.hbz.service.mapper.HbzOrderRecordMapper;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.TimeMillisFormat;
import com.troy.keeper.hbz.sys.annotation.Config;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.hbz.type.OrderTrans;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/11/1.
 */
@CommonsLog
@CrossOrigin
@RestController
@RequestMapping({"/api/order"})
public class HbzOrderResource {

    @Autowired
    private HbzOrderService hbzOrderService;

    @Autowired
    private HbzUserService hbzUserService;

    @Config("com.hbz.location.x.width")
    private Double xWidth;

    @Config("com.hbz.location.y.width")
    private Double yWidth;

    @Autowired
    private EntityService entityService;

    @Autowired
    private HbzOrderMapper hbzOrderMapper;

    @Autowired
    HbzOrderRecordMapper hbzOrderRecordMapper;

    @Autowired
    HbzOrderRecordService hbzOrderRecordService;

    @Autowired
    HbzCoordinateService hbzCoordinateService;

    //查询我创建的订单
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseDTO order(@RequestBody HbzOrderMapDTO orderDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzOrderDTO query = new HbzOrderDTO();
        new Bean2Bean().addExcludeProp(Const.ID_FIELDS).copyProperties(orderDTO, query);
        query.setCreateUser(user);
        query.setCreateUserId(user.getId());
        query.setStatus(Const.STATUS_ENABLED);
        List<HbzOrderDTO> orders = hbzOrderService.query(query);
        return new ResponseDTO(Const.STATUS_OK, null, orders.stream().map(MapSpec::mapTypeOrder).collect(Collectors.toList()));
    }

    //查询我创建的订单
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public ResponseDTO orderPage(@RequestBody HbzOrderMapDTO orderDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzOrderDTO query = new HbzOrderDTO();
        new Bean2Bean().addExcludeProp(Const.ID_FIELDS).copyProperties(orderDTO, query);
        query.setCreateUser(user);
        query.setCreateUserId(user.getId());
        query.setStatus(Const.STATUS_ENABLED);
        Page<HbzOrderDTO> orders = hbzOrderService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, null, orders.map(MapSpec::mapTypeOrder));
    }

    //查询我接到的订单
    @RequestMapping(value = "/task/query", method = RequestMethod.POST)
    public ResponseDTO taskorder(@RequestBody HbzOrderMapDTO orderDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzOrderDTO query = new HbzOrderDTO();
        new Bean2Bean().addExcludeProp(Const.ID_FIELDS).copyProperties(orderDTO, query);
        query.setTakeUserId(hbzUserService.getAdministrator(hbzUserService.currentUser().getId()).getId());
        query.setStatus(Const.STATUS_ENABLED);
        List<HbzOrderDTO> orders = hbzOrderService.query(query);
        return new ResponseDTO(Const.STATUS_OK, null, orders.stream().map(MapSpec::mapTypeOrder).collect(Collectors.toList()));
    }

    @RequestMapping(value = "/task/queryPage", method = RequestMethod.POST)
    public ResponseDTO ordertaskPage(@RequestBody HbzOrderMapDTO orderDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzOrderDTO query = new HbzOrderDTO();
        new Bean2Bean().addExcludeProp(Const.ID_FIELDS).copyProperties(orderDTO, query);
        query.setTakeUserId(hbzUserService.getAdministrator(hbzUserService.currentUser().getId()).getId());
        query.setStatus(Const.STATUS_ENABLED);
        Page<HbzOrderDTO> orders = hbzOrderService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, null, orders.map(MapSpec::mapTypeOrder));
    }

    //查询订单 -- 附近货源 -- 找货源
    @RequestMapping(value = "/task/near/query", method = RequestMethod.POST)
    public ResponseDTO queryTaskFslOrderNear(@RequestBody HbzOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzOrderDTO query = new HbzOrderDTO();
        new Bean2Bean().copyProperties(queryDTO, query);
        if (queryDTO.getLocationX() != null && queryDTO.getLocationY() != null) {
            Double distanceX = 1D / 111D * xWidth * Math.cos(Math.PI / 4 * queryDTO.getLocationY() / 90);
            Double distanceY = yWidth / 111D;
            if (queryDTO.getDistance() != null) {
                distanceX = 1D / 111D * queryDTO.getDistance() * Math.cos(Math.PI / 4 * queryDTO.getLocationY() / 90);
                distanceY = 1D / 111D * queryDTO.getDistance();
            }
            //用户在范围内
            query.setOriginXLE(queryDTO.getLocationX() + distanceX);
            query.setOriginXGE(queryDTO.getLocationX() - distanceX);

            query.setOriginYLE(queryDTO.getLocationY() + distanceY);
            query.setOriginYGE(queryDTO.getLocationY() - distanceY);
        } else if (queryDTO.getLocationX() != null || queryDTO.getLocationY() != null) {
            return new ResponseDTO(Const.STATUS_ERROR, "坐标必须传两个");
        }

        //订单必须是待接运
        query.setStatus(Const.STATUS_ENABLED);
        query.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);

        //排序
        List<String[]> sorts = new ArrayList<>();
        sorts.add(new String[]{"lastUpdatedDate", "DESC"});
        query.setSorts(sorts);

        List<HbzOrderDTO> list = hbzOrderService.query(query);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", list.stream().map(MapSpec::mapTypeOrder).collect(Collectors.toList()));
    }

    //查询附近订单
    @RequestMapping(value = "/task/near/queryPage", method = RequestMethod.POST)
    public ResponseDTO pagequeryTaskFslOrderNear(@RequestBody HbzOrderMapDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzOrderDTO query = new HbzOrderDTO();
        new Bean2Bean().copyProperties(queryDTO, query);
        if (queryDTO.getLocationX() != null && queryDTO.getLocationY() != null) {
            Double distanceX = 1D / 111D * xWidth * Math.cos(Math.PI / 4 * queryDTO.getLocationY() / 90);
            Double distanceY = yWidth / 111D;
            if (queryDTO.getDistance() != null) {
                distanceX = 1D / 111D * queryDTO.getDistance() * Math.cos(Math.PI / 4 * queryDTO.getLocationY() / 90);
                distanceY = 1D / 111D * queryDTO.getDistance();
            }
            //用户在范围内
            query.setOriginXLE(queryDTO.getLocationX() + distanceX);
            query.setOriginXGE(queryDTO.getLocationX() - distanceX);

            query.setOriginYLE(queryDTO.getLocationY() + distanceY);
            query.setOriginYGE(queryDTO.getLocationY() - distanceY);
        } else if (queryDTO.getLocationX() != null || queryDTO.getLocationY() != null) {
            return new ResponseDTO(Const.STATUS_ERROR, "坐标必须传两个");
        }

        //订单必须是待接运
        query.setStatus(Const.STATUS_ENABLED);
        query.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);

        //排序
        List<String[]> sorts = new ArrayList<>();
        sorts.add(new String[]{"lastUpdatedDate", "DESC"});
        query.setSorts(sorts);

        Page<HbzOrderDTO> page = hbzOrderService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功", page.map(MapSpec::mapTypeOrder));
    }

    /**
     * 得到订单详细信息
     *
     * @param od
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ResponseDTO getOrder(@RequestBody HbzOrderDTO od, HttpServletRequest request, HttpServletResponse response) {
        if (od.getId() == null)
            return new ResponseDTO(Const.STATUS_ERROR, "错误", null);
        HbzOrderDTO order = hbzOrderService.get(od);
        if (order != null)
            return new ResponseDTO(Const.STATUS_OK, "成功", Optional.of(order).map(MapSpec::mapTypeOrder).get());
        return new ResponseDTO(Const.STATUS_ERROR, null, null);
    }

    /**
     * 所有订单拒签订单
     *
     * @param od
     * @return
     */
    @RequestMapping(value = "/refuse", method = RequestMethod.POST)
    public ResponseDTO refuseOrder(@RequestBody HbzOrderDTO od, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO user = hbzUserService.currentUser();
        if (od.getId() == null)
            return new ResponseDTO(Const.STATUS_ERROR, "错误", null);
        HbzOrderDTO order = hbzOrderService.get(od);
        if (order != null) {
            order.setOfflineProcess(1);
            hbzOrderService.save(order);
            hbzOrderRecordService.save(new HbzOrderRecordDTO(order, order.getId(), user, user.getId(), order.getOrderTrans(), System.currentTimeMillis()));
            return new ResponseDTO(Const.STATUS_OK, "成功");
        }
        return new ResponseDTO(Const.STATUS_ERROR, "操作不成功");
    }

    /**
     * 所有订单退款
     *
     * @param od
     * @return
     */
    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public ResponseDTO refundOrder(@RequestBody HbzOrderDTO od, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO user = hbzUserService.currentUser();
        if (od.getId() == null)
            return new ResponseDTO(Const.STATUS_ERROR, "错误", null);
        HbzOrderDTO order = hbzOrderService.get(od);
        if (order != null) {
            order.setOrderTrans(OrderTrans.WAITE_TO_REFUNDDE);
            hbzOrderService.save(order);
            hbzOrderRecordService.save(new HbzOrderRecordDTO(order, order.getId(), user, user.getId(), order.getOrderTrans(), System.currentTimeMillis()));
            return new ResponseDTO(Const.STATUS_OK, "成功");
        }
        return new ResponseDTO(Const.STATUS_ERROR, "操作不成功");
    }

    /**
     * 车主 确认送达
     *
     * @param hbzOrderDTO
     * @return
     */
    @PostMapping("/driverSignArrival")
    public ResponseDTO driverSignArrival(@RequestBody HbzOrderDTO hbzOrderDTO) {
        HbzUserDTO currentUser = this.hbzUserService.currentUser();
        HbzOrderDTO hbzOrderDTOFromDb = this.hbzOrderService.get(hbzOrderDTO);

        if (hbzOrderDTOFromDb.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "必须传递订单id！", null);
        }
        if (!currentUser.getId().equals(hbzOrderDTOFromDb.getTakeUserId())) {
            return new ResponseDTO(Const.STATUS_ERROR, "只能操作自己的接运订单！", null);
        }
        if (hbzOrderDTOFromDb.getOrderTrans() != OrderTrans.TRANSPORT) {
            return new ResponseDTO(Const.STATUS_ERROR, "订单状态错误！", null);
        }

        hbzOrderDTO.setOrderTrans(OrderTrans.WAIT_TO_CONFIRM);
        if (this.hbzOrderService.updateOrderTrans(hbzOrderDTO)) {
            return new ResponseDTO(Const.STATUS_OK, "签收成功！", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "签收失败！", null);
        }
    }

    @RequestMapping("/loc")
    public ResponseDTO loc(@RequestBody HbzOrderMapDTO orderId) {
        HbzOrderDTO order = hbzOrderService.findById(orderId.getId());
        if (order == null) return new ResponseDTO(Const.STATUS_ERROR, "订单id不可用");
        HbzUserDTO dealUser = order.getDealUser();
        HbzCoordinateDTO query = new HbzCoordinateDTO();
        query.setStatus("1");
        List<String[]> orders = new ArrayList<>();
        orders.add(new String[]{"createdDate", "desc"});
        query.setSorts(orders);
        query.setUserId(dealUser.getId());
        query.setPage(0);
        query.setSize(1);
        Page<HbzCoordinateDTO> coordinates = hbzCoordinateService.queryPage(query, query.getPageRequest());
        if (coordinates.getTotalElements() > 0L) {
            HbzCoordinateDTO coordinate = coordinates.getContent().get(0);
            return new ResponseDTO(Const.STATUS_OK, "成功", new Double[]{coordinate.getPointX(), coordinate.getPointY()});
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "成功", "无法获取用户GPS数据");
        }
    }

    @Label("App、Web - 订单历史 - 列表查询")
    @RequestMapping(value = "/record/query", method = RequestMethod.POST)
    public ResponseDTO queryRecord(@RequestBody HbzOrderRecordMapDTO hbzOrderRecordMapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzOrderRecordDTO rq = new HbzOrderRecordDTO();
        new Bean2Bean().addPropMapper(new PropertyMapper<>("timeMillis", new TimeMillisFormat("yyyy-MM-dd HH:mm")::parse))
                .copyProperties(hbzOrderRecordMapDTO, rq);
        rq.setStatus("1");
        List<HbzOrderRecordDTO> rs = entityService.query(HbzOrderRecord.class, hbzOrderRecordMapper, rq, Arrays.asList("id", "orderTrans", "timeMillis"));
        //List<HbzOrderRecordDTO> rs = hbzOrderRecordService.query(rq);
        return new ResponseDTO(Const.STATUS_OK, "", rs.stream().map(MapSpec::mapOrderRecord).collect(Collectors.toList()));
    }

    @Label("App、Web - 订单历史 - 分页")
    @RequestMapping(value = "/record/queryPage", method = RequestMethod.POST)
    public ResponseDTO pqqueryRecord(@RequestBody HbzOrderRecordMapDTO hbzOrderRecordMapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzOrderRecordDTO rq = new HbzOrderRecordDTO();
        new Bean2Bean().addPropMapper(new PropertyMapper<>("timeMillis", new TimeMillisFormat("yyyy-MM-dd HH:mm")::parse))
                .copyProperties(hbzOrderRecordMapDTO, rq);
        rq.setStatus("1");
        Page<HbzOrderRecordDTO> psa = entityService.queryPage(HbzOrderRecord.class, hbzOrderRecordMapper, rq.getPageRequest(), rq, Arrays.asList("id", "orderTrans", "timeMillis"));
        //Page<HbzOrderRecordDTO> psa = hbzOrderRecordService.queryPage(rq, rq.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "", psa.map(MapSpec::mapOrderRecord));
    }

    /**
     * 货主 确认送达
     *
     * @param hbzOrderDTO
     * @return
     */
    @PostMapping("/consignorConfirmArrival")
    public ResponseDTO consignorConfirmArrival(@RequestBody HbzOrderDTO hbzOrderDTO) {
        HbzUserDTO currentUser = this.hbzUserService.currentUser();
        HbzOrderDTO hbzOrderDTOFromDb = this.hbzOrderService.get(hbzOrderDTO);

        if (hbzOrderDTOFromDb.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "必须传递订单id！", null);
        }
        if (!currentUser.getId().equals(hbzOrderDTOFromDb.getCreateUserId())) {
            return new ResponseDTO(Const.STATUS_ERROR, "只能操作自己创建的订单！", null);
        }
        if (hbzOrderDTOFromDb.getOrderTrans() != OrderTrans.WAIT_TO_CONFIRM) {
            return new ResponseDTO(Const.STATUS_ERROR, "订单状态错误！", null);
        }

        hbzOrderDTO.setOrderTrans(OrderTrans.WAIT_FOR_PAYMENT);
        if (this.hbzOrderService.updateOrderTrans(hbzOrderDTO)) {
            return new ResponseDTO(Const.STATUS_OK, "确认成功！", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "确认失败！", null);
        }
    }

}
