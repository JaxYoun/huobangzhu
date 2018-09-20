package com.troy.keeper.management.service.impl;

import com.troy.keeper.core.base.service.RedisService;
import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.hbz.dto.HbzExOrderDTO;
import com.troy.keeper.hbz.dto.HbzPlatformOrganizationDTO;
import com.troy.keeper.hbz.po.*;
import com.troy.keeper.hbz.repository.HbzAssignWorkRepository;
import com.troy.keeper.hbz.repository.HbzOrderRepository;
import com.troy.keeper.hbz.service.HbzExOrderService;
import com.troy.keeper.hbz.service.HbzSmOrgService;
import com.troy.keeper.hbz.sys.FormatedDate;
import com.troy.keeper.hbz.type.CopingStatusType;
import com.troy.keeper.hbz.type.ShippingStatus;
import com.troy.keeper.management.dto.*;
import com.troy.keeper.management.repository.*;
import com.troy.keeper.management.service.CargoInformationService;
import com.troy.keeper.system.domain.SmPostUser;
import com.troy.keeper.system.domain.SmUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 李奥
 * @date 2017/12/29.
 */
@Service
@Transactional
public class CargoInformationServiceImpl implements CargoInformationService {

    @Autowired
    private CargoInformationRepository cargoInformationRepository;

    @Autowired
    private HbzAreasRepository hbzAreaRepository;

    @Autowired
    private UserInformationRepository userInformationRepository;

    //托运用户信息
    @Autowired
    private ShipperUserRepository shipperUserRepository;

    //接运客户信息
    @Autowired
    private ReceiverUserRepository receiverUserRepository;

    //费用信息
    @Autowired
    private FeeScheduleRepository feeScheduleRepository;

    @Autowired
    private RedisService redisService;

    //平台指运订单分页查询
    @Autowired
    private HbzAssignWorkRepository hbzAssignWorkRepository;

    //应付
    @Autowired
    private DealManagementRepository dealManagementRepository;

    //应收
    @Autowired
    private ReceivableManagementRepository receivableManagementRepository;


    @Autowired
    private HbzExpressPiecesRepository hbzExpressPiecesRepository;

    @Autowired
    private HbzOrderRepository hbzOrderRepository;

    @Autowired
    private HbzExOrderService hbzExOrderService;

    @Autowired
    private HbzSmOrgService hbzSmOrgService;

