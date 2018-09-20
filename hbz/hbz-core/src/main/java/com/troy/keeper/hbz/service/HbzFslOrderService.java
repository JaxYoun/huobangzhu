package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzFslOrderDTO;
import com.troy.keeper.hbz.po.HbzFslOrder;

import java.util.List;

/**
 * Created by leecheng on 2017/11/10.
 */
public interface HbzFslOrderService extends BaseEntityService<HbzFslOrder, HbzFslOrderDTO> {

    /**
     * 单条插入
     * @param hbzFslOrderDTO
     */
    Long insertOne(HbzFslOrderDTO hbzFslOrderDTO);

}
