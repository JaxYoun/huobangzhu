package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzScoreOrderDTO;
import com.troy.keeper.hbz.po.HbzScoreOrder;

/**
 * Created by leecheng on 2017/12/20.
 */
public interface HbzScoreOrderService extends BaseEntityService<HbzScoreOrder, HbzScoreOrderDTO> {

    String createOrderNo();

}
