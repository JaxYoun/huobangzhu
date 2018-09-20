package com.troy.keeper.management.service.impl;

import com.troy.keeper.hbz.po.UserInformation;
import com.troy.keeper.management.dto.UserInformationDTO;
import com.troy.keeper.management.repository.UserInformationRepository;
import com.troy.keeper.management.service.UserInformationService;
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


/**
 * @author 李奥
 * @date 2017/12/23.
 */
@Service
@Transactional
public class UserInformationServiceImpl implements UserInformationService {

    @Autowired
    private UserInformationRepository userInformationRepository;

    //分页查询
    @Override
    public Page<UserInformationDTO> findByCondition(UserInformationDTO userInformationDTO, Pageable pageable) {
             userInformationDTO.setDataStatus("1");


        Page<UserInformation> page=userInformationRepository.findAll(new Specification<UserInformation>() {
            @Override
            public Predicate toPredicate(Root<UserInformation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();
                if (StringUtils.isNotBlank(userInformationDTO.getCompanyName())){
                    //单位名称
                    predicateList.add(criteriaBuilder.like(root.get("companyName"),"%"+userInformationDTO.getCompanyName()+"%"));
                }
                if (StringUtils.isNotBlank(userInformationDTO.getUserTelephone())){
                    //电话
                    predicateList.add(criteriaBuilder.equal(root.get("userTelephone"),userInformationDTO.getUserTelephone()));
                }
                if (StringUtils.isNotBlank(userInformationDTO.getUserAddress())){
                    //地址
                    predicateList.add(criteriaBuilder.like(root.get("userAddress"),"%"+userInformationDTO.getUserAddress()+"%"));
                }
                if (StringUtils.isNotBlank(userInformationDTO.getJianpin())){
                    //简拼
                    predicateList.add(criteriaBuilder.equal(root.get("jianpin"),userInformationDTO.getJianpin()));
                }
                if (userInformationDTO.getUserClassification() !=null){
                    //客户分类
                    predicateList.add(criteriaBuilder.equal(root.get("userClassification"),userInformationDTO.getUserClassification()));
                }
                predicateList.add(criteriaBuilder.equal(root.get("status"),userInformationDTO.getDataStatus()));




                Predicate[] ps = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(ps));

            }
        },pageable);
        return converterDto(page);
    }


    private Page<UserInformationDTO> converterDto(Page<UserInformation> page) {
        return  page.map(new Converter<UserInformation, UserInformationDTO>() {
            @Override
            public UserInformationDTO convert(UserInformation userInformation) {


                UserInformationDTO ui=new UserInformationDTO();
                BeanUtils.copyProperties(userInformation,ui);
                //下拉选  取数据字典的name值

                String  name= userInformationRepository.findUserClassification("UserClassification",userInformation.getUserClassification());
                ui.setUserClassificationValue(name);
                String  name1= userInformationRepository.findUserClassification("Bank",userInformation.getBank());
               ui.setBankValue(name1);
                return ui;
            }
        });
    }

    //新增客户信息
    @Override
    public Boolean addUserInformation(UserInformationDTO userInformationDTO) {

            UserInformation ui = new UserInformation();
            BeanUtils.copyProperties(userInformationDTO, ui);
            ui.setStatus("1");
            userInformationRepository.save(ui);
            return true;
    }
    //新增身份证验证重复
    @Override
    public Boolean idCard(UserInformationDTO userInformationDTO) {
        Long s=   userInformationRepository.idCard(userInformationDTO.getIdCard());
        if (s>0){
            return  false;
        }
        return true;
    }
    //新增银行账号重复
    @Override
    public Boolean bankAccount(UserInformationDTO userInformationDTO) {
        Long s=   userInformationRepository.bankAccount(userInformationDTO.getBankAccount());
        if (s>0){
            return  false;
        }
        return true;
    }

    //修改客户信息
    @Override
    public Boolean updateUserInformation(UserInformationDTO userInformationDTO) {

        UserInformation ui=new UserInformation();
        BeanUtils.copyProperties(userInformationDTO,ui);
        ui.setStatus("1");
        userInformationRepository.save(ui);
        return true;
    }

    //校验身份证号是否重复身份证号
    @Override
    public Boolean checkIdCard(UserInformationDTO userInformationDTO) {

      Long  str= userInformationRepository.checkIdCard(userInformationDTO.getIdCard(),userInformationDTO.getId());
        if (str >0){
            return  false;
        }else {

            return true;
        }
    }

    //校验银行账号是否重复
    @Override
    public Boolean checkBankAccount(UserInformationDTO userInformationDTO) {
        Long  s= userInformationRepository.checkBankAccount(userInformationDTO.getBankAccount(),userInformationDTO.getId());
    if (s>0){
        return false;
    }else {

        return true;
    }

//     List<UserInformation>  list = userInformationRepository.findByBankAccountAndAndIdNot(userInformationDTO.getBankAccount(),userInformationDTO.getId());
//     if (list.size()>0){
//         return  false;
//     }else {
//
//         return true;
//     }

    }



    //删除客户信息
    @Override
    public Boolean delateUserInformation(UserInformationDTO userInformationDTO) {

        userInformationRepository.deleteUserInformation(userInformationDTO.getId());

        return true;
    }


}
