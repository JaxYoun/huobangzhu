package com.troy.keeper.management.service.impl;

import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.hbz.dto.SmOrgDTO;
import com.troy.keeper.hbz.po.CargoInformation;
import com.troy.keeper.hbz.po.HbzArea;
import com.troy.keeper.hbz.po.OutsourcingGoods;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.hbz.sys.FormatedDate;
import com.troy.keeper.hbz.type.ShippingStatus;
import com.troy.keeper.management.dto.CargoInformationDTO;
import com.troy.keeper.management.repository.CargoInformationRepository;
import com.troy.keeper.management.repository.SmOrgManagementRepository;
import com.troy.keeper.management.repository.UserInformationRepository;
import com.troy.keeper.management.service.SmOrgManagementService;
import com.troy.keeper.system.domain.SmOrg;
import com.troy.keeper.system.domain.SmPostUser;
import com.troy.keeper.system.domain.SmUser;
import com.troy.keeper.system.repository.SmOrgRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 李奥
 * @date 2018/1/31.
 */
@Service
@Transactional
public class SmOrgManagementServiceImpl implements SmOrgManagementService {

    @Autowired
    private SmOrgManagementRepository smOrgManagementRepository;

    @Autowired
    private CargoInformationRepository cargoInformationRepository;

    @Autowired
    private UserInformationRepository userInformationRepository;


    @Override
    public List<SmOrgDTO> getListSmOrg(SmOrgDTO smOrgDTO) {

        List<SmOrgDTO> smOrgDTOList=new ArrayList<>();
        //通过当前登陆人获取机构id
        SmUser smUser=cargoInformationRepository.findSmOrgId(SecurityUtils.getCurrentUserId());
        Long a = null;
        if (smUser !=null) {
            List<SmPostUser> list1 = smUser.getSmPostUserList();

            for (int i = 0; i < list1.size(); i++) {
                a = list1.get(i).getSmPost().getOrgId();
            }
        }
        Long orgId =a;
        //通过机构id 查询当前机构下的所有的子机构  及机构的信息
//         List<SmOrg> listSmOrg=  smOrgManagementRepository.findAllSmorg(String.valueOf(orgId));
         List<SmOrg> listSmOrg=  smOrgManagementRepository.findAllSmorg("%"+orgId+"%");
//        List<SmOrgDTO> smOrgDTOList=new ArrayList<>();
        for (int i = 0; i <listSmOrg.size() ; i++) {
            SmOrgDTO DTO=new SmOrgDTO();
            SmOrg so=listSmOrg.get(i);
            BeanUtils.copyProperties(so,DTO);
//            new Bean2Bean().copyProperties(so,DTO,true);
            smOrgDTOList.add(DTO);
        }

            return smOrgDTOList;


    }


    @Override
    public List<SmOrgDTO> getListSmOrg1(SmOrgDTO smOrgDTO) {

        List<SmOrgDTO> smOrgDTOList=new ArrayList<>();
        SmUser smUser=cargoInformationRepository.findSmOrgId(SecurityUtils.getCurrentUserId());
        Long orgId =smUser.getOrgId();
        LinkedList<SmOrg> list = new LinkedList<>();
        list.add(smOrgManagementRepository.findOne(orgId));
        //通过机构id 查询当前机构下的所有的子机构  及机构的信息
//         List<SmOrg> listSmOrg=  smOrgManagementRepository.findAllSmorg(String.valueOf(orgId));
        List<SmOrg> listSmOrg=  smOrgManagementRepository.findAllSmorg("%"+orgId+"%");
//        List<SmOrgDTO> smOrgDTOList=new ArrayList<>();
        for (; list.size()>0 ; ) {
            SmOrg org = list.removeFirst();
            List<SmOrg> sub = smOrgManagementRepository.findByPId(org.getId());
            if(sub!=null)
                sub = sub.stream().filter(a->a.getStatus()!=null && a.getStatus().trim().equals("1")).collect(Collectors.toList());
            else
                sub = new ArrayList<>();
            list.addAll(sub);
            SmOrgDTO DTO = new SmOrgDTO();
            BeanUtils.copyProperties(org,DTO);
            smOrgDTOList.add(DTO);
        }

        return smOrgDTOList;


    }


