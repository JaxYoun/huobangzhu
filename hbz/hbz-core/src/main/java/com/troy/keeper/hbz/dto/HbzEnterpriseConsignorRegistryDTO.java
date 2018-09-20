package com.troy.keeper.hbz.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by leecheng on 2017/11/17.
 */
@Getter
@Setter
public class HbzEnterpriseConsignorRegistryDTO extends HbzUserRegistryDTO {

    private String organizationCode;
    private String organizationName;
    private String accountNo;
    private String dutyParagraph;
    private Double registryMoney;
    private String bank;
    private String businessLicense;


}
