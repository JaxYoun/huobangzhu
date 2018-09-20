package com.troy.keeper.management.service.impl;

import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.hbz.po.CargoInformation;
import com.troy.keeper.hbz.po.DealManagement;
import com.troy.keeper.hbz.po.OutsourcingGoods;
import com.troy.keeper.hbz.po.SubjectManagement;
import com.troy.keeper.hbz.type.CopingStatusType;
import com.troy.keeper.hbz.type.ShippingStatus;
import com.troy.keeper.management.dto.CargoInformationDTO;
import com.troy.keeper.management.dto.DealManagementDTO;
import com.troy.keeper.management.dto.SubjectManagementDTO;
import com.troy.keeper.management.repository.CargoInformationRepository;
import com.troy.keeper.management.repository.DealManagementRepository;
import com.troy.keeper.management.repository.SubjectManagementRepository;
import com.troy.keeper.management.repository.UserInformationRepository;
import com.troy.keeper.management.service.DealManagementService;
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

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author 李奥
 * @date 2018/1/22.
 */
@Service
@Transactional
public class DealManagementServiceImpl implements DealManagementService {

    @Autowired
    private DealManagementRepository dealManagementRepository;

    //科目管理基本信息
     @Autowired
    private SubjectManagementRepository subjectManagementRepository;
     //货物分页 查询
    @Autowired
    private CargoInformationRepository cargoInformationRepository;

    @Autowired
    private UserInformationRepository userInformationRepository;




    //科目应付分页
    @Override
    public Page<SubjectManagementDTO> findByCondition(SubjectManagementDTO subjectManagementDTO, Pageable pageable) {

        Page<SubjectManagement> page=subjectManagementRepository.findAll(new Specification<SubjectManagement>() {
            @Override
            public Predicate toPredicate(Root<SubjectManagement> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();
                   //应付状态的所有信息分页
                    predicateList.add(criteriaBuilder.equal(root.get("subjectType"),"0"));

                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));

            }
        },pageable);
        return converterDto(page);
    }




    private Page<SubjectManagementDTO> converterDto(Page<SubjectManagement> page) {
        return  page.map(new Converter<SubjectManagement, SubjectManagementDTO>() {
            @Override
            public SubjectManagementDTO convert(SubjectManagement subjectManagement) {

                SubjectManagementDTO sm=new SubjectManagementDTO();

                BeanUtils.copyProperties(subjectManagement,sm);
                return sm;
            }
        });
    }


