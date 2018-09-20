package com.troy.keeper.hbz.app.rest.web;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.dto.HbzFslMapDTO;
import com.troy.keeper.hbz.app.dto.HbzOrderMapDTO;
import com.troy.keeper.hbz.app.dto.HbzTakerInfoMapDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.*;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.type.OrderTrans;
import com.troy.keeper.hbz.type.OrderType;
import com.troy.keeper.hbz.type.Role;
import com.troy.keeper.hbz.type.SettlementType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Author：YangJx
 * @Description：车辆征集
 * @DateTime：2017/12/13 14:32
 */
@Slf4j
@RestController
@RequestMapping("/api/web/bid")
public class WebBidResource {

    @Autowired
    private HbzOrderService hbzOrderService;

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

    /**
     * 车主 分页条件查询可参与征集的订单
     *
     * @return
     */
    @PostMapping("/queryAvailableFslBidByPage")
    public ResponseDTO queryAvailableFslBidByPage(HbzFslMapDTO hbzFslMapDTO) {
        HbzUserDTO currentUserdDTO = hbzUserService.currentUser();  //获取当前用户

        //获取当前用户的车主身份
        Map<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put("role in", Arrays.asList(Role.EnterpriseAdmin, Role.PersonDriver));
        paramMap.put("status =", Const.STATUS_ENABLED);
        paramMap.put("users.id =", currentUserdDTO.getId());
        List<HbzRoleDTO> roleDTOList = hbzRoleService.query(paramMap);

        //不具有车主身份
        if (roleDTOList == null || roleDTOList.size() <= 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "您不具有车主身份，请认证！", null);
        } else if (roleDTOList.size() == 2) {  //兼具两种车主身份

        } else if (roleDTOList.size() == 1) {
            Role singleRole = roleDTOList.get(0).getRole();
            switch (singleRole) {
                case PersonDriver: {
                    //Double personalBond = currentUserdDTO.getBond() == null ? Double.valueOf(0D) : currentUserdDTO.getBond();
                    Integer persionalStarLevel = currentUserdDTO.getStarLevel() == null ? Integer.valueOf(0) : currentUserdDTO.getStarLevel();
                    Integer persionalNeed = Integer.valueOf(1);

                }
                case EnterpriseAdmin: {
                    HbzTransEnterpriseRegistryDTO transEnterpriseRegistry = hbzTransEnterpriseRegistryService.findTransEnterpriseRegistry(currentUserdDTO, true);
                    if (transEnterpriseRegistry == null) {
                        return new ResponseDTO(Const.STATUS_ERROR, "您不具有企业车主身份，请认证！", null);
                    }

                    Double enterpriseDriverRegistryMoney = transEnterpriseRegistry.getRegistryMoney() == null ? Double.valueOf(0D) : transEnterpriseRegistry.getRegistryMoney();
                    //Double personalBond = currentUserdDTO.getBond() == null ? Double.valueOf(0D) : currentUserdDTO.getBond();
                    Integer persionalStarLevel = currentUserdDTO.getStarLevel() == null ? Integer.valueOf(0) : currentUserdDTO.getStarLevel();
                    Integer persionalNeed = Integer.valueOf(2);
                }
            }
        }

