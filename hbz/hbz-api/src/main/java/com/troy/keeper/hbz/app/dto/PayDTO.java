package com.troy.keeper.hbz.app.dto;

import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.PayType;
import lombok.Data;

/**
 * Created by leecheng on 2017/12/28.
 */
@Data
public class PayDTO {

    private String orderNo;

    private PayType payType;

    private BusinessType businessType;
}