//    //应付货物分页
//    @Override
//    public Page<CargoInformationDTO> findCCargoInformationPage(CargoInformationDTO cargoInformationDTO, Pageable pageable) {
//
//        Page<CargoInformation> page=cargoInformationRepository.findAll(new Specification<CargoInformation>() {
//            @Override
//            public Predicate toPredicate(Root<CargoInformation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//
//                List<Predicate> predicateList = new ArrayList<>();
//                //查询状态为 1 的货物信息
//                predicateList.add(criteriaBuilder.equal(root.get("status"),"1"));
//
//                Subquery subquery = criteriaQuery.subquery(Long.class);
//                Root<DealManagement> dealManagementRoot = subquery.from(DealManagement.class);
//                subquery.select(dealManagementRoot.join("cargoInformation").get("id"));
//
//                predicateList.add(criteriaBuilder.not(root.get("id").in(subquery)));
//
//
//                Predicate[] ps = new Predicate[predicateList.size()];
//                return criteriaBuilder.and(predicateList.toArray(ps));
//
//            }
//        },pageable);
//        return converterCargoInformationDto(page);
//    }
//
//
//
//
//    private Page<CargoInformationDTO> converterCargoInformationDto(Page<CargoInformation> page) {
//        return  page.map(new Converter<CargoInformation, CargoInformationDTO>() {
//            @Override
//            public CargoInformationDTO convert(CargoInformation cargoInformation) {
//
//                CargoInformationDTO ci=new CargoInformationDTO();
//
//                BeanUtils.copyProperties(cargoInformation,ci);
//                return ci;
//            }
//        });
//    }


    //应付表分页查询
    @Override
    public Page<DealManagementDTO> dealPage(DealManagementDTO dealManagementDTO, Pageable pageable) {
        //查询当前组织机构的下的 货物信息
        SmUser smUser=cargoInformationRepository.findSmOrgId(SecurityUtils.getCurrentUserId());
        List<SmPostUser> list1=  smUser.getSmPostUserList();
        Long a = null;
        for (int i = 0; i <list1.size() ; i++) {
            a = list1.get(i).getSmPost().getOrgId();
        }
        Long orgId =a;

        Page<DealManagement> page=dealManagementRepository.findAll(new Specification<DealManagement>() {
            @Override
            public Predicate toPredicate(Root<DealManagement> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(dealManagementDTO.getCoding())){
                //应付编码
                predicateList.add(criteriaBuilder.equal(root.get("coding"),dealManagementDTO.getCoding()));
            }
            if (StringUtils.isNotBlank(dealManagementDTO.getSubjectName())){
                    //科目名称
                predicateList.add(criteriaBuilder.like(root.get("subjectName"),dealManagementDTO.getSubjectName()));
            }
            if (StringUtils.isNotBlank(dealManagementDTO.getOrderSource())){
                    //来源类型
                    predicateList.add(criteriaBuilder.equal(root.get("orderSource"),dealManagementDTO.getOrderSource()));
            }
            if (dealManagementDTO.getCopingStatus() !=null){
                    //付款状态
                    predicateList.add(criteriaBuilder.equal(root.get("copingStatus"),dealManagementDTO.getCopingStatus()));
            }
            if (StringUtils.isNotBlank(dealManagementDTO.getRecordStatus())){
                    //记录状态
                    predicateList.add(criteriaBuilder.equal(root.get("recordStatus"),dealManagementDTO.getRecordStatus()));
                }
                //当前登录人 只能查看自己站点的 货物
                predicateList.add(criteriaBuilder.equal(root.get("smOrgId"),orgId));

                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));

            }
        },pageable);
        return converterDealManagementDto(page);
    }



    private Page<DealManagementDTO> converterDealManagementDto(Page<DealManagement> page) {
        return  page.map(new Converter<DealManagement, DealManagementDTO>() {
            @Override
            public DealManagementDTO convert(DealManagement dealManagement) {

                DealManagementDTO dm=new DealManagementDTO();

                BeanUtils.copyProperties(dealManagement,dm);
                if (dealManagement.getCopingStatus() !=null){
                    dm.setCopingStatusValue(dealManagement.getCopingStatus().getName());
                }
                if (dealManagement.getBank() !=null){
                    String name = userInformationRepository.findUserClassification("Bank", dealManagement.getBank());
                    dm.setBankName(name);
                }

                return dm;
            }
        });
    }


    //新建应付信息
    @Override
    public Boolean savedealManagement(DealManagementDTO dealManagementDTO) {
        //查询当前组织机构的下的 货物信息
        SmUser smUser=cargoInformationRepository.findSmOrgId(SecurityUtils.getCurrentUserId());
        List<SmPostUser> list1=  smUser.getSmPostUserList();
        Long aSmOrg = null;
        for (int i = 0; i <list1.size() ; i++) {
            aSmOrg = list1.get(i).getSmPost().getOrgId();
        }
        Long orgId =aSmOrg;

        DealManagement dm=new DealManagement();
        BeanUtils.copyProperties(dealManagementDTO,dm);
        dm.setSmOrgId(orgId);
        String coding= dealManagementRepository.coding();
        if (coding==null){
            dm.setCoding("000001");
        }else {
          String  a= String.valueOf( Integer.valueOf(coding)+1);
          if (a.length()==1){
              dm.setCoding("00000"+a);
          }else if (a.length()==2){
              dm.setCoding("0000"+a);
          }else if (a.length()==3){
              dm.setCoding("000"+a);
          }else if (a.length()==4){
              dm.setCoding("00"+a);
          }else if (a.length()==5){
              dm.setCoding("0"+a);
          }else {
              dm.setCoding(a);
          }


        }
//        if (dealManagementDTO.getSubjectManagementId() !=null){
//            SubjectManagement subjectManagement= subjectManagementRepository.findOne(dealManagementDTO.getSubjectManagementId());
//
//            dm.setCoding(subjectManagement.getSubjectCode());
//            dm.setSubjectName(subjectManagement.getSubjectName());
//        }

        dm.setOrderSource("0");
        //来源编码
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        String  str=sdf.format(d);
        String  a= str.replace("-","").replace(" ","").replace(":","");
        String  B=null;
        if (dealManagementRepository.sourceCode() !=null){
            String  maxTrackingNumber =dealManagementRepository.sourceCode().substring(14);
            String trackingNumber= String.valueOf(Integer.parseInt(maxTrackingNumber)+1);
            B=a+trackingNumber;
            dm.setSourceCode(B);
        }else {
            B=a+"1";
            dm.setSourceCode(B);
        }

        //应付状态  应付金额   已付金额
        Double amountsPayable= dealManagementDTO.getAmountsPayable();
        Double  amountPaid=dealManagementDTO.getAmountPaid();
        if (amountsPayable !=null && amountsPayable>0){
            if (amountPaid==null || amountPaid==0){
                //未付款
                dm.setCopingStatus(CopingStatusType.UNPAID);
            }else  if (amountsPayable>amountPaid && amountPaid>0){
                //部分付款
                dm.setCopingStatus(CopingStatusType.PARTIAL_PAYMENT);
            }else{
                //付款完成
                dm.setCopingStatus(CopingStatusType.PAYMENT_COMPLETED);
            }
        }
        dm.setRecordStatus("1");


        dealManagementRepository.save(dm);

        return true;
    }

    //编辑某一个应付信息
    @Override
    public Boolean updateDealManagement(DealManagementDTO dealManagementDTO) {

        if (dealManagementDTO.getId() !=null){

            DealManagement dealManagement= dealManagementRepository.findOne(dealManagementDTO.getId());
            if (dealManagement !=null){
                String orderSource=  dealManagement.getOrderSource();
                if ("1".equals(orderSource)){//应付信息来源订单
                    dealManagement.setSmOrgId(dealManagement.getSmOrgId());
                    //订单来源--就只能编辑已收金额以后的信息
                    dealManagement.setCoding(dealManagement.getCoding());
                    dealManagement.setOrderSource(dealManagement.getOrderSource());
                    dealManagement.setSourceCode(dealManagement.getSourceCode());
                    dealManagement.setSubjectName(dealManagement.getSubjectName());
                    //应付金额
                    Double amountsPayable= dealManagement.getAmountsPayable();
                    dealManagement.setAmountsPayable(amountsPayable);
                    dealManagement.setAmountPaid(dealManagementDTO.getAmountPaid());
                    //应付状态     已付金额
                    Double  amountPaid=dealManagementDTO.getAmountPaid();
                    if (amountsPayable !=null && amountsPayable>0){
                        if (amountPaid==null || amountPaid==0){
                            //未付款
                            dealManagement.setCopingStatus(CopingStatusType.UNPAID);
                        }else  if (amountsPayable>amountPaid && amountPaid>0){
                            //部分付款
                            dealManagement.setCopingStatus(CopingStatusType.PARTIAL_PAYMENT);
                        }else{
                            //付款完成
                            dealManagement.setCopingStatus(CopingStatusType.PAYMENT_COMPLETED);
                        }
                    }
                    //收款方名称
                    dealManagement.setCompanyName(dealManagementDTO.getCompanyName());
                    dealManagement.setContact(dealManagementDTO.getContact());
                    dealManagement.setContactPhone(dealManagementDTO.getContactPhone());
                    dealManagement.setBank(dealManagementDTO.getBank());
                    dealManagement.setPayeeAccount(dealManagementDTO.getPayeeAccount());
                    dealManagement.setRecordStatus(dealManagementDTO.getRecordStatus());

                    dealManagementRepository.save(dealManagement);
                }

                if ("0".equals(orderSource)){//订单来源是杂项--所有的都可以修改
                    dealManagement.setSmOrgId(dealManagement.getSmOrgId());
                    dealManagement.setCoding(dealManagement.getCoding());
                    dealManagement.setOrderSource(dealManagement.getOrderSource());
                    dealManagement.setSourceCode(dealManagement.getSourceCode());

                    //收款方名称
                    dealManagement.setAmountsPayable(dealManagementDTO.getAmountsPayable());
                    dealManagement.setSubjectName(dealManagementDTO.getSubjectName());
                    dealManagement.setCompanyName(dealManagementDTO.getCompanyName());
                    dealManagement.setContact(dealManagementDTO.getContact());
                    dealManagement.setContactPhone(dealManagementDTO.getContactPhone());
                    dealManagement.setBank(dealManagementDTO.getBank());
                    dealManagement.setPayeeAccount(dealManagementDTO.getPayeeAccount());
                    dealManagement.setRecordStatus(dealManagementDTO.getRecordStatus());

                    //应付金额
                    Double amountsPayable= dealManagement.getAmountsPayable();
                    //应付状态     已付金额
                    Double  amountPaid=dealManagementDTO.getAmountPaid();
                    dealManagement.setAmountPaid(amountPaid);
                    if (amountsPayable !=null && amountsPayable>0){
                        if (amountPaid==null || amountPaid==0){
                            //未付款
                            dealManagement.setCopingStatus(CopingStatusType.UNPAID);
                        }else  if (amountsPayable>amountPaid && amountPaid>0){
                            //部分付款
                            dealManagement.setCopingStatus(CopingStatusType.PARTIAL_PAYMENT);
                        }else{
                            //付款完成
                            dealManagement.setCopingStatus(CopingStatusType.PAYMENT_COMPLETED);
                        }
                    }

                    dealManagementRepository.save(dealManagement);
                }

            }

        }



        return true;
    }

    //作废功能
    @Override
    public Boolean updateRecordStatus(DealManagementDTO dealManagementDTO) {
       if (dealManagementDTO.getId() !=null){
           dealManagementRepository.updateRecordStatus(dealManagementDTO.getId());
       }

        return true;
    }












}
