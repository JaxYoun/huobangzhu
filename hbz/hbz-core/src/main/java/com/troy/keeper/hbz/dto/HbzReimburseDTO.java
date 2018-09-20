package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.PayType;
import com.troy.keeper.hbz.type.ReimburseProgress;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by leecheng on 2018/1/3.
 */
@Setter
@Getter
public class HbzReimburseDTO extends BaseDTO {

    private BusinessType businessType;
    private String businessNo;
    private String tradeNo;
    private Double fee;
    private String reimburseNo;
    private PayType payType;
    private ReimburseProgress reProgress;

}
