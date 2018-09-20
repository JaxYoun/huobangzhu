package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzExOrderDTO;
import com.troy.keeper.hbz.po.HbzExOrder;

/**
 * Created by leecheng on 2017/11/29.
 */
public interface HbzExOrderService extends BaseEntityService<HbzExOrder, HbzExOrderDTO> {

    HbzExOrderDTO findByOrderNo(String orderNo);

}
