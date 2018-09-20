package com.troy.keeper.management.service.impl;

import com.troy.keeper.hbz.dto.HbzBuyOrderDTO;
import com.troy.keeper.hbz.po.*;
import com.troy.keeper.hbz.repository.HbzOrderRepository;
import com.troy.keeper.hbz.repository.HbzPayRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.service.mapper.HbzBuyOrderMapper;
import com.troy.keeper.hbz.sys.FormatedDate;
import com.troy.keeper.hbz.type.OrderTrans;
import com.troy.keeper.management.dto.HelpBuyDetailsDTO;
import com.troy.keeper.management.dto.HelpBuyOrderDetailsDTO;
import com.troy.keeper.management.dto.HelpBuyTableDTO;
import com.troy.keeper.management.dto.PlatformDetailsDTO;
import com.troy.keeper.management.repository.HbzAreasRepository;
import com.troy.keeper.management.repository.ManagerHbzBuyOrderRepository;
import com.troy.keeper.management.service.ManagerHbzBuyOrderService;
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
import java.util.*;

/**
 * @author 李奥
 * @date 2017/12/11.帮我买
 */
@Service
@Transactional
public class ManagerHbzBuyOrderServiceImpl  implements ManagerHbzBuyOrderService{

    @Autowired
    private ManagerHbzBuyOrderRepository managerHbzBuyOrderRepository;
    @Autowired
    private HbzBuyOrderMapper hbzBuyOrderMapper;
    @Autowired
    private HbzAreasRepository hbzAreaRepository;
    @Autowired
    private HbzOrderRepository hbzOrderRepository;
    @Autowired
    private HbzPayRepository hbzPayRepository;
    @Autowired
    private HbzUserRepository hbzUserRepository;


