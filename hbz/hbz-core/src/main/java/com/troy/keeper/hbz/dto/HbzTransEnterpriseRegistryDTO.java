package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.type.ReceiveAccountType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by leecheng on 2017/11/20.
 */
@Getter
@Setter
public class HbzTransEnterpriseRegistryDTO extends HbzUserRegistryDTO {

    @QueryColumn
    private ReceiveAccountType receiveAccountType;
    @QueryColumn
    private String receiveAccount;
    @QueryColumn
    private String organizationCode;
    @QueryColumn
    private String organizationName;
    @QueryColumn
    private String accountNo;
    private String dutyParagraph;
    private Double registryMoney;
    @QueryColumn
    private String bank;
    private String businessLicense;

}