        return null;
    }

    /**
     * 车主 参与征集
     *
     * @param takerInfoMapDTO
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/joinBid")
    public ResponseDTO joinBid(@RequestBody HbzTakerInfoMapDTO takerInfoMapDTO, HttpServletRequest request, HttpServletResponse response) {
        HbzUserDTO currentUser = hbzUserService.currentUser();
        if (takerInfoMapDTO.getOrderId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "订单id不能为空!", null);
        }
        HbzOrderDTO orderDTOWithId = new HbzOrderDTO();
        orderDTOWithId.setId(takerInfoMapDTO.getOrderId());
        HbzOrderDTO order = hbzOrderService.get(orderDTOWithId);

        if (order == null || order.getStatus().equals(Const.STATUS_DISABLED)) {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单!", null);
        } else if (order.getOrderTrans() != OrderTrans.ORDER_TO_BE_RECEIVED || order.getSettlementType() != SettlementType.LEVY_ONLINE_PAYMENT) {
            return new ResponseDTO(Const.STATUS_ERROR, "无效订单!", null);
        } else {
            HbzTakerInfoDTO query = new HbzTakerInfoDTO();
            query.setStatus(Const.STATUS_ENABLED);
            query.setOrderId(order.getId());
            if (hbzTakerInfoService.count(query) >= 10L) {
                return new ResponseDTO(Const.STATUS_ERROR, "该标已满!");
            }
            query.setUserId(hbzUserService.currentUser().getId());
            Long tenderCount = hbzTakerInfoService.count(query);
            if (tenderCount > 0) {
                return new ResponseDTO(Const.STATUS_ERROR, "已经投标，不能再次投标!", null);
            } else {
                HbzTenderDTO queryTender = new HbzTenderDTO();
                queryTender.setStatus(Const.STATUS_ENABLED);
                queryTender.setOrderId(orderDTOWithId.getId());
                List<HbzTenderDTO> tenders = hbzTenderService.query(queryTender);
                if (tenders == null || tenders.size() == 0) {
                    return new ResponseDTO(Const.STATUS_ERROR, "该标未发布投标条件!", null);
                } else if (tenders.size() > 1) {
                    return new ResponseDTO(Const.STATUS_ERROR, "存在双重标准，无法投!", null);
                } else {
                    HbzTenderDTO tender = tenders.get(0);
                    //根据招标要求的车主类型走各自的不同逻辑
                    switch (tender.getNeed().intValue()) {
                        case 1: {  //个人车主
                            return joinBidAsPersionalDriver(tender, currentUser);
                        }
                        case 2: {  //企业车主
                            return joinBidAsEnterpriseDriver(tender, currentUser);
                        }
                        case 3: {  //不限 车主身份
                            Map<String, Object> paramMap = new LinkedHashMap<>();
                            paramMap.put("role in", Arrays.asList(Role.EnterpriseAdmin, Role.PersonDriver));
                            paramMap.put("status =", Const.STATUS_ENABLED);
                            paramMap.put("users.id =", hbzUserService.currentUser().getId());
                            List<HbzRoleDTO> roleDTOList = hbzRoleService.query(paramMap);
                            if (roleDTOList.size() <= 0) {
                                return new ResponseDTO(Const.STATUS_ERROR, "你尚未通过资质认证，无法投标!", null);
                            } else {
                                HbzRoleDTO roleDTO = roleDTOList.get(0);
                                switch (roleDTO.getRole()) {
                                    case EnterpriseAdmin: {
                                        return joinBidAsEnterpriseDriver(tender, currentUser);
                                    }
                                    case PersonDriver: {
                                        return joinBidAsPersionalDriver(tender, currentUser);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return new ResponseDTO(Const.STATUS_ERROR, "操作失败!", null);
    }

    /**
     * 个人车主参与征集
     *
     * @param tenderDTO
     * @param currentUser
     * @return
     */
    private ResponseDTO joinBidAsPersionalDriver(HbzTenderDTO tenderDTO, HbzUserDTO currentUser) {
        Map<String, Object> queryRole = new LinkedHashMap<>();
        queryRole.put("role =", Role.PersonDriver);
        queryRole.put("status =", Const.STATUS_ENABLED);
        queryRole.put("users.id =", hbzUserService.currentUser().getId());
        Long roleCount = hbzRoleService.count(queryRole);
        if (roleCount == 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "你不是司机，无法投标!", null);
        } else {
            if (tenderDTO.getBond() != null) {  //保证金要求
                /**
                if (currentUser.getBond() == null) {
                    return new ResponseDTO(Const.STATUS_ERROR, "你尚未缴纳保证金，不能投标!", null);
                }
                if (tenderDTO.getBond() > currentUser.getBond()) {
                    return new ResponseDTO(Const.STATUS_ERROR, "保证金不足，无法参与此次投标，该标需要：" + tenderDTO.getBond(), null);
                }
                */
            }
            if (tenderDTO.getStarLevel() != null) {  //星级要求
                if (currentUser.getStarLevel() == null) {
                    return new ResponseDTO(Const.STATUS_ERROR, "你尚未获评星级!", null);
                }
                if (tenderDTO.getStarLevel() > currentUser.getStarLevel()) {
                    return new ResponseDTO(Const.STATUS_ERROR, "星级不够，该标需要星级：" + tenderDTO.getStarLevel(), null);
                }
            }
            HbzTakerInfoDTO takerInfoDTO = new HbzTakerInfoDTO();
            takerInfoDTO.setStatus(Const.STATUS_ENABLED);
            takerInfoDTO.setUserId(currentUser.getId());
            takerInfoDTO.setUser(currentUser);
            takerInfoDTO.setOrder(tenderDTO.getOrder());
            takerInfoDTO.setOrderId(tenderDTO.getOrder().getId());

            HbzTakerInfoDTO takerInfoDTOFromDB = hbzTakerInfoService.save(takerInfoDTO);
            if (takerInfoDTOFromDB != null) {
                return new ResponseDTO(Const.STATUS_OK, "投标成功!", null);
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, "投标失败!", null);
            }
        }
    }

    /**
     * 企业车主参与征集
     *
     * @param tenderDTO
     * @param currentUser
     * @return
     */
    private ResponseDTO joinBidAsEnterpriseDriver(HbzTenderDTO tenderDTO, HbzUserDTO currentUser) {
        Map<String, Object> queryRole = new LinkedHashMap<>();
        queryRole.put("role =", Role.EnterpriseAdmin);
        queryRole.put("status =", Const.STATUS_ENABLED);
        queryRole.put("users.id =", hbzUserService.currentUser().getId());
        Long roleCount = hbzRoleService.count(queryRole);
        if (roleCount == 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "你不是货运公司，无法投标!", null);
        } else {
            if (tenderDTO.getBond() != null) {  //保证金要求
                /**
                if (currentUser.getBond() == null) {
                    return new ResponseDTO(Const.STATUS_ERROR, "你未缴纳保证金，不能投标!", null);
                }
                if (tenderDTO.getBond() > currentUser.getBond()) {
                    return new ResponseDTO(Const.STATUS_ERROR, "保证金不足，无法投标，该标需要：" + tenderDTO.getBond(), null);
                }
                 */
            }
            if (tenderDTO.getRegistryMoney() != null) {  //注册资金要求
                HbzTransEnterpriseRegistryDTO transEnterpriseRegistry = hbzTransEnterpriseRegistryService.findTransEnterpriseRegistry(currentUser, true);
                if (transEnterpriseRegistry == null) {
                    return new ResponseDTO(Const.STATUS_ERROR, "未找到运输企业资质注册信息!", null);
                }
                if (transEnterpriseRegistry.getRegistryMoney() == null) {
                    return new ResponseDTO(Const.STATUS_ERROR, "你尚未缴纳注册资金!", null);
                }
                if (tenderDTO.getRegistryMoney() > transEnterpriseRegistry.getRegistryMoney()) {
                    return new ResponseDTO(Const.STATUS_ERROR, "你注册资金不够，该标要求注册资金：" + tenderDTO.getRegistryMoney(), null);
                }
            }
            if (tenderDTO.getStarLevel() != null) {  //星级要求
                if (currentUser.getStarLevel() == null) {
                    return new ResponseDTO(Const.STATUS_ERROR, "尚未获评星级!", null);
                }
                if (tenderDTO.getStarLevel() > currentUser.getStarLevel()) {
                    return new ResponseDTO(Const.STATUS_ERROR, "星级不够，该标需要星级：" + tenderDTO.getStarLevel(), null);
                }
            }

            HbzTakerInfoDTO takerInfoDTO = new HbzTakerInfoDTO();
            takerInfoDTO.setStatus(Const.STATUS_ENABLED);
            takerInfoDTO.setUserId(currentUser.getId());
            takerInfoDTO.setUser(currentUser);
            takerInfoDTO.setOrder(tenderDTO.getOrder());
            takerInfoDTO.setOrderId(tenderDTO.getOrder().getId());

            HbzTakerInfoDTO hbzTakerInfoDtoFromDb = hbzTakerInfoService.save(takerInfoDTO);
            if (hbzTakerInfoDtoFromDb != null) {
                return new ResponseDTO(Const.STATUS_OK, "投标成功!", null);
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, "投标失败!", null);
            }
        }
    }
}
