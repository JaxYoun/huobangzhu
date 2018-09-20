package com.troy.keeper.management.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 李奥
 * @date 2017/12/23.
 */
@Getter
@Setter
public class UserInformationDTO {

    //id
    private Long id;

    //单位名称
    private  String  companyName;

    //客户分类
    private  String  userClassification;
    private String   userClassificationValue;

    //联系人姓名
    private String userName;

    //电话
    private String  userTelephone;

    //身份证
    private  String  idCard;

    //联系地址
    private  String  userAddress;

    //开户行
    private String bank;
    private  String bankValue;


    //银行账号
    private  Long bankAccount;

    //单位名称简评
    private  String  jianpin;

    //备注
    private  String  remarks;

    //返回信息
    private String  msg;

    //状态
    private String dataStatus;


}
