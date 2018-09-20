package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzOrderDTO;
import com.troy.keeper.hbz.dto.HbzOrderRecordDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.helper.PropertyMapper;
import com.troy.keeper.hbz.service.HbzCoordinateService;
import com.troy.keeper.hbz.service.HbzOrderRecordService;
import com.troy.keeper.hbz.service.HbzOrderService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.TimeMillisFormat;
import com.troy.keeper.hbz.sys.annotation.Config;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.management.dto.HbzOrderMapDTO;
import com.troy.keeper.management.dto.HbzOrderRecordMapDTO;
import com.troy.keeper.management.utils.MapSpec;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Label("管理端 - 订单记录 - 列表查询")
    @RequestMapping(value = "/record/query", method = RequestMethod.POST)
    public ResponseDTO queryRecord(@RequestBody HbzOrderRecordMapDTO hbzOrderRecordMapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzOrderRecordDTO rq = new HbzOrderRecordDTO();
        new Bean2Bean().addPropMapper(new PropertyMapper<>("timeMillis", new TimeMillisFormat("yyyy-MM-dd HH:mm")::parse))
                .copyProperties(hbzOrderRecordMapDTO, rq);
        rq.setStatus("1");
        List<HbzOrderRecordDTO> rs = hbzOrderRecordService.query(rq);
        return new ResponseDTO(Const.STATUS_OK, "", rs.stream().map(MapSpec::mapOrderRecord).collect(Collectors.toList()));
    }

    @Label("管理端 - 订单记录 - 分页")
    @RequestMapping(value = "/record/queryPage", method = RequestMethod.POST)
    public ResponseDTO pageQueryRecord(@RequestBody HbzOrderRecordMapDTO hbzOrderRecordMapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzOrderRecordDTO rq = new HbzOrderRecordDTO();
        new Bean2Bean().addPropMapper(new PropertyMapper<>("timeMillis", new TimeMillisFormat("yyyy-MM-dd HH:mm")::parse))
                .copyProperties(hbzOrderRecordMapDTO, rq);
        rq.setStatus("1");
        Page<HbzOrderRecordDTO> rs = hbzOrderRecordService.queryPage(rq,rq.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "", rs.map(MapSpec::mapOrderRecord));
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


}