    //分页查询
    @Override
    public Page<CargoInformationDTO> findByCondition(CargoInformationDTO cargoInformationDTO, Pageable pageable) {
        //查询当前组织机构的下的 货物信息
        SmUser smUser = cargoInformationRepository.findSmOrgId(SecurityUtils.getCurrentUserId());
        List<SmPostUser> list1 = smUser.getSmPostUserList();
        Long a = null;
        for (int i = 0; i < list1.size(); i++) {
            a = list1.get(i).getSmPost().getOrgId();
        }
        Long orgId = a;

        Page<CargoInformation> page = cargoInformationRepository.findAll(new Specification<CargoInformation>() {
            @Override
            public Predicate toPredicate(Root<CargoInformation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();
                if (cargoInformationDTO.getSmallTime() != null) {
                    //时间范围的查询
                    predicateList.add(criteriaBuilder.ge(root.get("receiptDate"), cargoInformationDTO.getSmallTime()));
                }
                if (cargoInformationDTO.getBigTime() != null) {
                    //查收时分钟值需增加59999计算
                    predicateList.add(criteriaBuilder.le(root.get("receiptDate"), cargoInformationDTO.getBigTime() + 59999l));
                }
                if (StringUtils.isNotBlank(cargoInformationDTO.getTrackingNumber())) {
                    //物流编号
                    predicateList.add(criteriaBuilder.like(root.get("trackingNumber"), "%" + cargoInformationDTO.getTrackingNumber() + "%"));
                }
                if (StringUtils.isNotBlank(cargoInformationDTO.getWaybillNumber())) {
                    //运单号
                    predicateList.add(criteriaBuilder.like(root.get("waybillNumber"), "%" + cargoInformationDTO.getWaybillNumber() + "%"));
                }
                if (StringUtils.isNotBlank(cargoInformationDTO.getCommodityNumber())) {
                    //货物编号
                    predicateList.add(criteriaBuilder.like(root.get("commodityNumber"), "%" + cargoInformationDTO.getCommodityNumber() + "%"));
                }
                if (StringUtils.isNotBlank(cargoInformationDTO.getCommodityName())) {
                    //货物名称
                    predicateList.add(criteriaBuilder.like(root.get("commodityName"), "%" + cargoInformationDTO.getCommodityName() + "%"));
                }

                if (StringUtils.isNotBlank(cargoInformationDTO.getShipperUserCompanyName())) {
                    //托运单位
                    predicateList.add(criteriaBuilder.like(root.join("shipperUser").get("shipperUserCompanyName"), "%" + cargoInformationDTO.getShipperUserCompanyName() + "%"));
                }
                if (StringUtils.isNotBlank(cargoInformationDTO.getShipperUserTelephone())) {
                    //托运人电话
                    predicateList.add(criteriaBuilder.equal(root.join("shipperUser").get("shipperUserTelephone"), cargoInformationDTO.getShipperUserTelephone()));
                }


                if (StringUtils.isNotBlank(cargoInformationDTO.getReceiverUserCompanyName())) {
                    //收货单位
                    predicateList.add(criteriaBuilder.like(root.join("receiverUser").get("receiverUserCompanyName"), "%" + cargoInformationDTO.getReceiverUserCompanyName() + "%"));
                }
                if (StringUtils.isNotBlank(cargoInformationDTO.getReceiverUserTelephone())) {
                    //收货人电话
                    predicateList.add(criteriaBuilder.equal(root.join("receiverUser").get("receiverUserTelephone"), cargoInformationDTO.getReceiverUserTelephone()));
                }

                //发出
                if (StringUtils.isNotBlank(cargoInformationDTO.getOriginAreaCode())) {
                    predicateList.add(criteriaBuilder.equal(root.join("originArea").get("outCode"), cargoInformationDTO.getOriginAreaCode()));
                }
//                //省
//                if (cargoInformationDTO.getProvinceId() !=null){
//                    //找到所有的子节点
//                    List<Long> childIds = hbzAreaRepository.findAllIdsByParentId(cargoInformationDTO.getProvinceId());
//                    predicateList.add(root.get("originArea").get("id").in(childIds));
//                }
//                //市
//                if (cargoInformationDTO.getCityId() != null){
//                    //找到所有的子节点
//                    List<Long> childIds = hbzAreaRepository.findAreaIdsByParentId(cargoInformationDTO.getCityId());
//                    predicateList.add(root.get("originArea").get("id").in(childIds));
//                }
//                //区
//                if (cargoInformationDTO.getCountyId() != null){
//                    predicateList.add(criteriaBuilder.equal(root.get("originArea").get("id"),cargoInformationDTO.getCountyId()));
//                }
                //到站
                if (StringUtils.isNotBlank(cargoInformationDTO.getDestAreaCode())) {
                    predicateList.add(criteriaBuilder.equal(root.join("destArea").get("outCode"), cargoInformationDTO.getDestAreaCode()));
                }
//                //省
//                if (cargoInformationDTO.getProvinceToId() !=null){
//                    //找到所有的子节点
//                    List<Long> childIds = hbzAreaRepository.findAllIdsByParentId(cargoInformationDTO.getProvinceToId());
//                    predicateList.add(root.get("destArea").get("id").in(childIds));
//                }
//                //市
//                if (cargoInformationDTO.getCityToId() != null){
//                    //找到所有的子节点
//                    List<Long> childIds = hbzAreaRepository.findAreaIdsByParentId(cargoInformationDTO.getCityToId());
//
//                    predicateList.add(root.get("destArea").get("id").in(childIds));
//                }
//                //区
//                if (cargoInformationDTO.getCountyToId() != null){
//
//                    predicateList.add(criteriaBuilder.equal(root.get("destArea").get("id"),cargoInformationDTO.getCountyToId()));
//                }
                if (cargoInformationDTO.getPaymentMethod() != null) {
                    //付款方式
                    predicateList.add(criteriaBuilder.equal(root.join("feeSchedule").get("paymentMethod"), cargoInformationDTO.getPaymentMethod()));
                }

                if (StringUtils.isNotBlank(cargoInformationDTO.getIsReceipt())) {
                    //是否回单
                    predicateList.add(criteriaBuilder.equal(root.get("isReceipt"), cargoInformationDTO.getIsReceipt()));
                }
                if (StringUtils.isNotBlank(cargoInformationDTO.getIsDelivery())) {
                    //等话仿货
                    predicateList.add(criteriaBuilder.equal(root.get("isDelivery"), cargoInformationDTO.getIsDelivery()));
                }
                if (StringUtils.isNotBlank(cargoInformationDTO.getGoodsType())) {
                    //收货类型
                    predicateList.add(criteriaBuilder.equal(root.get("goodsType"), cargoInformationDTO.getGoodsType()));
                }
                if (StringUtils.isNotBlank(cargoInformationDTO.getInWar())) {
                    //中转站
                    predicateList.add(criteriaBuilder.like(root.get("inWar"), "%" + cargoInformationDTO.getInWar() + "%"));
                }
                //查询状态为 1 的货物信息
                predicateList.add(criteriaBuilder.equal(root.get("status"), "1"));
                //查询新建订单
                predicateList.add(root.get("shippingStatus").in(Arrays.asList(ShippingStatus.NEW, ShippingStatus.SECTION_START)));

                //当前登录人 只能查看自己站点的 货物
                predicateList.add(criteriaBuilder.equal(root.get("smOrgId"), orgId));


                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));

            }
        }, pageable);
        return converterDto(page);
    }


    private Page<CargoInformationDTO> converterDto(Page<CargoInformation> page) {
        return page.map(new Converter<CargoInformation, CargoInformationDTO>() {
            @Override
            public CargoInformationDTO convert(CargoInformation cargoInformation) {

                CargoInformationDTO ci = new CargoInformationDTO();

                BeanUtils.copyProperties(cargoInformation, ci);

                if (cargoInformation != null) {
                    if (cargoInformation.getOriginArea() != null) {
                        ci.setOriginAreaCode(cargoInformation.getOriginArea().getOutCode());
                    }
                    if (cargoInformation.getDestArea() != null) {
                        ci.setDestAreaCode(cargoInformation.getDestArea().getOutCode());
                    }


                    //收货日期
                    if (cargoInformation.getReceiptDate() != null) {
                        FormatedDate logIn = new FormatedDate(cargoInformation.getReceiptDate());
                        String receiptDate = logIn.getFormat("yyyy-MM-dd HH:mm");
                        ci.setReceiptDate(receiptDate);
                    }

                    //货物运输状态的value值
                    ci.setShippingStatusValue(cargoInformation.getShippingStatus().getName());

                    if (cargoInformation.getServiceMethodType() != null) {
                        ci.setServiceMethodTypeValue(cargoInformation.getServiceMethodType().getName());
                    }


                    HbzArea currentLevelOriginArea = cargoInformation.getOriginArea();
                    LinkedList<Long> longList = new LinkedList<>();
                    StringBuilder sb = new StringBuilder();
                    if (currentLevelOriginArea != null) {
                        while (currentLevelOriginArea.getLevel() > 0) {
                            sb.insert(0, currentLevelOriginArea.getAreaName() + " ");
                            switch (currentLevelOriginArea.getLevel().intValue()) {
                                case 1: {
                                    ci.setProvinceId(currentLevelOriginArea.getId());
                                    longList.addFirst(currentLevelOriginArea.getId());
                                    ci.setStartCity(longList);
                                }
                                break;
                                case 2: {
                                    ci.setCityId(currentLevelOriginArea.getId());
                                    longList.addFirst(currentLevelOriginArea.getId());
                                    ci.setStartCity(longList);
                                }
                                break;
                                case 3: {
                                    ci.setCountyId(currentLevelOriginArea.getId());
                                    longList.addFirst(currentLevelOriginArea.getId());
                                    ci.setStartCity(longList);
                                }
                                break;
                            }

                            currentLevelOriginArea = currentLevelOriginArea.getParent();
                        }
                        ci.setOriginArea(sb.toString());
                    }


                    HbzArea currentLevelArea = cargoInformation.getDestArea();
                    LinkedList<Long> endList = new LinkedList<>();
                    StringBuilder ss = new StringBuilder();
                    if (currentLevelArea != null) {
                        while (currentLevelArea.getLevel() > 0) {
                            ss.insert(0, currentLevelArea.getAreaName() + " ");
                            switch (currentLevelArea.getLevel().intValue()) {
                                case 1: {
                                    ci.setProvinceToId(currentLevelArea.getId());
                                    endList.addFirst(currentLevelArea.getId());
                                    ci.setEndCity(endList);
                                }
                                break;
                                case 2: {
                                    ci.setCityToId(currentLevelArea.getId());
                                    endList.addFirst(currentLevelArea.getId());
                                    ci.setEndCity(endList);
                                }
                                break;
                                case 3: {
                                    ci.setCountyToId(currentLevelArea.getId());
                                    endList.addFirst(currentLevelArea.getId());
                                    ci.setEndCity(endList);
                                }
                                break;
                            }

                            currentLevelArea = currentLevelArea.getParent();
                        }
                        ci.setDestArea(ss.toString());
                    }


                    String name = userInformationRepository.findUserClassification("PackagingStatus", cargoInformation.getPackagingStatus());
                    ci.setPackagingStatusName(name);
                    String name2 = userInformationRepository.findUserClassification("PackageUnit", cargoInformation.getPackageUnit());
                    ci.setPackageUnitValue(name2);
                    String name1 = userInformationRepository.findUserClassification("BillingUser", cargoInformation.getBillingUser());
                    ci.setBillingUserName(name1);
                    ///////////
                    // 托运用户信息
                    if (cargoInformation.getShipperUser() != null) {
                        ci.setShipperUserId(cargoInformation.getShipperUser().getId());
                        ci.setShipperUserCompanyName(cargoInformation.getShipperUser().getShipperUserCompanyName());
                        ci.setShipperUserName(cargoInformation.getShipperUser().getShipperUserName());
                        ci.setShipperUserTelephone(cargoInformation.getShipperUser().getShipperUserTelephone());
                        ci.setShipperUserAddress(cargoInformation.getShipperUser().getShipperUserAddress());
                        ci.setShipperUserZipCode(cargoInformation.getShipperUser().getShipperUserZipCode());
                    }
                    //接运用户信息

                    if (cargoInformation.getReceiverUser() != null) {
                        ci.setReceiverUserId(cargoInformation.getReceiverUser().getId());
                        ci.setReceiverUserCompanyName(cargoInformation.getReceiverUser().getReceiverUserCompanyName());
                        ci.setReceiverUserName(cargoInformation.getReceiverUser().getReceiverUserName());
                        ci.setReceiverUserTelephone(cargoInformation.getReceiverUser().getReceiverUserTelephone());
                        ci.setReceiverUserAddress(cargoInformation.getReceiverUser().getReceiverUserAddress());
                        ci.setReceiverUserZipCode(cargoInformation.getReceiverUser().getReceiverUserZipCode());
                    }
                    //费用信息
                    if (cargoInformation.getFeeSchedule() != null) {
                        ci.setFeeId(cargoInformation.getFeeSchedule().getId());
                        Double shippingCosts = cargoInformation.getFeeSchedule().getShippingCosts();
                        ci.setShippingCosts(shippingCosts);

                        Double deliveryFee = cargoInformation.getFeeSchedule().getDeliveryFee();
                        ci.setDeliveryFee(deliveryFee);

                        Double deliveryCharges = cargoInformation.getFeeSchedule().getDeliveryCharges();
                        ci.setDeliveryCharges(deliveryCharges);

                        Double premium = cargoInformation.getFeeSchedule().getPremium();
                        ci.setPremium(premium);

                        Double packagingFee = cargoInformation.getFeeSchedule().getPackagingFee();
                        ci.setPackagingFee(packagingFee);

                        Double otherFee = cargoInformation.getFeeSchedule().getOtherFee();
                        ci.setOtherFee(otherFee);
                        ci.setPaymentMethod(cargoInformation.getFeeSchedule().getPaymentMethod());
                        //总运费
                        ci.setFotalFee(shippingCosts + deliveryFee + deliveryCharges + premium + packagingFee + otherFee);

                        String name3 = userInformationRepository.findUserClassification("PaymentMethod", cargoInformation.getFeeSchedule().getPaymentMethod());
                        ci.setPaymentMethodName(name3);
                    }

                }
                return ci;
            }
        });
    }


    //新增收货管理
    @Override
    public Boolean addCargoInformation(CargoInformationDTO cargoInformationDTO) {

        CargoInformation ci = new CargoInformation();
        BeanUtils.copyProperties(cargoInformationDTO, ci);

        //收货日期的设置转换
        ci.setReceiptDate(Long.valueOf(cargoInformationDTO.getReceiptDate()));

        //增加组织机构id；
        SmUser smUser = cargoInformationRepository.findSmOrgId(SecurityUtils.getCurrentUserId());
        Long orgId = 0l;
        List<SmPostUser> list1 = smUser.getSmPostUserList();
        for (int i = 0; i < list1.size(); i++) {
            orgId = list1.get(i).getSmPost().getOrgId();
            //新增的时候 获取登陆用户的机构id
        }
        ci.setSmOrgId(orgId);

        //到缓存中去 取机构id
//          String  userId= String.valueOf(SecurityUtils.getCurrentUserId());
//          Long orgId= Long.valueOf(redisService.get("1"));
//          ci.setSmOrgId(orgId);


        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        String str = sdf.format(d);
        String a = str.replace("-", "").replace(" ", "").replace(":", "");
        String B = null;
        if (cargoInformationRepository.trackingNumber() != null) {
            String maxTrackingNumber = cargoInformationRepository.trackingNumber().substring(14);
            String trackingNumber = String.valueOf(Integer.parseInt(maxTrackingNumber) + 1);
            B = a + trackingNumber;
            ci.setTrackingNumber(B);
//            ci.setWaybillNumber(B);
        } else {
            B = a + "1";
            ci.setTrackingNumber(B);
//            ci.setWaybillNumber(B);
        }
        //运单编号
        ci.setWaybillNumber(cargoInformationDTO.getWaybillNumber());
        //货物状态
        ci.setShippingStatus(ShippingStatus.NEW);

        //库存数量  运单数量
        ci.setInventoryQuantity(cargoInformationDTO.getAmount());

        DecimalFormat df = new DecimalFormat("0.0000");

        //单个重量
        if (cargoInformationDTO.getWeight() != null && cargoInformationDTO.getAmount() > 0) {
            int Amount = cargoInformationDTO.getAmount();
            Double weight = cargoInformationDTO.getWeight();
            String s = df.format((weight / Amount));
            ci.setSingleWeight(Double.valueOf(s));
        }

        //单个体积
        if (cargoInformationDTO.getVolume() != null && cargoInformationDTO.getAmount() > 0) {
            Double volume = cargoInformationDTO.getVolume();
            int Amount2 = cargoInformationDTO.getAmount();
            String s2 = df.format((volume / Amount2));
            ci.setSingleVolume(Double.valueOf(s2));
        }

        //新增时自动设置物流编号
//        String str1= cargoInformationRepository.commodityNumber();
//        if (str1==null){
//            ci.setCommodityNumber("00001");
//        }else {
//            String  commodityNumber=String.valueOf(Integer.parseInt(str1)+1);
//
//            if (commodityNumber.length() == 1) {
//                ci.setCommodityNumber("0000" + commodityNumber);
//            } else if (commodityNumber.length() == 2) {
//                ci.setCommodityNumber("000" + commodityNumber);
//            } else if (commodityNumber.length() == 3) {
//                ci.setCommodityNumber("00" + commodityNumber);
//            } else if (commodityNumber.length() == 4) {
//                ci.setCommodityNumber("0" + commodityNumber);
//            } else {
//                ci.setCommodityNumber(commodityNumber);
//            }
//        }
        //物流编号
        ci.setCommodityNumber(cargoInformationDTO.getCommodityNumber());

        //新增时设置状态为1
        ci.setStatus("1");
        //保存发站的区域id
        if (cargoInformationDTO.getOriginAreaCode() != null) {
            HbzArea hbzArea = hbzAreaRepository.findByOutCode(cargoInformationDTO.getOriginAreaCode());
            ci.setOriginArea(hbzArea);
        }

//        //保存到站的区域id
        if (cargoInformationDTO.getDestAreaCode() != null) {
            HbzArea hbzArea = hbzAreaRepository.findByOutCode(cargoInformationDTO.getDestAreaCode());
            ci.setDestArea(hbzArea);
        }

        //保存托运用户信息
        ShipperUser su = new ShipperUser();
        su.setShipperUserCompanyName(cargoInformationDTO.getReceiverUserCompanyName());
        su.setShipperUserName(cargoInformationDTO.getShipperUserName());
        su.setShipperUserAddress(cargoInformationDTO.getShipperUserAddress());
        su.setShipperUserTelephone(cargoInformationDTO.getShipperUserTelephone());
        su.setShipperUserZipCode(cargoInformationDTO.getShipperUserZipCode());
        shipperUserRepository.save(su);

        //接运客户信息
        ReceiverUser ru = new ReceiverUser();
        ru.setReceiverUserCompanyName(cargoInformationDTO.getReceiverUserCompanyName());
        ru.setReceiverUserName(cargoInformationDTO.getReceiverUserName());
        ru.setReceiverUserTelephone(cargoInformationDTO.getReceiverUserTelephone());
        ru.setReceiverUserAddress(cargoInformationDTO.getReceiverUserAddress());
        ru.setReceiverUserZipCode(cargoInformationDTO.getReceiverUserZipCode());
        receiverUserRepository.save(ru);

        //费用表
        FeeSchedule fs = new FeeSchedule();
        Double shippingCosts = cargoInformationDTO.getShippingCosts();
        fs.setShippingCosts(shippingCosts);

        Double deliveryFee = cargoInformationDTO.getDeliveryFee();
        if (deliveryFee == null) {
            deliveryFee = 0.0;
            fs.setDeliveryFee(deliveryFee);
        } else {
            fs.setDeliveryFee(deliveryFee);
        }

        Double deliveryCharges = cargoInformationDTO.getDeliveryCharges();
        if (deliveryCharges == null) {
            deliveryCharges = 0.0;
            fs.setDeliveryCharges(deliveryCharges);
        } else {
            fs.setDeliveryCharges(deliveryCharges);
        }

        Double premium = cargoInformationDTO.getPremium();
        if (premium == null) {
            premium = 0.0;
            fs.setPremium(premium);
        } else {
            fs.setPremium(premium);
        }

        Double packagingFee = cargoInformationDTO.getPackagingFee();
        if (packagingFee == null) {
            packagingFee = 0.0;
            fs.setPackagingFee(packagingFee);
        } else {
            fs.setPackagingFee(packagingFee);
        }

        Double otherFee = cargoInformationDTO.getOtherFee();
        if (otherFee == null) {
            otherFee = 0.0;
            fs.setOtherFee(otherFee);
        } else {
            fs.setOtherFee(otherFee);
        }
        fs.setPaymentMethod(cargoInformationDTO.getPaymentMethod());
        //总运费
        fs.setFotalFee(shippingCosts + deliveryFee + deliveryCharges + premium + packagingFee + otherFee);
        feeScheduleRepository.save(fs);

        ci.setShipperUser(su);
        ci.setReceiverUser(ru);
        ci.setFeeSchedule(fs);
        cargoInformationRepository.save(ci);

        ///////保存应收，应付的详细数据////////////////////////////////////////////////////////////////
        for (int i = 0; i < 5; i++) {
            //应付的数据
            DealManagement dm = new DealManagement();
            dm.setOrderSource("1");
            dm.setSourceCode(B);
            dm.setCopingStatus(CopingStatusType.UNPAID);
            dm.setRecordStatus("1");
            dm.setSmOrgId(orgId);

            if (i == 0 && cargoInformationDTO.getDeliveryFee() != null && cargoInformationDTO.getDeliveryFee() > 0) {
                //保存应付编码
                dm.setCoding("A00001");
                dm.setSubjectName("提货费");
                //应收
                dm.setAmountsPayable(cargoInformationDTO.getDeliveryFee());
                dealManagementRepository.save(dm);
            }
            if (i == 1 && cargoInformationDTO.getDeliveryCharges() != null && cargoInformationDTO.getDeliveryCharges() > 0) {
                //保存应付编码
                dm.setCoding("B00002");
                dm.setSubjectName("送货费");
                //应收
                dm.setAmountsPayable(cargoInformationDTO.getDeliveryCharges());
                dealManagementRepository.save(dm);
            }
            if (i == 2 && cargoInformationDTO.getPremium() != null && cargoInformationDTO.getPremium() > 0) {
                //保存应付编码
                dm.setCoding("C00003");
                dm.setSubjectName("保费");
                //应收
                dm.setAmountsPayable(cargoInformationDTO.getPremium());
                dealManagementRepository.save(dm);
            }
            if (i == 3 && cargoInformationDTO.getPackagingFee() != null && cargoInformationDTO.getPackagingFee() > 0) {
                //保存应付编码
                dm.setCoding("D00004");
                dm.setSubjectName("包装费");
                //应收
                dm.setAmountsPayable(cargoInformationDTO.getPackagingFee());
                dealManagementRepository.save(dm);
            }
            if (i == 4 && cargoInformationDTO.getOtherFee() != null && cargoInformationDTO.getOtherFee() > 0) {
                //保存应付编码
                dm.setCoding("E00005");
                dm.setSubjectName("其他费用");
                //应收
                dm.setAmountsPayable(cargoInformationDTO.getOtherFee());
                dealManagementRepository.save(dm);
            }
        }

        Long number = cargoInformationRepository.waybillNumber(cargoInformationDTO.getWaybillNumber(), cargoInformationDTO.getId());
        if (number > 0) {
            return false;
        }

        //应收的数据
        if (cargoInformationDTO.getShippingCosts() != null || cargoInformationDTO.getShippingCosts() > 0) {

            ReceivableManagement rm = new ReceivableManagement();
            rm.setOrderSource("1");
            rm.setSourceCode(B);
            rm.setCopingStatus(CopingStatusType.UNPAID);
            rm.setRecordStatus("1");
            rm.setCoding("F00006");
            rm.setSubjectName("运费");
            rm.setSmOrgId(orgId);
            rm.setAmountsPayable(cargoInformationDTO.getShippingCosts());
            rm.setCompanyName(cargoInformationDTO.getShipperUserCompanyName());
            rm.setContact(cargoInformationDTO.getShipperUserName());
            rm.setContactPhone(cargoInformationDTO.getShipperUserTelephone());
            receivableManagementRepository.save(rm);
        }

        return true;

    }


    //修改货物信息
    @Override
    public Boolean updateCargoInformation(CargoInformationDTO cargoInformationDTO) {
        CargoInformation ci = cargoInformationRepository.findCargoInformationId(cargoInformationDTO.getId());
        String commodityNumber = ci.getCommodityNumber();
        String trackingNumber = ci.getTrackingNumber();


        BeanUtils.copyProperties(cargoInformationDTO, ci);

        ci.setId(cargoInformationDTO.getId());
        //货物状态
        ci.setShippingStatus(ShippingStatus.NEW);
        ci.setCommodityNumber(commodityNumber);
        ci.setTrackingNumber(trackingNumber);

        //新增时设置状态为1
        ci.setStatus("1");

        //增加组织机构id；
        SmUser smUser = cargoInformationRepository.findSmOrgId(SecurityUtils.getCurrentUserId());
        List<SmPostUser> list1 = smUser.getSmPostUserList();
        Long orgId = 0l;
        for (int i = 0; i < list1.size(); i++) {
            orgId = list1.get(i).getSmPost().getOrgId();
            //新增的时候 获取登陆用户的机构id
        }
        ci.setSmOrgId(orgId);


//        //保存发站的区域id
        if (cargoInformationDTO.getOriginAreaCode() != null) {
            HbzArea hbzArea = hbzAreaRepository.findByOutCode(cargoInformationDTO.getOriginAreaCode());
            ci.setOriginArea(hbzArea);
        }
//        //保存到站的区域id
        if (cargoInformationDTO.getDestAreaCode() != null) {
            HbzArea hbzArea = hbzAreaRepository.findByOutCode(cargoInformationDTO.getDestAreaCode());
            ci.setDestArea(hbzArea);
        }

        //库存数量  运单数量
        ci.setInventoryQuantity(cargoInformationDTO.getAmount());

        DecimalFormat df = new DecimalFormat("0.0000");

        //单个重量
        if (cargoInformationDTO.getWeight() != null && cargoInformationDTO.getAmount() > 0) {
            int Amount = cargoInformationDTO.getAmount();
            Double weight = cargoInformationDTO.getWeight();
            String s = df.format((weight / Amount));
            ci.setSingleWeight(Double.valueOf(s));
        }

        //单个体积
        if (cargoInformationDTO.getVolume() != null && cargoInformationDTO.getAmount() > 0) {
            Double volume = cargoInformationDTO.getVolume();
            int Amount2 = cargoInformationDTO.getAmount();
            String s2 = df.format((volume / Amount2));
            ci.setSingleVolume(Double.valueOf(s2));
        }

        //保存托运用户信息
        ShipperUser su = shipperUserRepository.findOne(cargoInformationDTO.getShipperUserId());
//        BeanUtils.copyProperties(cargoInformationDTO,su);
        su.setShipperUserCompanyName(cargoInformationDTO.getShipperUserCompanyName());
        su.setShipperUserName(cargoInformationDTO.getShipperUserName());
        su.setShipperUserTelephone(cargoInformationDTO.getShipperUserTelephone());
        su.setShipperUserAddress(cargoInformationDTO.getShipperUserAddress());
        su.setShipperUserZipCode(cargoInformationDTO.getShipperUserZipCode());
        shipperUserRepository.save(su);
        //接运客户信息
        ReceiverUser ru = receiverUserRepository.findOne(cargoInformationDTO.getReceiverUserId());
//        BeanUtils.copyProperties(cargoInformationDTO,ru);
        ru.setReceiverUserCompanyName(cargoInformationDTO.getReceiverUserCompanyName());
        ru.setReceiverUserName(cargoInformationDTO.getReceiverUserName());
        ru.setReceiverUserTelephone(cargoInformationDTO.getReceiverUserTelephone());
        ru.setReceiverUserAddress(cargoInformationDTO.getReceiverUserAddress());
        ru.setReceiverUserZipCode(cargoInformationDTO.getReceiverUserZipCode());
        receiverUserRepository.save(ru);
        //费用表
        FeeSchedule fs = feeScheduleRepository.findOne(cargoInformationDTO.getFeeId());
//        BeanUtils.copyProperties(cargoInformationDTO,fs);
        Double shippingCosts = cargoInformationDTO.getShippingCosts();
        fs.setShippingCosts(shippingCosts);

        Double deliveryFee = cargoInformationDTO.getDeliveryFee();
        if (deliveryFee == null) {
            deliveryFee = 0.0;
            fs.setDeliveryFee(deliveryFee);
        } else {
            fs.setDeliveryFee(deliveryFee);
        }

        Double deliveryCharges = cargoInformationDTO.getDeliveryCharges();
        if (deliveryCharges == null) {
            deliveryCharges = 0.0;
            fs.setDeliveryCharges(deliveryCharges);
        } else {
            fs.setDeliveryCharges(deliveryCharges);
        }

        Double premium = cargoInformationDTO.getPremium();
        if (premium == null) {
            premium = 0.0;
            fs.setPremium(premium);
        } else {
            fs.setPremium(premium);
        }

        Double packagingFee = cargoInformationDTO.getPackagingFee();
        if (packagingFee == null) {
            packagingFee = 0.0;
            fs.setPackagingFee(packagingFee);
        } else {
            fs.setPackagingFee(packagingFee);
        }

        Double otherFee = cargoInformationDTO.getOtherFee();
        if (otherFee == null) {
            otherFee = 0.0;
            fs.setOtherFee(otherFee);
        } else {
            fs.setOtherFee(otherFee);
        }
        fs.setPaymentMethod(cargoInformationDTO.getPaymentMethod());
        //总运费
        fs.setFotalFee(shippingCosts + deliveryFee + deliveryCharges + premium + packagingFee + otherFee);


        feeScheduleRepository.save(fs);

        ci.setShipperUser(su);
        ci.setReceiverUser(ru);
        ci.setFeeSchedule(fs);
        //保存修改运单之前判断运单编号是否重复
        // Long   number= cargoInformationRepository.waybillNumber(cargoInformationDTO.getWaybillNumber(),cargoInformationDTO.getId());
        //if (number>0){
        //    return  false;
        //}
        //编辑收货信息时修改应付，应收的数据
        List<DealManagement> dealManagementList = cargoInformationRepository.findAllDealManagement(trackingNumber);
        if (dealManagementList != null) {
            for (int i = 0; i < dealManagementList.size(); i++) {
                DealManagement dm = dealManagementList.get(i);
                dm.setSmOrgId(orgId);
                if ("提货费".equals(dm.getSubjectName())) {
                    dm.setAmountsPayable(ci.getFeeSchedule().getDeliveryFee());
                    dealManagementRepository.save(dm);
                }
                if ("送货费".equals(dm.getSubjectName())) {
                    dm.setAmountsPayable(ci.getFeeSchedule().getDeliveryCharges());
                    dealManagementRepository.save(dm);
                }
                if ("保费".equals(dm.getSubjectName())) {
                    dm.setAmountsPayable(ci.getFeeSchedule().getPremium());
                    dealManagementRepository.save(dm);
                }
                if ("包装费".equals(dm.getSubjectName())) {
                    dm.setAmountsPayable(ci.getFeeSchedule().getPackagingFee());
                    dealManagementRepository.save(dm);
                }
                if ("其他费用".equals(dm.getSubjectName())) {
                    dm.setAmountsPayable(ci.getFeeSchedule().getOtherFee());
                    dealManagementRepository.save(dm);
                }
            }
        }

        //修改应付
        ReceivableManagement rm = cargoInformationRepository.findAllReceivableManagement(trackingNumber);
        if (rm != null) {
            rm.setSmOrgId(orgId);
            rm.setAmountsPayable(ci.getFeeSchedule().getShippingCosts());
            rm.setCompanyName(ci.getShipperUser().getShipperUserCompanyName());
            rm.setContact(ci.getShipperUser().getShipperUserName());
            rm.setContactPhone(ci.getShipperUser().getShipperUserTelephone());
            receivableManagementRepository.save(rm);
        }


        cargoInformationRepository.save(ci);

        return true;

    }

    //修改运单编号时判断重复
    @Override
    public Boolean deleteCargoInformation(CargoInformationDTO cargoInformationDTO) {
        if (cargoInformationDTO.getId() != null) {
            cargoInformationRepository.deleteCargoInformation(cargoInformationDTO.getId());
            return true;
        } else {
            return false;
        }
    }


    //平台指运订单分页查询
    @Override
    public Page<HbzAssignWorkDTO> findHbzAssignWorkMapDTO(HbzAssignWorkDTO hbzAssignWorkDTO, Pageable pageable) {
        Page<HbzAssignWork> page = hbzAssignWorkRepository.findAll(new Specification<HbzAssignWork>() {
            @Override
            public Predicate toPredicate(Root<HbzAssignWork> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();
                //指派时间范围查询
                if (hbzAssignWorkDTO.getSmallTime() != null) {
                    predicateList.add(criteriaBuilder.ge(root.get("assignTime"), hbzAssignWorkDTO.getSmallTime()));
                }
                if (hbzAssignWorkDTO.getBigTime() != null) {
                    predicateList.add(criteriaBuilder.le(root.get("assignTime"), hbzAssignWorkDTO.getBigTime() + 59999L));
                }
                //加入当前用户过滤
                Long currentUser = SecurityUtils.getCurrentUserId();
                Collection<Long> ids = hbzSmOrgService.getSubOrgIdArray(currentUser);
                predicateList.add(root.get("platformOrganization").get("id").in(ids));
                //发出
//                //省
                if (StringUtils.isNotBlank(hbzAssignWorkDTO.getOriginAreaCode())) {
                    predicateList.add(criteriaBuilder.equal(root.join("originArea").get("outCode"), hbzAssignWorkDTO.getOriginAreaCode()));
                }
                //到站
                //省
                if (StringUtils.isNotBlank(hbzAssignWorkDTO.getDestAreaCode())) {
                    predicateList.add(criteriaBuilder.equal(root.join("destArea").get("outCode"), hbzAssignWorkDTO.getDestAreaCode()));
                }

                //运单号
                if (StringUtils.isNotBlank(hbzAssignWorkDTO.getWorkNo())) {
                    predicateList.add(criteriaBuilder.equal(root.get("workNo"), hbzAssignWorkDTO.getWorkNo()));
                }
                //货物类型
                if (StringUtils.isNotBlank(hbzAssignWorkDTO.getClassification())) {
                    predicateList.add(criteriaBuilder.equal(root.get("classification"), hbzAssignWorkDTO.getClassification()));
                }
                //收货人
                if (StringUtils.isNotBlank(hbzAssignWorkDTO.getReceivePerson())) {
                    predicateList.add(criteriaBuilder.like(root.get("receivePerson"), hbzAssignWorkDTO.getReceivePerson()));
                }
                //收货电话
                if (StringUtils.isNotBlank(hbzAssignWorkDTO.getReceivePersonPhone())) {
                    predicateList.add(criteriaBuilder.equal(root.get("receivePersonPhone"), hbzAssignWorkDTO.getReceivePersonPhone()));
                }


                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));

            }
        }, pageable);
        return converterHbzAssignWorko(page);
    }


    private Page<HbzAssignWorkDTO> converterHbzAssignWorko(Page<HbzAssignWork> page) {
        return page.map(new Converter<HbzAssignWork, HbzAssignWorkDTO>() {
            @Override
            public HbzAssignWorkDTO convert(HbzAssignWork hbzAssignWork) {

                HbzAssignWorkDTO hbzAssign = new HbzAssignWorkDTO();
                BeanUtils.copyProperties(hbzAssignWork, hbzAssign);

                HbzExOrderDTO order = hbzExOrderService.findByOrderNo(hbzAssignWork.getPlatformNo());
                if (order != null) {
                    hbzAssign.setOriginAreaWrapper(order.getOriginArea());
                    hbzAssign.setDestAreaWrapper(order.getDestArea());
                    hbzAssign.setOriginInfo(order.getOriginAddr());
                    hbzAssign.setDestInfo(order.getDestAddr());
                    hbzAssign.setCommodityName(order.getCommodityName());
                }

                if ("1".equals(hbzAssignWork.getWorkStatus())) {
                    hbzAssign.setWorkStatus("未接单");
                } else {
                    hbzAssign.setWorkStatus("已接单");
                }

                String name = userInformationRepository.findUserClassification("Classification", hbzAssignWork.getClassification());
                hbzAssign.setClassification(name);

                hbzAssign.setExpectedAmount(hbzAssignWork.getExpectedAmount());

                HbzArea currentLevelOriginArea = hbzAssignWork.getOriginArea();
                LinkedList<Long> longList = new LinkedList<>();
                StringBuilder sb = new StringBuilder();
                if (currentLevelOriginArea != null) {
                    while (currentLevelOriginArea.getLevel() > 0) {
                        sb.insert(0, currentLevelOriginArea.getAreaName() + " ");
                        switch (currentLevelOriginArea.getLevel().intValue()) {
                            case 1: {
                                hbzAssign.setProvinceId(currentLevelOriginArea.getId());
                                longList.addFirst(currentLevelOriginArea.getId());
                                hbzAssign.setStartCity(longList);
                            }
                            break;
                            case 2: {
                                hbzAssign.setCityId(currentLevelOriginArea.getId());
                                longList.addFirst(currentLevelOriginArea.getId());
                                hbzAssign.setStartCity(longList);
                            }
                            break;
                            case 3: {
                                hbzAssign.setCountyId(currentLevelOriginArea.getId());
                                longList.addFirst(currentLevelOriginArea.getId());
                                hbzAssign.setStartCity(longList);
                            }
                            break;
                        }

                        currentLevelOriginArea = currentLevelOriginArea.getParent();
                    }
                    hbzAssign.setOriginArea(sb.toString());
                }

                //取货地址
//                StringBuffer sb = new StringBuffer();
//                if (hbzAssignWork.getOriginArea() != null) {
//                    Integer level = hbzAssignWork.getOriginArea().getLevel();
//                    if (level == 1) {//省
//                        sb.append(hbzAssignWork.getOriginArea().getAreaName());
//                    } else if (level == 2) {//市
//                        HbzArea hbzAreaCity = hbzAssignWork.getOriginArea();
//                        sb.append(hbzAreaCity.getAreaName());
//                        sb.insert(0, hbzAreaCity.getParent().getAreaName() + " ");
//                        hbzAssign.setOriginArea(sb.toString());
//                    } else if (level == 3) {//区县
//                        HbzArea hbzAreaCounty = hbzAssignWork.getOriginArea();
//                        HbzArea hbzAreaCity = hbzAreaCounty.getParent();
//                        HbzArea hbzAreaPrivice = hbzAreaCity.getParent();
//                        hbzAssign.setOriginArea(hbzAreaPrivice.getAreaName() + " " + hbzAreaCity.getAreaName() + " " + hbzAreaCounty.getAreaName());
//                    }
//                }

                HbzArea currentLevelArea = hbzAssignWork.getDestArea();
                LinkedList<Long> endList = new LinkedList<>();
                StringBuilder ss = new StringBuilder();
                if (currentLevelArea != null) {
                    while (currentLevelArea.getLevel() > 0) {
                        ss.insert(0, currentLevelArea.getAreaName() + " ");
                        switch (currentLevelArea.getLevel().intValue()) {
                            case 1: {
                                hbzAssign.setProvinceToId(currentLevelArea.getId());
                                endList.addFirst(currentLevelArea.getId());
                                hbzAssign.setEndCity(endList);
                            }
                            break;
                            case 2: {
                                hbzAssign.setCityToId(currentLevelArea.getId());
                                endList.addFirst(currentLevelArea.getId());
                                hbzAssign.setEndCity(endList);
                            }
                            break;
                            case 3: {
                                hbzAssign.setCountyToId(currentLevelArea.getId());
                                endList.addFirst(currentLevelArea.getId());
                                hbzAssign.setEndCity(endList);
                            }
                            break;
                        }

                        currentLevelArea = currentLevelArea.getParent();
                    }
                    hbzAssign.setDestArea(ss.toString());
                }
                //送货地址
//                StringBuffer sbf = new StringBuffer();
//                if (hbzAssignWork.getDestArea() != null) {
//                    Integer level = hbzAssignWork.getDestArea().getLevel();
//                    if (level == 1) {//省
//                        sbf.append(hbzAssignWork.getDestArea().getAreaName());
//                    } else if (level == 2) {//市
//                        HbzArea hbzAreaCity = hbzAssignWork.getDestArea();
//                        sbf.append(hbzAreaCity.getAreaName());
//                        sbf.insert(0, hbzAreaCity.getParent().getAreaName() + " ");
//                        hbzAssign.setDestArea(sbf.toString());
//                    } else if (level == 3) {//区县
//                        HbzArea hbzAreaCounty = hbzAssignWork.getDestArea();
//                        HbzArea hbzAreaCity = hbzAreaCounty.getParent();
//                        HbzArea hbzAreaPrivice = hbzAreaCity.getParent();
//                        hbzAssign.setDestArea(hbzAreaPrivice.getAreaName() + " " + hbzAreaCity.getAreaName() + " " + hbzAreaCounty.getAreaName());
//                    }
//                }
                //指定运输到货时间
                if (hbzAssignWork.getAssignTime() != null) {

                    FormatedDate logIn2 = new FormatedDate(hbzAssignWork.getAssignTime());
                    String assignTime = logIn2.getFormat("yyyy-MM-dd");
                    hbzAssign.setAssignTime(assignTime);
                }
                if (hbzAssignWork.getClassification() != null) {
                    HbzPlatformOrganizationDTO hp = new HbzPlatformOrganizationDTO();
                    HbzPlatformOrganization hbzPlatformOrganization = hbzAssignWork.getPlatformOrganization();
                    BeanUtils.copyProperties(hbzPlatformOrganization, hp);
                    hbzAssign.setPlatformOrganizationDTO(hp);
                }

                return hbzAssign;
            }
        });
    }


    //点击接单查询该单子的详细信息
    @Override
    public HbzAssignWorkDTO findHbzAssignWorkInfomation(HbzAssignWorkDTO hbzAssignWorkDTO) {
        HbzAssignWorkDTO haw = new HbzAssignWorkDTO();
        if (hbzAssignWorkDTO.getId() != null) {
            HbzAssignWork hbzAssignWork = hbzAssignWorkRepository.findOne(hbzAssignWorkDTO.getId());
            BeanUtils.copyProperties(hbzAssignWork, haw);
            String name = userInformationRepository.findUserClassification("Classification", hbzAssignWork.getClassification());
            haw.setClassification(name);
            if ("1".equals(hbzAssignWork.getWorkStatus())) {
                haw.setWorkStatus("未接单");
            } else {
                haw.setWorkStatus("已接单");
            }
            haw.setExpectedAmount(hbzAssignWork.getExpectedAmount());
            HbzArea currentLevelOriginArea = hbzAssignWork.getOriginArea();
            LinkedList<Long> longList = new LinkedList<>();
            StringBuilder sb = new StringBuilder();
            if (currentLevelOriginArea != null) {
                while (currentLevelOriginArea.getLevel() > 0) {
                    sb.insert(0, currentLevelOriginArea.getAreaName() + " ");
                    switch (currentLevelOriginArea.getLevel().intValue()) {
                        case 1: {
                            haw.setProvinceId(currentLevelOriginArea.getId());
                            longList.addFirst(currentLevelOriginArea.getId());
                            haw.setStartCity(longList);
                        }
                        break;
                        case 2: {
                            haw.setCityId(currentLevelOriginArea.getId());
                            longList.addFirst(currentLevelOriginArea.getId());
                            haw.setStartCity(longList);
                        }
                        break;
                        case 3: {
                            haw.setCountyId(currentLevelOriginArea.getId());
                            longList.addFirst(currentLevelOriginArea.getId());
                            haw.setStartCity(longList);
                        }
                        break;
                    }

                    currentLevelOriginArea = currentLevelOriginArea.getParent();
                }
                haw.setOriginArea(sb.toString());
            }
            //取货地址
//            StringBuffer sb = new StringBuffer();
//            if (hbzAssignWork.getOriginArea() != null) {
//                haw.setOriginAreaId(hbzAssignWork.getOriginArea().getId());
//                Integer level = hbzAssignWork.getOriginArea().getLevel();
//                if (level == 1) {//省
//                    sb.append(hbzAssignWork.getOriginArea().getAreaName());
//                } else if (level == 2) {//市
//                    HbzArea hbzAreaCity = hbzAssignWork.getOriginArea();
//                    sb.append(hbzAreaCity.getAreaName());
//                    sb.insert(0, hbzAreaCity.getParent().getAreaName() + " ");
//                    haw.setOriginArea(sb.toString());
//                } else if (level == 3) {//区县
//                    HbzArea hbzAreaCounty = hbzAssignWork.getOriginArea();
//                    HbzArea hbzAreaCity = hbzAreaCounty.getParent();
//                    HbzArea hbzAreaPrivice = hbzAreaCity.getParent();
//                    haw.setOriginArea(hbzAreaPrivice.getAreaName() + " " + hbzAreaCity.getAreaName() + " " + hbzAreaCounty.getAreaName());
//                }
//            }


            //送货地址
            HbzArea currentLevelArea = hbzAssignWork.getDestArea();
            LinkedList<Long> endList = new LinkedList<>();
            StringBuilder ss = new StringBuilder();
            if (currentLevelArea != null) {
                while (currentLevelArea.getLevel() > 0) {
                    ss.insert(0, currentLevelArea.getAreaName() + " ");
                    switch (currentLevelArea.getLevel().intValue()) {
                        case 1: {
                            haw.setProvinceToId(currentLevelArea.getId());
                            endList.addFirst(currentLevelArea.getId());
                            haw.setEndCity(endList);
                        }
                        break;
                        case 2: {
                            haw.setCityToId(currentLevelArea.getId());
                            endList.addFirst(currentLevelArea.getId());
                            haw.setEndCity(endList);
                        }
                        break;
                        case 3: {
                            haw.setCountyToId(currentLevelArea.getId());
                            endList.addFirst(currentLevelArea.getId());
                            haw.setEndCity(endList);
                        }
                        break;
                    }

                    currentLevelArea = currentLevelArea.getParent();
                }
                haw.setDestArea(ss.toString());
            }
//            StringBuffer sbf = new StringBuffer();
//            if (hbzAssignWork.getDestArea() != null) {
//                haw.setDestAreaId(hbzAssignWork.getDestArea().getId());
//                Integer level = hbzAssignWork.getDestArea().getLevel();
//                if (level == 1) {//省
//                    sbf.append(hbzAssignWork.getDestArea().getAreaName());
//                } else if (level == 2) {//市
//                    HbzArea hbzAreaCity = hbzAssignWork.getDestArea();
//                    sbf.append(hbzAreaCity.getAreaName());
//                    sbf.insert(0, hbzAreaCity.getParent().getAreaName() + " ");
//                    haw.setDestArea(sbf.toString());
//                } else if (level == 3) {//区县
//                    HbzArea hbzAreaCounty = hbzAssignWork.getDestArea();
//                    HbzArea hbzAreaCity = hbzAreaCounty.getParent();
//                    HbzArea hbzAreaPrivice = hbzAreaCity.getParent();
//                    haw.setDestArea(hbzAreaPrivice.getAreaName() + " " + hbzAreaCity.getAreaName() + " " + hbzAreaCounty.getAreaName());
//                }
//            }
            //指定运输到货时间
            if (hbzAssignWork.getAssignTime() != null) {

                FormatedDate logIn2 = new FormatedDate(hbzAssignWork.getAssignTime());
                String assignTime = logIn2.getFormat("yyyy-MM-dd");
                haw.setAssignTime(assignTime);
            }
            if (hbzAssignWork.getClassification() != null) {
                HbzPlatformOrganizationDTO hp = new HbzPlatformOrganizationDTO();
                HbzPlatformOrganization hbzPlatformOrganization = hbzAssignWork.getPlatformOrganization();
                BeanUtils.copyProperties(hbzPlatformOrganization, hp);
                haw.setPlatformOrganizationDTO(hp);
            }

        }
        return haw;
    }

    //保存平台指定的订单
    @Override
    public Boolean saveHbzAssignWork(CargoInformationDTO cargoInformationDTO) {
        CargoInformation ci = new CargoInformation();
        BeanUtils.copyProperties(cargoInformationDTO, ci);

        //收货日期的设置转换
        ci.setReceiptDate(Long.valueOf(cargoInformationDTO.getReceiptDate()));

        //增加组织机构id；
        SmUser smUser = cargoInformationRepository.findSmOrgId(SecurityUtils.getCurrentUserId());
        List<SmPostUser> list1 = smUser.getSmPostUserList();
        for (int i = 0; i < list1.size(); i++) {
            Long orgId = list1.get(i).getSmPost().getOrgId();
            // 新增的时候 获取登陆用户的机构id
            ci.setSmOrgId(orgId);
        }

        //到缓存中去 取机构id
//          String  userId= String.valueOf(SecurityUtils.getCurrentUserId());
//          Long orgId= Long.valueOf(redisService.get("1"));
//          ci.setSmOrgId(orgId);


        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        String str = sdf.format(d);
        String a = str.replace("-", "").replace(" ", "").replace(":", "");
        String B = null;
        if (cargoInformationRepository.trackingNumber() != null) {
            String maxTrackingNumber = cargoInformationRepository.trackingNumber().substring(14);
            String trackingNumber = String.valueOf(Integer.parseInt(maxTrackingNumber) + 1);
            B = a + trackingNumber;
            ci.setTrackingNumber(B);
            ci.setWaybillNumber(B);
        } else {
            B = a + "1";
            ci.setTrackingNumber(B);
            ci.setWaybillNumber(B);
        }
        //货物状态
        ci.setShippingStatus(ShippingStatus.NEW);

        //库存数量  运单数量
        ci.setInventoryQuantity(cargoInformationDTO.getAmount());
        DecimalFormat df = new DecimalFormat("0.0000");
        //单个重量
        if (cargoInformationDTO.getWeight() != null && cargoInformationDTO.getAmount() > 0) {
            int Amount = cargoInformationDTO.getAmount();
            Double weight = cargoInformationDTO.getWeight();
            String s = df.format((weight / Amount));
            ci.setSingleWeight(Double.valueOf(s));
        }

        //单个体积
        if (cargoInformationDTO.getVolume() != null && cargoInformationDTO.getAmount() > 0) {
            Double volume = cargoInformationDTO.getVolume();
            int Amount2 = cargoInformationDTO.getAmount();
            String s2 = df.format((volume / Amount2));
            ci.setSingleVolume(Double.valueOf(s2));
        }

        //新增时自动设置物流编号
        //String str1= cargoInformationRepository.commodityNumber();
        //if (str1==null){
        //    ci.setCommodityNumber("00001");
        //}else {
        //    String  commodityNumber=String.valueOf(str1+1);
        //
        //    if (commodityNumber.length() == 1) {
        //        ci.setCommodityNumber("0000" + commodityNumber);
        //    } else if (commodityNumber.length() == 2) {
        //        ci.setCommodityNumber("000" + commodityNumber);
        //    } else if (commodityNumber.length() == 3) {
        //        ci.setCommodityNumber("00" + commodityNumber);
        //    } else if (commodityNumber.length() == 4) {
        //        ci.setCommodityNumber("0" + commodityNumber);
        //    } else {
        //        ci.setCommodityNumber(commodityNumber);
        //    }
        //}

//        //新增时设置状态为1
        ci.setStatus("1");
//        //保存发站的区域id
        if (cargoInformationDTO.getOriginAreaCode() != null) {
            HbzArea hbzArea = hbzAreaRepository.findByOutCode(cargoInformationDTO.getOriginAreaCode());
            ci.setOriginArea(hbzArea);
        }
//        if (cargoInformationDTO.getOriginAreaId() !=null){
//            HbzArea  hbzArea= hbzAreaRepository.findOne(cargoInformationDTO.getOriginAreaId());
//            ci.setOriginArea(hbzArea);
//        }
//        //保存到站的区域id
        if (cargoInformationDTO.getDestAreaCode() != null) {
            HbzArea hbzArea = hbzAreaRepository.findByOutCode(cargoInformationDTO.getDestAreaCode());
            ci.setDestArea(hbzArea);
        }
//        if (cargoInformationDTO.getDestAreaId() !=null){
//            HbzArea  hbzArea= hbzAreaRepository.findOne(cargoInformationDTO.getDestAreaId());
//            ci.setDestArea(hbzArea);
//        }
        //保存托运用户信息
        ShipperUser su = new ShipperUser();
        su.setShipperUserCompanyName(cargoInformationDTO.getReceiverUserCompanyName());
        su.setShipperUserName(cargoInformationDTO.getShipperUserName());
        su.setShipperUserAddress(cargoInformationDTO.getShipperUserAddress());
        su.setShipperUserTelephone(cargoInformationDTO.getShipperUserTelephone());
        shipperUserRepository.save(su);

        //接运客户信息
        ReceiverUser ru = new ReceiverUser();
        ru.setReceiverUserCompanyName(cargoInformationDTO.getReceiverUserCompanyName());
        ru.setReceiverUserName(cargoInformationDTO.getReceiverUserName());
        ru.setReceiverUserTelephone(cargoInformationDTO.getReceiverUserTelephone());
        ru.setReceiverUserAddress(cargoInformationDTO.getReceiverUserAddress());
        ru.setReceiverUserZipCode(cargoInformationDTO.getReceiverUserZipCode());
        receiverUserRepository.save(ru);

        //费用表
        FeeSchedule fs = new FeeSchedule();
        fs.setShippingCosts(cargoInformationDTO.getShippingCosts() != null ? cargoInformationDTO.getShippingCosts() : 0.0);
        fs.setDeliveryFee(cargoInformationDTO.getDeliveryFee() != null ? cargoInformationDTO.getDeliveryFee() : 0.0);
        fs.setDeliveryCharges(cargoInformationDTO.getDeliveryCharges() != null ? cargoInformationDTO.getDeliveryCharges() : 0.0);
        fs.setPremium(cargoInformationDTO.getPremium() != null ? cargoInformationDTO.getPremium() : 0.0);
        fs.setPackagingFee(cargoInformationDTO.getPackagingFee() != null ? cargoInformationDTO.getPackagingFee() : 0.0);
        fs.setOtherFee(cargoInformationDTO.getOtherFee() != null ? cargoInformationDTO.getOtherFee() : 0.0);
        fs.setPaymentMethod(cargoInformationDTO.getPaymentMethod());
        feeScheduleRepository.save(fs);

        ci.setShipperUser(su);
        ci.setReceiverUser(ru);
        ci.setFeeSchedule(fs);
        cargoInformationRepository.save(ci);
        //设置平台的指定表中的物流编号
        if (cargoInformationDTO.getHbzAssignWorkId() != null) {

            HbzAssignWork  hbzAssignWork= hbzAssignWorkRepository.findById(cargoInformationDTO.getHbzAssignWorkId());
            //HbzAssignWork  hw=new HbzAssignWork();
            //BeanUtils.copyProperties(hbzAssignWork,hw);
            hbzAssignWork.setLogisticsNo(B);
            hbzAssignWork.setWorkStatus("0");
            hbzAssignWorkRepository.save(hbzAssignWork);
            //通过订单编号找到对应的订单 并通过订单id找到快递派件对象 然后设置物流编号保存
            HbzOrder hbzOrder = hbzOrderRepository.findFirstByOrderNo(hbzAssignWork.getPlatformNo());
            HbzExpressPieces hbzExpressPieces = hbzExpressPiecesRepository.findByHbzExOrderId(hbzOrder.getId());
            hbzExpressPieces.setTrackingNumber(B);
            hbzExpressPiecesRepository.save(hbzExpressPieces);
        }

        return true;
    }

    //根据登陆用户获取当前登陆人的网点
    public Long smOrgId() {

        SmUser smUser = cargoInformationRepository.findSmOrgId(SecurityUtils.getCurrentUserId());
        List<SmPostUser> list1 = smUser.getSmPostUserList();
        Long a = null;
        for (int i = 0; i < list1.size(); i++) {
            a = list1.get(i).getSmPost().getOrgId();
        }
        Long orgId = a;

        if (a == null) {
            return 0l;
        } else {
            return orgId;
        }

    }

    //收货导出
    public List<ExprotCargoInformationDTO> cargoInformationExport(ExprotCargoInformationDTO exprotCargoInformationDTO) {
        //查询当前组织机构的下的 货物信息
        SmUser smUser = cargoInformationRepository.findSmOrgId(SecurityUtils.getCurrentUserId());
        List<SmPostUser> list1 = smUser.getSmPostUserList();
        Long a = null;
        for (int i = 0; i < list1.size(); i++) {
            a = list1.get(i).getSmPost().getOrgId();
        }
        Long orgId = a;

        List<CargoInformation> exportList = cargoInformationRepository.findAll(new Specification<CargoInformation>() {
            @Override
            public Predicate toPredicate(Root<CargoInformation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();
                if (exprotCargoInformationDTO.getSmallTime() != null) {
                    //时间范围的查询
                    predicateList.add(criteriaBuilder.ge(root.get("receiptDate"), exprotCargoInformationDTO.getSmallTime()));
                }
                if (exprotCargoInformationDTO.getBigTime() != null) {
                    //时间范围的查询
                    predicateList.add(criteriaBuilder.le(root.get("receiptDate"), exprotCargoInformationDTO.getBigTime() + 59999L));
                }
                if (StringUtils.isNotBlank(exprotCargoInformationDTO.getTrackingNumber())) {
                    //物流编号
                    predicateList.add(criteriaBuilder.like(root.get("trackingNumber"), "%" + exprotCargoInformationDTO.getTrackingNumber() + "%"));
                }
                if (StringUtils.isNotBlank(exprotCargoInformationDTO.getWaybillNumber())) {
                    //运单号
                    predicateList.add(criteriaBuilder.like(root.get("waybillNumber"), "%" + exprotCargoInformationDTO.getWaybillNumber() + "%"));
                }
                if (StringUtils.isNotBlank(exprotCargoInformationDTO.getCommodityName())) {
                    //货物名称
                    predicateList.add(criteriaBuilder.like(root.get("commodityName"), "%" + exprotCargoInformationDTO.getCommodityName() + "%"));
                }

                if (StringUtils.isNotBlank(exprotCargoInformationDTO.getReceiverUserCompanyName())) {
                    //收货单位
                    predicateList.add(criteriaBuilder.like(root.join("receiverUser").get("receiverUserCompanyName"), "%" + exprotCargoInformationDTO.getReceiverUserCompanyName() + "%"));
                }
                if (StringUtils.isNotBlank(exprotCargoInformationDTO.getReceiverUserTelephone())) {
                    //收货人电话
                    predicateList.add(criteriaBuilder.equal(root.join("receiverUser").get("receiverUserTelephone"), exprotCargoInformationDTO.getReceiverUserTelephone()));
                }

                //发出
                if (StringUtils.isNotBlank(exprotCargoInformationDTO.getOriginAreaCode())) {
                    predicateList.add(criteriaBuilder.equal(root.join("originArea").get("outCode"), exprotCargoInformationDTO.getOriginAreaCode()));
                }
                //到站
                if (StringUtils.isNotBlank(exprotCargoInformationDTO.getDestAreaCode())) {
                    predicateList.add(criteriaBuilder.equal(root.join("destArea").get("outCode"), exprotCargoInformationDTO.getDestAreaCode()));
                }
                if (exprotCargoInformationDTO.getPaymentMethod() != null) {
                    //付款方式
                    predicateList.add(criteriaBuilder.equal(root.join("feeSchedule").get("paymentMethod"), exprotCargoInformationDTO.getPaymentMethod()));
                }

                if (StringUtils.isNotBlank(exprotCargoInformationDTO.getIsReceipt())) {
                    //是否回单
                    predicateList.add(criteriaBuilder.equal(root.get("isReceipt"), exprotCargoInformationDTO.getIsReceipt()));
                }
                if (StringUtils.isNotBlank(exprotCargoInformationDTO.getIsDelivery())) {
                    //等话仿货
                    predicateList.add(criteriaBuilder.equal(root.get("isDelivery"), exprotCargoInformationDTO.getIsDelivery()));
                }
                if (StringUtils.isNotBlank(exprotCargoInformationDTO.getInWar())) {
                    //中转站
                    predicateList.add(criteriaBuilder.like(root.get("inWar"), "%" + exprotCargoInformationDTO.getInWar() + "%"));
                }
                //查询状态为 1 的货物信息
                predicateList.add(criteriaBuilder.equal(root.get("status"), "1"));
                //查询新建订单
                predicateList.add(root.get("shippingStatus").in(Arrays.asList(ShippingStatus.NEW, ShippingStatus.SECTION_START)));

                //当前登录人 只能查看自己站点的 货物
                predicateList.add(criteriaBuilder.equal(root.get("smOrgId"), orgId));
                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));
            }
        });
        return this.exportDto(exportList);
    }

    private List<ExprotCargoInformationDTO> exportDto(List<CargoInformation> exportList) {

        List<ExprotCargoInformationDTO> list = new ArrayList<>();

        for (int i = 0; i < exportList.size(); i++) {
            ExprotCargoInformationDTO ciDTO = new ExprotCargoInformationDTO();
            CargoInformation ci = exportList.get(i);
            BeanUtils.copyProperties(ci, ciDTO);
//           ciDTO.setTrackingNumber(ci.getTrackingNumber());
//           ciDTO.setWaybillNumber(ci.getWaybillNumber());
            String goodsType = ci.getGoodsType();
            if ("1".equals(goodsType)) {
                ciDTO.setGoodsType("整车");
            } else if ("0".equals(goodsType)) {
                ciDTO.setGoodsType("零担");
            } else {
                ciDTO.setGoodsType("快递");
            }
            //发站城市
            HbzArea currentLevelOriginArea = ci.getOriginArea();
            StringBuilder sb = new StringBuilder();
            if (currentLevelOriginArea != null) {
                while (currentLevelOriginArea.getLevel() > 0) {
                    sb.insert(0, currentLevelOriginArea.getAreaName() + " ");
                    currentLevelOriginArea = currentLevelOriginArea.getParent();
                }
                ciDTO.setOriginArea(sb.toString());
            } else {
                ciDTO.setOriginArea("- -");
            }
            //到站城市
            HbzArea currentLevelArea = ci.getDestArea();
            LinkedList<Long> endList = new LinkedList<>();
            StringBuilder ss = new StringBuilder();
            if (currentLevelArea != null) {
                while (currentLevelArea.getLevel() > 0) {
                    ss.insert(0, currentLevelArea.getAreaName() + " ");

                    currentLevelArea = currentLevelArea.getParent();
                }
                ciDTO.setDestArea(ss.toString());
            } else {
                ciDTO.setDestArea("- -");
            }

            if (ci.getReceiverUser() != null && ci.getReceiverUser().getReceiverUserCompanyName() != null) {
                ciDTO.setReceiverUserCompanyName(ci.getReceiverUser().getReceiverUserCompanyName());
            } else {
                ciDTO.setReceiverUserCompanyName("- -");
            }

            if (ci.getReceiverUser() != null && ci.getReceiverUser().getReceiverUserTelephone() != null) {
                ciDTO.setReceiverUserTelephone(ci.getReceiverUser().getReceiverUserTelephone());
            } else {
                ciDTO.setReceiverUserTelephone("- -");
            }
            String name2 = userInformationRepository.findUserClassification("PackageUnit", ci.getPackageUnit());
            if (name2 != null) {
                ciDTO.setPackageUnitValue(name2);
            } else {
                ciDTO.setPackageUnitValue("- -");
            }

            if (ci.getOnCollection() != null) {
                ciDTO.setOnCollection(ci.getOnCollection());
            } else {
                ciDTO.setOnCollection(0.0);
            }

            if (ci.getAdvancePayment() != null) {
                ciDTO.setAdvancePayment(ci.getAdvancePayment());
            } else {
                ciDTO.setAdvancePayment(0.0);
            }

            if (ci.getFeeSchedule() != null && ci.getFeeSchedule().getPaymentMethod() != null) {
                String name3 = userInformationRepository.findUserClassification("PaymentMethod", ci.getFeeSchedule().getPaymentMethod());
                ciDTO.setPaymentMethodName(name3);

                Double fotalFee = ci.getFeeSchedule().getFotalFee();
                if (fotalFee == null) {
                    fotalFee = 0.0;
                }


                if ("现付".equals(name3)) { //已付
                    ciDTO.setPaid(fotalFee);
                }
                if (!"现付".equals(name3)) {
                    ciDTO.setPaid(0.0);
                }
                if ("提付".equals(name3)) {//提付
                    ciDTO.setPay(fotalFee);
                }
                if (!"提付".equals(name3)) {
                    ciDTO.setPay(0.0);
                }
                if ("回付".equals(name3)) {//回付
                    ciDTO.setPayback(fotalFee);
                }
                if (!"回付".equals(name3)) {
                    ciDTO.setPayback(0.0);
                }
            } else {
                ciDTO.setPaymentMethodName("- -");
            }

            if (ci.getShippingStatus() != null) {
                ciDTO.setShippingStatusValue(ci.getShippingStatus().getName());
            } else {
                ciDTO.setShippingStatusValue("- -");
            }


            list.add(ciDTO);
        }

        return list;
    }

    //分页查询
    @Override
    public Page<UserInformationDTO> userTable(UserInformationDTO userInformationDTO, Pageable pageable) {
        userInformationDTO.setDataStatus("1");


        Page<UserInformation> page = userInformationRepository.findAll(new Specification<UserInformation>() {
            @Override
            public Predicate toPredicate(Root<UserInformation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();
                if (StringUtils.isNotBlank(userInformationDTO.getCompanyName())) {
                    //单位名称
                    predicateList.add(criteriaBuilder.like(root.get("companyName"), "%" + userInformationDTO.getCompanyName() + "%"));
                }
                if (StringUtils.isNotBlank(userInformationDTO.getUserTelephone())) {
                    //电话
                    predicateList.add(criteriaBuilder.equal(root.get("userTelephone"), userInformationDTO.getUserTelephone()));
                }
                if (StringUtils.isNotBlank(userInformationDTO.getUserAddress())) {
                    //地址
                    predicateList.add(criteriaBuilder.like(root.get("userAddress"), "%" + userInformationDTO.getUserAddress() + "%"));
                }
                if (StringUtils.isNotBlank(userInformationDTO.getJianpin())) {
                    //简拼
                    predicateList.add(criteriaBuilder.equal(root.get("jianpin"), userInformationDTO.getJianpin()));
                }
//                if (userInformationDTO.getUserClassification() !=null){
                //客户分类
                predicateList.add(criteriaBuilder.equal(root.get("userClassification"), "0"));
//                }
                predicateList.add(criteriaBuilder.equal(root.get("status"), userInformationDTO.getDataStatus()));


                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));

            }
        }, pageable);
        return userInformation(page);
    }


    private Page<UserInformationDTO> userInformation(Page<UserInformation> page) {
        return page.map(new Converter<UserInformation, UserInformationDTO>() {
            @Override
            public UserInformationDTO convert(UserInformation userInformation) {


                UserInformationDTO ui = new UserInformationDTO();
                BeanUtils.copyProperties(userInformation, ui);
                //下拉选  取数据字典的name值

                String name = userInformationRepository.findUserClassification("UserClassification", userInformation.getUserClassification());
                ui.setUserClassificationValue(name);
                String name1 = userInformationRepository.findUserClassification("Bank", userInformation.getBank());
                ui.setBankValue(name1);
                return ui;
            }
        });
    }

    //新增收货时 收货方条件的过滤
    @Override
    public Page<UserInformationDTO> receivinguserTable(UserInformationDTO userInformationDTO, Pageable pageable) {
        userInformationDTO.setDataStatus("1");


        Page<UserInformation> page = userInformationRepository.findAll(new Specification<UserInformation>() {
            @Override
            public Predicate toPredicate(Root<UserInformation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();
                if (StringUtils.isNotBlank(userInformationDTO.getCompanyName())) {
                    //单位名称
                    predicateList.add(criteriaBuilder.like(root.get("companyName"), "%" + userInformationDTO.getCompanyName() + "%"));
                }
                if (StringUtils.isNotBlank(userInformationDTO.getUserTelephone())) {
                    //电话
                    predicateList.add(criteriaBuilder.equal(root.get("userTelephone"), userInformationDTO.getUserTelephone()));
                }
                if (StringUtils.isNotBlank(userInformationDTO.getUserAddress())) {
                    //地址
                    predicateList.add(criteriaBuilder.like(root.get("userAddress"), "%" + userInformationDTO.getUserAddress() + "%"));
                }
                if (StringUtils.isNotBlank(userInformationDTO.getJianpin())) {
                    //简拼
                    predicateList.add(criteriaBuilder.equal(root.get("jianpin"), userInformationDTO.getJianpin()));
                }
//                if (userInformationDTO.getUserClassification() !=null){
                //客户分类
                predicateList.add(criteriaBuilder.equal(root.get("userClassification"), "1"));
//                }
                predicateList.add(criteriaBuilder.equal(root.get("status"), userInformationDTO.getDataStatus()));


                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));

            }
        }, pageable);
        return receivinguserInformation(page);
    }


    private Page<UserInformationDTO> receivinguserInformation(Page<UserInformation> page) {
        return page.map(new Converter<UserInformation, UserInformationDTO>() {
            @Override
            public UserInformationDTO convert(UserInformation userInformation) {


                UserInformationDTO ui = new UserInformationDTO();
                BeanUtils.copyProperties(userInformation, ui);
                //下拉选  取数据字典的name值

                String name = userInformationRepository.findUserClassification("UserClassification", userInformation.getUserClassification());
                ui.setUserClassificationValue(name);
                String name1 = userInformationRepository.findUserClassification("Bank", userInformation.getBank());
                ui.setBankValue(name1);
                return ui;
            }
        });
    }

    /////////////////////////////////////////////////////////////////////////
    //不分页查询
    @Override
    public List<CargoInformationDTO> notTablePage(CargoInformationDTO cargoInformationDTO) {
        //查询当前组织机构的下的 货物信
        //        SmUser  smUser=cargoInformationRepository.findSmOrgId(SecurityUtils.getCurrentUserId());
        //        List<SmPostUser> list1=  smUser.getSmPostUserList();
        //        Long a = null;
        //        for (int i = 0; i <list1.size() ; i++) {
        //          a = list1.get(i).getSmPost().getOrgId();
        //        }
        //        Long orgId =a;

        List<CargoInformation> page = cargoInformationRepository.findAll(new Specification<CargoInformation>() {
            @Override
            public Predicate toPredicate(Root<CargoInformation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();
                if (cargoInformationDTO.getSmallTime() != null) {
                    //时间范围的查询
                    predicateList.add(criteriaBuilder.ge(root.get("receiptDate"), cargoInformationDTO.getSmallTime()));
                }
                if (cargoInformationDTO.getBigTime() != null) {
                    //时间范围的查询
                    predicateList.add(criteriaBuilder.le(root.get("receiptDate"), cargoInformationDTO.getBigTime()));
                }
                if (StringUtils.isNotBlank(cargoInformationDTO.getTrackingNumber())) {
                    //物流编号
                    predicateList.add(criteriaBuilder.like(root.get("trackingNumber"), "%" + cargoInformationDTO.getTrackingNumber() + "%"));
                }
                if (StringUtils.isNotBlank(cargoInformationDTO.getWaybillNumber())) {
                    //运单号
                    predicateList.add(criteriaBuilder.like(root.get("waybillNumber"), "%" + cargoInformationDTO.getWaybillNumber() + "%"));
                }
                if (StringUtils.isNotBlank(cargoInformationDTO.getCommodityNumber())) {
                    //货物编号
                    predicateList.add(criteriaBuilder.like(root.get("commodityNumber"), "%" + cargoInformationDTO.getCommodityNumber() + "%"));
                }
                if (StringUtils.isNotBlank(cargoInformationDTO.getCommodityName())) {
                    //货物名称
                    predicateList.add(criteriaBuilder.like(root.get("commodityName"), "%" + cargoInformationDTO.getCommodityName() + "%"));
                }

                if (StringUtils.isNotBlank(cargoInformationDTO.getShipperUserCompanyName())) {
                    //托运单位
                    predicateList.add(criteriaBuilder.like(root.join("shipperUser").get("shipperUserCompanyName"), "%" + cargoInformationDTO.getShipperUserCompanyName() + "%"));
                }
                if (StringUtils.isNotBlank(cargoInformationDTO.getShipperUserTelephone())) {
                    //托运人电话
                    predicateList.add(criteriaBuilder.equal(root.join("shipperUser").get("shipperUserTelephone"), cargoInformationDTO.getShipperUserTelephone()));
                }


                if (StringUtils.isNotBlank(cargoInformationDTO.getReceiverUserCompanyName())) {
                    //收货单位
                    predicateList.add(criteriaBuilder.like(root.join("receiverUser").get("receiverUserCompanyName"), "%" + cargoInformationDTO.getReceiverUserCompanyName() + "%"));
                }
                if (StringUtils.isNotBlank(cargoInformationDTO.getReceiverUserTelephone())) {
                    //收货人电话
                    predicateList.add(criteriaBuilder.equal(root.join("receiverUser").get("receiverUserTelephone"), cargoInformationDTO.getReceiverUserTelephone()));
                }

                //发出
                if (StringUtils.isNotBlank(cargoInformationDTO.getOriginAreaCode())) {
                    predicateList.add(criteriaBuilder.equal(root.join("originArea").get("outCode"), cargoInformationDTO.getOriginAreaCode()));
                }

                //到站
                if (StringUtils.isNotBlank(cargoInformationDTO.getDestAreaCode())) {
                    predicateList.add(criteriaBuilder.equal(root.join("destArea").get("outCode"), cargoInformationDTO.getDestAreaCode()));
                }

                if (cargoInformationDTO.getPaymentMethod() != null) {
                    //付款方式
                    predicateList.add(criteriaBuilder.equal(root.join("feeSchedule").get("paymentMethod"), cargoInformationDTO.getPaymentMethod()));
                }

                if (StringUtils.isNotBlank(cargoInformationDTO.getIsReceipt())) {
                    //是否回单
                    predicateList.add(criteriaBuilder.equal(root.get("isReceipt"), cargoInformationDTO.getIsReceipt()));
                }
                if (StringUtils.isNotBlank(cargoInformationDTO.getIsDelivery())) {
                    //等话仿货
                    predicateList.add(criteriaBuilder.equal(root.get("isDelivery"), cargoInformationDTO.getIsDelivery()));
                }
                if (StringUtils.isNotBlank(cargoInformationDTO.getInWar())) {
                    //中转站
                    predicateList.add(criteriaBuilder.like(root.get("inWar"), "%" + cargoInformationDTO.getInWar() + "%"));
                }
                //查询状态为 1 的货物信息
                predicateList.add(criteriaBuilder.equal(root.get("status"), "1"));
                //查询新建订单
                predicateList.add(root.get("shippingStatus").in(Arrays.asList(ShippingStatus.NEW, ShippingStatus.SECTION_START)));

                //当前登录人 只能查看自己站点的 货物
                predicateList.add(criteriaBuilder.equal(root.get("smOrgId"), 1l));

                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));


            }
        });
        return this.noPageDto(page);
    }


    private List<CargoInformationDTO> noPageDto(List<CargoInformation> page) {
        List<CargoInformationDTO> list = new ArrayList<>();

        for (int i = 0; i < page.size(); i++) {
            CargoInformationDTO ci = new CargoInformationDTO();
            CargoInformation cif = page.get(i);
            BeanUtils.copyProperties(cif, ci);
            if (cif.getOriginArea() != null) {
                ci.setOriginAreaCode(cif.getOriginArea().getOutCode());
            }
            if (cif.getDestArea() != null) {
                ci.setDestAreaCode(cif.getDestArea().getOutCode());
            }
            //收货日期
            if (cif.getReceiptDate() != null) {
                FormatedDate logIn = new FormatedDate(cif.getReceiptDate());
                String receiptDate = logIn.getFormat("yyyy-MM-dd HH:mm:ss");
                ci.setReceiptDate(receiptDate);
            }
            //货物运输状态的value值
            ci.setShippingStatusValue(cif.getShippingStatus().getName());
            if (cif.getServiceMethodType() != null) {
                ci.setServiceMethodTypeValue(cif.getServiceMethodType().getName());
            }

            HbzArea currentLevelOriginArea = cif.getOriginArea();
            LinkedList<Long> longList = new LinkedList<>();
            StringBuilder sb = new StringBuilder();
            if (currentLevelOriginArea != null) {
                while (currentLevelOriginArea.getLevel() > 0) {
                    sb.insert(0, currentLevelOriginArea.getAreaName() + " ");
                    switch (currentLevelOriginArea.getLevel().intValue()) {
                        case 1: {
                            ci.setProvinceId(currentLevelOriginArea.getId());
                            longList.addFirst(currentLevelOriginArea.getId());
                            ci.setStartCity(longList);
                        }
                        break;
                        case 2: {
                            ci.setCityId(currentLevelOriginArea.getId());
                            longList.addFirst(currentLevelOriginArea.getId());
                            ci.setStartCity(longList);
                        }
                        break;
                        case 3: {
                            ci.setCountyId(currentLevelOriginArea.getId());
                            longList.addFirst(currentLevelOriginArea.getId());
                            ci.setStartCity(longList);
                        }
                        break;
                    }

                    currentLevelOriginArea = currentLevelOriginArea.getParent();
                }
                ci.setOriginArea(sb.toString());
            }


            HbzArea currentLevelArea = cif.getDestArea();
            LinkedList<Long> endList = new LinkedList<>();
            StringBuilder ss = new StringBuilder();
            if (currentLevelArea != null) {
                while (currentLevelArea.getLevel() > 0) {
                    ss.insert(0, currentLevelArea.getAreaName() + " ");
                    switch (currentLevelArea.getLevel().intValue()) {
                        case 1: {
                            ci.setProvinceToId(currentLevelArea.getId());
                            endList.addFirst(currentLevelArea.getId());
                            ci.setEndCity(endList);
                        }
                        break;
                        case 2: {
                            ci.setCityToId(currentLevelArea.getId());
                            endList.addFirst(currentLevelArea.getId());
                            ci.setEndCity(endList);
                        }
                        break;
                        case 3: {
                            ci.setCountyToId(currentLevelArea.getId());
                            endList.addFirst(currentLevelArea.getId());
                            ci.setEndCity(endList);
                        }
                        break;
                    }

                    currentLevelArea = currentLevelArea.getParent();
                }
                ci.setDestArea(ss.toString());
            }
            String name = userInformationRepository.findUserClassification("PackagingStatus", cif.getPackagingStatus());
            ci.setPackagingStatusName(name);
            String name2 = userInformationRepository.findUserClassification("PackageUnit", cif.getPackageUnit());
            ci.setPackageUnitValue(name2);
            String name1 = userInformationRepository.findUserClassification("BillingUser", cif.getBillingUser());
            ci.setBillingUserName(name1);
            ///////////
            // 托运用户信息
            if (cif.getShipperUser() != null) {
                ci.setShipperUserId(cif.getShipperUser().getId());
                ci.setShipperUserCompanyName(cif.getShipperUser().getShipperUserCompanyName());
                ci.setShipperUserName(cif.getShipperUser().getShipperUserName());
                ci.setShipperUserTelephone(cif.getShipperUser().getShipperUserTelephone());
                ci.setShipperUserAddress(cif.getShipperUser().getShipperUserAddress());
                ci.setShipperUserZipCode(cif.getShipperUser().getShipperUserZipCode());
            }
            //接运用户信息
            if (cif.getReceiverUser() != null) {
                ci.setReceiverUserId(cif.getReceiverUser().getId());
                ci.setReceiverUserCompanyName(cif.getReceiverUser().getReceiverUserCompanyName());
                ci.setReceiverUserName(cif.getReceiverUser().getReceiverUserName());
                ci.setReceiverUserTelephone(cif.getReceiverUser().getReceiverUserTelephone());
                ci.setReceiverUserAddress(cif.getReceiverUser().getReceiverUserAddress());
                ci.setReceiverUserZipCode(cif.getReceiverUser().getReceiverUserZipCode());
            }
            //费用信息
            if (cif.getFeeSchedule() != null) {
                ci.setFeeId(cif.getFeeSchedule().getId());
                Double shippingCosts = cif.getFeeSchedule().getShippingCosts();
                ci.setShippingCosts(shippingCosts);

                Double deliveryFee = cif.getFeeSchedule().getDeliveryFee();
                ci.setDeliveryFee(deliveryFee);

                Double deliveryCharges = cif.getFeeSchedule().getDeliveryCharges();
                ci.setDeliveryCharges(deliveryCharges);

                Double premium = cif.getFeeSchedule().getPremium();
                ci.setPremium(premium);

                Double packagingFee = cif.getFeeSchedule().getPackagingFee();
                ci.setPackagingFee(packagingFee);

                Double otherFee = cif.getFeeSchedule().getOtherFee();
                ci.setOtherFee(otherFee);
                ci.setPaymentMethod(cif.getFeeSchedule().getPaymentMethod());
                //总运费
                ci.setFotalFee(shippingCosts + deliveryFee + deliveryCharges + premium + packagingFee + otherFee);

                String name3 = userInformationRepository.findUserClassification("PaymentMethod", cif.getFeeSchedule().getPaymentMethod());
                ci.setPaymentMethodName(name3);
            }
            list.add(ci);
        }
        return list;
    }


}
