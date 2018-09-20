package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzBillDTO;
import com.troy.keeper.hbz.dto.HbzRefundDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.po.HbzRefund;
import com.troy.keeper.hbz.type.BusinessType;

/**
 * Created by leecheng on 2018/3/6.
 */
public interface HbzRefundService extends BaseEntityService<HbzRefund, HbzRefundDTO> {

    String createRefundNo();

    String createReqNo();

    void bill(HbzBillDTO bill);

    boolean refund(String businessNo, BusinessType businessType, HbzUserDTO user);

    boolean refundImmediate(String businessNo, BusinessType businessType, HbzUserDTO user);

    public boolean refundWith(HbzRefundDTO hbzRefund);
}
