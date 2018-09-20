package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzTenderMapDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.*;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.service.wrapper.AddInfo;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.type.BizCode;
import com.troy.keeper.hbz.type.OrderTrans;
import com.troy.keeper.hbz.type.Role;
import com.troy.keeper.hbz.type.SettlementType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/11/29.
 */
@RestController
@RequestMapping("/api/order/tender")
public class HbzTenderResource {

    @Autowired
    private HbzOrderService hbzOrderService;
    @Autowired
    HbzTenderService hbzTenderService;
    @Autowired
    HbzUserService hbzUserService;
    @Autowired
    HbzBondService hbzBondService;
    @Autowired
    HbzPledgeService hbzPledgeService;
    @Autowired
    HbzOrderRecordService record;
    @Autowired
    MapService mapService;
    @Autowired
    HbzAreaService hbzAreaService;
    @Autowired
    HbzCoordinateService hbzCoordinateService;
    @Autowired
    HbzRoleService hbzRoleService;
    @Autowired
    SitePushMessageService sitePushMessageService;
    @Autowired
    HbzFslOrderService hbzFslOrderService;
    @Autowired
    HbzLtlOrderService hbzLtlOrderService;


    //整车专线 - 车辆征集 - 条件创建 -- 货主端
    @RequestMapping(value = {"/create", "/update"}, method = RequestMethod.POST)
    public ResponseDTO tenderOrder(@RequestBody HbzTenderMapDTO hbzTenderMapDTO, HttpServletRequest request, HttpServletResponse response) {
        if (hbzTenderMapDTO.getOrderId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "订单标识不能为空", null);
        }
        HbzOrderDTO order = hbzOrderService.findById(hbzTenderMapDTO.getOrderId());
        if (order == null || order.getStatus().equals(Const.STATUS_DISABLED)) {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        } else if (order.getOrderTrans() != OrderTrans.CONFIRMED || order.getSettlementType() != SettlementType.LEVY_ONLINE_PAYMENT) {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        } else {
            HbzTenderDTO query = new HbzTenderDTO();
            query.setStatus(Const.STATUS_ENABLED);
            query.setOrderId(order.getId());
            Long count = hbzTenderService.count(query);
            List<HbzTenderDTO> tenders = hbzTenderService.query(query);
            HbzTenderDTO tender = new HbzTenderDTO();
            if (count > 0) {
                tender = tenders.get(0);
            } else {
                switch (order.getOrderType()) {
                    case FSL: {
                        //TODO 新建时需要货主保证金
                        HbzUserDTO administrator = hbzUserService.getAdministrator(hbzUserService.currentUser().getId());
                        boolean ec2000exists = false;
                        boolean pcExists = false;
                        List<HbzBondDTO> ec2000;
                        List<HbzBondDTO> c500 = null;
                        //1、用户角色判断是否检测企业保证金，不满足则进入步骤2
                        if (hbzUserService.haveRole(administrator.getId(), Role.EnterpriseAdmin) || hbzUserService.haveRole(administrator.getId(), Role.EnterpriseConsignor)) {
                            //判断企业货主保存金是否存在
                            ec2000 = hbzBondService.findByAvailableUserBondGrade(administrator, "BOND_SL", "EC2000");
                            ec2000exists = ec2000.size() > 0;
                        }
                        //2、非企业用户，判断等级用户相应的等级保证金是否存在，并且如果存在则冻结保证金并质押
                        if (!ec2000exists) {
                            c500 = hbzBondService.findByAvailableUserBondGrade(administrator, "BOND_FSL", "C500");
                            pcExists = c500.size() > 0;
                        }
                        if (!ec2000exists && !pcExists) {
                            return new ResponseDTO(Const.STATUS_ERROR, "请交纳保证金", "NeedBond");
                        } else if (!ec2000exists && pcExists) {
                            //冻结一笔保证金，并且使用该订单编号
                            hbzPledgeService.pledge(c500.get(0), order.getOrderNo(), BizCode.ORDER);
                        }
                        break;
                    }
                    case LTL: {
                        //TODO 判断保证金 - 零担专线
                        HbzUserDTO administrator = hbzUserService.getAdministrator(hbzUserService.currentUser().getId());
                        boolean ec2000exists = false;
                        boolean pcExists = false;
                        List<HbzBondDTO> ec2000;
                        List<HbzBondDTO> c200 = null;
                        //1、用户角色判断是否检测企业保证金，不满足则进入步骤2
                        if (hbzUserService.haveRole(administrator.getId(), Role.EnterpriseAdmin) || hbzUserService.haveRole(administrator.getId(), Role.EnterpriseConsignor)) {
                            //判断企业货主保存金是否存在
                            ec2000 = hbzBondService.findByAvailableUserBondGrade(administrator, "BOND_SL", "EC2000");
                            ec2000exists = ec2000.size() > 0;
                        }
                        //2、非企业用户，判断等级用户相应的等级保证金是否存在，并且如果存在则冻结保证金并质押
                        if (!ec2000exists) {
                            c200 = hbzBondService.findByAvailableUserBondGrade(administrator, "BOND_LTL", "C200");
                            pcExists = c200.size() > 0;
                        }
                        if (!ec2000exists && !pcExists) {
                            return new ResponseDTO(Const.STATUS_ERROR, "请交纳保证金", "NeedBond");
                        } else if (!ec2000exists && pcExists) {
                            //冻结一笔保证金，并且使用该订单编号
                            hbzPledgeService.pledge(c200.get(0), order.getOrderNo(), BizCode.ORDER);
                        }
                    }
                }
            }

            new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(hbzTenderMapDTO, tender);
            tender.setStatus(Const.STATUS_ENABLED);
            tender = hbzTenderService.save(tender);

            order.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);
            hbzOrderService.save(order);
            record.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), order.getOrderTrans(), System.currentTimeMillis()));

            if (tender != null) {
                HbzCoordinateDTO coordinateQuery = new HbzCoordinateDTO();
                AddInfo addInfo = mapService.getLocationX(order.getOriginX(), order.getOriginY());
                if (addInfo != null) {
                    HbzAreaDTO hbzArea = hbzAreaService.findByOutCode(addInfo.getCityCode());
                    if (hbzArea != null) {
                        coordinateQuery.setStatus("1");
                        coordinateQuery.setAreaId(hbzArea.getId());
                        coordinateQuery.setSyncMillisGE(System.currentTimeMillis() - 1800L * 1000L);
                        List<HbzCoordinateDTO> list = hbzCoordinateService.query(coordinateQuery);
                        List<HbzUserDTO> users = list.stream().map(HbzCoordinateDTO::getUser).filter(user -> {
                            HbzRoleDTO roleQuery = new HbzRoleDTO();
                            roleQuery.setStatus("1");
                            roleQuery.setUserId(user.getId());
                            roleQuery.setRoles(Arrays.asList(Role.EnterpriseAdmin, Role.PersonDriver));
                            return hbzRoleService.count(roleQuery) > 0;
                        }).collect(Collectors.toList());
                        switch (order.getOrderType()) {
                            case FSL:
                                HbzFslOrderDTO fsl = hbzFslOrderService.findById(order.getId());
                                sitePushMessageService.sendMessageImmediately(users,
                                        "货源通知消息",
                                        "有新的货源",
                                        "您有新的货源消息啦\n" + fsl.getOriginAddress() + "-->" + fsl.getDestAddress() + "（" + fsl.getCommodityName() + "/" + fsl.getCommodityWeight() + "／" + fsl.getCommodityVolume() + "），现在联系货主！");
                                break;
                            case LTL:
                                HbzLtlOrderDTO ltl = hbzLtlOrderService.findById(order.getId());
                                sitePushMessageService.sendMessageImmediately(users,
                                        "货源通知消息",
                                        "有新的货源",
                                        "您有新的货源消息啦\n" + ltl.getOriginAddress() + "-->" + ltl.getDestAddress() + "（" + ltl.getCommodityName() + "/" + ltl.getCommodityWeight() + "／" + ltl.getCommodityVolume() + "），现在联系货主！");
                                break;
                        }
                    }
                }
                return new ResponseDTO(Const.STATUS_OK, "创建投标条件成功", null);
            } else
                return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        }
    }

    //整车专线 - 车辆征集 - 条件创建 -- 货主端
    @RequestMapping(value = {"/get"}, method = RequestMethod.POST)
    public ResponseDTO tenderByOrder(@RequestBody HbzTenderMapDTO hbzTenderMapDTO, HttpServletRequest request, HttpServletResponse response) {
        if (hbzTenderMapDTO.getOrderId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "订单标识不能为空", null);
        }
        HbzOrderDTO idDTO = new HbzOrderDTO();
        idDTO.setId(hbzTenderMapDTO.getOrderId());
        HbzOrderDTO order = hbzOrderService.get(idDTO);
        if (order == null || order.getStatus().equals(Const.STATUS_DISABLED)) {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        } else {
            HbzTenderDTO query = new HbzTenderDTO();
            query.setStatus(Const.STATUS_ENABLED);
            query.setOrderId(order.getId());
            Long count = hbzTenderService.count(query);
            List<HbzTenderDTO> tenders = hbzTenderService.query(query);
            if (count > 0) {
                HbzTenderDTO tender = tenders.get(0);
                return new ResponseDTO<>(Const.STATUS_OK, "查询成功", Optional.of(tender).map(MapSpec::mapTender).get());
            }
            return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
        }
    }
}
