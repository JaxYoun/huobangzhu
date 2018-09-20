package com.troy.keeper.management.service.impl;

import com.troy.keeper.hbz.dto.HbzAssignWorkDTO;
import com.troy.keeper.hbz.po.HbzArea;
import com.troy.keeper.hbz.po.HbzExOrder;
import com.troy.keeper.hbz.po.HbzExpressPieces;
import com.troy.keeper.hbz.po.LogisticsDetails;
import com.troy.keeper.hbz.service.HbzAssignWorkService;
import com.troy.keeper.hbz.sys.FormatedDate;
import com.troy.keeper.hbz.type.OrderTrans;
import com.troy.keeper.management.dto.ExLogisticsDetailsDTO;
import com.troy.keeper.management.dto.ManagementHbzExOrderDTO;
import com.troy.keeper.management.repository.HbzAreasRepository;
import com.troy.keeper.management.repository.HbzExOrdersRepository;
import com.troy.keeper.management.repository.HbzExpressPiecesRepository;
import com.troy.keeper.management.service.HbzExpressPiecesService;
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
import java.util.LinkedList;
import java.util.List;

/**
 * @author 李奥
 * @date 2017/12/20.
 */
@Service
@Transactional
public class HbzExpressPiecesServiceImpl implements HbzExpressPiecesService {
    @Autowired
    private HbzExOrdersRepository hbzExpressPiecesRepository;
    @Autowired
    private HbzAreasRepository hbzAreaRepository;
    @Autowired
    private HbzExpressPiecesRepository hbzExpress;
    @Autowired
    private HbzAssignWorkService hbzAssignWorkService;

