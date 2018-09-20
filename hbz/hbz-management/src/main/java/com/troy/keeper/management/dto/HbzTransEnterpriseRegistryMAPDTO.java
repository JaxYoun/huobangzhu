package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.type.CertificateType;
import com.troy.keeper.hbz.type.RegistryCode;
import com.troy.keeper.hbz.type.RegistryProgress;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by leecheng on 2017/11/20.
 */
@Getter
@Setter
public class HbzTransEnterpriseRegistryMAPDTO {

    //@ValueFormat(validations = {@Validation(use = "trans_enterprise_registry", format = ValidConstants.NULL, msg = "注册时不能指定具体id")})
    private Long id;

    private HbzUserDTO user;

    private Long userId;

    //证件照片Base64编码
    //@ValueFormat(validations = {@Validation(use = "trans_enterprise_registry", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")})
    private String certificates;

    //注册类型
    //@ValueFormat(validations = {@Validation(use = "trans_enterprise_registry", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")})
    private RegistryCode registryCode;

    //证件类型
    //@ValueFormat(validations = {@Validation(use = "trans_enterprise_registry", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")})
    private CertificateType certificateType;

    //证件编号
    //@ValueFormat(validations = {@Validation(use = "trans_enterprise_registry", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")})
    private String certificateNo;

    //名称
    //@ValueFormat(validations = {@Validation(use = "trans_enterprise_registry", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")})
    private String owerName;

    //时间 yyyy-MM-dd
    private String owerCreateTime;

    //注册进度
    private RegistryProgress registryProgress;
    private String organizationName;
    private String organizationCode;
    private String accountNo;
    private Double registryMoney;
    private String dutyParagraph;
    private String bank;
    private String businessLicense;
}
