package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.type.ReceiveAccountType;
import com.troy.keeper.hbz.type.TransType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by leecheng on 2017/11/6.
 */
@Getter
@Setter
public class HbzPersonDriverRegistryDTO extends HbzUserRegistryDTO {

    private ReceiveAccountType receiveAccountType;

    private String receiveAccount;

    //持证照片
    @QueryColumn
    private String certifiedPhoto;

    //驾照有效期
    @QueryColumn
    private Long drivingValidity;

    @QueryColumn
    private String strongInsuranceImage;

    @QueryColumn
    private String licensePlateNumber;
    //驾驶证
    @QueryColumn
    private String drivingLicense;

    @QueryColumn
    private String drivingLicense2;

    @QueryColumn
    private String vehicle45degreePhoto;

    @QueryColumn
    private TransType transType;

    @QueryColumn
    private Double transLength;

    @QueryColumn
    private Double load;

    //行驶证正面
    @QueryColumn
    private String plyLicense;

    //行驶证另一面
    @QueryColumn
    private String plyLicense2;

}
