package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzBillDTO;
import com.troy.keeper.hbz.dto.HbzPayDTO;
import com.troy.keeper.hbz.po.HbzPay;
import com.troy.keeper.hbz.type.BusinessType;

/**
 * Created by leecheng on 2017/10/30.
 */
public interface HbzPayService extends BaseEntityService<HbzPay, HbzPayDTO> {

    String createTradeNo(BusinessType businessType);

    public default HbzPayDTO findByTradeNo(String tradeNo) {
        throw new IllegalStateException("错误");
    }

    HbzPay findByBusinessNo(String businessNo);

    void bill(HbzBillDTO bill);
}
