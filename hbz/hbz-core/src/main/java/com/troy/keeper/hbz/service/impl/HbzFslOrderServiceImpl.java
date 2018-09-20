package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzFslOrderDTO;
import com.troy.keeper.hbz.po.HbzFslOrder;
import com.troy.keeper.hbz.repository.HbzFslOrderRepository;
import com.troy.keeper.hbz.service.HbzFslOrderService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzFslOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by leecheng on 2017/11/10.
 */
@Transactional
@Service
public class HbzFslOrderServiceImpl extends BaseEntityServiceImpl<HbzFslOrder, HbzFslOrderDTO> implements HbzFslOrderService {

    @Autowired
    private HbzFslOrderRepository hbzFslOrderRepository;
    @Autowired
    private HbzFslOrderMapper hbzFslOrderMapper;

    @Override
    public BaseMapper<HbzFslOrder, HbzFslOrderDTO> getMapper() {
        return hbzFslOrderMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzFslOrderRepository;
    }

    @Override
    public Long insertOne(HbzFslOrderDTO hbzFslOrderDTO) {
        HbzFslOrder hbzFslOrder = new HbzFslOrder();
        this.hbzFslOrderMapper.dto2entity(hbzFslOrderDTO, hbzFslOrder);
        return this.hbzFslOrderRepository.save(hbzFslOrder).getId();
    }
}
