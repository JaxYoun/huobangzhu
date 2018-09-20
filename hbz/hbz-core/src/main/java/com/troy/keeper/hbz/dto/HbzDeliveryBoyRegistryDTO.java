package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.type.ReceiveAccountType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by leecheng on 2017/11/17.
 */
@Getter
@Setter
public class HbzDeliveryBoyRegistryDTO extends HbzUserRegistryDTO {

    private ReceiveAccountType receiveAccountType;

    private String receiveAccount;

    private String certifiedPhoto;

}