    //分页查询
    @Override
    public Page<ManagementHbzExOrderDTO> findByCondition(ManagementHbzExOrderDTO managementHbzExOrderDTO, Pageable pageable) {

        Page<HbzExOrder> page = hbzExpressPiecesRepository.findAll(new Specification<HbzExOrder>() {
            @Override
            public Predicate toPredicate(Root<HbzExOrder> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (StringUtils.isNotBlank(managementHbzExOrderDTO.getOrderNo())) {
                    //订单编号
                    predicateList.add(criteriaBuilder.like(root.get("orderNo"), "%" + managementHbzExOrderDTO.getOrderNo() + "%"));
                }
                if (StringUtils.isNotBlank(managementHbzExOrderDTO.getOrganizationName())) {
                    //所属公司
                    predicateList.add(criteriaBuilder.like(root.join("createUser").join("ent").get("organizationName"), "%" + managementHbzExOrderDTO.getOrganizationName() + "%"));
                }
                if (StringUtils.isNotBlank(managementHbzExOrderDTO.getCreateUser())) {
                    //订单创建人
                    predicateList.add(criteriaBuilder.like(root.join("createUser").get("nickName"), "%" + managementHbzExOrderDTO.getCreateUser() + "%"));
                }
                if (StringUtils.isNotBlank(managementHbzExOrderDTO.getCreateUserTelephone())) {
                    //订单创建人 电话
                    predicateList.add(criteriaBuilder.equal(root.join("createUser").get("telephone"), managementHbzExOrderDTO.getCreateUserTelephone()));
                }
                //订单取件城市
                //发出
                if (StringUtils.isNotBlank(managementHbzExOrderDTO.getOriginAreaCode())) {
                    predicateList.add(criteriaBuilder.equal(root.join("originArea").get("outCode"), managementHbzExOrderDTO.getOriginAreaCode()));
                }
                //到站
                if (StringUtils.isNotBlank(managementHbzExOrderDTO.getDestAreaCode())) {
                    predicateList.add(criteriaBuilder.equal(root.join("destArea").get("outCode"), managementHbzExOrderDTO.getDestAreaCode()));
                }
                if (managementHbzExOrderDTO.getOrderTrans() != null) {
                    //订单的状态
                    predicateList.add(criteriaBuilder.equal(root.get("orderTrans"), managementHbzExOrderDTO.getOrderTrans()));
                }
                //时间范围的查询
                if (managementHbzExOrderDTO.getSmallTime() != null) {

                    predicateList.add(criteriaBuilder.ge(root.get("createdDate"), managementHbzExOrderDTO.getSmallTime()));
                }
                if (managementHbzExOrderDTO.getBigTime() != null) {
                    predicateList.add(criteriaBuilder.le(root.get("createdDate"), managementHbzExOrderDTO.getBigTime() + 59999L));
                }
                if (managementHbzExOrderDTO.getExpressCompanyType() != null) {
                    //第三方公司名称
                    predicateList.add(criteriaBuilder.equal(root.join("hbzExpressPieces", JoinType.LEFT).get("expressCompanyType"), managementHbzExOrderDTO.getExpressCompanyType()));
                }
                if (StringUtils.isNotBlank(managementHbzExOrderDTO.getTrackingNumber())) {
                    //第三方快递编号
                    predicateList.add(criteriaBuilder.equal(root.join("hbzExpressPieces", JoinType.LEFT).get("trackingNumber"), managementHbzExOrderDTO.getTrackingNumber()));
                }

                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));
            }
        }, pageable);

        return converterDto(page);
    }


    //分页查询出来的 参数封装
    private Page<ManagementHbzExOrderDTO> converterDto(Page<HbzExOrder> page) {
        return page.map(new Converter<HbzExOrder, ManagementHbzExOrderDTO>() {
            @Override
            public ManagementHbzExOrderDTO convert(HbzExOrder hbzExOrder) {
                ManagementHbzExOrderDTO me = new ManagementHbzExOrderDTO();


                me.setOrderNo(hbzExOrder.getOrderNo());
                if (hbzExOrder.getCreateUser() != null && hbzExOrder.getCreateUser().getEnt() != null) {
                    me.setOrganizationName(hbzExOrder.getCreateUser().getEnt().getOrganizationName());
                } else {
                    me.setOrganizationName("- -");
                }
                if (hbzExOrder.getCreateUser() != null) {
                    me.setCreateUser(hbzExOrder.getCreateUser().getNickName());
                    me.setCreateUserTelephone(hbzExOrder.getCreateUser().getTelephone());
                }
                FormatedDate logIn2 = new FormatedDate(hbzExOrder.getCreatedDate());
                String createTime = logIn2.getFormat("yyyy-MM-dd HH:mm");
                me.setCreateTime(createTime);

                //取货地址
                HbzArea currentLevelOriginArea = hbzExOrder.getOriginArea();
                StringBuilder sb = new StringBuilder();
                if (currentLevelOriginArea != null) {
                    while (currentLevelOriginArea.getLevel() > 0) {
                        sb.insert(0, currentLevelOriginArea.getAreaName() + " ");

                        currentLevelOriginArea = currentLevelOriginArea.getParent();
                    }
                    me.setOriginArea(sb.toString());
                }

                me.setOriginLinker(hbzExOrder.getOriginLinker());
                me.setOriginTelephone(hbzExOrder.getOriginTelephone());

                //送货地址
                HbzArea currentLevelArea = hbzExOrder.getDestArea();
                StringBuilder ss = new StringBuilder();
                if (currentLevelArea != null) {
                    while (currentLevelArea.getLevel() > 0) {
                        ss.insert(0, currentLevelArea.getAreaName() + " ");
                        currentLevelArea = currentLevelArea.getParent();
                    }
                    me.setDestArea(ss.toString());
                }

                me.setLinker(hbzExOrder.getLinker());
                me.setTelephone(hbzExOrder.getTelephone());
                me.setCommodityWeight(hbzExOrder.getCommodityWeight());
                me.setCommodityVolume(hbzExOrder.getCommodityVolume());
                me.setAmount(hbzExOrder.getAmount());
                me.setOrderTrans(hbzExOrder.getOrderTrans());
                if (hbzExOrder.getOrderTrans() != null) {
                    me.setOrderTransValue(hbzExOrder.getOrderTrans().getName());
                }
                me.setId(hbzExOrder.getId());
                if (hbzExOrder.getHbzExpressPieces() != null) {
                    me.setExId(hbzExOrder.getHbzExpressPieces().getId());
                    me.setTrackingNumber(hbzExOrder.getHbzExpressPieces().getTrackingNumber());
                    me.setExpressCompanyType(hbzExOrder.getHbzExpressPieces().getExpressCompanyType());
                    if (hbzExOrder.getHbzExpressPieces().getExpressCompanyType() != null) {
                        me.setExpressCompanyTypeValue(hbzExOrder.getHbzExpressPieces().getExpressCompanyType().getName());
                    }
                    me.setIsNull(true);
                } else {
                    me.setIsNull(false);
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

    //通过快递的 id  查询快递派件表中的 及快递详情
    @Override
    public ManagementHbzExOrderDTO findHbzHbzExOrder(ManagementHbzExOrderDTO managementHbzExOrderDTO) {
        ManagementHbzExOrderDTO me = new ManagementHbzExOrderDTO();
        HbzExOrder hbzExOrder = hbzExpressPiecesRepository.findHbzHbzExOrder(managementHbzExOrderDTO.getId());

        if (hbzExOrder.getOrderNo() != null) {
            me.setOrderNo(hbzExOrder.getOrderNo());
        }

        if (hbzExOrder.getCreateUser() != null && hbzExOrder.getCreateUser().getEnt() != null) {
            me.setOrganizationName(hbzExOrder.getCreateUser().getEnt().getOrganizationName());
        } else {
            me.setOrganizationName("- -");
        }
        if (hbzExOrder.getCreateUser() != null) {
            me.setCreateUser(hbzExOrder.getCreateUser().getNickName());
            me.setCreateUserTelephone(hbzExOrder.getCreateUser().getTelephone());
        }
        FormatedDate logIn2 = new FormatedDate(hbzExOrder.getCreatedDate());
        String createTime = logIn2.getFormat("yyyy-MM-dd HH:mm");
        me.setCreateTime(createTime);

        //取货地址
        HbzArea currentLevelOriginArea = hbzExOrder.getOriginArea();
        StringBuilder sb = new StringBuilder();
        if (currentLevelOriginArea != null) {
            while (currentLevelOriginArea.getLevel() > 0) {
                sb.insert(0, currentLevelOriginArea.getAreaName() + " ");

                currentLevelOriginArea = currentLevelOriginArea.getParent();
            }
            me.setOriginArea(sb.toString());
        }

        me.setOriginLinker(hbzExOrder.getOriginLinker());
        me.setOriginTelephone(hbzExOrder.getOriginTelephone());
        me.setRelatedPictures(hbzExOrder.getRelatedPictures());
        //送货地址
        HbzArea currentLevelArea = hbzExOrder.getDestArea();
        StringBuilder ss = new StringBuilder();
        if (currentLevelArea != null) {
            while (currentLevelArea.getLevel() > 0) {
                ss.insert(0, currentLevelArea.getAreaName() + " ");
                currentLevelArea = currentLevelArea.getParent();
            }
            me.setDestArea(ss.toString());
        }

        me.setLinker(hbzExOrder.getLinker());
        me.setTelephone(hbzExOrder.getTelephone());
        me.setCommodityWeight(hbzExOrder.getCommodityWeight());
        me.setCommodityVolume(hbzExOrder.getCommodityVolume());
        me.setAmount(hbzExOrder.getAmount());
        me.setOrderTrans(hbzExOrder.getOrderTrans());
        if (hbzExOrder.getOrderTrans() != null) {
            me.setOrderTransValue(hbzExOrder.getOrderTrans().getName());
        }
        me.setId(hbzExOrder.getId());
        me.setCommodityClass(hbzExOrder.getCommodityClass());
        if (hbzExOrder.getCommodityClass() != null) {
            me.setCommodityClassValue(hbzExOrder.getCommodityClass().getName());
        }
        me.setCommodityDesc(hbzExOrder.getCommodityDesc());

        if (hbzExOrder.getOrderTakeTime() != null) {
            FormatedDate logIn3 = new FormatedDate(hbzExOrder.getOrderTakeTime());
            String takeTime = logIn3.getFormat("yyyy-MM-dd HH:mm");
            me.setTakeTime(takeTime);
        }
        me.setOriginAddr(hbzExOrder.getOriginAddr());
        me.setDestAddr(hbzExOrder.getDestAddr());

        if (hbzExOrder.getHbzExpressPieces() != null) {
            me.setExId(hbzExOrder.getHbzExpressPieces().getId());
            me.setTrackingNumber(hbzExOrder.getHbzExpressPieces().getTrackingNumber());
            me.setExpressCompanyType(hbzExOrder.getHbzExpressPieces().getExpressCompanyType());

            FormatedDate logIn4 = new FormatedDate(hbzExOrder.getHbzExpressPieces().getSendTime());
            String sendTime = logIn4.getFormat("yyyy-MM-dd HH:mm");
            me.setSendTime(sendTime);
            if (hbzExOrder.getHbzExpressPieces().getExpressCompanyType() != null) {
                me.setExpressCompanyTypeValue(hbzExOrder.getHbzExpressPieces().getExpressCompanyType().getName());
            }

            me.setIsNull(true);
        } else {
            me.setIsNull(false);
        }
        return me;
    }


    //保存派件数据
    @Override
    public Boolean saveHbzExpressPieces(ManagementHbzExOrderDTO managementHbzExOrderDTO) {

        if (managementHbzExOrderDTO.getId() != null) {

            HbzExOrder hbzExOrder = hbzExpressPiecesRepository.findOne(managementHbzExOrderDTO.getId());
            if (hbzExOrder != null) {
                HbzExpressPieces hbzExpressPieces = new HbzExpressPieces();
                String sendTime = managementHbzExOrderDTO.getSendTime();
                if (StringUtils.isNotBlank(sendTime)) {
                    hbzExpressPieces.setSendTime(Long.valueOf(sendTime));
                }
                BeanUtils.copyProperties(managementHbzExOrderDTO, hbzExpressPieces);
                hbzExpressPieces.setHbzExOrder(hbzExOrder);
                hbzExOrder.setOrderTrans(OrderTrans.WAIT_TO_TAKE);
                hbzExpressPiecesRepository.save(hbzExOrder);
                hbzExpress.save(hbzExpressPieces);
                return true;
            } else {
                return false;
            }
        }
        return false;

    }

    ////通过本表的 id  即是实体类中的exId 查询物流详情记录中的 数据
    @Override
    public List<ExLogisticsDetailsDTO> findLogisticsDetails(ExLogisticsDetailsDTO exLogisticsDetailsDTO) {

        List<ExLogisticsDetailsDTO> list2 = new ArrayList<>();

        List<LogisticsDetails> list = hbzExpress.findLogisticsDetail(exLogisticsDetailsDTO.getId());
        for (int i = 0; i < list.size(); i++) {
            ExLogisticsDetailsDTO e = new ExLogisticsDetailsDTO();
            Long id = list.get(i).getId();
            String CommodityDesc = list.get(i).getCommodityDesc();
            FormatedDate logIn4 = new FormatedDate(list.get(i).getSendTime());
            String sendTime = logIn4.getFormat("yyyy-MM-dd HH:mm:ss");
            String Information = CommodityDesc + "  " + sendTime;
            e.setId(id);
            e.setInformation(Information);
            list2.add(e);
        }

        return list2;
    }


}
