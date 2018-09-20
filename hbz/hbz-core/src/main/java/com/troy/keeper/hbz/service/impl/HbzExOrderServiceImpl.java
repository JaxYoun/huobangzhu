package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzExOrderDTO;
import com.troy.keeper.hbz.po.HbzExOrder;
import com.troy.keeper.hbz.repository.HbzExOrderRepository;
import com.troy.keeper.hbz.service.HbzExOrderService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzExOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2017/11/29.
 */
@Service
@Transactional
public class HbzExOrderServiceImpl extends BaseEntityServiceImpl<HbzExOrder, HbzExOrderDTO> implements HbzExOrderService {

    @Autowired
    HbzExOrderMapper mapper;

    @Autowired
    HbzExOrderRepository repo;

    @Override
    public BaseMapper<HbzExOrder, HbzExOrderDTO> getMapper() {
        return mapper;
    }

    @Override
    public BaseRepository getRepository() {
        return repo;
    }

    @Override
    public HbzExOrderDTO findByOrderNo(String orderNo) {
        return mapper.map(repo.findByOrderNo(orderNo));
    }
}
