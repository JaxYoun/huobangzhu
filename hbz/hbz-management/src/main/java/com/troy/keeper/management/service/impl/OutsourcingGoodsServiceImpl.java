package com.troy.keeper.management.service.impl;

import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.hbz.po.*;
import com.troy.keeper.hbz.sys.FormatedDate;
import com.troy.keeper.hbz.type.ShippingStatus;
import com.troy.keeper.management.dto.CargoInformationDTO;
import com.troy.keeper.management.dto.OutsourcingDetailsDTO;
import com.troy.keeper.management.dto.OutsourcingGoodsDTO;
import com.troy.keeper.management.dto.UserInformationDTO;
import com.troy.keeper.management.repository.*;
import com.troy.keeper.management.service.OutsourcingGoodsService;
import com.troy.keeper.monomer.security.domain.User;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 李奥
 * @date 2018/1/15.
 */

@Service
@Transactional
public class OutsourcingGoodsServiceImpl implements OutsourcingGoodsService {

    @Autowired
    private OutsourcingGoodsRepository outsourcingGoodsRepository;

    @Autowired
    private OutsourcingDetailsRepository outsourcingDetailsRepository;

    @Autowired
    private UserInformationRepository userInformationRepository;

    @Autowired
    private HbzAreasRepository hbzAreasRepository;

    @Autowired
    private HbzAreasRepository hbzAreaRepository;

    @Autowired
    private CargoInformationRepository cargoInformationRepository;

    @Autowired
    private SmOrgRepository smOrgRepository;