    //分页查询
    @Override
    public Page<HelpBuyTableDTO> findByCondition(HelpBuyTableDTO helpBuyTableDTO, Pageable pageable) {



        Page<HbzBuyOrder> page=managerHbzBuyOrderRepository.findAll(new Specification<HbzBuyOrder>() {
            @Override
            public Predicate toPredicate(Root<HbzBuyOrder> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//
                List<Predicate> predicateList = new ArrayList<>();
                if (StringUtils.isNotBlank(helpBuyTableDTO.getOrderNo() )){

                    predicateList.add(criteriaBuilder.like(root.get("orderNo"), "%" + helpBuyTableDTO.getOrderNo() + "%"));
                }
                //所属公司
                if(StringUtils.isNotBlank(helpBuyTableDTO.getOrganizationName() )){
                    predicateList.add(criteriaBuilder.like(root.join("createUser").join("ent").get("organizationName"),"%"+helpBuyTableDTO.getOrganizationName()+"%"));
                }
                if (StringUtils.isNotBlank(helpBuyTableDTO.getCreateUser() )){
                        //订单创建人
                    predicateList.add(criteriaBuilder.like(root.join("createUser").get("nickName"),"%"+helpBuyTableDTO.getCreateUser()+"%"));
                }
                if (StringUtils.isNotBlank(helpBuyTableDTO.getCreateUserTelephone() )){
                        //创建人电话
                    predicateList.add(criteriaBuilder.equal(root.join("createUser").get("telephone"),helpBuyTableDTO.getCreateUserTelephone()));
                }
                if (helpBuyTableDTO.getOrderTrans() !=null){
                    //订单状态
                    predicateList.add(criteriaBuilder.equal(root.get("orderTrans"),helpBuyTableDTO.getOrderTrans()));
                }
                if (helpBuyTableDTO.getIsPayOf() !=null){
                    //是否付款
                    predicateList.add(criteriaBuilder.equal(root.get("orderTrans"),helpBuyTableDTO.getIsPayOf()));
                }
                if (StringUtils.isNotBlank(helpBuyTableDTO.getCommodityName() )){
                    //商品名称
                    predicateList.add(criteriaBuilder.like(root.get("commodityName"),"%"+helpBuyTableDTO.getCommodityName()+"%"));

                }
                if (StringUtils.isNotBlank(helpBuyTableDTO.getTeakUser() )){
                    //接单人
                    predicateList.add(criteriaBuilder.like(root.join("takeUser").get("nickName"),"%"+helpBuyTableDTO.getTeakUser()+"%"));
                }
                if (StringUtils.isNotBlank(helpBuyTableDTO.getTeakUserTelephone() )){
                    //接单人电话
                    predicateList.add(criteriaBuilder.equal(root.join("takeUser").get("telephone"),helpBuyTableDTO.getTeakUserTelephone()));
                }
                if (helpBuyTableDTO.getSmallTime() !=null){
                    //时间范围的查询
                    predicateList.add(criteriaBuilder.ge(root.get("createdDate"),helpBuyTableDTO.getSmallTime()));
                }
                if (helpBuyTableDTO.getBigTime() !=null){
                    //时间范围的查询
                    predicateList.add(criteriaBuilder.le(root.get("createdDate"),helpBuyTableDTO.getBigTime()+59999L));
                }



                //订单归属城市
                if (StringUtils.isNotBlank(helpBuyTableDTO.getDestAreaCode())){
                    predicateList.add(criteriaBuilder.equal(root.join("destArea").get("outCode"),helpBuyTableDTO.getDestAreaCode()));
                }
                //省
//                if (helpBuyTableDTO.getProvinceToId() !=null){
//                    //找到所有的子节点
//                    List<Long> childIds = hbzAreaRepository.findAllIdsByParentId(helpBuyTableDTO.getProvinceToId());
//                    predicateList.add(root.get("destArea").get("id").in(childIds));
//                }
//                //市
//                if (helpBuyTableDTO.getCityToId() != null){
//                    //找到所有的子节点
//                    List<Long> childIds = hbzAreaRepository.findAreaIdsByParentId(helpBuyTableDTO.getCityToId());
//
//                    predicateList.add(root.get("destArea").get("id").in(childIds));
//                }
//                //区
//                if (helpBuyTableDTO.getCountyToId() != null){
//
//                    predicateList.add(criteriaBuilder.equal(root.get("destArea").get("id"),helpBuyTableDTO.getCountyToId()));
//                }



                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));

            }
        },pageable);
        return converterDto(page);
    }




    private Page<HelpBuyTableDTO> converterDto(Page<HbzBuyOrder> page) {
        return  page.map(new Converter<HbzBuyOrder, HelpBuyTableDTO>() {
            @Override
            public HelpBuyTableDTO convert(HbzBuyOrder hbzBuyOrder) {
                HelpBuyTableDTO helpBuyTableDTO=new HelpBuyTableDTO();
                helpBuyTableDTO.setId(hbzBuyOrder.getId());
                helpBuyTableDTO.setOrderNo(hbzBuyOrder.getOrderNo());
                if (hbzBuyOrder.getCreateUser() !=null && hbzBuyOrder.getCreateUser().getEnt() !=null){
                    //归属公司
                    helpBuyTableDTO.setOrganizationName(hbzBuyOrder.getCreateUser().getEnt().getOrganizationName());
                }else {
                    helpBuyTableDTO.setOrganizationName("- -");

                }
                helpBuyTableDTO.setCommodityName(hbzBuyOrder.getCommodityName());
                helpBuyTableDTO.setCommodityCount(hbzBuyOrder.getCommodityCount());
                helpBuyTableDTO.setCommodityAmount(hbzBuyOrder.getCommodityAmount());
                helpBuyTableDTO.setRemuneration(hbzBuyOrder.getRemuneration());
                if (hbzBuyOrder.getCreateUser() !=null){
                    helpBuyTableDTO.setCreateUser(hbzBuyOrder.getCreateUser().getNickName());
                    helpBuyTableDTO.setCreateUserTelephone(hbzBuyOrder.getCreateUser().getTelephone());
                }
                FormatedDate logIn2 = new FormatedDate(hbzBuyOrder.getCreatedDate());
                String creatTime = logIn2.getFormat("yyyy-MM-dd HH:mm");
                helpBuyTableDTO.setCreatTime(creatTime);
                if (hbzBuyOrder.getTimeLimit() !=null){
                    helpBuyTableDTO.setTimeLimit(hbzBuyOrder.getTimeLimit());
                    helpBuyTableDTO.setTimeLimitValue(hbzBuyOrder.getTimeLimit().getName());
                }
                FormatedDate logIn3 = new FormatedDate(hbzBuyOrder.getStartTime());
                String startTime = logIn3.getFormat("yyyy-MM-dd HH:mm");
                helpBuyTableDTO.setStartTime(startTime);
                if (hbzBuyOrder.getTakeUser() !=null){
                    helpBuyTableDTO.setTeakUser(hbzBuyOrder.getTakeUser().getNickName());
                    helpBuyTableDTO.setTeakUserTelephone(hbzBuyOrder.getTakeUser().getTelephone());
                }
                //配送地址
                helpBuyTableDTO.setDestInfo(hbzBuyOrder.getDestInfo());
                helpBuyTableDTO.setOrderTrans(hbzBuyOrder.getOrderTrans());
                helpBuyTableDTO.setOrderTransValue(hbzBuyOrder.getOrderTrans().getName());

                HbzArea currentLevelArea = hbzBuyOrder.getDestArea();
                LinkedList<Long> endList=new LinkedList<>();
                StringBuilder ss = new StringBuilder();
                if (currentLevelArea !=null) {
                    while (currentLevelArea.getLevel() > 0) {
                        ss.insert(0, currentLevelArea.getAreaName() + " ");
                        currentLevelArea = currentLevelArea.getParent();
                    }
                    helpBuyTableDTO.setDestArea(ss.toString());
                }

//                StringBuffer sbf = new StringBuffer();
//                if (hbzBuyOrder.getDestArea() != null) {
//                    Integer level = hbzBuyOrder.getDestArea().getLevel();
//                    if (level == 1) {//省
//                        sbf.append(hbzBuyOrder.getDestArea().getAreaName());
//                    } else if (level == 2) {//市
//                        HbzArea hbzAreaCity = hbzBuyOrder.getDestArea();
//                        sbf.append(hbzAreaCity.getAreaName());
//                        sbf.insert(0, hbzAreaCity.getParent().getAreaName() + " ");
//                        helpBuyTableDTO.setDestArea(sbf.toString());
//                    } else if (level == 3) {//区县
//                        HbzArea hbzAreaCounty = hbzBuyOrder.getDestArea();
//                        HbzArea hbzAreaCity = hbzAreaCounty.getParent();
//                        HbzArea hbzAreaPrivice = hbzAreaCity.getParent();
//                        helpBuyTableDTO.setDestArea(hbzAreaPrivice.getAreaName() + " " + hbzAreaCity.getAreaName() + " " + hbzAreaCounty.getAreaName());
//                    }
//                }

                return helpBuyTableDTO;
            }
        });
    }
    //订单详情按钮
    @Override
    public HelpBuyOrderDetailsDTO findHelpBuyOrderDetails(HelpBuyOrderDetailsDTO helpBuyOrderDetailsDTO) {
        HbzBuyOrder hbzBuyOrder=  managerHbzBuyOrderRepository.findHbzBuyOrder(helpBuyOrderDetailsDTO.getId());
        HelpBuyOrderDetailsDTO hby=new HelpBuyOrderDetailsDTO();
        if (hbzBuyOrder !=null){
            hby.setId(hbzBuyOrder.getId());
            //配送地址
            hby.setDestInfo(hbzBuyOrder.getDestInfo());
            hby.setCommodityName(hbzBuyOrder.getCommodityName());
            hby.setCommodityCount(hbzBuyOrder.getCommodityCount());
            hby.setCommodityAmount(hbzBuyOrder.getCommodityAmount());
            hby.setBuyNeedInfo(hbzBuyOrder.getBuyNeedInfo());
            hby.setDestAddress(hbzBuyOrder.getDestAddress());
            hby.setLinker(hbzBuyOrder.getLinker());
            hby.setLinkTelephone(hbzBuyOrder.getLinkTelephone());
            hby.setTimeLimit(hbzBuyOrder.getTimeLimit());
            hby.setTimeLimitValue(hbzBuyOrder.getTimeLimit().getName());
            FormatedDate long2 = new FormatedDate(hbzBuyOrder.getStartTime());
            String startTime = long2.getFormat("yyyy-MM-dd HH:mm");
            hby.setStartTime(startTime);
            hby.setRemuneration(hbzBuyOrder.getRemuneration());
            if (hbzBuyOrder.getCreateUser() !=null){
                hby.setCreateUser(hbzBuyOrder.getCreateUser().getNickName());
                hby.setCreateUserTelephone(hbzBuyOrder.getCreateUser().getTelephone());
            }
            if (hbzBuyOrder.getCreateUser() !=null && hbzBuyOrder.getCreateUser().getEnt() !=null){
                hby.setOrg(hbzBuyOrder.getCreateUser().getEnt().getOrganizationName());
            }
            hby.setOrderNo(hbzBuyOrder.getOrderNo());
            hby.setOrderTrans(hbzBuyOrder.getOrderTrans());
            hby.setOrderTransValue(hbzBuyOrder.getOrderTrans().getName());
            hby.setRelatedPictures(hbzBuyOrder.getRelatedPictures());
        }



        return hby;
    }
    //接单人详情
    @Override
    public HbzBuyOrderDTO findHbzBuyOrder(HbzBuyOrderDTO hbzBuyOrderDTO) {
        HbzBuyOrder hbzBuyOrder= managerHbzBuyOrderRepository.findHbzBuyOrder(hbzBuyOrderDTO.getId());


        return hbzBuyOrderMapper.map(hbzBuyOrder);
    }

  //物流详情按钮
    @Override
    public List<HelpBuyDetailsDTO> findHelpBuyDetails(HelpBuyDetailsDTO helpBuyDetailsDTO) {
        List<HelpBuyDetailsDTO> list=new ArrayList<>();
        int[] number={1,2,3};
     for(int i=0;i<number.length;i++) {
         HelpBuyDetailsDTO hd = new HelpBuyDetailsDTO();
         hd.setA("2017年10月27日20:20 签收");
         hd.setB("2017年10月27日19:30 购买商品");
         hd.setC("2017年10月27日19:20 成都金牛区XXX路XXX11号接运收件");
         String a = "2017年10月27日19:30";
         String b = "茶店子派送点";
         hd.setInformation(a + b);
         hd.setId(1l);
         list.add(hd);
     }
        return list;
    }
   //平台付款详情
    @Override
    public PlatformDetailsDTO findPlatformDetails(PlatformDetailsDTO platformDetailsDTO) {
        PlatformDetailsDTO pd=new PlatformDetailsDTO();
        HbzOrder hbzOrder = hbzOrderRepository.findOne(platformDetailsDTO.getId());
        if (hbzOrder!=null) {
            HbzPay hbzPay = hbzPayRepository.findByBusinessNo(hbzOrder.getOrderNo());
            if(hbzPay!=null) {
                pd.setPaymentNumber(hbzPay.getTradeNo());
                //pd.setAccountType("个人");
                pd.setPaymentAmount(hbzPay.getFee() + "");
                //pd.setPaymentAccount("280198012312128");
                pd.setPayerName(hbzUserRepository.findOne(hbzPay.getCreatedBy()).getNickName());
                FormatedDate long2 = new FormatedDate(hbzPay.getCreatedDate());
                String startTime = long2.getFormat("yyyy-MM-dd HH:mm:ss");
                pd.setPaymentDate(startTime);
            }
        }
        return pd;
    }


}
