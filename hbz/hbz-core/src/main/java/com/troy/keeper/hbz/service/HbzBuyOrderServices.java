package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzBuyOrderDTO;
import com.troy.keeper.hbz.po.HbzBuyOrder;

/**
 * Created by leecheng on 2017/10/24.
 */
public interface HbzBuyOrderServices extends BaseEntityService<HbzBuyOrder, HbzBuyOrderDTO> {

    /**
     * 查询帮买订单详情
     * @param hbzBuyOrderDTO
     * @return
     */
    HbzBuyOrderDTO getHbzBuyOrderDetail(HbzBuyOrderDTO hbzBuyOrderDTO);

}
