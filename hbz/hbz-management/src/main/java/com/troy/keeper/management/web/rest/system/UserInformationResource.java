package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.management.dto.UserInformationDTO;
import com.troy.keeper.management.service.UserInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李奥
 * @date 2017/12/23.
 */
@RestController
public class UserInformationResource {

    @Autowired
    private UserInformationService userInformationService;


    //分页查询
    @RequestMapping("/api/manager/userInformationTable")
    public ResponseDTO userInformationTable(@RequestBody UserInformationDTO userInformationDTO, Pageable pageable){
        return new ResponseDTO("200", "客户信息列表分页查询",userInformationService.findByCondition(userInformationDTO,pageable));
    }

    //新增客户信息

    @RequestMapping("/api/manager/addUserInformation")
    public ResponseDTO addUserInformation(@RequestBody UserInformationDTO userInformationDTO){
      Boolean s=  userInformationService.idCard(userInformationDTO);
      Boolean s1=  userInformationService.bankAccount(userInformationDTO);
    if (s==false){
        return new ResponseDTO("401", "新增身份证号码已存在",s);
    }
    if (s1==false){
        return new ResponseDTO("401", "新增银行账号已存在",s1);
    }

        return new ResponseDTO("200", "新增成功",userInformationService.addUserInformation(userInformationDTO));
    }

    //修改客户信息
    @RequestMapping("/api/manager/updateUserInformation")
    public ResponseDTO updateUserInformation(@RequestBody UserInformationDTO userInformationDTO){
        Boolean str=  userInformationService.checkIdCard(userInformationDTO);
         Boolean s= userInformationService.checkBankAccount(userInformationDTO);

        if (str ==false){
            return new ResponseDTO("401", "修改时身份证号码已存在",str);
        }
        if (s==false){
            return new ResponseDTO("401", "修改时银行卡号已存在",s);
        }

            return new ResponseDTO("200", "修改成功",userInformationService.updateUserInformation(userInformationDTO));



    }

    //删除客户信息
    @RequestMapping("/api/manager/delateUserInformation")
    public ResponseDTO delateUserInformation(@RequestBody UserInformationDTO userInformationDTO){


            return new ResponseDTO("200", "删除成功",userInformationService.delateUserInformation(userInformationDTO));


    }









}
