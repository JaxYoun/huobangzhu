package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzSendOrderDTO;
import com.troy.keeper.hbz.po.HbzSendOrder;

/**
 * Created by leec on 2017/11/28.
 */
public interface HbzSendOrderService extends BaseEntityService<HbzSendOrder, HbzSendOrderDTO> {

    /**
     * 查询帮送订单详情
     * @param hbzSendOrderDTO
     * @return
     */
    HbzSendOrderDTO getSendOrderDetail(HbzSendOrderDTO hbzSendOrderDTO);

}
