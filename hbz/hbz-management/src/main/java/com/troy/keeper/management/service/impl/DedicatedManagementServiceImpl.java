package com.troy.keeper.management.service.impl;

import com.sun.javafx.collections.MappingChange;
import com.troy.keeper.hbz.dto.*;
import com.troy.keeper.hbz.dto.HbzTakerInfoDTO;
import com.troy.keeper.hbz.po.*;
import com.troy.keeper.hbz.repository.HbzOrderRepository;
import com.troy.keeper.hbz.repository.HbzUserRegistryRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.service.HbzTypeValService;
import com.troy.keeper.hbz.service.mapper.*;
import com.troy.keeper.hbz.sys.FormatedDate;
import com.troy.keeper.hbz.type.PayProgress;
import com.troy.keeper.management.dto.*;
import com.troy.keeper.management.repository.DedicatedManagementRepository;
import com.troy.keeper.management.repository.HbzAreasRepository;
import com.troy.keeper.management.repository.HbzOrderRecRepository;
import com.troy.keeper.management.repository.UserInformationRepository;
import com.troy.keeper.management.service.DedicatedManagementService;
import com.troy.keeper.management.utils.HbzPayChildMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 李奥
 * @date 2017/11/28.
 */
@Service
@Transactional
public class DedicatedManagementServiceImpl implements DedicatedManagementService {

    @Autowired
    private DedicatedManagementRepository dedicatedManagementRepository;

    @Autowired
    private HbzAreaMapper hbzAreaMapper;


    @Autowired
    private HbzAreasRepository hbzAreaRepository;

    @Autowired
    private HbzPayMapper hbzPayMapper;

    @Autowired
    private HbzPersonDriverRegistryMapper hbzPersonDriverRegistryMapper;
    @Autowired
    private HbzFslOrderMapper hbzFslOrderMapper;
    @Autowired
    private HbzTenderMapper hbzTenderMapper;
    @Autowired
    private HbzTakerInfoMapper hbzTakerInfoMapper;

    @Autowired
    private HbzPayChildMapper hbzPayChildMapper;

    //物流详情
    @Autowired
    private HbzOrderRecRepository hbzOrderRecRepository;

    @Autowired
    private HbzOrderRepository hbzOrderRepository;

    @Autowired
    private HbzUserRegistryRepository hbzUserRegistryRepository;


