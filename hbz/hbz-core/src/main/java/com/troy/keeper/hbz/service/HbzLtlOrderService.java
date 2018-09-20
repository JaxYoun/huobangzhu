package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzLtlOrderDTO;
import com.troy.keeper.hbz.po.HbzFslOrder;
import com.troy.keeper.hbz.po.HbzLtlOrder;

/**
 * Created by leecheng on 2017/11/10.
 */
public interface HbzLtlOrderService extends BaseEntityService<HbzLtlOrder, HbzLtlOrderDTO> {

    /**
     * 单条插入
     * @param hbzLtlOrderDTO
     */
    Long insertOne(HbzLtlOrderDTO hbzLtlOrderDTO);

}
