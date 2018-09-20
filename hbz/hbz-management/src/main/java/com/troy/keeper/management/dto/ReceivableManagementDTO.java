package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.type.CopingStatusType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 李奥
 * @date 2018/1/23.
 */
@Getter
@Setter
public class ReceivableManagementDTO {

    private Long  id;

    private Long  subjectManagementId;

    //应付编码
    private String  coding;

    //订单来源  1--代表收运订单 0--代表杂项
    private  String  orderSource;

    //来源编码
    private String sourceCode;

    //科目名称
    private String  subjectName;


    //应付金额
    private  Double  amountsPayable;

    //已付款金额
    private Double  amountPaid;

    //应付状态
    private CopingStatusType copingStatus;
    private String  copingStatusValue;

    //收款方公司名称
    private String  companyName;


    //联系人
    private String  contact;

    //联系方式
    private  String contactPhone;

    //收款方银行
    private  String  bank;

    //收款方账号
    private  String  payeeAccount;


    //记录状态  1--代表正常   0---代表作废
    private  String  recordStatus;


    //科目管理的一对多关系
    private SubjectManagementDTO subjectManagementDTO;


    //收货信息表中的
    private CargoInformationDTO cargoInformationDTO;


    //该收货的信息属于那个站点 新建的收货信息
    private Long smOrgId;

}
