package com.troy.keeper.management.service.impl;

import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.hbz.po.*;
import com.troy.keeper.hbz.sys.FormatedDate;
import com.troy.keeper.hbz.type.CopingStatusType;
import com.troy.keeper.management.dto.DealManagementDTO;
import com.troy.keeper.management.dto.ReceivableManagementDTO;
import com.troy.keeper.management.dto.StartVehicleDTO;
import com.troy.keeper.management.repository.CargoInformationRepository;
import com.troy.keeper.management.repository.ReceivableManagementRepository;
import com.troy.keeper.management.repository.SubjectManagementRepository;
import com.troy.keeper.management.service.ReceivableManagementService;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 李奥
 * @date 2018/1/23.
 */
@Service
@Transactional
public class ReceivableManagementServiceImpl  implements ReceivableManagementService {

    @Autowired
    private ReceivableManagementRepository receivableManagementRepository;

    //科目管理基本信息
    @Autowired
    private SubjectManagementRepository subjectManagementRepository;

    //货物分页 查询
    @Autowired
    private CargoInformationRepository cargoInformationRepository;




   //应收分页查询
    @Override
    public Page<ReceivableManagementDTO> findConfirmed(ReceivableManagementDTO receivableManagementDTO, Pageable pageable) {

        //查询当前组织机构的下的 货物信息
        SmUser smUser=cargoInformationRepository.findSmOrgId(SecurityUtils.getCurrentUserId());
        List<SmPostUser> list1=  smUser.getSmPostUserList();
        Long a = null;
        for (int i = 0; i <list1.size() ; i++) {
            a = list1.get(i).getSmPost().getOrgId();
        }
        Long orgId =a;

        Page<ReceivableManagement> page=receivableManagementRepository.findAll(new Specification<ReceivableManagement>() {
            @Override
            public Predicate toPredicate(Root<ReceivableManagement> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();
                if (StringUtils.isNotBlank(receivableManagementDTO.getCoding())){
                    //应付编码
                    predicateList.add(criteriaBuilder.equal(root.get("coding"),receivableManagementDTO.getCoding()));
                }
                if (StringUtils.isNotBlank(receivableManagementDTO.getSubjectName())){
                    //科目名称
                    predicateList.add(criteriaBuilder.like(root.get("subjectName"),receivableManagementDTO.getSubjectName()));
                }
                if (StringUtils.isNotBlank(receivableManagementDTO.getOrderSource())){
                    //来源类型
                    predicateList.add(criteriaBuilder.equal(root.get("orderSource"),receivableManagementDTO.getOrderSource()));
                }
                if (receivableManagementDTO.getCopingStatus() !=null){
                    //付款状态
                    predicateList.add(criteriaBuilder.equal(root.get("copingStatus"),receivableManagementDTO.getCopingStatus()));
                }
                if (StringUtils.isNotBlank(receivableManagementDTO.getRecordStatus())){
                    //记录状态
                    predicateList.add(criteriaBuilder.equal(root.get("recordStatus"),receivableManagementDTO.getRecordStatus()));
                }
                //当前登录人 只能查看自己站点的 货物
                predicateList.add(criteriaBuilder.equal(root.get("smOrgId"),orgId));


                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));

            }
        },pageable);
        return converterDto(page);
    }


    private Page<ReceivableManagementDTO> converterDto(Page<ReceivableManagement> page) {
        return  page.map(new Converter<ReceivableManagement, ReceivableManagementDTO>() {
            @Override
            public ReceivableManagementDTO convert(ReceivableManagement receivableManagement) {
                ReceivableManagementDTO dm=new ReceivableManagementDTO();

                BeanUtils.copyProperties(receivableManagement,dm);
                if (receivableManagement.getCopingStatus() !=null){
                    dm.setCopingStatusValue(receivableManagement.getCopingStatus().getName());
                }

                return dm;
            }
        });
    }

    //新增应收的信息
    @Override
    public Boolean addReceivableManagement(ReceivableManagementDTO receivableManagementDTO) {
        //查询当前组织机构的下的 货物信息
        SmUser smUser=cargoInformationRepository.findSmOrgId(SecurityUtils.getCurrentUserId());
        List<SmPostUser> list1=  smUser.getSmPostUserList();
        Long aSmOrg = null;
        for (int i = 0; i <list1.size() ; i++) {
            aSmOrg = list1.get(i).getSmPost().getOrgId();
        }
        Long orgId =aSmOrg;

        ReceivableManagement rm=new ReceivableManagement();

        BeanUtils.copyProperties(receivableManagementDTO,rm);
        rm.setSmOrgId(orgId);
        String coding= receivableManagementRepository.coding();
        if (coding==null){
            rm.setCoding("000001");
        }else {
            String  a= String.valueOf( Integer.valueOf(coding)+1);
            if (a.length()==1){
                rm.setCoding("00000"+a);
            }else if (a.length()==2){
                rm.setCoding("0000"+a);
            }else if (a.length()==3){
                rm.setCoding("000"+a);
            }else if (a.length()==4){
                rm.setCoding("00"+a);
            }else if (a.length()==5){
                rm.setCoding("0"+a);
            }else {
                rm.setCoding(a);
            }
        }
//        if (receivableManagementDTO.getSubjectManagementId() !=null){
//            SubjectManagement subjectManagement= subjectManagementRepository.findOne(receivableManagementDTO.getSubjectManagementId());
//
//            rm.setCoding(subjectManagement.getSubjectCode());
//            rm.setSubjectName(subjectManagement.getSubjectName());
//        }
        rm.setOrderSource("0");

        //来源编码
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        String  str=sdf.format(d);
        String  a= str.replace("-","").replace(" ","").replace(":","");
        String  B=null;
        if (receivableManagementRepository.sourceCode() !=null){
            String  maxTrackingNumber =receivableManagementRepository.sourceCode().substring(14);
            String trackingNumber= String.valueOf(Integer.parseInt(maxTrackingNumber)+1);
            B=a+trackingNumber;
            rm.setSourceCode(B);
        }else {
            B=a+"1";
            rm.setSourceCode(B);
        }
        //应付状态  应付金额   已付金额
        Double amountsPayable= receivableManagementDTO.getAmountsPayable();
        Double  amountPaid=receivableManagementDTO.getAmountPaid();
        if (amountsPayable !=null && amountsPayable>0){
            if (amountPaid==null || amountPaid==0){
                //未付款
                rm.setCopingStatus(CopingStatusType.UNPAID);
            }else  if (amountsPayable>amountPaid && amountPaid>0){
                //部分付款
                rm.setCopingStatus(CopingStatusType.PARTIAL_PAYMENT);
            }else{
                //付款完成
                rm.setCopingStatus(CopingStatusType.PAYMENT_COMPLETED);
            }
        }
        rm.setRecordStatus("1");
        receivableManagementRepository.save(rm);

        return true;
    }

    //编辑应收信息
    @Override
    public Boolean updateRmInformation(ReceivableManagementDTO receivableManagementDTO) {
        if (receivableManagementDTO.getId() !=null){

            ReceivableManagement receivableManagement= receivableManagementRepository.findOne(receivableManagementDTO.getId());

            if (receivableManagement !=null){
                String orderSource=receivableManagement.getOrderSource();
                Double amountsPayable=receivableManagement.getAmountsPayable();
                if ("1".equals(orderSource)){
                    receivableManagement.setSmOrgId(receivableManagement.getSmOrgId());
                    receivableManagement.setCoding(receivableManagement.getCoding());
                    receivableManagement.setOrderSource(orderSource);
                    receivableManagement.setSourceCode(receivableManagement.getSourceCode());
                    receivableManagement.setSubjectName(receivableManagement.getSubjectName());
                    receivableManagement.setAmountsPayable(amountsPayable);
                    //已付金额
                    receivableManagement.setAmountPaid(receivableManagementDTO.getAmountPaid());
                    //应付状态  应付金额   已付金额
                    Double  amountPaid=receivableManagementDTO.getAmountPaid();
                    if (amountsPayable !=null && amountsPayable>0){
                        if (amountPaid==null || amountPaid==0){
                            //未付款
                            receivableManagement.setCopingStatus(CopingStatusType.UNPAID);
                        }else  if (amountsPayable>amountPaid && amountPaid>0){
                            //部分付款
                            receivableManagement.setCopingStatus(CopingStatusType.PARTIAL_PAYMENT);
                        }else{
                            //付款完成
                            receivableManagement.setCopingStatus(CopingStatusType.PAYMENT_COMPLETED);
                        }
                    }
                    receivableManagement.setCompanyName(receivableManagementDTO.getCompanyName());
                    receivableManagement.setContact(receivableManagementDTO.getContact());
                    receivableManagement.setContactPhone(receivableManagementDTO.getContactPhone());
                    receivableManagement.setRecordStatus(receivableManagementDTO.getRecordStatus());

                    receivableManagementRepository.save(receivableManagement);
                }
                if ("0".equals(orderSource)){
                    receivableManagement.setSmOrgId(receivableManagement.getSmOrgId());
                    receivableManagement.setCoding(receivableManagement.getCoding());
                    receivableManagement.setOrderSource(orderSource);
                    receivableManagement.setSourceCode(receivableManagement.getSourceCode());
                    //科目名称
                    receivableManagement.setSubjectName(receivableManagementDTO.getSubjectName());
                    receivableManagement.setAmountsPayable(receivableManagementDTO.getAmountsPayable());
                    receivableManagement.setAmountPaid(receivableManagementDTO.getAmountPaid());
                    //应付状态  应付金额   已付金额
                    Double  amountPaid=receivableManagementDTO.getAmountPaid();
                    if (amountsPayable !=null && amountsPayable>0){
                        if (amountPaid==null || amountPaid==0){
                            //未付款
                            receivableManagement.setCopingStatus(CopingStatusType.UNPAID);
                        }else  if (amountsPayable>amountPaid && amountPaid>0){
                            //部分付款
                            receivableManagement.setCopingStatus(CopingStatusType.PARTIAL_PAYMENT);
                        }else{
                            //付款完成
                            receivableManagement.setCopingStatus(CopingStatusType.PAYMENT_COMPLETED);
                        }
                    }
                    receivableManagement.setCompanyName(receivableManagementDTO.getCompanyName());
                    receivableManagement.setContact(receivableManagementDTO.getContact());
                    receivableManagement.setContactPhone(receivableManagementDTO.getContactPhone());
                    receivableManagement.setRecordStatus(receivableManagementDTO.getRecordStatus());
                    receivableManagementRepository.save(receivableManagement);

                }
            }
        }

        return true;
    }


    //修改应收信息的记录状态
    @Override
    public Boolean updateRecordStatus(ReceivableManagementDTO receivableManagementDTO) {

        if (receivableManagementDTO.getId() !=null){
            receivableManagementRepository.updateRecordStatus(receivableManagementDTO.getId());
            return true;
        }else {
            return false;
        }

    }









}
