package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzTakerInfoMapDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.*;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.po.HbzPledge;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.annotation.Config;
import com.troy.keeper.hbz.sys.annotation.Label;
import com.troy.keeper.hbz.type.*;
import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/11/1.
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping({"/api/order"})
public class HbzTakerResource {

    @Autowired
    private HbzOrderService hbzOrderService;

    @Autowired
    private HbzPayService hbzPayService;

    @Config("com.tencent.wechat.appName")
    private String appName;

    @Autowired
    private WechatService wechatService;

    @Autowired
    private AlipayService alipayService;

    @Autowired
    HbzTenderService hbzTenderService;

    @Autowired
    HbzUserService hbzUserService;

    @Autowired
    HbzUserRegistryService hbzUserRegistryService;

    @Autowired
    HbzTakerInfoService hbzTakerInfoService;

    @Autowired
    HbzRoleService hbzRoleService;

    @Autowired
    HbzTransEnterpriseRegistryService hbzTransEnterpriseRegistryService;

    @Autowired
    HbzPledgeService hbzPledgeService;

    @Autowired
    HbzBondService hbzBondService;

    @Autowired
    HbzOrderRecordService hbzOrderRecordService;

    @Autowired
    SitePushMessageService sitePushMessageService;

    @Config("com.hbz.tender.take.max")
    private Integer maxCount;