    @Override
    public Page<UserInformationDTO> findByCondition(UserInformationDTO userInformationDTO, Pageable pageable) {


        Page<UserInformation> page = userInformationRepository.findAll(new Specification<UserInformation>() {
            @Override
            public Predicate toPredicate(Root<UserInformation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();

                Subquery subquery = criteriaQuery.subquery(Long.class);
                Root<OutsourcingGoods> outsourcingGoodsRoot = subquery.from(OutsourcingGoods.class);
                subquery.select(outsourcingGoodsRoot.join("userInformation").get("id"));
                subquery.where(criteriaBuilder.notEqual(outsourcingGoodsRoot.get("shippingStatus"), ShippingStatus.DURING_SHIPPING));

                Subquery allsub = criteriaQuery.subquery(Long.class);
                Root<OutsourcingGoods> outsourcingGoodsRoot2 =allsub.from(OutsourcingGoods.class);
                allsub.select(outsourcingGoodsRoot2.join("userInformation").get("id"));

                predicateList.add(
                        criteriaBuilder.or(
                        root.get("id").in(subquery),
                       criteriaBuilder.not( root.get("id").in(allsub))
                        )

                );
                predicateList.add(criteriaBuilder.equal(root.get("userClassification"), "2"));

                if (StringUtils.isNotBlank(userInformationDTO.getCompanyName())){
                    predicateList.add(criteriaBuilder.like(root.get("companyName"),"%"+userInformationDTO.getCompanyName()+"%"));
                }

                if(StringUtils.isNotBlank(userInformationDTO.getUserTelephone())){
                    predicateList.add(criteriaBuilder.equal(root.get("userTelephone"),userInformationDTO.getUserTelephone()));

                }
                if(StringUtils.isNotBlank(userInformationDTO.getUserAddress())){
                    predicateList.add(criteriaBuilder.like(root.get("userAddress"),"%"+userInformationDTO.getUserAddress()+"%"));

                }
                if(StringUtils.isNotBlank(userInformationDTO.getJianpin())){
                    predicateList.add(criteriaBuilder.like(root.get("jianpin"),"%"+userInformationDTO.getJianpin()+"%"));

                }




//                predicateList.add(criteriaBuilder.equal(root.get("userClassification"),"2"));


                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));

            }
        }, pageable);

        return converterDto(page);


    }

    private Page<UserInformationDTO> converterDto(Page<UserInformation> page) {
        return page.map(new Converter<UserInformation, UserInformationDTO>() {
            @Override
            public UserInformationDTO convert(UserInformation userInformation) {

                UserInformationDTO ci = new UserInformationDTO();
                BeanUtils.copyProperties(userInformation, ci);

                return ci;
            }
        });
    }


    //批量保存分包货物信息
    @Override
    public Boolean addBatchOutCommodity(OutsourcingGoodsDTO outsourcingGoodsDTO) {

        //发车编码
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        String str = sdf.format(d);
        String a = str.replace("-", "").replace(" ", "").replace(":", "");

        OutsourcingGoods oc = new OutsourcingGoods();
        BeanUtils.copyProperties(outsourcingGoodsDTO, oc);

        //添加一个运输的状态
        oc.setShippingStatus(ShippingStatus.NEW);
        oc.setReceiptDate(Long.valueOf(outsourcingGoodsDTO.getReceiptDate()));
        oc.setReceiptToDate(Long.valueOf(outsourcingGoodsDTO.getReceiptToDate()));
        oc.setStartNumber(a);
        oc.setOutNumber(outsourcingGoodsDTO.getOutNumber());
        oc.setStatus("1");

        //增加组织机构id；
        SmUser  smUser=cargoInformationRepository.findSmOrgId(SecurityUtils.getCurrentUserId());
        Long   orgId=0l;
        List<SmPostUser> list1=  smUser.getSmPostUserList();
        for (int i = 0; i <list1.size() ; i++) {
            orgId= list1.get(i).getSmPost().getOrgId();
            //新增的时候 获取登陆用户的机构id
        }
        oc.setSmOrgId(orgId);

        //保存区域
        if (outsourcingGoodsDTO.getOriginAreaCode() != null) {
            HbzArea originArea = hbzAreasRepository.findByOutCode(outsourcingGoodsDTO.getOriginAreaCode());
            oc.setOriginArea(originArea);
        }


        //保存分包商信息
        if (outsourcingGoodsDTO.getUserInformationId() != null) {
            UserInformation ui = userInformationRepository.findOne(outsourcingGoodsDTO.getUserInformationId());
            oc.setUserInformation(ui);
        }

        //保存分包详情
        List<Long> idList=new ArrayList<>();
        if (outsourcingGoodsDTO.getOutsourcingDetailsDTOS() != null) {
            List<OutsourcingDetails> listOcd = new ArrayList<>();
            List<OutsourcingDetailsDTO> listOCDto = outsourcingGoodsDTO.getOutsourcingDetailsDTOS();
            for (int i = 0; i < listOCDto.size(); i++) {
                OutsourcingDetails ocd = new OutsourcingDetails();
                Long  cargoInformationId=listOCDto.get(i).getCargoInformationId();
                idList.add(cargoInformationId);
                CargoInformation ci = cargoInformationRepository.findOne(cargoInformationId);
                BeanUtils.copyProperties(ci, ocd, "id");

                ocd.setCargoInformation(ci);

                if (ci.getFeeSchedule() !=null){
                    ocd.setFotalFee(ci.getFeeSchedule().getFotalFee());
                }
                //设置分包后的物流状态
//                ocd.setShippingStatus(ShippingStatus.OUTSOURCING);
                ocd.setShippingStatus(ci.getShippingStatus());
                listOcd.add(ocd);
            }
            oc.setOutsourcingDetails(listOcd);

        }


        outsourcingGoodsRepository.save(oc);
        //更新收货表中的货物运输状态
        for (int i = 0; i <idList.size() ; i++) {
            cargoInformationRepository.updateShippingStatus(idList.get(i));
        }


        return true;
    }


    //外包发车单分页查询
    @Override
    public Page<OutsourcingGoodsDTO> findByOutsourcingGoods(OutsourcingGoodsDTO outsourcingGoodsDTO, Pageable pageable) {
        //查询当前组织机构的下的 货物信息
        SmUser smUser=cargoInformationRepository.findSmOrgId(SecurityUtils.getCurrentUserId());
        Long orgId = smUser.getOrgId();

        //List<SmPostUser> list1=  smUser.getSmPostUserList();
        //Long a = null;
        //for (int i = 0; i <list1.size() ; i++) {
        //    a = list1.get(i).getSmPost().getOrgId();
        //}
        //Long orgId =a;

        Page<OutsourcingGoods> page = outsourcingGoodsRepository.findAll(new Specification<OutsourcingGoods>() {
            @Override
            public Predicate toPredicate(Root<OutsourcingGoods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();
                if (outsourcingGoodsDTO.getSmallTime() != null) {
                    predicateList.add(criteriaBuilder.ge(root.get("receiptDate"), outsourcingGoodsDTO.getSmallTime()));
                }
                if (outsourcingGoodsDTO.getBigTime() != null) {
                    predicateList.add(criteriaBuilder.le(root.get("receiptDate"), outsourcingGoodsDTO.getBigTime()+59999L));
                }
                //发出
                if (StringUtils.isNotBlank(outsourcingGoodsDTO.getOriginAreaCode())){
                    predicateList.add(criteriaBuilder.equal(root.join("originArea").get("outCode"),outsourcingGoodsDTO.getOriginAreaCode()));
                }
//                //省
//                if (outsourcingGoodsDTO.getProvinceId() != null) {
//                    //找到所有的子节点
//                    List<Long> childIds = hbzAreaRepository.findAllIdsByParentId(outsourcingGoodsDTO.getProvinceId());
//                    predicateList.add(root.get("originArea").get("id").in(childIds));
//                }
//                //市
//                if (outsourcingGoodsDTO.getCityId() != null) {
//                    //找到所有的子节点
//                    List<Long> childIds = hbzAreaRepository.findAreaIdsByParentId(outsourcingGoodsDTO.getCityId());
//                    predicateList.add(root.get("originArea").get("id").in(childIds));
//                }
//                //区
//                if (outsourcingGoodsDTO.getCountyId() != null) {
//                    predicateList.add(criteriaBuilder.equal(root.get("originArea").get("id"), outsourcingGoodsDTO.getCountyId()));
//                }
                //到站
                if (StringUtils.isNotBlank(outsourcingGoodsDTO.getDestAreaCode())){
                    predicateList.add(criteriaBuilder.equal(root.join("destArea").get("outCode"),outsourcingGoodsDTO.getDestAreaCode()));
                }
//                //省
//                if (outsourcingGoodsDTO.getProvinceToId() != null) {
//                    //找到所有的子节点
//                    List<Long> childIds = hbzAreaRepository.findAllIdsByParentId(outsourcingGoodsDTO.getProvinceToId());
//                    predicateList.add(root.get("destArea").get("id").in(childIds));
//                }
//                //市
//                if (outsourcingGoodsDTO.getCityToId() != null) {
//                    //找到所有的子节点
//                    List<Long> childIds = hbzAreaRepository.findAreaIdsByParentId(outsourcingGoodsDTO.getCityToId());
//
//                    predicateList.add(root.get("destArea").get("id").in(childIds));
//                }
//                //区
//                if (outsourcingGoodsDTO.getCountyToId() != null) {
//
//                    predicateList.add(criteriaBuilder.equal(root.get("destArea").get("id"), outsourcingGoodsDTO.getCountyToId()));
//                }
                //发车编号
                if (StringUtils.isNotBlank(outsourcingGoodsDTO.getStartNumber())) {
                    predicateList.add(criteriaBuilder.equal(root.get("startNumber"), outsourcingGoodsDTO.getStartNumber()));
                }

                //车牌号
                if (StringUtils.isNotBlank(outsourcingGoodsDTO.getNumberPlate())) {
                    predicateList.add(criteriaBuilder.equal(root.get("numberPlate"), outsourcingGoodsDTO.getNumberPlate()));
                }
                //司机名称
                if (StringUtils.isNotBlank(outsourcingGoodsDTO.getDriverName())) {
                    predicateList.add(criteriaBuilder.like(root.get("driverName"), "%"+outsourcingGoodsDTO.getDriverName()+"%"));
                }
                //司机电话
                if (StringUtils.isNotBlank(outsourcingGoodsDTO.getDriverTelephone())) {
                    predicateList.add(criteriaBuilder.equal(root.get("driverTelephone"), outsourcingGoodsDTO.getDriverTelephone()));
                }

                //当前登录人 只能查看自己站点的 货物
                predicateList.add(criteriaBuilder.equal(root.get("smOrgId"),orgId));

                List<Long> list = getSmOrgList(orgId);
                if(list.size() > 0){
                    predicateList.add(root.get("smOrgId").in(list));
                }

                //查询状态为 1 的货物信息
                predicateList.add(criteriaBuilder.equal(root.get("status"),"1"));

                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));

            }
        }, pageable);

        return converterOutsourcingGoodsDto(page);
    }


    //递归出机构中本机构及所有的子机构
    private List<Long> getSmOrgList(Long orgId) {
        List<Long> ids = new ArrayList();
        try {
            List<SmOrg> list = smOrgRepository.getListByPId(orgId);
            if (null != list && list.size()>0) {
                for (int i = 0; i < list.size(); i++) {
                    SmOrg smOrg = list.get(i);
                    ids.addAll(getSmOrgList(smOrg.getId()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ids.add(orgId);
        return ids;
    }


    private Page<OutsourcingGoodsDTO> converterOutsourcingGoodsDto(Page<OutsourcingGoods> page) {
        return page.map(new Converter<OutsourcingGoods, OutsourcingGoodsDTO>() {
            @Override
            public OutsourcingGoodsDTO convert(OutsourcingGoods outsourcingGoods) {

                OutsourcingGoodsDTO oc = new OutsourcingGoodsDTO();
                BeanUtils.copyProperties(outsourcingGoods, oc);

                if (outsourcingGoods.getShippingStatus() !=null){
                    oc.setShippingStatusValue(outsourcingGoods.getShippingStatus().getName());
                }

                if (outsourcingGoods.getOriginArea() !=null){
                    oc.setOriginAreaCode(outsourcingGoods.getOriginArea().getOutCode());
                }
//                if (outsourcingGoods.getDestArea() !=null){
//                    oc.setDestAreaCode(outsourcingGoods.getDestArea().getOutCode());
//                }
                //时间转换
                if (outsourcingGoods.getReceiptDate() != null) {
                    FormatedDate logIn = new FormatedDate(outsourcingGoods.getReceiptDate());
                    String receiptDate = logIn.getFormat("yyyy-MM-dd HH:mm");
                    oc.setReceiptDate(receiptDate);
                }
                if (outsourcingGoods.getReceiptToDate() != null) {
                    FormatedDate logIn2 = new FormatedDate(outsourcingGoods.getReceiptToDate());
                    String receiptDate = logIn2.getFormat("yyyy-MM-dd HH:mm");
                    oc.setReceiptToDate(receiptDate);
                }
                //取货地址
                HbzArea currentLevelOriginArea= outsourcingGoods.getOriginArea();
                LinkedList<Long> longList=new LinkedList<>();
                StringBuilder sb = new StringBuilder();
                if (currentLevelOriginArea !=null) {
                    while (currentLevelOriginArea.getLevel() > 0) {
                        sb.insert(0, currentLevelOriginArea.getAreaName() + " ");
                        switch (currentLevelOriginArea.getLevel().intValue()) {
                            case 1: {
                                oc.setProvinceId(currentLevelOriginArea.getId());
                                longList.addFirst(currentLevelOriginArea.getId());
                                oc.setStartCity(longList);
                            }
                            break;
                            case 2: {
                                oc.setCityId(currentLevelOriginArea.getId());
                                longList.addFirst(currentLevelOriginArea.getId());
                                oc.setStartCity(longList);
                            }
                            break;
                            case 3: {
                                oc.setCountyId(currentLevelOriginArea.getId());
                                longList.addFirst(currentLevelOriginArea.getId());
                                oc.setStartCity(longList);
                            }
                            break;
                        }

                        currentLevelOriginArea = currentLevelOriginArea.getParent();
                    }
                    oc.setOriginArea(sb.toString());
                }


                //用户的基本信息
                if (outsourcingGoods.getUserInformation() != null) {
                    UserInformation ui = outsourcingGoods.getUserInformation();
                    UserInformationDTO userInformationDTO = new UserInformationDTO();
                    BeanUtils.copyProperties(ui, userInformationDTO);
                    oc.setUserInformationDTO(userInformationDTO);
                }


                return oc;
            }
        });
    }


    //点击编辑
    @Override
    public OutsourcingGoodsDTO selectUpdateOutsourcingGoods(OutsourcingGoodsDTO outsourcingGoodsDTO) {

//        if (outsourcingGoodsDTO.getId() !=null){
        OutsourcingGoodsDTO outsourcingGoodsDTO1 = new OutsourcingGoodsDTO();

        OutsourcingGoods outsourcingGoods = outsourcingGoodsRepository.findOne(outsourcingGoodsDTO.getId());
        BeanUtils.copyProperties(outsourcingGoods, outsourcingGoodsDTO1);

        //运输状态
        if (outsourcingGoods.getShippingStatus() !=null){
            outsourcingGoodsDTO1.setShippingStatusValue(outsourcingGoods.getShippingStatus().getName());
        }

        if (outsourcingGoods.getUserInformation() !=null){
            outsourcingGoodsDTO1.setUserInformationId(outsourcingGoods.getUserInformation().getId());
        }

        if (outsourcingGoods.getOriginArea() !=null){
            outsourcingGoodsDTO1.setOriginAreaCode(outsourcingGoods.getOriginArea().getOutCode());
        }

        //时间转换
        if (outsourcingGoods.getReceiptDate() != null) {
            FormatedDate logIn = new FormatedDate(outsourcingGoods.getReceiptDate());
            String receiptDate = logIn.getFormat("yyyy-MM-dd HH:mm:ss");
            outsourcingGoodsDTO1.setReceiptDate(receiptDate);
        }
        if (outsourcingGoods.getReceiptToDate() != null) {
            FormatedDate logIn2 = new FormatedDate(outsourcingGoods.getReceiptToDate());
            String receiptDate = logIn2.getFormat("yyyy-MM-dd HH:mm:ss");
            outsourcingGoodsDTO1.setReceiptToDate(receiptDate);
        }
        //取货地址
        HbzArea currentLevelOriginArea= outsourcingGoods.getOriginArea();
        LinkedList<Long> longList=new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        if (currentLevelOriginArea !=null) {
            while (currentLevelOriginArea.getLevel() > 0) {
                sb.insert(0, currentLevelOriginArea.getAreaName() + " ");
                switch (currentLevelOriginArea.getLevel().intValue()) {
                    case 1: {
                        outsourcingGoodsDTO1.setProvinceId(currentLevelOriginArea.getId());
                        longList.addFirst(currentLevelOriginArea.getId());
                        outsourcingGoodsDTO1.setStartCity(longList);
                    }
                    break;
                    case 2: {
                        outsourcingGoodsDTO1.setCityId(currentLevelOriginArea.getId());
                        longList.addFirst(currentLevelOriginArea.getId());
                        outsourcingGoodsDTO1.setStartCity(longList);
                    }
                    break;
                    case 3: {
                        outsourcingGoodsDTO1.setCountyId(currentLevelOriginArea.getId());
                        longList.addFirst(currentLevelOriginArea.getId());
                        outsourcingGoodsDTO1.setStartCity(longList);
                    }
                    break;
                }

                currentLevelOriginArea = currentLevelOriginArea.getParent();
            }
            outsourcingGoodsDTO1.setOriginArea(sb.toString());
        }


        //货物信息
        if (outsourcingGoods.getOutsourcingDetails().size() > 0) {
            List<OutsourcingDetails> listOd = outsourcingGoods.getOutsourcingDetails();
            List<OutsourcingDetailsDTO> listDTO = new ArrayList<>();
            for (int i = 0; i < listOd.size(); i++) {
                OutsourcingDetailsDTO odDTO = new OutsourcingDetailsDTO();
                OutsourcingDetails ods = listOd.get(i);
                BeanUtils.copyProperties(ods, odDTO);
                if (ods.getReceiverUser() !=null){
                    odDTO.setReceiverUserName(ods.getReceiverUser().getReceiverUserName());
                    odDTO.setReceiverUserTelephone(ods.getReceiverUser().getReceiverUserTelephone());
                }
                if (ods.getFeeSchedule() !=null){
                    odDTO.setPaymentMethod(ods.getFeeSchedule().getPaymentMethod());
                    String methodName = userInformationRepository.findUserClassification("PaymentMethod", ods.getFeeSchedule().getPaymentMethod());
                    odDTO.setPaymentMethodName(methodName);

                }



                if (ods.getCargoInformation() !=null){
                    odDTO.setCargoInformationId(ods.getCargoInformation().getId());
                }
                if (ods.getOriginArea() !=null){
                    odDTO.setOriginAreaCode(ods.getOriginArea().getOutCode());
                }
                if (ods.getDestArea() !=null){
                    odDTO.setDestAreaCode(ods.getDestArea().getOutCode());
                }

                //时间转换
                if (ods.getReceiptDate() != null) {
                    FormatedDate logIn = new FormatedDate(ods.getReceiptDate());
                    String receiptDate = logIn.getFormat("yyyy-MM-dd HH:mm:ss");
                    odDTO.setReceiptDate(receiptDate);
                }

                //取货地址
                HbzArea currentOriginArea= ods.getOriginArea();
                LinkedList<Long> longList2=new LinkedList<>();
                StringBuilder sb2 = new StringBuilder();
                if (currentOriginArea !=null) {
                    while (currentOriginArea.getLevel() > 0) {
                        sb2.insert(0, currentOriginArea.getAreaName() + " ");
                        switch (currentOriginArea.getLevel().intValue()) {
                            case 1: {
                                odDTO.setProvinceId(currentOriginArea.getId());
                                longList2.addFirst(currentOriginArea.getId());
                                odDTO.setStartCity(longList2);
                            }
                            break;
                            case 2: {
                                odDTO.setCityId(currentOriginArea.getId());
                                longList2.addFirst(currentOriginArea.getId());
                                odDTO.setStartCity(longList2);
                            }
                            break;
                            case 3: {
                                odDTO.setCountyId(currentOriginArea.getId());
                                longList2.addFirst(currentOriginArea.getId());
                                odDTO.setStartCity(longList2);
                            }
                            break;
                        }

                        currentOriginArea = currentOriginArea.getParent();
                    }
                    odDTO.setOriginArea(sb2.toString());
                }

                //送货地址
                if (ods.getDestArea() !=null) {
                    HbzArea currentDestArea = ods.getDestArea();
                    LinkedList<Long> endList2 = new LinkedList<>();
                    StringBuilder ss2 = new StringBuilder();
                    if (currentDestArea !=null) {
                        while (currentDestArea.getLevel() > 0) {
                            ss2.insert(0, currentDestArea.getAreaName() + " ");
                            switch (currentDestArea.getLevel().intValue()) {
                                case 1: {
                                    odDTO.setProvinceToId(currentDestArea.getId());
                                    endList2.addFirst(currentDestArea.getId());
                                    odDTO.setEndCity(endList2);
                                }
                                break;
                                case 2: {
                                    odDTO.setCityToId(currentDestArea.getId());
                                    endList2.addFirst(currentDestArea.getId());
                                    odDTO.setEndCity(endList2);
                                }
                                break;
                                case 3: {
                                    odDTO.setCountyToId(currentDestArea.getId());
                                    endList2.addFirst(currentDestArea.getId());
                                    odDTO.setEndCity(endList2);
                                }
                                break;
                            }

                            currentDestArea = currentDestArea.getParent();
                        }
                        odDTO.setDestArea(ss2.toString());
                    }
                }
//
                //包装状态
                String  name= userInformationRepository.findUserClassification("PackagingStatus",ods.getPackagingStatus());
                odDTO.setPackagingStatusName(name);
                //包装单位
                String  name2= userInformationRepository.findUserClassification("PackageUnit",ods.getPackageUnit());
                odDTO.setPackageUnitValue(name2);

                //开票员
                String name1 = userInformationRepository.findUserClassification("BillingUser", ods.getBillingUser());
                odDTO.setBillingUserName(name1);
                if (ods.getServiceMethodType() !=null){
                    odDTO.setServiceMethodTypeValue(ods.getServiceMethodType().getName());
                }

                //运输状态
                if (ods.getShippingStatus() !=null){
                    odDTO.setShippingStatusValue(ods.getShippingStatus().getName());
                }


                listDTO.add(odDTO);
            }
            outsourcingGoodsDTO1.setOutsourcingDetailsDTOS(listDTO);

        }


//        }
        return outsourcingGoodsDTO1;

    }

    //添加新建状态下  编辑初始分包用户信息 保存功能
    public  Boolean addNewBatchOutCommodity(OutsourcingGoodsDTO outsourcingGoodsDTO){
        //想查询
        OutsourcingGoods  outsourcingGoods=  outsourcingGoodsRepository.findOne(outsourcingGoodsDTO.getId());
        //删除原有的货物
        List<OutsourcingDetails>  outsourcingDetailsList= outsourcingGoods.getOutsourcingDetails();
        for (int i = 0; i < outsourcingDetailsList.size(); i++) {
            //编辑之前先把货物更新为原来的 运输状态
          Long   cifId=  outsourcingDetailsList.get(i).getCargoInformation().getId();
          ShippingStatus str=  outsourcingDetailsList.get(i).getShippingStatus();
            cargoInformationRepository.returnShippingStatus(cifId,str);
            //删除旧数据
          Long detailsId=  outsourcingDetailsList.get(i).getId();
            outsourcingDetailsRepository.delete(detailsId);
        }

//        BeanUtils.copyProperties(outsourcingGoodsDTO,outsourcingGoods);
        //单位名称
        outsourcingGoods.setCompanyName(outsourcingGoodsDTO.getCompanyName());
        //司机姓名
        outsourcingGoods.setDriverName(outsourcingGoodsDTO.getDriverName());
        outsourcingGoods.setDriverTelephone(outsourcingGoodsDTO.getDriverTelephone());
        outsourcingGoods.setNumberPlate(outsourcingGoodsDTO.getNumberPlate());
        outsourcingGoods.setOutNumber(outsourcingGoodsDTO.getOutNumber());
        outsourcingGoods.setStartNumber(outsourcingGoods.getStartNumber());
        if (outsourcingGoodsDTO.getReceiptDate() !=null){
            outsourcingGoods.setReceiptDate(Long.valueOf(outsourcingGoodsDTO.getReceiptDate()));
        }
        if (outsourcingGoodsDTO.getReceiptToDate() !=null){
            outsourcingGoods.setReceiptToDate(Long.valueOf(outsourcingGoodsDTO.getReceiptToDate()));
        }

        outsourcingGoods.setSmOrgId(outsourcingGoods.getSmOrgId());
        outsourcingGoods.setStatus("1");
        outsourcingGoods.setRemarks(outsourcingGoodsDTO.getRemarks());
        outsourcingGoods.setShippingStatus(ShippingStatus.NEW);
        //保存区域
        if (outsourcingGoodsDTO.getOriginAreaCode() != null) {
            HbzArea originArea = hbzAreasRepository.findByOutCode(outsourcingGoodsDTO.getOriginAreaCode());
            outsourcingGoods.setOriginArea(originArea);
        }
        //保存分包商信息
        if (outsourcingGoodsDTO.getUserInformationId() != null) {
            UserInformation ui = userInformationRepository.findOne(outsourcingGoodsDTO.getUserInformationId());
            outsourcingGoods.setUserInformation(ui);
        }
        outsourcingGoods.setReceiptDate(Long.valueOf(outsourcingGoodsDTO.getReceiptDate()));
        outsourcingGoods.setReceiptToDate(Long.valueOf(outsourcingGoodsDTO.getReceiptToDate()));


        //保存分包详情
        List<Long> idList=new ArrayList<>();
        if (outsourcingGoodsDTO.getOutsourcingDetailsDTOS() != null) {
            List<OutsourcingDetails> listOcd = new ArrayList<>();
            List<OutsourcingDetailsDTO> listOCDto = outsourcingGoodsDTO.getOutsourcingDetailsDTOS();
            for (int i = 0; i < listOCDto.size(); i++) {
                OutsourcingDetails ocd = new OutsourcingDetails();
                Long cargoInformationId= listOCDto.get(i).getCargoInformationId();
                idList.add(cargoInformationId);
                CargoInformation ci = cargoInformationRepository.findOne(cargoInformationId);
                BeanUtils.copyProperties(ci, ocd, "id");
                ocd.setCargoInformation(ci);
                if (ci.getFeeSchedule() !=null){
                    ocd.setFotalFee(ci.getFeeSchedule().getFotalFee());
                }
                //设置分包后的物流状态
//                ocd.setShippingStatus(ShippingStatus.OUTSOURCING);
                ocd.setShippingStatus(ci.getShippingStatus());
                listOcd.add(ocd);
            }
            outsourcingGoods.setOutsourcingDetails(listOcd);

        }

        outsourcingGoodsRepository.save(outsourcingGoods);

        //更新收货表中的货物运输状态
        for (int i = 0; i <idList.size() ; i++) {
            cargoInformationRepository.updateShippingStatus(idList.get(i));
        }


        return true;
    }









    //点击编辑备注
    public Boolean addRemarks(OutsourcingGoodsDTO outsourcingGoodsDTO){

        if (outsourcingGoodsDTO.getId() !=null){
            outsourcingGoodsRepository.saveRemarks(outsourcingGoodsDTO.getId(),outsourcingGoodsDTO.getRemarks());
            return  true;
        }else {
            return  false;
        }
    }




    //点击发车确认
    public  Boolean saveStartCarIsTrue(OutsourcingGoodsDTO outsourcingGoodsDTO){

        if (outsourcingGoodsDTO.getId() !=null){
            outsourcingGoodsRepository.uopdateShippingStatus(outsourcingGoodsDTO.getId());
            return  true;
        }else {
            return false;
        }


    }

    //点击收货确认
    public Boolean saveConfirmationIsTrue(OutsourcingGoodsDTO outsourcingGoodsDTO){
        //分包用户的id
        if (outsourcingGoodsDTO.getId() !=null){
            OutsourcingGoods outsourcingGoods=  outsourcingGoodsRepository.findOne(outsourcingGoodsDTO.getId());
            //分包货物信息
            List<OutsourcingDetails> outsourcingDetailsList= outsourcingGoods.getOutsourcingDetails();
            for (int i = 0; i < outsourcingDetailsList.size(); i++) {
             Long  DetailsId=  outsourcingDetailsList.get(i).getId();
                outsourcingDetailsRepository.outsourcingDetailsShippingStatus(DetailsId);
            }
            outsourcingGoodsRepository.outShippingStatus(outsourcingGoodsDTO.getId());
            return true;
        }else {
            return  false;
        }


    }


    //外包发车单导出
    public List<OutsourcingGoodsDTO> outsourcingGoodsExport(OutsourcingGoodsDTO outsourcingGoodsDTO){
        List<OutsourcingGoods> page = outsourcingGoodsRepository.findAll(new Specification<OutsourcingGoods>() {
            @Override
            public Predicate toPredicate(Root<OutsourcingGoods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();
                if (outsourcingGoodsDTO.getSmallTime() != null) {
                    predicateList.add(criteriaBuilder.ge(root.get("receiptDate"), outsourcingGoodsDTO.getSmallTime()));
                }
                if (outsourcingGoodsDTO.getBigTime() != null) {
                    predicateList.add(criteriaBuilder.le(root.get("receiptDate"), outsourcingGoodsDTO.getBigTime()+59999L));
                }
                //发出
                if (StringUtils.isNotBlank(outsourcingGoodsDTO.getOriginAreaCode())){
                    predicateList.add(criteriaBuilder.equal(root.join("originArea").get("outCode"),outsourcingGoodsDTO.getOriginAreaCode()));
                }
                //到站
                if (StringUtils.isNotBlank(outsourcingGoodsDTO.getDestAreaCode())){
                    predicateList.add(criteriaBuilder.equal(root.join("destArea").get("outCode"),outsourcingGoodsDTO.getDestAreaCode()));
                }
                //发车编号
                if (StringUtils.isNotBlank(outsourcingGoodsDTO.getStartNumber())) {
                    predicateList.add(criteriaBuilder.equal(root.get("startNumber"), outsourcingGoodsDTO.getStartNumber()));
                }

                //车牌号
                if (StringUtils.isNotBlank(outsourcingGoodsDTO.getNumberPlate())) {
                    predicateList.add(criteriaBuilder.equal(root.get("numberPlate"), outsourcingGoodsDTO.getNumberPlate()));
                }
                //司机名称
                if (StringUtils.isNotBlank(outsourcingGoodsDTO.getDriverName())) {
                    predicateList.add(criteriaBuilder.like(root.get("driverName"), "%"+outsourcingGoodsDTO.getDriverName()+"%"));
                }
                //司机电话
                if (StringUtils.isNotBlank(outsourcingGoodsDTO.getDriverTelephone())) {
                    predicateList.add(criteriaBuilder.equal(root.get("driverTelephone"), outsourcingGoodsDTO.getDriverTelephone()));
                }

                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));

            }
        });

        return exprotDto(page);
    }


    private List<OutsourcingGoodsDTO> exprotDto(List<OutsourcingGoods> page) {
        List<OutsourcingGoodsDTO> list=new ArrayList<>();
        for (int i = 0; i <page.size() ; i++) {
            OutsourcingGoodsDTO outDTO=new OutsourcingGoodsDTO();
            OutsourcingGoods out=  page.get(i);
            BeanUtils.copyProperties(out,outDTO);

            //发站城市
            HbzArea currentOriginArea= out.getOriginArea();
            StringBuilder sb2 = new StringBuilder();
            if (currentOriginArea !=null) {
                while (currentOriginArea.getLevel() > 0) {

                    sb2.insert(0, currentOriginArea.getAreaName() + " ");
                    currentOriginArea = currentOriginArea.getParent();
                }
                outDTO.setOriginArea(sb2.toString());
            }

            //时间转换
            if (out.getReceiptDate() != null) {
                FormatedDate logIn = new FormatedDate(out.getReceiptDate());
                String receiptDate = logIn.getFormat("yyyy-MM-dd HH:mm");
                outDTO.setReceiptDate(receiptDate);
            }
            if (out.getReceiptToDate() != null) {
                FormatedDate logIn2 = new FormatedDate(out.getReceiptToDate());
                String receiptDate = logIn2.getFormat("yyyy-MM-dd HH:mm");
                outDTO.setReceiptToDate(receiptDate);
            }

            if (out.getShippingStatus() !=null){
                outDTO.setShippingStatusValue(out.getShippingStatus().getName());
            }

            list.add(outDTO);
        }

        return list;
    }

    //删除外包发车单
    public Boolean deleteOutInformation(OutsourcingGoodsDTO outsourcingGoodsDTO){
        if (outsourcingGoodsDTO.getId() !=null){
            OutsourcingGoods og=outsourcingGoodsRepository.findOne(outsourcingGoodsDTO.getId());
           List<OutsourcingDetails> ogs= og.getOutsourcingDetails();
           if (ogs !=null){
               //当外包发车里面的货物不为空时，需要更新收货表中的货物运输状态
               for (int i = 0; i <ogs.size() ; i++) {
                   OutsourcingDetails outsourcingDetails=ogs.get(i);
                   Long outDetailsId=outsourcingDetails.getId();
                   if (outsourcingDetails.getCargoInformation()!=null) {
                       Long cifId = outsourcingDetails.getCargoInformation().getId();
                     Integer   amount=  outsourcingDetails.getCargoInformation().getAmount();
                     Integer   inventoryQuantity=  outsourcingDetails.getCargoInformation().getInventoryQuantity();
                     ShippingStatus shippingStatus=ShippingStatus.NEW;
                       if (amount==inventoryQuantity){
                           shippingStatus=ShippingStatus.NEW;
                       }else if (amount>inventoryQuantity){
                           shippingStatus=ShippingStatus.SECTION_START;
                       }
                       cargoInformationRepository.updateOutShippingStatus(cifId,shippingStatus);
                   }
                   outsourcingDetailsRepository.delete(outDetailsId);

               }
               outsourcingGoodsRepository.delete(outsourcingGoodsDTO.getId());


           }else {
               //当外包车里面没有货物的话 直接删除该外包发车单
               outsourcingGoodsRepository.delete(outsourcingGoodsDTO.getId());
           }
           return true;
        }
        return  false;
    }




}
