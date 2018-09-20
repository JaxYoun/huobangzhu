package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.type.CertificateType;
import com.troy.keeper.hbz.type.RegistryCode;
import com.troy.keeper.hbz.type.RegistryProgress;
import lombok.Data;

/**
 * Created by leecheng on 2018/1/4.
 */
@Data
public class HbzUserRegistryMapDTO extends HbzBaseMapDTO {

    Long id;

    //关联用户
    private HbzUserDTO user;

    private Long userId;

    private String nickName;

    private String login;

    private String telephone;

    //证件照片Base64编码
    private String certificates;

    //注册类型
    private RegistryCode registryCode;

    //证件类型
    private CertificateType certificateType;

    //证件编号
    private String certificateNo;

    //名称
    private String owerName;

    //时间
    private String owerCreateTime;

    //注册进度
    private RegistryProgress registryProgress;
}
