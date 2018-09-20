package com.troy.keeper.management.service.impl;

import com.troy.keeper.hbz.dto.HbzAssignWorkDTO;
import com.troy.keeper.hbz.po.HbzArea;
import com.troy.keeper.hbz.po.LogisticsDetails;
import com.troy.keeper.hbz.service.HbzAssignWorkService;
import com.troy.keeper.hbz.service.mapper.HbzAreaMapper;
import com.troy.keeper.hbz.sys.FormatedDate;
import com.troy.keeper.management.dto.ExLogisticsDetailsDTO;
import com.troy.keeper.management.repository.HbzAreasRepository;
import com.troy.keeper.management.repository.LogisticsDetailsRepository;
import com.troy.keeper.util.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import com.troy.keeper.hbz.po.HbzExpressPieces;
import com.troy.keeper.management.dto.ManagementHbzExOrderDTO;
import com.troy.keeper.management.repository.HbzExpressPiecesRepository;
import com.troy.keeper.management.service.LogisticsDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 李奥
 * @date 2017/12/21.
 */
@Service
@Transactional
public class LogisticsDetailsServiceImpl implements LogisticsDetailsService {
    @Autowired
    private HbzExpressPiecesRepository hbzExpressPiecesRepository;
    @Autowired
    private HbzAreasRepository hbzAreaRepository;
    @Autowired
    private LogisticsDetailsRepository logisticsDetailsRepository;

