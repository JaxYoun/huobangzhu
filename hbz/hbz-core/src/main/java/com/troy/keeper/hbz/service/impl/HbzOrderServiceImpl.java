package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.core.utils.MapperUtils;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.*;
import com.troy.keeper.hbz.helper.DateUtils;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzOrder;
import com.troy.keeper.hbz.repository.CostStaticsRepository;
import com.troy.keeper.hbz.repository.HbzOrderRepository;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzOrderMapper;
import com.troy.keeper.hbz.service.wrapper.AddInfo;
import com.troy.keeper.hbz.sys.annotation.Config;
import com.troy.keeper.hbz.type.OrderTrans;
import com.troy.keeper.hbz.type.OrderType;
import com.troy.keeper.hbz.type.RateType;
import com.troy.keeper.hbz.type.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/10/24.
 */
@Service
@Transactional
public class HbzOrderServiceImpl extends BaseEntityServiceImpl<HbzOrder, HbzOrderDTO> implements HbzOrderService {

    @Autowired
    private HbzOrderRepository hbzOrderRepository;

    @Autowired
    private CostStaticsRepository costStaticsRepository;

    @Autowired
    private HbzOrderMapper hbzOrderMapper;

    @Autowired
    private HbzUserService hbzUserService;

    @Autowired
    HbzRateService rate;

    @Autowired
    SitePushMessageService sitePushMessageService;

    @Autowired
    HbzCoordinateService hbzCoordinateService;

    @Autowired
    MapService mapService;

    @Autowired
    HbzAreaService hbzAreaService;

    @Autowired
    HbzRoleService hbzRoleService;

    @Autowired
    HbzBuyOrderServices hbzBuyOrderService;

    @Autowired
    HbzSendOrderService hbzSendOrderService;

    @Autowired
    HbzExOrderService hbzExOrderService;

    @Override
    public BaseMapper<HbzOrder, HbzOrderDTO> getMapper() {
        return hbzOrderMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzOrderRepository;
    }

    @Override
    public HbzOrderDTO findByOrderNo(String orderNo) {
        return getMapper().map(hbzOrderRepository.findFirstByOrderNo(orderNo));
    }

