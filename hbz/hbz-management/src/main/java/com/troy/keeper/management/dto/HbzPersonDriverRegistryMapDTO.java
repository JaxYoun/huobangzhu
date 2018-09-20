package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import com.troy.keeper.hbz.type.CertificateType;
import com.troy.keeper.hbz.type.TransType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by leecheng on 2017/11/16.
 */
@Setter
@Getter
public class HbzPersonDriverRegistryMapDTO {

    private Long id;

    //关联用户
    private HbzUserDTO user;

    private Long userId;

    //交通强制险照片
    private String strongInsuranceImage;

    //证件照片Base64编码
    @ValueFormat(validations = {@Validation(use = "person_driver_registry_submit", format = ValidConstants.NOT_NULL, msg = "证件照片[{fieldName}]不能为空")})
    private String certificates;

    //证件类型
    private CertificateType certificateType;

    //证件编号
    //@ValueFormat(validations = {@Validation(use = "person_driver_registry_submit", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")})
    private String certificateNo;

    //名称
    //@ValueFormat(validations = {@Validation(use = "person_driver_registry_submit", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")})
    private String owerName;

    //时间
    private String owerCreateTime;

    //持证照片
    @ValueFormat(validations = {@Validation(use = "person_driver_registry_submit", format = ValidConstants.NOT_NULL, msg = "持证照片[{fieldName}]不能为空")})
    private String certifiedPhoto;

    //驾照有效期
    //@ValueFormat(validations = {@Validation(use = "person_driver_registry_submit", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")})
    private String drivingValidity;

    private String licensePlateNumber;

    private String vehicle45degreePhoto;

    private TransType transType;

    //车长
    private Double transLength;

    //最小载重
    private Double load;

    //驾驶证
    @ValueFormat(validations = {@Validation(use = "person_driver_registry_submit", format = ValidConstants.NOT_NULL, msg = "驾驶证[{fieldName}]不能为空")})
    private String drivingLicense;

    //@ValueFormat(validations = {@Validation(use = "person_driver_registry_submit", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")})
    private String drivingLicense2;

    //行驶证正面
    @ValueFormat(validations = {@Validation(use = "person_driver_registry_submit", format = ValidConstants.NOT_NULL, msg = "行驶证[{fieldName}]不能为空")})
    private String plyLicense;

    //行驶证另一面
    //@ValueFormat(validations = {@Validation(use = "person_driver_registry_submit", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")})
    private String plyLicense2;
}