    //分页查询
    @Override
    public Page<ManagementHbzExOrderDTO> findByCondition(ManagementHbzExOrderDTO managementHbzExOrderDTO, Pageable pageable) {

        Page<HbzExpressPieces> page = hbzExpressPiecesRepository.findAll(new Specification<HbzExpressPieces>() {
            @Override
            public Predicate toPredicate(Root<HbzExpressPieces> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (StringUtils.isNotBlank(managementHbzExOrderDTO.getOrderNo())) {
                    //订单编号
                    predicateList.add(criteriaBuilder.like(root.join("hbzExOrder").get("orderNo"), "%" + managementHbzExOrderDTO.getOrderNo() + "%"));
                }
                //订单收件联系人
                if (StringUtils.isNotBlank(managementHbzExOrderDTO.getLinker())) {
                    predicateList.add(criteriaBuilder.like(root.join("hbzExOrder").get("linker"), "%" + managementHbzExOrderDTO.getLinker() + "%"));
                }

                //订单收件人联系电话
                if (StringUtils.isNotBlank(managementHbzExOrderDTO.getTelephone())) {
                    predicateList.add(criteriaBuilder.equal(root.join("hbzExOrder").get("telephone"), managementHbzExOrderDTO.getTelephone()));
                }
                //订单取件城市
                if (StringUtils.isNotBlank(managementHbzExOrderDTO.getOriginAreaCode())) {
                    predicateList.add(criteriaBuilder.equal(root.join("originArea").get("outCode"), managementHbzExOrderDTO.getOriginAreaCode()));
                }
//                //省
//                if (managementHbzExOrderDTO.getProvinceId() !=null){
//                    //找到所有的子节点
//                    List<Long> childIds = hbzAreaRepository.findAllIdsByParentId(managementHbzExOrderDTO.getProvinceId());
//                    predicateList.add(root.join("hbzExOrder").get("originArea").get("id").in(childIds));
//                }
//                //市
//                if (managementHbzExOrderDTO.getCityId() != null){
//                    //找到所有的子节点
//                    List<Long> childIds = hbzAreaRepository.findAreaIdsByParentId(managementHbzExOrderDTO.getCityId());
//                    predicateList.add(root.join("hbzExOrder").get("originArea").get("id").in(childIds));
//                }
//                //区
//                if (managementHbzExOrderDTO.getCountyId() != null){
//                    predicateList.add(criteriaBuilder.equal(root.join("hbzExOrder").get("originArea").get("id"),managementHbzExOrderDTO.getCountyId()));
//                }
                //到站
                if (StringUtils.isNotBlank(managementHbzExOrderDTO.getDestAreaCode())) {
                    predicateList.add(criteriaBuilder.equal(root.join("destArea").get("outCode"), managementHbzExOrderDTO.getDestAreaCode()));
                }
                //省
//                if (managementHbzExOrderDTO.getProvinceToId() !=null){
//                    //找到所有的子节点
//                    List<Long> childIds = hbzAreaRepository.findAllIdsByParentId(managementHbzExOrderDTO.getProvinceToId());
//                    predicateList.add(root.join("hbzExOrder").get("destArea").get("id").in(childIds));
//                }
//                //市
//                if (managementHbzExOrderDTO.getCityToId() != null){
//                    //找到所有的子节点
//                    List<Long> childIds = hbzAreaRepository.findAreaIdsByParentId(managementHbzExOrderDTO.getCityToId());
//
//                    predicateList.add(root.join("hbzExOrder").get("destArea").get("id").in(childIds));
//                }
//                //区
//                if (managementHbzExOrderDTO.getCountyToId() != null){
//
//                    predicateList.add(criteriaBuilder.equal(root.join("hbzExOrder").get("destArea").get("id"),managementHbzExOrderDTO.getCountyToId()));
//                }

                //快递派件时间
                if (managementHbzExOrderDTO.getSmallTime() != null) {

                    predicateList.add(criteriaBuilder.ge(root.get("sendTime"), managementHbzExOrderDTO.getSmallTime()));
                }
                if (managementHbzExOrderDTO.getBigTime() != null) {
                    predicateList.add(criteriaBuilder.le(root.get("sendTime"), managementHbzExOrderDTO.getBigTime() + 59999L));
                }
                if (managementHbzExOrderDTO.getExpressCompanyType() != null) {
                    //第三方公司名称
                    predicateList.add(criteriaBuilder.equal(root.get("expressCompanyType"), managementHbzExOrderDTO.getExpressCompanyType()));
                }
                if (StringUtils.isNotBlank(managementHbzExOrderDTO.getTrackingNumber())) {
                    //第三方快递编号
                    predicateList.add(criteriaBuilder.equal(root.get("trackingNumber"), managementHbzExOrderDTO.getTrackingNumber()));
                }

                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));
            }
        }, pageable);

        return converterDto(page);
    }


    @Autowired
    HbzAssignWorkService hbzAssignWorkService;

    @Autowired
    HbzAreaMapper hbzAreaMapper;

    //分页查询出来的 参数封装
    private Page<ManagementHbzExOrderDTO> converterDto(Page<HbzExpressPieces> page) {
        return page.map(new Converter<HbzExpressPieces, ManagementHbzExOrderDTO>() {
            @Override
            public ManagementHbzExOrderDTO convert(HbzExpressPieces hbzExpressPieces) {
                ManagementHbzExOrderDTO me = new ManagementHbzExOrderDTO();
                me.setExId(hbzExpressPieces.getId());
                me.setExpressCompanyType(hbzExpressPieces.getExpressCompanyType());
                if (hbzExpressPieces.getExpressCompanyType() != null) {
                    me.setExpressCompanyTypeValue(hbzExpressPieces.getExpressCompanyType().getName());
                }
                me.setTrackingNumber(hbzExpressPieces.getTrackingNumber());
                FormatedDate logIn2 = new FormatedDate(hbzExpressPieces.getSendTime());
                String sendTime = logIn2.getFormat("yyyy-MM-dd HH:mm");
                me.setSendTime(sendTime);
                /////////////////////////////////////////////////////////////////////////////
                if (hbzExpressPieces.getHbzExOrder() != null) {
                    me.setOrderNo(hbzExpressPieces.getHbzExOrder().getOrderNo());
                    //取货地址
                    if (hbzExpressPieces.getHbzExOrder().getOriginArea() != null) {
                        HbzArea currentLevelOriginArea = hbzExpressPieces.getHbzExOrder().getOriginArea();
                        StringBuilder sb = new StringBuilder();
                        if (currentLevelOriginArea != null) {
                            while (currentLevelOriginArea.getLevel() > 0) {
                                sb.insert(0, currentLevelOriginArea.getAreaName() + " ");
                                currentLevelOriginArea = currentLevelOriginArea.getParent();
                            }
                            me.setOriginArea(sb.toString());
                        }
                        me.setOriginAreaWrapper(hbzAreaMapper.map(hbzExpressPieces.getHbzExOrder().getOriginArea()));
                    }
//                     StringBuffer sb = new StringBuffer();
//                     if (hbzExpressPieces.getHbzExOrder().getOriginArea() != null) {
//                         Integer level = hbzExpressPieces.getHbzExOrder().getOriginArea().getLevel();
//                         if (level == 1) {//省
//                             sb.append(hbzExpressPieces.getHbzExOrder().getOriginArea().getAreaName());
//                         } else if (level == 2) {//市
//                             HbzArea hbzAreaCity = hbzExpressPieces.getHbzExOrder().getOriginArea();
//                             sb.append(hbzAreaCity.getAreaName());
//                             sb.insert(0, hbzAreaCity.getParent().getAreaName() + " ");
//                             me.setOriginArea(sb.toString());
//                         } else if (level == 3) {//区县
//                             HbzArea hbzAreaCounty = hbzExpressPieces.getHbzExOrder().getOriginArea();
//                             HbzArea hbzAreaCity = hbzAreaCounty.getParent();
//                             HbzArea hbzAreaPrivice = hbzAreaCity.getParent();
//                             me.setOriginArea(hbzAreaPrivice.getAreaName() + " " + hbzAreaCity.getAreaName() + " " + hbzAreaCounty.getAreaName());
//                         }
//                     }
                    //送货地址
                    if (hbzExpressPieces.getHbzExOrder().getDestArea() != null) {
                        HbzArea currentLevelArea = hbzExpressPieces.getHbzExOrder().getDestArea();
                        StringBuilder ss = new StringBuilder();
                        if (currentLevelArea != null) {
                            while (currentLevelArea.getLevel() > 0) {
                                ss.insert(0, currentLevelArea.getAreaName() + " ");
                                currentLevelArea = currentLevelArea.getParent();
                            }
                            me.setDestArea(ss.toString());
                        }
                        me.setDestAreaWrapper(hbzAreaMapper.map(hbzExpressPieces.getHbzExOrder().getDestArea()));
                    }
//                     StringBuffer sbf = new StringBuffer();
//                     if (hbzExpressPieces.getHbzExOrder().getDestArea() != null) {
//                         Integer level = hbzExpressPieces.getHbzExOrder().getDestArea().getLevel();
//                         if (level == 1) {//省
//                             sbf.append(hbzExpressPieces.getHbzExOrder().getDestArea().getAreaName());
//                         } else if (level == 2) {//市
//                             HbzArea hbzAreaCity = hbzExpressPieces.getHbzExOrder().getDestArea();
//                             sbf.append(hbzAreaCity.getAreaName());
//                             sbf.insert(0, hbzAreaCity.getParent().getAreaName() + " ");
//                             me.setDestArea(sbf.toString());
//                         } else if (level == 3) {//区县
//                             HbzArea hbzAreaCounty = hbzExpressPieces.getHbzExOrder().getDestArea();
//                             HbzArea hbzAreaCity = hbzAreaCounty.getParent();
//                             HbzArea hbzAreaPrivice = hbzAreaCity.getParent();
//                             me.setDestArea(hbzAreaPrivice.getAreaName() + " " + hbzAreaCity.getAreaName() + " " + hbzAreaCounty.getAreaName());
//                         }
//                     }
                    //收件人
                    me.setLinker(hbzExpressPieces.getHbzExOrder().getLinker());
                    me.setTelephone(hbzExpressPieces.getHbzExOrder().getTelephone());
                    me.setOriginLinker(hbzExpressPieces.getHbzExOrder().getOriginLinker());
                    me.setOriginTelephone(hbzExpressPieces.getHbzExOrder().getOriginTelephone());
                    me.setCommodityWeight(hbzExpressPieces.getHbzExOrder().getCommodityWeight());
                    me.setCommodityVolume(hbzExpressPieces.getHbzExOrder().getCommodityVolume());
                    me.setAmount(hbzExpressPieces.getHbzExOrder().getAmount());

                }

                //查询派记录
                HbzAssignWorkDTO queryAssignWork = new HbzAssignWorkDTO();
                queryAssignWork.setStatus("1");
                queryAssignWork.setPlatformNo(me.getOrderNo());
                Long count = hbzAssignWorkService.count(queryAssignWork);
                me.setIsAssigned(count > 0L);

                return me;
            }
        });
    }

    //保存快递记录  新增信息
    @Override
    public Boolean saveLogisticsDetails(ExLogisticsDetailsDTO exLogisticsDetailsDTO) {

        if (exLogisticsDetailsDTO.getId() != null) {
            HbzExpressPieces hbzExpressPieces = hbzExpressPiecesRepository.findOne(exLogisticsDetailsDTO.getId());

            if (hbzExpressPieces != null) {
                LogisticsDetails logisticsDetails = new LogisticsDetails();
                String sendTime = exLogisticsDetailsDTO.getSendTime();
                if (StringUtils.isNotBlank(sendTime)) {
                    logisticsDetails.setSendTime(Long.valueOf(sendTime));
                }
                BeanUtils.copyProperties(exLogisticsDetailsDTO, logisticsDetails);
                logisticsDetails.setHbzExpressPieces(hbzExpressPieces);

                logisticsDetailsRepository.save(logisticsDetails);
                return true;
            } else {
                return false;
            }
        }
        return false;

    }


    //删除物流详情
    @Override
    public Boolean deletaLogisticsDetails(ExLogisticsDetailsDTO exLogisticsDetailsDTO) {

        if (exLogisticsDetailsDTO.getId() != null) {
            logisticsDetailsRepository.delete(exLogisticsDetailsDTO.getId());
        }
        return true;
    }


}