    @Override
    public Page<CargoInformationDTO> selectCargoInformation(CargoInformationDTO cargoInformationDTO, Pageable pageable) {
        //查询当前组织机构的下的 货物信息
        SmUser  smUser=cargoInformationRepository.findSmOrgId(SecurityUtils.getCurrentUserId());
        List<SmPostUser> list1=  smUser.getSmPostUserList();
        Long a = null;
        for (int i = 0; i <list1.size() ; i++) {
          a = list1.get(i).getSmPost().getOrgId();
        }
        Long orgId =a;

        Page<CargoInformation> page=cargoInformationRepository.findAll(new Specification<CargoInformation>() {
            @Override
            public Predicate toPredicate(Root<CargoInformation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();

                Subquery subquery = criteriaQuery.subquery(Long.class);
                Root<SmOrg> outsourcingGoodsRoot = subquery.from(SmOrg.class);
                subquery.select(outsourcingGoodsRoot.get("id"));
                subquery.where(criteriaBuilder.like(outsourcingGoodsRoot.get("relationship"),"%"+orgId+"%"));


                predicateList.add(root.get("smOrgId").in(subquery));


                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));

            }
        },pageable);
        return smOrgDto(page);
    }




    private Page<CargoInformationDTO> smOrgDto(Page<CargoInformation> page) {
        return  page.map(new Converter<CargoInformation, CargoInformationDTO>() {
            @Override
            public CargoInformationDTO convert(CargoInformation cargoInformation) {

                CargoInformationDTO ci=new CargoInformationDTO();

                BeanUtils.copyProperties(cargoInformation,ci);

                if (cargoInformation !=null) {
                    if (cargoInformation.getOriginArea() !=null){
                        ci.setOriginAreaCode(cargoInformation.getOriginArea().getOutCode());
                    }
                    if (cargoInformation.getDestArea() !=null){
                        ci.setDestAreaCode(cargoInformation.getDestArea().getOutCode());
                    }


                    //收货日期
                    if ( cargoInformation.getReceiptDate() !=null){
                        FormatedDate logIn = new FormatedDate(cargoInformation.getReceiptDate());
                        String receiptDate = logIn.getFormat("yyyy-MM-dd HH:mm:ss");
                        ci.setReceiptDate(receiptDate);
                    }

                    //货物运输状态的value值
                    ci.setShippingStatusValue(cargoInformation.getShippingStatus().getName());

                    if (cargoInformation.getServiceMethodType() !=null) {
                        ci.setServiceMethodTypeValue(cargoInformation.getServiceMethodType().getName());
                    }


                    HbzArea currentLevelOriginArea= cargoInformation.getOriginArea();
                    LinkedList<Long> longList=new LinkedList<>();
                    StringBuilder sb = new StringBuilder();
                    while(currentLevelOriginArea.getLevel() >0 ){
                        sb.insert(0,currentLevelOriginArea.getAreaName() + " " );
                        switch(currentLevelOriginArea.getLevel().intValue()){
                            case 1:{
                                ci.setProvinceId(currentLevelOriginArea.getId());
                                longList.addFirst(currentLevelOriginArea.getId());
                                ci.setStartCity(longList);
                            }break;
                            case 2:{
                                ci.setCityId(currentLevelOriginArea.getId());
                                longList.addFirst(currentLevelOriginArea.getId());
                                ci.setStartCity(longList);
                            }break;
                            case 3:{
                                ci.setCountyId(currentLevelOriginArea.getId());
                                longList.addFirst(currentLevelOriginArea.getId());
                                ci.setStartCity(longList);
                            }break;
                        }

                        currentLevelOriginArea = currentLevelOriginArea.getParent();
                    }
                    ci.setOriginArea(sb.toString());


                    HbzArea currentLevelArea = cargoInformation.getDestArea();
                    LinkedList<Long> endList=new LinkedList<>();
                    StringBuilder ss = new StringBuilder();
                    while(currentLevelArea.getLevel() >0 ){
                        ss.insert(0,currentLevelArea.getAreaName() + " " );
                        switch(currentLevelArea.getLevel().intValue()){
                            case 1:{
                                ci.setProvinceToId(currentLevelArea.getId());
                                endList.addFirst(currentLevelArea.getId());
                                ci.setEndCity(endList);
                            }break;
                            case 2:{
                                ci.setCityToId(currentLevelArea.getId());
                                endList.addFirst(currentLevelArea.getId());
                                ci.setEndCity(endList);
                            }break;
                            case 3:{
                                ci.setCountyToId(currentLevelArea.getId());
                                endList.addFirst(currentLevelArea.getId());
                                ci.setEndCity(endList);
                            }break;
                        }

                        currentLevelArea = currentLevelArea.getParent();
                    }
                    ci.setDestArea(ss.toString());



                    String name = userInformationRepository.findUserClassification("PackagingStatus", cargoInformation.getPackagingStatus());
                    ci.setPackagingStatusName(name);
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
                        Double shippingCosts= cargoInformation.getFeeSchedule().getShippingCosts();
                        ci.setShippingCosts(shippingCosts);

                        Double  deliveryFee= cargoInformation.getFeeSchedule().getDeliveryFee();
                        ci.setDeliveryFee(deliveryFee);

                        Double deliveryCharges= cargoInformation.getFeeSchedule().getDeliveryCharges();
                        ci.setDeliveryCharges(deliveryCharges);

                        Double  premium= cargoInformation.getFeeSchedule().getPremium();
                        ci.setPremium(premium);

                        Double packagingFee=  cargoInformation.getFeeSchedule().getPackagingFee();
                        ci.setPackagingFee(packagingFee);

                        Double otherFee= cargoInformation.getFeeSchedule().getOtherFee();
                        ci.setOtherFee(otherFee);
                        ci.setPaymentMethod(cargoInformation.getFeeSchedule().getPaymentMethod());
                        //总运费
                        ci.setFotalFee(shippingCosts+deliveryFee+deliveryCharges+premium+packagingFee+otherFee);
                        String name3 = userInformationRepository.findUserClassification("PaymentMethod", cargoInformation.getFeeSchedule().getPaymentMethod());
                        ci.setPaymentMethodName(name3);
                    }

                }
                return ci;
            }
        });
    }
    //////////////////////////////////////////////////////////////////////////////



    @Override
    public Page<CargoInformationDTO> selectSmOrgCargoInformation(CargoInformationDTO cargoInformationDTO, Pageable pageable) {

        Page<CargoInformation> page=cargoInformationRepository.findAll(new Specification<CargoInformation>() {
            @Override
            public Predicate toPredicate(Root<CargoInformation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();

                /**
                Subquery subquery = criteriaQuery.subquery(Long.class);
                Root<SmOrg> outsourcingGoodsRoot = subquery.from(SmOrg.class);
                subquery.select(outsourcingGoodsRoot.get("id"));
                subquery.where(criteriaBuilder.like(outsourcingGoodsRoot.get("relationship"),"%"+cargoInformationDTO.getSmOrgId()+"%"));
                */
                LinkedList<Long> orgids = new LinkedList<>();
                LinkedList<Long> allids = new LinkedList<>();
                orgids.add(cargoInformationDTO.getSmOrgId());
                while(orgids.size()>0){
                    Long currentId = orgids.removeFirst();
                    allids.add(currentId);
                    List<SmOrg> orgs =  smOrgManagementRepository.findByPId(currentId);
                    if(orgs!=null)
                        orgs = orgs.stream().filter(a->a.getStatus()!=null && a.getStatus().trim().equals("1")).collect(Collectors.toList());
                    else
                        orgs = new ArrayList<>();
                    List<Long> subid = orgs.stream().map(SmOrg::getId).collect(Collectors.toList());
                    orgids.addAll(subid);
                }

                predicateList.add(root.get("smOrgId").in(allids));
                //货物名称
                if (StringUtils.isNotBlank(cargoInformationDTO.getCommodityName())){
                    predicateList.add(criteriaBuilder.like(root.get("commodityName"),"%"+cargoInformationDTO.getCommodityName()+"%"));
                }

                //查询状态为 1 的货物信息
                predicateList.add(criteriaBuilder.equal(root.get("status"),"1"));
                //查询新建订单
                predicateList.add(root.get("shippingStatus").in(Arrays.asList(ShippingStatus.NEW,ShippingStatus.SECTION_START)));

                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));

            }
        },pageable);
        return smOrgOneDto(page);
    }




    private Page<CargoInformationDTO> smOrgOneDto(Page<CargoInformation> page) {
        return  page.map(new Converter<CargoInformation, CargoInformationDTO>() {
            @Override
            public CargoInformationDTO convert(CargoInformation cargoInformation) {

                CargoInformationDTO ci=new CargoInformationDTO();

                BeanUtils.copyProperties(cargoInformation,ci);

                if (cargoInformation !=null) {
                    if (cargoInformation.getOriginArea() !=null){
                        ci.setOriginAreaCode(cargoInformation.getOriginArea().getOutCode());
                    }
                    if (cargoInformation.getDestArea() !=null){
                        ci.setDestAreaCode(cargoInformation.getDestArea().getOutCode());
                    }


                    //收货日期
                    if ( cargoInformation.getReceiptDate() !=null){
                        FormatedDate logIn = new FormatedDate(cargoInformation.getReceiptDate());
                        String receiptDate = logIn.getFormat("yyyy-MM-dd HH:mm:ss");
                        ci.setReceiptDate(receiptDate);
                    }

                    //货物运输状态的value值
                    ci.setShippingStatusValue(cargoInformation.getShippingStatus().getName());

                    if (cargoInformation.getServiceMethodType() !=null) {
                        ci.setServiceMethodTypeValue(cargoInformation.getServiceMethodType().getName());
                    }


                    HbzArea currentLevelOriginArea= cargoInformation.getOriginArea();
                    if (currentLevelOriginArea !=null) {
                        LinkedList<Long> longList = new LinkedList<>();
                        StringBuilder sb = new StringBuilder();
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
                    if (currentLevelArea !=null) {
                        LinkedList<Long> endList = new LinkedList<>();
                        StringBuilder ss = new StringBuilder();
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
                    String name1 = userInformationRepository.findUserClassification("BillingUser", cargoInformation.getBillingUser());
                    ci.setBillingUserName(name1);

                    String packageUnitValue = userInformationRepository.findUserClassification("PackageUnit", cargoInformation.getPackageUnit());
                    ci.setPackageUnitValue(packageUnitValue);


                    ///////////
                    // 托运用户信息
                    if (cargoInformation.getShipperUser() != null) {
                        ci.setShipperUserId(cargoInformation.getShipperUser().getId());
                        ci.setShipperUserCompanyName(cargoInformation.getShipperUser().getShipperUserCompanyName());
                        ci.setShipperUserName(cargoInformation.getShipperUser().getShipperUserName());
                        ci.setShipperUserTelephone(cargoInformation.getShipperUser().getShipperUserTelephone());
                        ci.setShipperUserAddress(cargoInformation.getShipperUser().getShipperUserAddress());
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
                        Double shippingCosts= cargoInformation.getFeeSchedule().getShippingCosts();
                        ci.setShippingCosts(shippingCosts);

                        Double  deliveryFee= cargoInformation.getFeeSchedule().getDeliveryFee();
                        ci.setDeliveryFee(deliveryFee);

                        Double deliveryCharges= cargoInformation.getFeeSchedule().getDeliveryCharges();
                        ci.setDeliveryCharges(deliveryCharges);

                        Double  premium= cargoInformation.getFeeSchedule().getPremium();
                        ci.setPremium(premium);

                        Double packagingFee=  cargoInformation.getFeeSchedule().getPackagingFee();
                        ci.setPackagingFee(packagingFee);

                        Double otherFee= cargoInformation.getFeeSchedule().getOtherFee();
                        ci.setOtherFee(otherFee);
                        ci.setPaymentMethod(cargoInformation.getFeeSchedule().getPaymentMethod());
                        //总运费
                        ci.setFotalFee(shippingCosts+deliveryFee+deliveryCharges+premium+packagingFee+otherFee);
                        String name3 = userInformationRepository.findUserClassification("PaymentMethod", cargoInformation.getFeeSchedule().getPaymentMethod());
                        ci.setPaymentMethodName(name3);
                    }

                }
                return ci;
            }
        });
    }






}
