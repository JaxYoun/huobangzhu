package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.type.TransType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 李奥   接单人详情 的DTO
 * @date 2017/12/8.
 */
@Getter
@Setter
public class TeakUserInformationDTO {
    //订单id
    private Long id;
    //接单人id
    private Long takeUserId;
    //接单人姓名
    private String teakUser;
    // 接单人电话
    private String teakUserTelephone;
    //所属公司
    private String org;
    //接单车牌号
    private String licensePlateNumber;
    //接单车型
    private TransType transType;
    private String transTypeValue;
    //接单车长
    private Double carLength;
    //载重
    private Double load;
    //持证照片
    private String certifiedPhoto;
    //交通强制险照片
    private String strongInsuranceImage;
}