    @Override
    public synchronized String createNewOrderNo(OrderType orderType) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentTimestamp = format.format(new Date());
        String bizNo;
        int init = 0;
        while (true) {
            bizNo = orderType.toString() + currentTimestamp + "-" + StringHelper.frontCompWithZore(++init, 6);
            HbzOrder ls = hbzOrderRepository.findFirstByOrderNo(bizNo);
            if (ls == null) {
                return bizNo;
            }
        }
    }

    @Override
    public Map<String, Object> completeOrder(String orderNo) {
        HbzOrderDTO queryOrder = new HbzOrderDTO();
        queryOrder.setOrderNo(orderNo);
        queryOrder.setStatus(Const.STATUS_ENABLED);
        List<HbzOrderDTO> orderList = query(queryOrder);
        HbzOrderDTO hbzOrder = orderList.get(0);
        switch (hbzOrder.getOrderType()) {
            case FSL:
            case LTL: {
                hbzOrder.setOrderTrans(OrderTrans.PAID);    //线上已付款
                HbzRateDTO consignorRate = new HbzRateDTO();
                consignorRate.setUserId(hbzOrder.getCreateUserId());
                consignorRate.setUser(hbzOrder.getCreateUser());
                consignorRate.setOrder(hbzOrder);
                consignorRate.setOrderId(hbzOrder.getId());
                consignorRate.setType(RateType.CONSIGNOR);
                consignorRate.setStar(0);
                consignorRate.setStatus("1");
                rate.save(consignorRate);
                HbzRateDTO tranRate = new HbzRateDTO();
                tranRate.setUser(hbzOrder.getTakeUser());
                tranRate.setStatus("1");
                tranRate.setUserId(hbzOrder.getTakeUserId());
                tranRate.setOrder(hbzOrder);
                tranRate.setOrderId(hbzOrder.getId());
                tranRate.setType(RateType.PROVIDER);
                tranRate.setStar(0);
                rate.save(tranRate);
                break;
            }
            case BUY:
            case SEND:
            case EX: {
                hbzOrder.setOrderTrans(OrderTrans.ORDER_TO_BE_RECEIVED);    //待接订单
                HbzCoordinateDTO coordinateQuery = new HbzCoordinateDTO();
                AddInfo addInfo = mapService.getLocationX(hbzOrder.getOriginX(), hbzOrder.getOriginY());
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
                            roleQuery.setRoles(Arrays.asList(Role.EnterpriseAdmin, Role.PersonDriver, Role.DeliveryBoy));
                            return hbzRoleService.count(roleQuery) > 0;
                        }).collect(Collectors.toList());
                        switch (hbzOrder.getOrderType()) {
                            case BUY:
                                HbzBuyOrderDTO buy = hbzBuyOrderService.findById(hbzOrder.getId());
                                sitePushMessageService.sendMessageImmediately(users,
                                        "货源通知消息",
                                        "有新的货源",
                                        "您有新的货源消息啦！\n" +
                                                buy.getDestAddress() + "（" + buy.getCommodityName() + "），现在联系货主\n"
                                );
                                break;
                            case SEND:
                                HbzSendOrderDTO send = hbzSendOrderService.findById(hbzOrder.getId());
                                sitePushMessageService.sendMessageImmediately(users,
                                        "货源通知消息",
                                        "有新的货源",
                                        "您有新的货源消息啦！\n" +
                                                send.getOriginAddress() + "-->" + send.getDestAddress() + "（" + send.getCommodityName() + "），现在联系货主"
                                );
                                break;
                            case EX:
                                HbzExOrderDTO ex = hbzExOrderService.findById(hbzOrder.getId());
                                // sitePushMessageService.sendMessageImmediately(users, "有新的订单", "[" + hbzOrder.getCreateUser().getNickName() + "]发布了一条新的订单！", "[" + hbzOrder.getCreateUser().getNickName() + "]发布了一条新的订单！");
                                break;
                        }
                    }
                }
                break;
            }
        }
        save(hbzOrder);
        return null;
    }

    /**
     * 构造查询条件
     *
     * @param hbzOrderDTO
     * @return
     */
    public Specification<HbzOrder> getHbzOrderSpecification(HbzOrderDTO hbzOrderDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            if (hbzOrderDTO.getOrderType() != null && StringUtils.isNotBlank(hbzOrderDTO.getOrderType().getName())) {
                predicateList.add(criteriaBuilder.equal(root.get("orderType").get("name"), hbzOrderDTO.getOrderType().getName()));
            }
            if (StringUtils.isNotBlank(hbzOrderDTO.getOrderNo())) {
                predicateList.add(criteriaBuilder.equal(root.get("orderNo"), hbzOrderDTO.getOrderNo()));
            }

            /*Predicate[] predicates = new Predicate[predicateList.size()];
            return criteriaBuilder.and(predicateList.toArray(predicates));*/

            //构造排序方式及排序字段
            List<Order> sortOrderList = new ArrayList<>();
            sortOrderList.add(criteriaBuilder.asc(root.get("lastUpdatedDate")));
            query.orderBy(sortOrderList);

            Predicate[] predicatesArr = new Predicate[predicateList.size()];
            return query.where(predicateList.toArray(predicatesArr)).getRestriction();
        };
    }

    @Override
    public Page<HbzOrderDTO> getSpecialLineGoodsListByPage(HbzOrderDTO hbzOrderDTO, Pageable pageable) throws Exception {
        Specification<HbzOrder> specification = getHbzOrderSpecification(hbzOrderDTO);

        Page<HbzOrderDTO> hbzOrderPage = hbzOrderRepository.findAll(specification, pageable).map(hbzOrder -> {
            HbzOrderDTO hbzOrderDTO1 = new HbzOrderDTO();
            BeanUtils.copyProperties(hbzOrder, hbzOrderDTO1);
            return hbzOrderDTO1;
        });

        return hbzOrderPage;
    }

    @Override
    public HbzOrderDTO getSpecialLineGoodsListDetailById(HbzOrderDTO hbzOrderDTO) {
        HbzOrderDTO hbzOrderDTO1 = new HbzOrderDTO();
        HbzOrder hbzOrder = hbzOrderRepository.findOne(hbzOrderDTO.getId());
        BeanUtils.copyProperties(hbzOrder, hbzOrderDTO1);
        return hbzOrderDTO1;
    }

    /**
     * 更新订单流转状态
     *
     * @param hbzOrderDTO
     * @return
     */
    @Override
    public boolean updateOrderTrans(HbzOrderDTO hbzOrderDTO) {
        return this.hbzOrderRepository.updateOrderTransById(hbzOrderDTO.getOrderTrans(), hbzOrderDTO.getId()) != null;
    }

    /**
     * 货主费用支出统计
     */
    @Override
    public Map<String, Double> cost(CostStaticsDTO costStaticsDTO) {
        Map<String, Double> result = new HashMap<>();
        List<Object[]> lists = costStaticsRepository.queryOrderCostStatistics(hbzUserService.currentUser().getId(), costStaticsDTO);
        for (Object[] objects : lists) {
            result.put(objects[0].toString(), (Double) objects[1]);
        }
        return result;
    }

    /**
     * 车主费用收入统计
     */
    @Override
    public Map<String, Double> income(CostStaticsDTO costStaticsDTO) {
        Map<String, Double> result = new HashMap<>();
        List<Object[]> lists = costStaticsRepository.queryOrderIncomeStatistics(hbzUserService.currentUser().getId(), costStaticsDTO);
        for (Object[] objects : lists) {
            result.put(objects[0].toString(), (Double) objects[1]);
        }
        return result;
    }
}
