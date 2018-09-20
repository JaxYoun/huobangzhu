package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzLtlOrderDTO;
import com.troy.keeper.hbz.po.HbzFslOrder;
import com.troy.keeper.hbz.po.HbzLtlOrder;
import com.troy.keeper.hbz.repository.HbzLtlOrderRepository;
import com.troy.keeper.hbz.service.HbzLtlOrderService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzLtlOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2017/11/10.
 */
@Transactional
@Service
public class HbzLtlOrderServiceImpl extends BaseEntityServiceImpl<HbzLtlOrder, HbzLtlOrderDTO> implements HbzLtlOrderService {

    @Autowired
    private HbzLtlOrderRepository hbzLtlOrderRepository;
    @Autowired
    private HbzLtlOrderMapper hbzLtlOrderMapper;

    @Override
    public BaseMapper<HbzLtlOrder, HbzLtlOrderDTO> getMapper() {
        return hbzLtlOrderMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzLtlOrderRepository;
    }

    @Override
    public Long insertOne(HbzLtlOrderDTO hbzLtlOrderDTO) {
        HbzLtlOrder hbzLtlOrder = new HbzLtlOrder();
        hbzLtlOrderMapper.dto2entity(hbzLtlOrderDTO, hbzLtlOrder);
        return hbzLtlOrderRepository.save(hbzLtlOrder).getId();
    }
}