    //分页查询
    @Override
    public Page<DedicatedLineManagementDTO> findByCondition(DedicatedLineManagementDTO dedicatedLineManagementDTO, Pageable pageable) {
        if (pageable.getSort() != null) {
            Iterator<Sort.Order> ito = pageable.getSort().iterator();
            if (!ito.hasNext()) {
                pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(new Sort.Order(Sort.Direction.DESC, "createdDate")));
            }
        } else {
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(new Sort.Order(Sort.Direction.DESC, "createdDate")));
        }
        Page<HbzFslOrder> page = dedicatedManagementRepository.findAll(new Specification<HbzFslOrder>() {
            @Override
            public Predicate toPredicate(Root<HbzFslOrder> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (StringUtils.isNotBlank(dedicatedLineManagementDTO.getOrderNo())) {
                    //运单号---》订单编号
                    predicateList.add(criteriaBuilder.like(root.get("orderNo"), "%" + dedicatedLineManagementDTO.getOrderNo() + "%"));
                }
                if (StringUtils.isNotBlank(dedicatedLineManagementDTO.getOrg())) {
                    //订单归属机构
//                   predicateList.add(criteriaBuilder.like(root.get("createUser").get("org").get("OrgType"), "%" + dedicatedLineManagementDTO.getOrg() + "%"));
                    predicateList.add(criteriaBuilder.like(root.join("createUser").join("ent").get("organizationName"), "%" + dedicatedLineManagementDTO.getOrg() + "%"));
                }
                if (StringUtils.isNotBlank(dedicatedLineManagementDTO.getCreateUser())) {
                    //订单创建人
                    predicateList.add(criteriaBuilder.like(root.join("createUser").get("nickName"), "%" + dedicatedLineManagementDTO.getCreateUser() + "%"));
                }
                if (StringUtils.isNotBlank(dedicatedLineManagementDTO.getCreateUsertelephone())) {
                    //订单创建人电话
                    predicateList.add(criteriaBuilder.equal(root.get("createUser").get("telephone"), dedicatedLineManagementDTO.getCreateUsertelephone()));
                }
                if (StringUtils.isNotBlank(dedicatedLineManagementDTO.getTakeUser())) {
                    //接单人
                    predicateList.add(criteriaBuilder.like(root.join("takeUser").get("nickName"), "%" + dedicatedLineManagementDTO.getTakeUser() + "%"));
                }
                if (StringUtils.isNotBlank(dedicatedLineManagementDTO.getTakeUserTelephone())) {
                    //接单人电话
                    predicateList.add(criteriaBuilder.equal(root.join("takeUser").get("telephone"), dedicatedLineManagementDTO.getTakeUserTelephone()));
                }
                if (dedicatedLineManagementDTO.getOrderTrans() != null) {
                    //订单状态
                    predicateList.add(criteriaBuilder.equal(root.get("orderTrans"), dedicatedLineManagementDTO.getOrderTrans()));
                }


                //发出
                //发出
                if (StringUtils.isNotBlank(dedicatedLineManagementDTO.getOriginAreaCode())) {
                    predicateList.add(criteriaBuilder.equal(root.join("originArea").get("outCode"), dedicatedLineManagementDTO.getOriginAreaCode()));
                }
                //到站
                //到站
                if (StringUtils.isNotBlank(dedicatedLineManagementDTO.getDestAreaCode())) {
                    predicateList.add(criteriaBuilder.equal(root.join("destArea").get("outCode"), dedicatedLineManagementDTO.getDestAreaCode()));
                }
                if (dedicatedLineManagementDTO.getSettlementType() != null) {

                    //结算方式
                    predicateList.add(criteriaBuilder.equal(root.get("settlementType"), dedicatedLineManagementDTO.getSettlementType()));
                }
                //时间范围的查询
                if (dedicatedLineManagementDTO.getSmallTime() != null) {

                    predicateList.add(criteriaBuilder.ge(root.get("createdDate"), dedicatedLineManagementDTO.getSmallTime()));
                }
                if (dedicatedLineManagementDTO.getBigTime() != null) {
                    //处理查询时间的结束值
                    predicateList.add(criteriaBuilder.le(root.get("createdDate"), dedicatedLineManagementDTO.getBigTime()+59999l));

                }

                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));


            }
        }, pageable);
        return converterDto(page);
    }


    private Page<DedicatedLineManagementDTO> converterDto(Page<HbzFslOrder> page) {
        return page.map(new Converter<HbzFslOrder, DedicatedLineManagementDTO>() {
            @Override
            public DedicatedLineManagementDTO convert(HbzFslOrder hbzFslOrder) {
                DedicatedLineManagementDTO dedicatedLineManagementDTO = new DedicatedLineManagementDTO();
                if (hbzFslOrder != null) {

                    dedicatedLineManagementDTO.setId(hbzFslOrder.getId());
                    dedicatedLineManagementDTO.setCommodityName(hbzFslOrder.getCommodityName());
                    if (hbzFslOrder.getOrderTakeStart() != null) {
                        FormatedDate logIn = new FormatedDate(hbzFslOrder.getOrderTakeStart());
                        String orderTakeStart = logIn.getFormat("yyyy-MM-dd HH:mm");
                        dedicatedLineManagementDTO.setOrderTakeStart(orderTakeStart);
                    }

                    dedicatedLineManagementDTO.setCommodityDescribe(hbzFslOrder.getCommodityDescribe());
                    dedicatedLineManagementDTO.setCommodityWeight(hbzFslOrder.getCommodityWeight());
                    dedicatedLineManagementDTO.setCommodityVolume(hbzFslOrder.getCommodityVolume());
                    dedicatedLineManagementDTO.setTransType(hbzFslOrder.getTransType());
                    dedicatedLineManagementDTO.setTransLen(hbzFslOrder.getTransLen());
                    dedicatedLineManagementDTO.setRelatedPictures(hbzFslOrder.getRelatedPictures());
                    if (hbzFslOrder.getTransType() != null) {
                        dedicatedLineManagementDTO.setTransTypeValue(hbzFslOrder.getTransType().getName());
                    }

                    dedicatedLineManagementDTO.setMaxLoad(hbzFslOrder.getMaxLoad());
                    //指定运输到货时间
                    if (hbzFslOrder.getDestlimit() != null) {

                        FormatedDate logIn2 = new FormatedDate(hbzFslOrder.getDestlimit());
                        String destlimit = logIn2.getFormat("yyyy-MM-dd HH:mm");
                        dedicatedLineManagementDTO.setDestlimit(destlimit);
                    }
                    //取货地址
                    HbzArea currentLevelOriginArea = hbzFslOrder.getOriginArea();
                    LinkedList<Long> longList = new LinkedList<>();
                    StringBuilder sb = new StringBuilder();
                    if (currentLevelOriginArea != null) {
                        while (currentLevelOriginArea.getLevel() > 0) {
                            sb.insert(0, currentLevelOriginArea.getAreaName() + " ");
                            switch (currentLevelOriginArea.getLevel().intValue()) {
                                case 1: {
                                    longList.addFirst(currentLevelOriginArea.getId());
                                    dedicatedLineManagementDTO.setStartCity(longList);
                                }
                                break;
                                case 2: {
                                    longList.addFirst(currentLevelOriginArea.getId());
                                    dedicatedLineManagementDTO.setStartCity(longList);
                                }
                                break;
                                case 3: {
                                    longList.addFirst(currentLevelOriginArea.getId());
                                    dedicatedLineManagementDTO.setStartCity(longList);
                                }
                                break;
                            }

                            currentLevelOriginArea = currentLevelOriginArea.getParent();
                        }
                        dedicatedLineManagementDTO.setOriginArea(sb.toString());
                    }
                    //具体地址
                    dedicatedLineManagementDTO.setOriginAddress(hbzFslOrder.getOriginAddress());
                    dedicatedLineManagementDTO.setLinkMan(hbzFslOrder.getLinkMan());
                    dedicatedLineManagementDTO.setLinkTelephone(hbzFslOrder.getLinkTelephone());
                    //送货地址
                    //到站地址
                    HbzArea currentLevelArea = hbzFslOrder.getDestArea();
                    LinkedList<Long> endList = new LinkedList<>();
                    StringBuilder ss = new StringBuilder();
                    if (currentLevelArea != null) {


                        while (currentLevelArea.getLevel() > 0) {
                            ss.insert(0, currentLevelArea.getAreaName() + " ");
                            switch (currentLevelArea.getLevel().intValue()) {
                                case 1: {
                                    endList.addFirst(currentLevelArea.getId());
                                    dedicatedLineManagementDTO.setEndCity(endList);
                                }
                                break;
                                case 2: {
                                    endList.addFirst(currentLevelArea.getId());
                                    dedicatedLineManagementDTO.setEndCity(endList);
                                }
                                break;
                                case 3: {
                                    endList.addFirst(currentLevelArea.getId());
                                    dedicatedLineManagementDTO.setEndCity(endList);
                                }
                                break;
                            }

                            currentLevelArea = currentLevelArea.getParent();
                        }
                        dedicatedLineManagementDTO.setDestArea(ss.toString());
                    }
                    //具体地址
                    dedicatedLineManagementDTO.setDestAddress(hbzFslOrder.getDestAddress());
                    dedicatedLineManagementDTO.setDestLinker(hbzFslOrder.getDestLinker());
                    dedicatedLineManagementDTO.setDestTelephone(hbzFslOrder.getDestTelephone());
                    dedicatedLineManagementDTO.setLinkRemark(hbzFslOrder.getLinkRemark());
                    dedicatedLineManagementDTO.setSettlementType(hbzFslOrder.getSettlementType());
                    if (hbzFslOrder.getSettlementType() != null) {
                        dedicatedLineManagementDTO.setSettlementTypeValue(hbzFslOrder.getSettlementType().getName());
                    }

                    dedicatedLineManagementDTO.setAmount(hbzFslOrder.getAmount());
                    //单价
                    dedicatedLineManagementDTO.setUnitPrice(hbzFslOrder.getUnitPrice());
                    //创建人
                    if (hbzFslOrder.getCreateUser() != null) {
                        dedicatedLineManagementDTO.setCreateUser(hbzFslOrder.getCreateUser().getNickName());
                    }
                    //联系电话
                    if (hbzFslOrder.getCreateUser() != null) {
                        dedicatedLineManagementDTO.setCreateUsertelephone(hbzFslOrder.getCreateUser().getTelephone());
                    }
                    //所属公司
                    if (hbzFslOrder.getCreateUser() != null && hbzFslOrder.getCreateUser().getEnt() != null) {
                        dedicatedLineManagementDTO.setOrg(hbzFslOrder.getCreateUser().getEnt().getOrganizationName());
                    } else {
                        dedicatedLineManagementDTO.setOrg("- -");
                    }

                    dedicatedLineManagementDTO.setOrderNo(hbzFslOrder.getOrderNo());
                    //订单创建时间
                    FormatedDate logInCreatedDate = new FormatedDate(hbzFslOrder.getCreatedDate());
                    String createdDate = logInCreatedDate.getFormat("yyyy-MM-dd HH:mm");
                    dedicatedLineManagementDTO.setCreateUserTime(createdDate);

                    dedicatedLineManagementDTO.setOrderTrans(hbzFslOrder.getOrderTrans());
                    if (hbzFslOrder.getOrderTrans() != null) {
                        dedicatedLineManagementDTO.setOrderTransValue(hbzFslOrder.getOrderTrans().getName());
                    }
                    dedicatedLineManagementDTO.setOrderType(hbzFslOrder.getOrderType());
                    if (hbzFslOrder.getOrderType() != null) {
                        dedicatedLineManagementDTO.setOrderTypeValue(hbzFslOrder.getOrderType().getName());
                    }

                    //接单人
                    if (hbzFslOrder.getTakeUser() != null) {

                        dedicatedLineManagementDTO.setTakeUser(hbzFslOrder.getTakeUser().getNickName());
                    }
                    if (hbzFslOrder.getTakeUser() != null) {

                        dedicatedLineManagementDTO.setTakeUserTelephone(hbzFslOrder.getTakeUser().getTelephone());
                    }
                    // 接单人的id
                    if (hbzFslOrder.getTakeUser() != null) {

                        dedicatedLineManagementDTO.setTakeUserId(hbzFslOrder.getTakeUser().getId());
                    }
                }
                dedicatedLineManagementDTO.setOfflineProcess(hbzFslOrder.getOfflineProcess());
                return dedicatedLineManagementDTO;
            }
        });
    }


    //城市联动查询
    @Override
    public List<HbzAreaDTO> findCity(HbzAreaDTO hbzAreaDTO) {
        Long parentId = hbzAreaDTO.getParentId();
        List<HbzArea> hbzAreaList = dedicatedManagementRepository.findById(parentId);
        List<HbzAreaDTO> hbzAreaDTOList = hbzAreaList.stream().map(hbzAreaMapper::map).collect(Collectors.toList());
        return hbzAreaDTOList;
    }

    //查询接单人详细信息
    @Override
    public TeakUserInformationDTO findTeakUserInformation(TeakUserInformationDTO teakUserInformationDTO) {
        TeakUserInformationDTO tu = new TeakUserInformationDTO();
        //接单人信息
        HbzFslOrder hbzFslOrder = dedicatedManagementRepository.findHbzFslOrder(teakUserInformationDTO.getId());
        if (hbzFslOrder.getTakeUser() != null) {

            tu.setTeakUser(hbzFslOrder.getTakeUser().getNickName());
        }
        if (hbzFslOrder.getTakeUser() != null) {

            tu.setTeakUserTelephone(hbzFslOrder.getTakeUser().getTelephone());
        }
        if (hbzFslOrder.getTakeUser() != null && hbzFslOrder.getTakeUser().getEnt() != null) {

            tu.setOrg(hbzFslOrder.getTakeUser().getEnt().getOrganizationName());
        }

        if (hbzFslOrder.getTakeUser() != null && hbzFslOrder.getTakeUser().getId() != null) {
            HbzUserRegistry hbzUserRegistry = hbzUserRegistryRepository.findByCreatedBy(hbzFslOrder.getTakeUser().getId());
            HbzPersonDriverRegistry hbzPersonDriverRegistry = dedicatedManagementRepository.findHbzPersonDriverRegistry(hbzUserRegistry.getId());
            if (hbzPersonDriverRegistry != null) {
                //车长
                tu.setCarLength(hbzPersonDriverRegistry.getTransLength());
                //载重
                tu.setLoad(hbzPersonDriverRegistry.getLoad());
                ////持证照片
                //tu.setCertifiedPhoto(hbzPersonDriverRegistry.getCertifiedPhoto());
                //交通强制险照片
                //tu.setStrongInsuranceImage(hbzPersonDriverRegistry.getStrongInsuranceImage());
                tu.setLicensePlateNumber(hbzPersonDriverRegistry.getLicensePlateNumber());
                //tu.setTransTypeValue(hbzPersonDriverRegistry.getTransType().getName());
                if (hbzPersonDriverRegistry.getTransType() != null) {

                    tu.setTransTypeValue(hbzPersonDriverRegistry.getTransType().getName());
                }
            }
        }

        return tu;
    }

    //根据订单编号 查询支付信息
    @Override
    public HbzPayChildDTO findHbzPay(HbzPayChildDTO hbzPayChildDTO) {
        List<PayProgress> list = new ArrayList<>();
        list.add(PayProgress.SUCCESS);
        HbzPay hbzPay = dedicatedManagementRepository.findHbzPay(hbzPayChildDTO.getBusinessNo(), list);
        HbzPayChildDTO hb = new HbzPayChildDTO();
        if (hbzPay != null) {

            BeanUtils.copyProperties(hbzPay, hb);
            hb.setPayTypeValue(hbzPay.getPayType().getName());
        }
        return hb;


    }

    //根据订单 id  查询车辆征集条件
    @Override
    public HbzTendersDTO findHbzTender(HbzTendersDTO hbzTendersDTO) {
        HbzTender hbzTender = dedicatedManagementRepository.findHbzTender(hbzTendersDTO.getOrderId());
        HbzTendersDTO htDTO = new HbzTendersDTO();
        if (hbzTender != null) {
            BeanUtils.copyProperties(hbzTender, htDTO);
            htDTO.setOrderId(hbzTender.getId());
            if (hbzTender.getRegistryMoney() != null) {
                String a = String.valueOf(hbzTender.getRegistryMoney());
                String subA = a.substring(0, a.indexOf("."));
                String name = dedicatedManagementRepository.findUserClassification("Registered_funds", subA);
                htDTO.setRegistryMoneyValue(name);
            }
            String name2 = dedicatedManagementRepository.findUserClassification("Driver", String.valueOf(hbzTender.getNeed()));
            htDTO.setNeedValue(name2);
            if (hbzTender.getBond() != null) {
                String b = String.valueOf(hbzTender.getBond());
                String subB = b.substring(0, b.indexOf("."));
                String name3 = dedicatedManagementRepository.findUserClassification("Security_deposit", subB);
                htDTO.setBondValue(name3);
            }
            String name4 = dedicatedManagementRepository.findUserClassification("Credit_level", String.valueOf(hbzTender.getStarLevel()));
            htDTO.setStarLevelValue(name4);
        }

        return htDTO;
    }


    //根据订单id 查询参与征集司机的信息
    @Override
    public List<HbzTakerInfoDTO> findfindHbzTakerInfo(HbzTakerInfoDTO hbzTakerInfoDTO) {
        List<HbzTakerInfo> list = dedicatedManagementRepository.findHbzTakerInfo(hbzTakerInfoDTO.getOrderId());


        return list.stream().map(hbzTakerInfoMapper::map).collect(Collectors.toList());
    }


    //物流详情
    @Override
    public List<LogisticsDetailsDTO> findLogisticsDetails(LogisticsDetailsDTO logisticsDetailsDTO) {
        List<LogisticsDetailsDTO> list = new ArrayList<>();

        List<HbzOrderRecord> hbzOrderRecord = hbzOrderRecRepository.findOrderRec(logisticsDetailsDTO.getId());

        HbzOrder hbzOrder =  hbzOrderRepository.findOne(logisticsDetailsDTO.getId());

        if (hbzOrderRecord != null) {
            for (int i = 0; i < hbzOrderRecord.size(); i++) {

                LogisticsDetailsDTO ld = new LogisticsDetailsDTO();
                HbzOrderRecord hor = hbzOrderRecord.get(i);
                if (hor != null) {
                    //状态
                    String orderTransValue = "--";
                    if (hor.getOrderTrans() != null) {
                        ld.setOrderTrans(hor.getOrderTrans());
                        orderTransValue = hor.getOrderTrans().getName();
                        ld.setOrderTransValue(orderTransValue);
                    }
                    //时间
                    FormatedDate logInCreatedDate = new FormatedDate(hor.getTimeMillis());
                    String timeMillis = logInCreatedDate.getFormat("yyyy-MM-dd HH:mm:ss");
                    ld.setTimeMillis(timeMillis);
                    if(null != hbzOrder.getOfflineProcess() && hbzOrder.getOfflineProcess()==1){
                        ld.setInformation("客户拒绝签收，工作人员处理中");
                    }else{
                        ld.setInformation(timeMillis + " " + orderTransValue);
                    }
                }
                list.add(ld);
            }
        }

        return list;
    }

    //订单  详情按钮
    @Override
    public DedicatedLineManagementDTO findHbzFslOrder(DedicatedLineManagementDTO dedicatedLineManagementDTO) {
        HbzFslOrder hbzFslOrder = dedicatedManagementRepository.findHbzFslOrder(dedicatedLineManagementDTO.getId());

        DedicatedLineManagementDTO dedicate = new DedicatedLineManagementDTO();
        if (hbzFslOrder != null) {

            dedicate.setId(hbzFslOrder.getId());
            dedicate.setCommodityName(hbzFslOrder.getCommodityName());
            if (hbzFslOrder.getOrderTakeStart() != null) {
                FormatedDate logIn = new FormatedDate(hbzFslOrder.getOrderTakeStart());
                String orderTakeStart = logIn.getFormat("yyyy-MM-dd HH:mm");
                dedicate.setOrderTakeStart(orderTakeStart);
            }

            dedicate.setCommodityDescribe(hbzFslOrder.getCommodityDescribe());
            dedicate.setCommodityWeight(hbzFslOrder.getCommodityWeight());
            dedicate.setCommodityVolume(hbzFslOrder.getCommodityVolume());
            dedicate.setTransType(hbzFslOrder.getTransType());
            dedicate.setTransLen(hbzFslOrder.getTransLen());
            dedicate.setRelatedPictures(hbzFslOrder.getRelatedPictures());
            if (hbzFslOrder.getTransType() != null) {
                dedicate.setTransTypeValue(hbzFslOrder.getTransType().getName());
            }

            dedicate.setMaxLoad(hbzFslOrder.getMaxLoad());
            //指定运输到货时间
            if (hbzFslOrder.getDestlimit() != null) {

                FormatedDate logIn2 = new FormatedDate(hbzFslOrder.getDestlimit());
                String destlimit = logIn2.getFormat("yyyy-MM-dd HH:mm");
                dedicate.setDestlimit(destlimit);
            }
            //取货地址
            HbzArea currentLevelOriginArea = hbzFslOrder.getOriginArea();
            LinkedList<Long> longList = new LinkedList<>();
            StringBuilder sb = new StringBuilder();
            if (currentLevelOriginArea != null) {


                while (currentLevelOriginArea.getLevel() > 0) {
                    sb.insert(0, currentLevelOriginArea.getAreaName() + " ");
                    switch (currentLevelOriginArea.getLevel().intValue()) {
                        case 1: {
                            longList.addFirst(currentLevelOriginArea.getId());
                            dedicate.setStartCity(longList);
                        }
                        break;
                        case 2: {
                            longList.addFirst(currentLevelOriginArea.getId());
                            dedicate.setStartCity(longList);
                        }
                        break;
                        case 3: {
                            longList.addFirst(currentLevelOriginArea.getId());
                            dedicate.setStartCity(longList);
                        }
                        break;
                    }

                    currentLevelOriginArea = currentLevelOriginArea.getParent();
                }
                dedicate.setOriginArea(sb.toString());
            }
            //具体地址
            dedicate.setOriginAddress(hbzFslOrder.getOriginAddress());
            dedicate.setLinkMan(hbzFslOrder.getLinkMan());
            dedicate.setLinkTelephone(hbzFslOrder.getLinkTelephone());
            //到站地址
            HbzArea currentLevelArea = hbzFslOrder.getDestArea();
            LinkedList<Long> endList = new LinkedList<>();
            StringBuilder ss = new StringBuilder();
            if (currentLevelArea != null) {
                while (currentLevelArea.getLevel() > 0) {
                    ss.insert(0, currentLevelArea.getAreaName() + " ");
                    switch (currentLevelArea.getLevel().intValue()) {
                        case 1: {
                            endList.addFirst(currentLevelArea.getId());
                            dedicate.setEndCity(endList);
                        }
                        break;
                        case 2: {
                            endList.addFirst(currentLevelArea.getId());
                            dedicate.setEndCity(endList);
                        }
                        break;
                        case 3: {
                            endList.addFirst(currentLevelArea.getId());
                            dedicate.setEndCity(endList);
                        }
                        break;
                    }

                    currentLevelArea = currentLevelArea.getParent();
                }
                dedicate.setDestArea(ss.toString());
            }
            //具体地址
            dedicate.setDestAddress(hbzFslOrder.getDestAddress());
            dedicate.setDestLinker(hbzFslOrder.getDestLinker());
            dedicate.setDestTelephone(hbzFslOrder.getDestTelephone());
            dedicate.setLinkRemark(hbzFslOrder.getLinkRemark());
            dedicate.setSettlementType(hbzFslOrder.getSettlementType());
            if (hbzFslOrder.getSettlementType() != null) {
                dedicate.setSettlementTypeValue(hbzFslOrder.getSettlementType().getName());
            }

            dedicate.setAmount(hbzFslOrder.getAmount());
            //单价
            dedicate.setUnitPrice(hbzFslOrder.getUnitPrice());
            //创建人
            if (hbzFslOrder.getCreateUser() != null) {
                dedicate.setCreateUser(hbzFslOrder.getCreateUser().getNickName());
            }
            //联系电话
            if (hbzFslOrder.getCreateUser() != null) {
                dedicate.setCreateUsertelephone(hbzFslOrder.getCreateUser().getTelephone());
            }
            //所属公司
            if (hbzFslOrder.getCreateUser() != null && hbzFslOrder.getCreateUser().getEnt() != null) {
                dedicate.setOrg(hbzFslOrder.getCreateUser().getEnt().getOrganizationName());
            }

            dedicate.setOrderNo(hbzFslOrder.getOrderNo());
            //订单创建时间
            FormatedDate logInCreatedDate = new FormatedDate(hbzFslOrder.getCreatedDate());
            String createdDate = logInCreatedDate.getFormat("yyyy-MM-dd HH:mm");
            dedicate.setCreateUserTime(createdDate);

            dedicatedLineManagementDTO.setOrderTrans(hbzFslOrder.getOrderTrans());
            if (hbzFslOrder.getOrderTrans() != null) {
                dedicate.setOrderTransValue(hbzFslOrder.getOrderTrans().getName());
            }
            dedicate.setOrderType(hbzFslOrder.getOrderType());
            if (hbzFslOrder.getOrderType() != null) {
                dedicate.setOrderTypeValue(hbzFslOrder.getOrderType().getName());
            }

            //接单人
            if (hbzFslOrder.getTakeUser() != null) {

                dedicate.setTakeUser(hbzFslOrder.getTakeUser().getNickName());
            }
            if (hbzFslOrder.getTakeUser() != null) {

                dedicate.setTakeUserTelephone(hbzFslOrder.getTakeUser().getTelephone());
            }
            // 接单人的id
            if (hbzFslOrder.getTakeUser() != null) {

                dedicate.setTakeUserId(hbzFslOrder.getTakeUser().getId());
            }
        }

        return dedicate;
    }

    @Override
    public List<HbzTakerInfoCallDTO> findCall(HbzTakerInfoCallDTO hbzTakerInfoCallDTO) {
        List<HbzTakerInfo> hbzTakerInfo = dedicatedManagementRepository.findHbzTakerInfo(hbzTakerInfoCallDTO.getId());
        List<HbzTakerInfoCallDTO> list = new ArrayList<>();
        for (int i = 0; i < hbzTakerInfo.size(); i++) {
            HbzTakerInfoCallDTO hc = new HbzTakerInfoCallDTO();
            hc.setTeakUser(hbzTakerInfo.get(i).getUser().getNickName());
            hc.setTeakUserTelephone(hbzTakerInfo.get(i).getUser().getTelephone());
            if (hbzTakerInfo.get(i).getUser() != null && hbzTakerInfo.get(i).getUser().getEnt() != null) {

                hc.setOrganizationName(hbzTakerInfo.get(i).getUser().getEnt().getOrganizationName());
            }
            hc.setStarLevel(hbzTakerInfo.get(i).getUser().getStarLevel());

            FormatedDate logInCreatedDate = new FormatedDate(hbzTakerInfo.get(i).getCreatedDate());
            String createdDate = logInCreatedDate.getFormat("yyyy-MM-dd HH:mm:ss");
            hc.setTeakUserTime(createdDate);
            list.add(hc);
        }

        return list;
    }


    //根据接单人用户的id 查询用户的车辆信息
//    @Override
//    public HbzPersonDriverRegistryDTO findHbzPersonDriverRegistry(HbzPersonDriverRegistryDTO hbzPersonDriverRegistryDTO) {
//        HbzPersonDriverRegistry hbzPersonDriverRegistry= dedicatedManagementRepository.findHbzPersonDriverRegistry(hbzPersonDriverRegistryDTO.getId());
//
//        return hbzPersonDriverRegistryMapper.map(hbzPersonDriverRegistry);
//    }


//    //根据订单id 查询整车订单信息
//    @Override
//    public HbzFslOrderDTO findHbzFslOrder(HbzFslOrderDTO hbzFslOrderDTO) {
//        HbzFslOrder hbzFslOrder=  dedicatedManagementRepository.findHbzFslOrder(hbzFslOrderDTO.getId());
//        return hbzFslOrderMapper.map(hbzFslOrder);
//    }


}
