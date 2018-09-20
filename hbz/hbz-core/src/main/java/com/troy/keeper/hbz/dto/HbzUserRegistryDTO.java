package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.type.CertificateType;
import com.troy.keeper.hbz.type.RegistryCode;
import com.troy.keeper.hbz.type.RegistryProgress;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by leecheng on 2017/10/25.
 */
@Getter
@Setter
public class HbzUserRegistryDTO extends BaseDTO {

    //关联用户
    private HbzUserDTO user;

    @QueryColumn(propName = "user.id")
    private Long userId;

    @QueryColumn(propName = "user.nickName",queryOper = "like")
    private String nickName;

    @QueryColumn(propName = "user.login", queryOper = "like")
    private String login;

    @QueryColumn(propName = "user.telephone", queryOper = "like")
    private String telephone;

    //证件照片Base64编码
    private String certificates;

    //注册类型
    @QueryColumn
    private RegistryCode registryCode;

    //证件类型
    @QueryColumn
    private CertificateType certificateType;

    //证件编号
    @QueryColumn
    private String certificateNo;

    //名称
    @QueryColumn
    private String owerName;

    //时间
    private Long owerCreateTime;

    //注册进度
    @QueryColumn
    private RegistryProgress registryProgress;

}
