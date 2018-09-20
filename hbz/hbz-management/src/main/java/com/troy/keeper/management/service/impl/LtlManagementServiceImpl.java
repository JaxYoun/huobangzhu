package com.troy.keeper.management.service.impl;

import com.troy.keeper.hbz.dto.*;
import com.troy.keeper.hbz.dto.HbzTakerInfoDTO;
import com.troy.keeper.hbz.po.*;
import com.troy.keeper.hbz.service.mapper.*;
import com.troy.keeper.hbz.sys.FormatedDate;
import com.troy.keeper.hbz.type.PayProgress;
import com.troy.keeper.management.dto.*;
import com.troy.keeper.management.repository.DedicatedManagementRepository;
import com.troy.keeper.management.repository.HbzAreasRepository;
import com.troy.keeper.management.repository.LtlManagementRepository;
import com.troy.keeper.management.service.DedicatedManagementService;
import com.troy.keeper.management.service.LtlManagementService;
import com.troy.keeper.management.utils.HbzPayChildMapper;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 李奥
 * @date 2018/2/5.
 */
@Service
@Transactional
public class LtlManagementServiceImpl implements LtlManagementService {

    @Autowired
    private LtlManagementRepository ltlManagementRepository;





    //分页查询
    @Override
    public Page<LtlManagementDTO> findByCondition(LtlManagementDTO ltlManagementDTO, Pageable pageable) {



        Page<HbzLtlOrder> page=ltlManagementRepository.findAll(new Specification<HbzLtlOrder>() {
            @Override
            public Predicate toPredicate(Root<HbzLtlOrder> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();

                if (StringUtils.isNotBlank(ltlManagementDTO.getOrderNo()) ){
                    //运单号---》订单编号
                    predicateList.add(criteriaBuilder.like(root.get("orderNo"), "%" + ltlManagementDTO.getOrderNo() + "%"));
                }
                if (StringUtils.isNotBlank(ltlManagementDTO.getEnt())){
                    //订单归属机构
                    predicateList.add(criteriaBuilder.like(root.join("createUser").join("ent").get("organizationName"),"%"+ltlManagementDTO.getEnt()+"%"));
                }
                if (StringUtils.isNotBlank(ltlManagementDTO.getCreateUser())){
                    //订单创建人
                    predicateList.add(criteriaBuilder.like(root.join("createUser").get("nickName"), "%" + ltlManagementDTO.getCreateUser()+ "%"));
                }
                if (StringUtils.isNotBlank(ltlManagementDTO.getCreateUsertelephone())){
                    //订单创建人电话
                    predicateList.add(criteriaBuilder.equal(root.get("createUser").get("telephone"),  ltlManagementDTO.getCreateUsertelephone()));
                }
                if (StringUtils.isNotBlank(ltlManagementDTO.getTakeUser() )){
                    //接单人
                    predicateList.add(criteriaBuilder.like(root.join("takeUser").get("nickName"), "%" + ltlManagementDTO.getTakeUser() + "%"));
                }
                if (StringUtils.isNotBlank(ltlManagementDTO.getTakeUserTelephone())){
                    //接单人电话
                    predicateList.add(criteriaBuilder.equal(root.join("takeUser").get("telephone"),  ltlManagementDTO.getTakeUserTelephone()));
                }
                if (ltlManagementDTO.getOrderTrans() !=null){
                    //订单状态
                    predicateList.add(criteriaBuilder.equal(root.get("orderTrans"),ltlManagementDTO.getOrderTrans()));
                }
                //发出
                //发出
                if (StringUtils.isNotBlank(ltlManagementDTO.getOriginAreaCode())){
                    predicateList.add(criteriaBuilder.equal(root.join("originArea").get("outCode"),ltlManagementDTO.getOriginAreaCode()));
                }
                //到站
                //到站
                if (StringUtils.isNotBlank(ltlManagementDTO.getDestAreaCode())){
                    predicateList.add(criteriaBuilder.equal(root.join("destArea").get("outCode"),ltlManagementDTO.getDestAreaCode()));
                }
                if (ltlManagementDTO.getSettlementType() !=null){

                    //结算方式
                    predicateList.add(criteriaBuilder.equal(root.get("settlementType"),ltlManagementDTO.getSettlementType()));
                }
                //时间范围的查询
                if (ltlManagementDTO.getSmallTime() !=null){

                    predicateList.add(criteriaBuilder.ge(root.get("createdDate"),ltlManagementDTO.getSmallTime()));
                }
                if ( ltlManagementDTO.getBigTime() !=null){
                    predicateList.add(criteriaBuilder.le(root.get("createdDate"),ltlManagementDTO.getBigTime()+59999L));
                }

                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));

            }
        },pageable);
        return converterDto(page);
    }

    private Page<LtlManagementDTO> converterDto(Page<HbzLtlOrder> page) {
        return  page.map(new Converter<HbzLtlOrder, LtlManagementDTO>() {
            @Override
            public LtlManagementDTO convert(HbzLtlOrder  hbzLtlOrder) {
                LtlManagementDTO ltlManagementDTO = new LtlManagementDTO();
                if (hbzLtlOrder !=null) {

                    ltlManagementDTO.setId(hbzLtlOrder.getId());
                    ltlManagementDTO.setCommodityName(hbzLtlOrder.getCommodityName());
                    if (hbzLtlOrder.getOrderTakeStart() !=null){
                        FormatedDate logIn = new FormatedDate(hbzLtlOrder.getOrderTakeStart());
                        String orderTakeStart = logIn.getFormat("yyyy-MM-dd HH:mm");
                        ltlManagementDTO.setOrderTakeStart(orderTakeStart);
                    }

                    ltlManagementDTO.setCommodityDescribe(hbzLtlOrder.getCommodityDescribe());
                    ltlManagementDTO.setCommodityWeight(hbzLtlOrder.getCommodityWeight());
                    ltlManagementDTO.setCommodityVolume(hbzLtlOrder.getCommodityVolume());
                    ltlManagementDTO.setTransType(hbzLtlOrder.getTransType());
                    ltlManagementDTO.setTransLen(hbzLtlOrder.getTransLen());
                    ltlManagementDTO.setRelatedPictures(hbzLtlOrder.getRelatedPictures());
                    if (hbzLtlOrder.getTransType() != null) {
                        ltlManagementDTO.setTransTypeValue(hbzLtlOrder.getTransType().getName());
                    }

                    ltlManagementDTO.setMaxLoad(hbzLtlOrder.getMaxLoad());
                    //指定运输到货时间
                    if (hbzLtlOrder.getDestlimit() != null) {

                        FormatedDate logIn2 = new FormatedDate(hbzLtlOrder.getDestlimit());
                        String destlimit = logIn2.getFormat("yyyy-MM-dd HH:mm");
                        ltlManagementDTO.setDestlimit(destlimit);
                    }
                    //取货地址
                    HbzArea currentLevelOriginArea= hbzLtlOrder.getOriginArea();
                    LinkedList<Long> longList=new LinkedList<>();
                    StringBuilder sb = new StringBuilder();
                    while(currentLevelOriginArea.getLevel() >0 ){
                        sb.insert(0,currentLevelOriginArea.getAreaName() + " " );
                        switch(currentLevelOriginArea.getLevel().intValue()){
                            case 1:{
                                longList.addFirst(currentLevelOriginArea.getId());
                                ltlManagementDTO.setStartCity(longList);
                            }break;
                            case 2:{
                                longList.addFirst(currentLevelOriginArea.getId());
                                ltlManagementDTO.setStartCity(longList);
                            }break;
                            case 3:{
                                longList.addFirst(currentLevelOriginArea.getId());
                                ltlManagementDTO.setStartCity(longList);
                            }break;
                        }

                        currentLevelOriginArea = currentLevelOriginArea.getParent();
                    }
                    ltlManagementDTO.setOriginArea(sb.toString());



                    //具体地址
                    ltlManagementDTO.setOriginAddress(hbzLtlOrder.getOriginAddress());
                    ltlManagementDTO.setLinkMan(hbzLtlOrder.getLinkMan());
                    ltlManagementDTO.setLinkTelephone(hbzLtlOrder.getLinkTelephone());
                    //分类
                    ltlManagementDTO.setCommodityType(hbzLtlOrder.getCommodityType());
                    if (hbzLtlOrder.getCommodityType() !=null){
                        ltlManagementDTO.setCommodityTypeValue(hbzLtlOrder.getCommodityType().getName());
                    }
                    //质量单元
                    ltlManagementDTO.setWeightUnit(hbzLtlOrder.getWeightUnit());
                    if (hbzLtlOrder.getWeightUnit() !=null){
                        ltlManagementDTO.setWeightUnitValue(hbzLtlOrder.getWeightUnit().getName());
                    }
                    //体积单位
                    ltlManagementDTO.setVolumeUnit(hbzLtlOrder.getVolumeUnit());
                    if (hbzLtlOrder.getVolumeUnit() !=null){
                        ltlManagementDTO.setVolumeUnitValue(hbzLtlOrder.getVolumeUnit().getName());
                    }
                    //原始地址
                    ltlManagementDTO.setOriginInfo(hbzLtlOrder.getOriginInfo());
                    //目的地
                    ltlManagementDTO.setDestInfo(hbzLtlOrder.getDestInfo());
                    //到站地址
                    HbzArea currentLevelArea = hbzLtlOrder.getDestArea();
                    LinkedList<Long> endList=new LinkedList<>();
                    StringBuilder ss = new StringBuilder();
                    while(currentLevelArea.getLevel() >0 ){
                        ss.insert(0,currentLevelArea.getAreaName() + " " );
                        switch(currentLevelArea.getLevel().intValue()){
                            case 1:{
                                endList.addFirst(currentLevelArea.getId());
                                ltlManagementDTO.setEndCity(endList);
                            }break;
                            case 2:{
                                endList.addFirst(currentLevelArea.getId());
                                ltlManagementDTO.setEndCity(endList);
                            }break;
                            case 3:{
                                endList.addFirst(currentLevelArea.getId());
                                ltlManagementDTO.setEndCity(endList);
                            }break;
                        }

                        currentLevelArea = currentLevelArea.getParent();
                    }
                    ltlManagementDTO.setDestArea(ss.toString());


                    //具体地址
                    ltlManagementDTO.setDestAddress(hbzLtlOrder.getDestAddress());
                    ltlManagementDTO.setDestLinker(hbzLtlOrder.getDestLinker());
                    ltlManagementDTO.setDestTelephone(hbzLtlOrder.getDestTelephone());
                    ltlManagementDTO.setLinkRemark(hbzLtlOrder.getLinkRemark());
                    ltlManagementDTO.setSettlementType(hbzLtlOrder.getSettlementType());
                    if (hbzLtlOrder.getSettlementType() != null) {
                        ltlManagementDTO.setSettlementTypeValue(hbzLtlOrder.getSettlementType().getName());
                    }

                    ltlManagementDTO.setAmount(hbzLtlOrder.getAmount());
                    //单价
                    ltlManagementDTO.setUnitPrice(hbzLtlOrder.getUnitPrice());
//                    //创建人
                    if (hbzLtlOrder.getCreateUser() != null) {
                        ltlManagementDTO.setCreateUser(hbzLtlOrder.getCreateUser().getNickName());
                    }
                    //联系电话
                    if (hbzLtlOrder.getCreateUser() != null) {
                        ltlManagementDTO.setCreateUsertelephone(hbzLtlOrder.getCreateUser().getTelephone());
                    }
                    //所属公司
                    if (hbzLtlOrder.getCreateUser() != null && hbzLtlOrder.getCreateUser().getEnt() != null) {
                        ltlManagementDTO.setEnt(hbzLtlOrder.getCreateUser().getEnt().getOrganizationName());
                    }else {
                        ltlManagementDTO.setEnt("- -");
                    }

                    ltlManagementDTO.setOrderNo(hbzLtlOrder.getOrderNo());
                    //订单创建时间
                    FormatedDate logInCreatedDate = new FormatedDate(hbzLtlOrder.getCreatedDate());
                    String createdDate = logInCreatedDate.getFormat("yyyy-MM-dd HH:mm");
                    ltlManagementDTO.setCreateUserTime(createdDate);

                    ltlManagementDTO.setOrderTrans(hbzLtlOrder.getOrderTrans());
                    if (hbzLtlOrder.getOrderTrans() != null) {
                        ltlManagementDTO.setOrderTransValue(hbzLtlOrder.getOrderTrans().getName());
                    }
                    ltlManagementDTO.setOrderType(hbzLtlOrder.getOrderType());
                    if (hbzLtlOrder.getOrderType() != null) {
                        ltlManagementDTO.setOrderTypeValue(hbzLtlOrder.getOrderType().getName());
                    }

                    //接单人
                    if (hbzLtlOrder.getTakeUser() != null) {

                        ltlManagementDTO.setTakeUser(hbzLtlOrder.getTakeUser().getNickName());
                    }
                    if (hbzLtlOrder.getTakeUser() != null) {

                        ltlManagementDTO.setTakeUserTelephone(hbzLtlOrder.getTakeUser().getTelephone());
                    }
                    // 接单人的id
                    if (hbzLtlOrder.getTakeUser() != null) {

                        ltlManagementDTO.setTakeUserId(hbzLtlOrder.getTakeUser().getId());
                    }
                }

                return ltlManagementDTO;
            }
        });
    }


    //零担id 查询零担的详细信息
    public LtlManagementDTO ltlInformation(LtlManagementDTO ltlManagementDTO){

        HbzLtlOrder ltlManagement= ltlManagementRepository.findOne(ltlManagementDTO.getId());

        LtlManagementDTO ltlManagementDTO1=exchangeOrder(ltlManagement);

        return ltlManagementDTO1;
    }

    public LtlManagementDTO exchangeOrder(HbzLtlOrder  hbzLtlOrder) {
        LtlManagementDTO ltlManagementDTO = new LtlManagementDTO();
        if (hbzLtlOrder !=null) {

            ltlManagementDTO.setId(hbzLtlOrder.getId());
            ltlManagementDTO.setCommodityName(hbzLtlOrder.getCommodityName());
            if (hbzLtlOrder.getOrderTakeStart() !=null){
                FormatedDate logIn = new FormatedDate(hbzLtlOrder.getOrderTakeStart());
                String orderTakeStart = logIn.getFormat("yyyy-MM-dd HH:mm");
                ltlManagementDTO.setOrderTakeStart(orderTakeStart);
            }

            ltlManagementDTO.setCommodityDescribe(hbzLtlOrder.getCommodityDescribe());
            ltlManagementDTO.setCommodityWeight(hbzLtlOrder.getCommodityWeight());
            ltlManagementDTO.setCommodityVolume(hbzLtlOrder.getCommodityVolume());
            ltlManagementDTO.setTransType(hbzLtlOrder.getTransType());
            ltlManagementDTO.setTransLen(hbzLtlOrder.getTransLen());
            ltlManagementDTO.setRelatedPictures(hbzLtlOrder.getRelatedPictures());
            if (hbzLtlOrder.getTransType() != null) {
                ltlManagementDTO.setTransTypeValue(hbzLtlOrder.getTransType().getName());
            }

            ltlManagementDTO.setMaxLoad(hbzLtlOrder.getMaxLoad());
            //指定运输到货时间
            if (hbzLtlOrder.getDestlimit() != null) {

                FormatedDate logIn2 = new FormatedDate(hbzLtlOrder.getDestlimit());
                String destlimit = logIn2.getFormat("yyyy-MM-dd HH:mm");
                ltlManagementDTO.setDestlimit(destlimit);
            }
            //取货地址
            HbzArea currentLevelOriginArea= hbzLtlOrder.getOriginArea();
            LinkedList<Long> longList=new LinkedList<>();
            StringBuilder sb = new StringBuilder();
            while(currentLevelOriginArea.getLevel() >0 ){
                sb.insert(0,currentLevelOriginArea.getAreaName() + " " );
                switch(currentLevelOriginArea.getLevel().intValue()){
                    case 1:{
                        longList.addFirst(currentLevelOriginArea.getId());
                        ltlManagementDTO.setStartCity(longList);
                    }break;
                    case 2:{
                        longList.addFirst(currentLevelOriginArea.getId());
                        ltlManagementDTO.setStartCity(longList);
                    }break;
                    case 3:{
                        longList.addFirst(currentLevelOriginArea.getId());
                        ltlManagementDTO.setStartCity(longList);
                    }break;
                }

                currentLevelOriginArea = currentLevelOriginArea.getParent();
            }
            ltlManagementDTO.setOriginArea(sb.toString());



            //具体地址
            ltlManagementDTO.setOriginAddress(hbzLtlOrder.getOriginAddress());
            ltlManagementDTO.setLinkMan(hbzLtlOrder.getLinkMan());
            ltlManagementDTO.setLinkTelephone(hbzLtlOrder.getLinkTelephone());
            //分类
            ltlManagementDTO.setCommodityType(hbzLtlOrder.getCommodityType());
            if (hbzLtlOrder.getCommodityType() !=null){
                ltlManagementDTO.setCommodityTypeValue(hbzLtlOrder.getCommodityType().getName());
            }
            //质量单元
            ltlManagementDTO.setWeightUnit(hbzLtlOrder.getWeightUnit());
            if (hbzLtlOrder.getWeightUnit() !=null){
                ltlManagementDTO.setWeightUnitValue(hbzLtlOrder.getWeightUnit().getName());
            }
            //体积单位
            ltlManagementDTO.setVolumeUnit(hbzLtlOrder.getVolumeUnit());
            if (hbzLtlOrder.getVolumeUnit() !=null){
                ltlManagementDTO.setVolumeUnitValue(hbzLtlOrder.getVolumeUnit().getName());
            }
            //原始地址
            ltlManagementDTO.setOriginInfo(hbzLtlOrder.getOriginInfo());
            //目的地
            ltlManagementDTO.setDestInfo(hbzLtlOrder.getDestInfo());
            //到站地址
            HbzArea currentLevelArea = hbzLtlOrder.getDestArea();
            LinkedList<Long> endList=new LinkedList<>();
            StringBuilder ss = new StringBuilder();
            while(currentLevelArea.getLevel() >0 ){
                ss.insert(0,currentLevelArea.getAreaName() + " " );
                switch(currentLevelArea.getLevel().intValue()){
                    case 1:{
                        endList.addFirst(currentLevelArea.getId());
                        ltlManagementDTO.setEndCity(endList);
                    }break;
                    case 2:{
                        endList.addFirst(currentLevelArea.getId());
                        ltlManagementDTO.setEndCity(endList);
                    }break;
                    case 3:{
                        endList.addFirst(currentLevelArea.getId());
                        ltlManagementDTO.setEndCity(endList);
                    }break;
                }

                currentLevelArea = currentLevelArea.getParent();
            }
            ltlManagementDTO.setDestArea(ss.toString());


            //具体地址
            ltlManagementDTO.setDestAddress(hbzLtlOrder.getDestAddress());
            ltlManagementDTO.setDestLinker(hbzLtlOrder.getDestLinker());
            ltlManagementDTO.setDestTelephone(hbzLtlOrder.getDestTelephone());
            ltlManagementDTO.setLinkRemark(hbzLtlOrder.getLinkRemark());
            ltlManagementDTO.setSettlementType(hbzLtlOrder.getSettlementType());
            if (hbzLtlOrder.getSettlementType() != null) {
                ltlManagementDTO.setSettlementTypeValue(hbzLtlOrder.getSettlementType().getName());
            }

            ltlManagementDTO.setAmount(hbzLtlOrder.getAmount());
            //单价
            ltlManagementDTO.setUnitPrice(hbzLtlOrder.getUnitPrice());
//                    //创建人
            if (hbzLtlOrder.getCreateUser() != null) {
                ltlManagementDTO.setCreateUser(hbzLtlOrder.getCreateUser().getNickName());
            }
            //联系电话
            if (hbzLtlOrder.getCreateUser() != null) {
                ltlManagementDTO.setCreateUsertelephone(hbzLtlOrder.getCreateUser().getTelephone());
            }
            //所属公司
            if (hbzLtlOrder.getCreateUser() != null && hbzLtlOrder.getCreateUser().getEnt() != null) {
                ltlManagementDTO.setEnt(hbzLtlOrder.getCreateUser().getEnt().getOrganizationName());
            }else {
                ltlManagementDTO.setEnt("- -");
            }

            ltlManagementDTO.setOrderNo(hbzLtlOrder.getOrderNo());
            //订单创建时间
            FormatedDate logInCreatedDate = new FormatedDate(hbzLtlOrder.getCreatedDate());
            String createdDate = logInCreatedDate.getFormat("yyyy-MM-dd HH:mm");
            ltlManagementDTO.setCreateUserTime(createdDate);

            ltlManagementDTO.setOrderTrans(hbzLtlOrder.getOrderTrans());
            if (hbzLtlOrder.getOrderTrans() != null) {
                ltlManagementDTO.setOrderTransValue(hbzLtlOrder.getOrderTrans().getName());
            }
            ltlManagementDTO.setOrderType(hbzLtlOrder.getOrderType());
            if (hbzLtlOrder.getOrderType() != null) {
                ltlManagementDTO.setOrderTypeValue(hbzLtlOrder.getOrderType().getName());
            }

            //接单人
            if (hbzLtlOrder.getTakeUser() != null) {

                ltlManagementDTO.setTakeUser(hbzLtlOrder.getTakeUser().getNickName());
                ltlManagementDTO.setTakeUserId(hbzLtlOrder.getTakeUser().getId());
                ltlManagementDTO.setTakeUserTelephone(hbzLtlOrder.getTakeUser().getTelephone());

            }
        }

        return ltlManagementDTO;
    }


    //查询接单人详细信息
    @Override
    public TeakUserInformationDTO findTeakUserInformation(TeakUserInformationDTO teakUserInformationDTO) {
        TeakUserInformationDTO tu= new TeakUserInformationDTO();
        //接单人信息
        HbzLtlOrder hbzFslOrder=  ltlManagementRepository.findOne(teakUserInformationDTO.getId());
        if (hbzFslOrder.getTakeUser() !=null){

            tu.setTeakUser(hbzFslOrder.getTakeUser().getNickName());
        }
        if (hbzFslOrder.getTakeUser() !=null){

            tu.setTeakUserTelephone(hbzFslOrder.getTakeUser().getTelephone());
        }
        if (hbzFslOrder.getTakeUser() !=null && hbzFslOrder.getTakeUser().getEnt()!=null){

            tu.setOrg(hbzFslOrder.getTakeUser().getEnt().getOrganizationName());
        }

        if (hbzFslOrder.getTakeUser() !=null && hbzFslOrder.getTakeUser().getId() !=null){

            HbzPersonDriverRegistry hbzPersonDriverRegistry= ltlManagementRepository.findHbzPersonDriverRegistry(hbzFslOrder.getTakeUser().getId());
            if (hbzPersonDriverRegistry !=null){
                tu.setCarLength(hbzPersonDriverRegistry.getTransLength());
                tu.setLoad(hbzPersonDriverRegistry.getLoad());
                ////持证照片
                //tu.setCertifiedPhoto(hbzPersonDriverRegistry.getCertifiedPhoto());
                //交通强制险照片
                //tu.setStrongInsuranceImage(hbzPersonDriverRegistry.getStrongInsuranceImage());
                tu.setLicensePlateNumber(hbzPersonDriverRegistry.getLicensePlateNumber());
                //tu.setTransType(hbzPersonDriverRegistry.getTransType());
                if (hbzPersonDriverRegistry.getTransType() !=null){

                    tu.setTransTypeValue(hbzPersonDriverRegistry.getTransType().getName());
                }
            }
        }

        return tu;
    }


}
