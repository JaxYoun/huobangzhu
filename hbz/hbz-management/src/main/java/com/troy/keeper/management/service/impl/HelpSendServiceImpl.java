package com.troy.keeper.management.service.impl;

import com.troy.keeper.hbz.po.HbzArea;
import com.troy.keeper.hbz.po.HbzSendOrder;
import com.troy.keeper.hbz.sys.FormatedDate;
import com.troy.keeper.management.dto.HelpSendDTO;
import com.troy.keeper.management.repository.HbzAreasRepository;
import com.troy.keeper.management.repository.HelpSendRepository;
import com.troy.keeper.management.service.HelpSendService;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 李奥     帮我送
 * @date 2017/12/18.
 */
@Service
@Transactional
public class HelpSendServiceImpl  implements HelpSendService {
    @Autowired
    private HelpSendRepository helpSendRepository;
    @Autowired
    private HbzAreasRepository hbzAreaRepository;



    //分页查询
    @Override
    public Page<HelpSendDTO> findByCondition(HelpSendDTO helpSendDTO, Pageable pageable) {

        Page<HbzSendOrder> page= helpSendRepository.findAll(new Specification<HbzSendOrder>() {
            @Override
            public Predicate toPredicate(Root<HbzSendOrder> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(helpSendDTO.getOrderNo())){
                //订单编号模糊查询
                predicateList.add(criteriaBuilder.like(root.get("orderNo"),"%"+helpSendDTO.getOrderNo()+"%"));
            }
            if (StringUtils.isNotBlank(helpSendDTO.getOrganizationName())){
                //订单归属公司
                predicateList.add(criteriaBuilder.like(root.join("createUser").join("ent").get("organizationName"),"%"+helpSendDTO.getOrganizationName()+"%"));
            }
            if (StringUtils.isNotBlank(helpSendDTO.getCreateUser())){
                //订单创建人模糊查询
                predicateList.add(criteriaBuilder.like(root.join("createUser").get("nickName"),"%"+helpSendDTO.getCreateUser()+"%"));
            }
            if (StringUtils.isNotBlank(helpSendDTO.getCreateUserTelephone())){
                //订单创建人 电话
                predicateList.add(criteriaBuilder.equal(root.join("createUser").get("telephone"),helpSendDTO.getCreateUserTelephone()));
            }
            //订单归属城市
                if (StringUtils.isNotBlank(helpSendDTO.getOriginAreaCode())){
                    predicateList.add(criteriaBuilder.equal(root.join("originArea").get("outCode"),helpSendDTO.getOriginAreaCode()));
                }
//            //省
//            if (helpSendDTO.getProvinceId() !=null){
//                //找到所有的子节点
//                List<Long> childIds = hbzAreaRepository.findAllIdsByParentId(helpSendDTO.getProvinceId());
//                predicateList.add(root.get("originArea").get("id").in(childIds));
//            }
//            //市
//            if (helpSendDTO.getCityId() != null){
//                //找到所有的子节点
//                List<Long> childIds = hbzAreaRepository.findAreaIdsByParentId(helpSendDTO.getCityId());
//                predicateList.add(root.get("originArea").get("id").in(childIds));
//            }
//            //区
//            if (helpSendDTO.getCountyId() != null){
//                predicateList.add(criteriaBuilder.equal(root.get("originArea").get("id"),helpSendDTO.getCountyId()));
//            }

            if (helpSendDTO.getOrderTrans() !=null){
                //订单状态
                predicateList.add(criteriaBuilder.equal(root.get("orderTrans"),helpSendDTO.getOrderTrans()));
            }
            if (StringUtils.isNotBlank(helpSendDTO.getTakeUser())){
                //接单人  模糊查询
                predicateList.add(criteriaBuilder.like(root.join("takeUser").get("nickName"),"%"+helpSendDTO.getTakeUser()+"%"));
            }
            if (StringUtils.isNotBlank(helpSendDTO.getTakeUserTelephone())){
                //接单人 电话
                predicateList.add(criteriaBuilder.equal(root.join("takeUser").get("telephone"),helpSendDTO.getTakeUserTelephone()));
            }
            if (helpSendDTO.getSmallTime() !=null){
                //时间范围的查询
                predicateList.add(criteriaBuilder.ge(root.get("createdDate"),helpSendDTO.getSmallTime()));
            }
            if (helpSendDTO.getBigTime() !=null){
                //时间范围的查询
                predicateList.add(criteriaBuilder.le(root.get("createdDate"),helpSendDTO.getBigTime()+59999L));
            }


                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));
            }
        },pageable);

        return    converterDto(page);
    }



    //分页查询出来的 参数封装
    private Page<HelpSendDTO> converterDto(Page<HbzSendOrder> page) {
        return  page.map(new Converter<HbzSendOrder, HelpSendDTO>() {
            @Override
            public HelpSendDTO convert(HbzSendOrder hbzSendOrder) {
                HelpSendDTO hs=new HelpSendDTO();
                hs.setId(hbzSendOrder.getId());
                hs.setOrderNo(hbzSendOrder.getOrderNo());
                if (hbzSendOrder.getCreateUser() !=null && hbzSendOrder.getCreateUser().getEnt() !=null){
                    hs.setOrganizationName(hbzSendOrder.getCreateUser().getEnt().getOrganizationName());
                }else {
                    hs.setOrganizationName("- -");
                }
                hs.setOriginAddress(hbzSendOrder.getOriginAddress());
                hs.setOriginLinker(hbzSendOrder.getOriginLinker());
                //取货联系人
                hs.setOriginLinkTelephone(hbzSendOrder.getOriginLinkTelephone());
                hs.setAmount(hbzSendOrder.getAmount());
                if (hbzSendOrder.getCreateUser() !=null){
                    hs.setCreateUser(hbzSendOrder.getCreateUser().getNickName());
                    hs.setCreateUserTelephone(hbzSendOrder.getCreateUser().getTelephone());
                }
                FormatedDate logIn3 = new FormatedDate(hbzSendOrder.getCreatedDate());
                String createdDate = logIn3.getFormat("yyyy-MM-dd HH:mm");
                hs.setCreateTime(createdDate);
                //配送时间要求
                hs.setTimeLimit(hbzSendOrder.getTimeLimit());
                if (hbzSendOrder.getTimeLimit() !=null){
                    hs.setTimeLimitValue(hbzSendOrder.getTimeLimit().getName());
                }
                FormatedDate logIn4 = new FormatedDate(hbzSendOrder.getStartTime());
                String startTime = logIn4.getFormat("yyyy-MM-dd HH:mm");
                hs.setStartTime(startTime);
                //订单归属城市
                HbzArea currentLevelOriginArea= hbzSendOrder.getOriginArea();
                StringBuilder sb = new StringBuilder();
                if (currentLevelOriginArea !=null) {
                    while (currentLevelOriginArea.getLevel() > 0) {
                        sb.insert(0, currentLevelOriginArea.getAreaName() + " ");
                        currentLevelOriginArea = currentLevelOriginArea.getParent();
                    }
                    hs.setOriginArea(sb.toString());
                }

//                StringBuffer sbf = new StringBuffer();
//                if (hbzSendOrder.getOriginArea() != null) {
//                    Integer level = hbzSendOrder.getOriginArea().getLevel();
//                    if (level == 1) {//省
//                        sbf.append(hbzSendOrder.getOriginArea().getAreaName());
//                    } else if (level == 2) {//市
//                        HbzArea hbzAreaCity = hbzSendOrder.getOriginArea();
//                        sbf.append(hbzAreaCity.getAreaName());
//                        sbf.insert(0, hbzAreaCity.getParent().getAreaName() + " ");
//                        hs.setOriginArea(sbf.toString());
//                    } else if (level == 3) {//区县
//                        HbzArea hbzAreaCounty = hbzSendOrder.getOriginArea();
//                        HbzArea hbzAreaCity = hbzAreaCounty.getParent();
//                        HbzArea hbzAreaPrivice = hbzAreaCity.getParent();
//                        hs.setOriginArea(hbzAreaPrivice.getAreaName() + " " + hbzAreaCity.getAreaName() + " " + hbzAreaCounty.getAreaName());
//                    }
//                }
                //接单人
                if (hbzSendOrder.getTakeUser() != null){
                    hs.setTakeUser(hbzSendOrder.getTakeUser().getNickName());
                    hs.setTakeUserTelephone(hbzSendOrder.getTakeUser().getTelephone());
                }
                hs.setOrderTrans(hbzSendOrder.getOrderTrans());
                if (hbzSendOrder.getOrderTrans() !=null){
                    hs.setOrderTransValue(hbzSendOrder.getOrderTrans().getName());
                }
                hs.setCommodityName(hbzSendOrder.getCommodityName());

                return hs;
            }
        });
    }

    @Override
    public HelpSendDTO findHelpSend(HelpSendDTO helpSendDTO) {
        HelpSendDTO hs=new HelpSendDTO();
        HbzSendOrder hbzSendOrder=  helpSendRepository.findHbzSendOrder(helpSendDTO.getId());
     if (hbzSendOrder !=null) {
         hs.setRelatedPictures(hbzSendOrder.getRelatedPictures());
         hs.setId(hbzSendOrder.getId());
         hs.setOrderNo(hbzSendOrder.getOrderNo());
         hs.setOrderTrans(hbzSendOrder.getOrderTrans());
         if (hbzSendOrder.getOrderTrans() != null) {
             hs.setOrderTransValue(hbzSendOrder.getOrderTrans().getName());
         }
         hs.setCommodityWeight(hbzSendOrder.getCommodityWeight());
         hs.setCommodityVolume(hbzSendOrder.getCommodityVolume());
         hs.setCommodityDesc(hbzSendOrder.getCommodityDesc());
         //取货地址
         HbzArea currentLevelOriginArea = hbzSendOrder.getOriginArea();
         StringBuilder sb = new StringBuilder();
         if (currentLevelOriginArea != null) {
             while (currentLevelOriginArea.getLevel() > 0) {
                 sb.insert(0, currentLevelOriginArea.getAreaName() + " ");
                 currentLevelOriginArea = currentLevelOriginArea.getParent();
             }
             hs.setOriginArea(sb.toString());
         }
//         StringBuffer sbf = new StringBuffer();
//         if (hbzSendOrder.getOriginArea() != null) {
//             Integer level = hbzSendOrder.getOriginArea().getLevel();
//             if (level == 1) {//省
//                 sbf.append(hbzSendOrder.getOriginArea().getAreaName());
//             } else if (level == 2) {//市
//                 HbzArea hbzAreaCity = hbzSendOrder.getOriginArea();
//                 sbf.append(hbzAreaCity.getAreaName());
//                 sbf.insert(0, hbzAreaCity.getParent().getAreaName() + " ");
//                 hs.setOriginArea(sbf.toString());
//             } else if (level == 3) {//区县
//                 HbzArea hbzAreaCounty = hbzSendOrder.getOriginArea();
//                 HbzArea hbzAreaCity = hbzAreaCounty.getParent();
//                 HbzArea hbzAreaPrivice = hbzAreaCity.getParent();
//                 hs.setOriginArea(hbzAreaPrivice.getAreaName() + " " + hbzAreaCity.getAreaName() + " " + hbzAreaCounty.getAreaName());
//             }
//         }
         //送货地址
         HbzArea currentLevelArea = hbzSendOrder.getDestArea();
         LinkedList<Long> endList = new LinkedList<>();
         StringBuilder ss = new StringBuilder();
         if (currentLevelArea != null) {
             while (currentLevelArea.getLevel() > 0) {
                 ss.insert(0, currentLevelArea.getAreaName() + " ");
                 currentLevelArea = currentLevelArea.getParent();
             }
             hs.setDestArea(ss.toString());
         }
         hs.setOriginAddress(hbzSendOrder.getOriginAddress());
         hs.setOriginLinker(hbzSendOrder.getOriginLinker());
         hs.setOriginLinkTelephone(hbzSendOrder.getOriginLinkTelephone());
         hs.setTakeLimit(hbzSendOrder.getTakeLimit());
         if (hbzSendOrder.getTakeTime() != null) {
             FormatedDate logIn2 = new FormatedDate(hbzSendOrder.getTakeTime());
             String takeTime = logIn2.getFormat("yyyy-MM-dd HH:mm");
             hs.setTakeTime(takeTime);
         }
         hs.setDestAddress(hbzSendOrder.getDestAddress());
         //目的地址
         hs.setDestInfo(hbzSendOrder.getDestInfo());
         hs.setLinker(hbzSendOrder.getLinker());
         hs.setLinkTelephone(hbzSendOrder.getLinkTelephone());
         hs.setTimeLimit(hbzSendOrder.getTimeLimit());
         if (hbzSendOrder.getTimeLimit() != null) {
             hs.setTimeLimitValue(hbzSendOrder.getTimeLimit().getName());
         }
         if (hbzSendOrder.getStartTime() != null){
             //开始配送时间
             FormatedDate logIn3 = new FormatedDate(hbzSendOrder.getStartTime());
         String startTime = logIn3.getFormat("yyyy-MM-dd HH:mm");
         hs.setStartTime(startTime);
          }

         //取货时间
         if (hbzSendOrder.getOrderTakeTime() != null) {
             //开始配送时间
             FormatedDate logIn7 = new FormatedDate(hbzSendOrder.getOrderTakeTime());
             String orderTakeTime = logIn7.getFormat("yyyy-MM-dd HH:mm");
             hs.setOrderTakeTime(orderTakeTime);
         }

         hs.setAmount(hbzSendOrder.getAmount());
         if (hbzSendOrder.getCreateUser() !=null) {
             hs.setCreateUser(hbzSendOrder.getCreateUser().getNickName());
             hs.setCreateUserTelephone(hbzSendOrder.getCreateUser().getTelephone());
         }
         if (hbzSendOrder.getCreateUser() !=null && hbzSendOrder.getCreateUser().getEnt() !=null){
             hs.setOrganizationName(hbzSendOrder.getCreateUser().getEnt().getOrganizationName());
         }
         hs.setCommodityName(hbzSendOrder.getCommodityName());

         return  hs;
     }

        return hs;
    }

    //接单人详情
    @Override
    public HelpSendDTO findTeakUser(HelpSendDTO helpSendDTO) {
        HelpSendDTO hs=new HelpSendDTO();
        HbzSendOrder hbzSendOrder=   helpSendRepository.findHbzSendOrder(helpSendDTO.getId());
        if (hbzSendOrder !=null){
              hs.setId(hbzSendOrder.getId());
              hs.setOrderNo(hbzSendOrder.getOrderNo());
           if (hbzSendOrder.getTakeUser() !=null){
               hs.setTakeUser(hbzSendOrder.getTakeUser().getNickName());
               hs.setTakeUserTelephone(hbzSendOrder.getTakeUser().getTelephone());
           }
           if ( hbzSendOrder.getTakeUser() !=null && hbzSendOrder.getTakeUser().getEnt() !=null){
               hs.setOrganizationName(hbzSendOrder.getTakeUser().getEnt().getOrganizationName());
           }
         return  hs;
        }
        return hs;
    }


}
