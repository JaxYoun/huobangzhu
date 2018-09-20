package com.troy.keeper.management.service;

import com.troy.keeper.management.dto.UserInformationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author 李奥
 * @date 2017/12/23.
 */
public interface UserInformationService {

    //分页
    public Page<UserInformationDTO> findByCondition(UserInformationDTO userInformationDTO, Pageable pageable);

    //新增客户信息
    public Boolean addUserInformation(UserInformationDTO userInformationDTO);

    //新增身份证验证重复
    public  Boolean idCard(UserInformationDTO userInformationDTO);
    //新增银行账号重复
    public  Boolean bankAccount(UserInformationDTO userInformationDTO);




    //修改客户信息
    public Boolean updateUserInformation(UserInformationDTO userInformationDTO);

    //校验身份证号是否重复身份证号
    public  Boolean checkIdCard(UserInformationDTO userInformationDTO);

    //校验银行账号是否重复
    public  Boolean checkBankAccount(UserInformationDTO userInformationDTO);

    //删除客户信息
    public Boolean delateUserInformation(UserInformationDTO userInformationDTO);




}