    @Label("App - 专线 - 车辆征集单 - 发货方 - 查询车辆征集列表")
    @RequestMapping(value = "/taker/consignor/query", method = RequestMethod.POST)
    public ResponseDTO takerOrder(@RequestBody HbzTakerInfoMapDTO takerInfoMapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO user = hbzUserService.currentUser();
        if (takerInfoMapDTO.getOrderId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "订单标识不能为空", null);
        }
        HbzOrderDTO idDTO = new HbzOrderDTO();
        idDTO.setId(takerInfoMapDTO.getOrderId());
        HbzOrderDTO order = hbzOrderService.get(idDTO);
        if (order == null || order.getStatus().equals(Const.STATUS_DISABLED)) {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        } else {
            HbzTakerInfoDTO query = new HbzTakerInfoDTO();
            query.setStatus(Const.STATUS_ENABLED);
            new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(takerInfoMapDTO, query);
            query.setOrderId(order.getId());
            List<HbzTakerInfoDTO> takerInfo = hbzTakerInfoService.query(query);
            return new ResponseDTO(Const.STATUS_OK, "查询成功", takerInfo.stream().map(MapSpec::mapTaker).collect(Collectors.toList()));
        }
    }

    @Label("App - 专线 - 车辆征集单 - 货运方 - 查询我参与的征集列表")
    @RequestMapping(value = "/taker/driver/query", method = RequestMethod.POST)
    public ResponseDTO takers(@RequestBody HbzTakerInfoMapDTO takerInfoMapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzTakerInfoDTO query = new HbzTakerInfoDTO();
        query.setStatus(Const.STATUS_ENABLED);
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(takerInfoMapDTO, query);
        query.setUserId(user.getId());
        query.setStatus("1");
        List<HbzTakerInfoDTO> takerInfo = hbzTakerInfoService.query(query);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", takerInfo.stream().map(MapSpec::mapTaker).collect(Collectors.toList()));
    }

    @Label("App - 专线 - 车辆征集单 - 货运方 - 查询我参与的征集列表分页")
    @RequestMapping(value = "/taker/driver/queryPage", method = RequestMethod.POST)
    public ResponseDTO ptakers(@RequestBody HbzTakerInfoMapDTO takerInfoMapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO user = hbzUserService.currentUser();
        HbzTakerInfoDTO query = new HbzTakerInfoDTO();
        query.setStatus(Const.STATUS_ENABLED);
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(takerInfoMapDTO, query);
        query.setUserId(user.getId());
        query.setStatus("1");
        Page<HbzTakerInfoDTO> page = hbzTakerInfoService.queryPage(query, query.getPageRequest());
        return new ResponseDTO(Const.STATUS_OK, "查询成功", page.map(MapSpec::mapTaker));
    }

    @Label("App - 专线 - 车辆征集单 - 发货方 - 查询车辆征集列表分页")
    @RequestMapping(value = "/taker/consignor/queryPage", method = RequestMethod.POST)
    public ResponseDTO pageTaker(@RequestBody HbzTakerInfoMapDTO takerInfoMapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO user = hbzUserService.currentUser();
        if (takerInfoMapDTO.getOrderId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "订单标识不能为空", null);
        }
        HbzOrderDTO idDTO = new HbzOrderDTO();
        idDTO.setId(takerInfoMapDTO.getOrderId());
        HbzOrderDTO order = hbzOrderService.get(idDTO);
        if (order == null || order.getStatus().equals(Const.STATUS_DISABLED)) {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        } else {
            HbzTakerInfoDTO query = new HbzTakerInfoDTO();
            new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(takerInfoMapDTO, query);
            query.setStatus(Const.STATUS_ENABLED);
            query.setOrderId(order.getId());
            Page<HbzTakerInfoDTO> page = hbzTakerInfoService.queryPage(query, query.getPageRequest());
            return new ResponseDTO(Const.STATUS_OK, "查询成功", page.map(MapSpec::mapTaker));
        }
    }

    @Label("App - 专线 - 车辆征集单 - 货运方 - 参与车辆征集")
    @RequestMapping(value = "/taker/create", method = RequestMethod.POST)
    public ResponseDTO takeCr(@RequestBody HbzTakerInfoMapDTO takerInfoMapDTO, HttpServletRequest request, HttpServletResponse response) {

        String[] err = ValidationHelper.valid(takerInfoMapDTO, "hbz_taker_create");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_VALIDATION_ERROR, "验证不通过", err);
        }
        HbzUserDTO agent = hbzUserService.currentUser();
        HbzUserDTO user = hbzUserService.getAdministrator(agent.getId());
        HbzOrderDTO order = hbzOrderService.findById(takerInfoMapDTO.getOrderId());

        if (order == null || order.getStatus().equals(Const.STATUS_DISABLED)) {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        } else if (order.getOrderTrans() != OrderTrans.ORDER_TO_BE_RECEIVED || order.getSettlementType() != SettlementType.LEVY_ONLINE_PAYMENT) {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单", null);
        } else {
            HbzTakerInfoDTO query = new HbzTakerInfoDTO();
            query.setStatus(Const.STATUS_ENABLED);
            query.setOrderId(order.getId());
            Long takeCount = hbzTakerInfoService.count(query);
            if (takeCount >= maxCount) {
                return new ResponseDTO(Const.STATUS_ERROR, "该标已满");
            }
            query.setUserId(user.getId());
            Long tenderCount = hbzTakerInfoService.count(query);
            if (tenderCount > 0) {
                return new ResponseDTO(Const.STATUS_ERROR, "已经投标，不能再次投标", null);
            } else {
                HbzTenderDTO queryTender = new HbzTenderDTO();
                queryTender.setStatus(Const.STATUS_ENABLED);
                queryTender.setOrderId(order.getId());
                List<HbzTenderDTO> tenders = hbzTenderService.query(queryTender);
                if (tenders == null || tenders.size() == 0) {
                    return new ResponseDTO(Const.STATUS_ERROR, "该标未发布投标条件", null);
                } else if (tenders.size() > 1) {
                    return new ResponseDTO(Const.STATUS_ERROR, "存在双重标准，无法投", null);
                } else {
                    HbzTenderDTO tender = tenders.get(0);
                    switch (tender.getNeed().intValue()) {
                        case 1: {
                            Map<String, Object> queryRole = new LinkedHashMap<>();
                            queryRole.put("role =", Role.PersonDriver);
                            queryRole.put("status =", Const.STATUS_ENABLED);
                            queryRole.put("users.id =", user.getId());
                            Long roleCount = hbzRoleService.count(queryRole);
                            if (roleCount == 0) {
                                return new ResponseDTO(Const.STATUS_ERROR, "你不是司机，无法投标", null);
                            } else {
                                if (tender.getStarLevel() != null) {
                                    if (user.getStarLevel() == null) {
                                        return new ResponseDTO(Const.STATUS_ERROR, "你未评星级", null);
                                    }
                                    if (tender.getStarLevel() > user.getStarLevel()) {
                                        return new ResponseDTO(Const.STATUS_ERROR, "你星级不够，该标星级需要：" + tender.getStarLevel(), null);
                                    }
                                }
                            }
                            break;
                        }
                        case 2: {
                            Map<String, Object> queryRole = new LinkedHashMap<>();
                            queryRole.put("role =", Role.EnterpriseAdmin);
                            queryRole.put("status =", Const.STATUS_ENABLED);
                            queryRole.put("users.id =", user.getId());
                            Long roleCount = hbzRoleService.count(queryRole);
                            if (roleCount == 0) {
                                return new ResponseDTO(Const.STATUS_ERROR, "你不是货运公司，无法投标", null);
                            } else {
                                if (tender.getRegistryMoney() != null && tender.getRegistryMoney() > 0D) {
                                    HbzTransEnterpriseRegistryDTO transEnterpriseRegistry = hbzTransEnterpriseRegistryService.findTransEnterpriseRegistry(user, true);
                                    if (transEnterpriseRegistry == null) {
                                        return new ResponseDTO(Const.STATUS_ERROR, "你没有资质注册信息", null);
                                    }
                                    if (transEnterpriseRegistry.getRegistryMoney() == null)
                                        return new ResponseDTO(Const.STATUS_ERROR, "你还没有注册资金", null);
                                    if (tender.getRegistryMoney() > transEnterpriseRegistry.getRegistryMoney())
                                        return new ResponseDTO(Const.STATUS_ERROR, "你注册资金不够，该标要求注册资金：" + tender.getRegistryMoney(), null);
                                }
                            }
                            break;
                        }
                        case 3: {
                            Map<String, Object> queryRole = new LinkedHashMap<>();
                            queryRole.put("role in", Arrays.asList(Role.EnterpriseAdmin, Role.PersonDriver));
                            queryRole.put("status =", Const.STATUS_ENABLED);
                            queryRole.put("users.id =", user.getId());
                            Long roleCount = hbzRoleService.count(queryRole);
                            if (roleCount == 0) {
                                return new ResponseDTO(Const.STATUS_ERROR, "你没有资质，无法投标", null);
                            } else {
                                if (tender.getRegistryMoney() != null && tender.getRegistryMoney() > 0D) {
                                    HbzTransEnterpriseRegistryDTO transEnterpriseRegistry = hbzTransEnterpriseRegistryService.findTransEnterpriseRegistry(user, true);
                                    if (transEnterpriseRegistry == null) {
                                        return new ResponseDTO(Const.STATUS_ERROR, "你没有资质注册信息", null);
                                    }
                                    if (transEnterpriseRegistry.getRegistryMoney() == null) {
                                        return new ResponseDTO(Const.STATUS_ERROR, "你还没有注册资金", null);
                                    }
                                    if (tender.getRegistryMoney() > transEnterpriseRegistry.getRegistryMoney())
                                        return new ResponseDTO(Const.STATUS_ERROR, "你注册资金不够，该标要求注册资金：" + tender.getRegistryMoney(), null);
                                }
                                if (tender.getStarLevel() != null) {
                                    if (user.getStarLevel() == null) {
                                        return new ResponseDTO(Const.STATUS_ERROR, "你未评星级", null);
                                    }
                                    if (tender.getStarLevel() > user.getStarLevel()) {
                                        return new ResponseDTO(Const.STATUS_ERROR, "你星级不够，该标星级需要：" + tender.getStarLevel(), null);
                                    }
                                }
                            }
                            break;
                        }
                    }
                    switch (tender.getBond().intValue()) {
                        case 0: {

                        }
                        break;
                        case 1: {
                            //是否有企业保证金，我的账户
                            List<HbzBondDTO> ed2000s = hbzBondService.findByAvailableUserBondGrade(user, "BOND_SL", "ED2000");
                            if (ed2000s.size() > 0) {
                                log.info("{}存在企业保证金，不用冻结，直接可投标!", user.getTelephone());
                            } else {
                                switch (order.getOrderType()) {
                                    case FSL: {
                                        List<HbzBondDTO> d500s = hbzBondService.findByAvailableUserBondGrade(user, "BOND_FSL", "D500");
                                        if (d500s.size() > 0) {
                                            HbzBondDTO bond = d500s.get(0);
                                            hbzPledgeService.pledge(bond, "Tender#".concat(String.valueOf(tender.getId())), BizCode.Tender);
                                        } else {
                                            return new ResponseDTO(Const.STATUS_ERROR, "对不起，该订单需要保证金.");
                                        }
                                    }
                                    break;
                                    case LTL: {
                                        List<HbzBondDTO> d200s = hbzBondService.findByAvailableUserBondGrade(user, "BOND_LTL", "D200");
                                        if (d200s.size() > 0) {
                                            HbzBondDTO bond = d200s.get(0);
                                            hbzPledgeService.pledge(bond, "Tender#".concat(String.valueOf(tender.getId())), BizCode.Tender);
                                        } else {
                                            return new ResponseDTO(Const.STATUS_ERROR, "对不起，该订单需要保证金.");
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                        break;
                        default: {
                            return new ResponseDTO(Const.STATUS_ERROR, "不支持的保证金类型.");
                        }
                    }
                    HbzTakerInfoDTO takerInfo = new HbzTakerInfoDTO();
                    takerInfo.setStatus(Const.STATUS_ENABLED);
                    takerInfo.setOffer(takerInfoMapDTO.getOffer());
                    takerInfo.setUserId(user.getId());
                    takerInfo.setUser(user);
                    takerInfo.setAgent(agent);
                    takerInfo.setAgentId(agent.getId());
                    takerInfo.setOrder(tender.getOrder());
                    takerInfo.setOrderId(tender.getOrder().getId());
                    takerInfo.setTakeType(TakeType.TAKEING);
                    takerInfo = hbzTakerInfoService.save(takerInfo);
                    takeCount++;
                    if (takeCount >= maxCount) {
                        order.setOrderTrans(OrderTrans.LOCKED_ORDER);
                        hbzOrderService.save(order);
                    }
                    if (takerInfo != null) {
                        return new ResponseDTO(Const.STATUS_OK, "投标成功", null);
                    } else {
                        return new ResponseDTO(Const.STATUS_ERROR, "失败", null);
                    }
                }
            }
        }
    }

    @Label("App - 专线 - 车辆征集单 - 发货方 - 车辆征集详细")
    @RequestMapping(value = "/taker/get", method = RequestMethod.POST)
    public ResponseDTO takeget(@RequestBody HbzTakerInfoMapDTO takerInfoMapDTO, HttpServletRequest request, HttpServletResponse response) {
        if (takerInfoMapDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "订单标识不能为空", null);
        }
        HbzTakerInfoDTO query = new HbzTakerInfoDTO();
        query.setId(takerInfoMapDTO.getId());
        HbzTakerInfoDTO takerInfo = hbzTakerInfoService.get(query);
        if (takerInfo != null) {
            return new ResponseDTO(Const.STATUS_OK, "成功", Optional.of(takerInfo).map(MapSpec::mapTaker).get());
        }
        return new ResponseDTO(Const.STATUS_ERROR, "错误", null);
    }


    @Label("App - 专线 - 车辆征集单 - 发货方 - 选择")
    @RequestMapping(value = "/taker/consignor/take", method = RequestMethod.POST)
    public ResponseDTO take(@RequestBody HbzTakerInfoMapDTO takerInfoMapDTO, HttpServletRequest request, HttpServletResponse response) {
        if (takerInfoMapDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "投标不能为空", null);
        }
        HbzTakerInfoDTO idc = new HbzTakerInfoDTO();
        idc.setId(takerInfoMapDTO.getId());
        HbzTakerInfoDTO taker = hbzTakerInfoService.get(idc);
        if (taker != null) {
            HbzOrderDTO order = taker.getOrder();
            if (taker.getUserId() != null) {
                if (order != null) {
                    HbzTenderDTO tender = hbzTenderService.findByOrderId(taker.getOrderId());
                    switch (tender.getBond().intValue()) {
                        case 1: {
                            List<HbzBondDTO> ed2000s = hbzBondService.findByAvailableUserBondGrade(taker.getUser(), "BOND_SL", "ED2000");
                            if (ed2000s.size() > 0) {
                                log.info("{}存在企业保证金，不用冻结，直接可投标!", taker.getUser().getTelephone());
                            } else {
                                hbzPledgeService.unPledge("Tender#".concat(String.valueOf(tender.getId())), BizCode.Tender);
                                switch (order.getOrderType()) {
                                    case FSL: {
                                        List<HbzBondDTO> d500s = hbzBondService.findByAvailableUserBondGrade(taker.getUser(), "BOND_FSL", "D500");
                                        if (d500s.size() > 0) {
                                            HbzBondDTO bond = d500s.get(0);
                                            hbzPledgeService.pledge(bond, order.getOrderNo(), BizCode.ORDER);
                                        } else {
                                            return new ResponseDTO(Const.STATUS_ERROR, "该用户保证金失效.");
                                        }
                                    }
                                    break;
                                    case LTL: {
                                        List<HbzBondDTO> d200s = hbzBondService.findByAvailableUserBondGrade(taker.getUser(), "BOND_LTL", "D200");
                                        if (d200s.size() > 0) {
                                            HbzBondDTO bond = d200s.get(0);
                                            hbzPledgeService.pledge(bond, order.getOrderNo(), BizCode.ORDER);
                                        } else {
                                            return new ResponseDTO(Const.STATUS_ERROR, "该用户保证金失效.");
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                        break;
                        case 0: {

                        }
                        break;
                        default: {
                            return new ResponseDTO(Const.STATUS_ERROR, "不支持的保证金类型.");
                        }
                    }
                    order.setOrderTrans(OrderTrans.WAIT_TO_TAKE);
                    order.setTakeUserId(taker.getUserId());
                    order.setAmount(taker.getOffer());
                    taker.setTakeType(TakeType.TAKE);
                    hbzTakerInfoService.save(taker);
                    HbzTakerInfoDTO queryOtherTaker = new HbzTakerInfoDTO();
                    queryOtherTaker.setOrderId(order.getId());
                    queryOtherTaker.setStatus("1");
                    queryOtherTaker.setTakeType(TakeType.TAKEING);
                    hbzTakerInfoService.query(queryOtherTaker).stream().map(tk -> {
                        tk.setTakeType(TakeType.DISABLE);
                        return tk;
                    }).forEach(hbzTakerInfoService::save);
                    if (hbzOrderService.save(order) != null) {
                        hbzOrderRecordService.saveOrderRec(new HbzOrderRecordDTO(order, order.getId(), hbzUserService.currentUser(), hbzUserService.currentUser().getId(), order.getOrderTrans(), System.currentTimeMillis()));
                        sitePushMessageService.sendMessageImmediately(Arrays.asList(taker.getUser()),
                                "订单通知消息",
                                "有新的订单",
                                "恭喜您，车辆征集中您被货主选定为运货司机，快到收货订单中查看订单详情吧。");
                        return new ResponseDTO(Const.STATUS_OK, "指定接标人成功!", null);
                    }
                }
            }
        }
        return new ResponseDTO(Const.STATUS_ERROR, "错误", null);
    }

}
