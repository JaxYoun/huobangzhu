package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzSendOrderDTO;
import com.troy.keeper.hbz.po.HbzSendOrder;
import com.troy.keeper.hbz.repository.HbzSendOrderRepository;
import com.troy.keeper.hbz.service.HbzSendOrderService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzSendOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2017/11/28.
 */
@Service
@Transactional
public class HbzSendOrderServiceImpl extends BaseEntityServiceImpl<HbzSendOrder, HbzSendOrderDTO> implements HbzSendOrderService {

    @Autowired
    private HbzSendOrderRepository hbzSendOrderRepository;

    @Autowired
    private HbzSendOrderMapper mapper;

    @Override
    public BaseMapper<HbzSendOrder, HbzSendOrderDTO> getMapper() {
        return mapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzSendOrderRepository;
    }

    @Override
    public HbzSendOrderDTO getSendOrderDetail(HbzSendOrderDTO hbzSendOrderDTO) {
        HbzSendOrder hbzSendOrder = this.hbzSendOrderRepository.findOne(hbzSendOrderDTO.getId());
        HbzSendOrderDTO hbzSendOrderDTOFromDB = new HbzSendOrderDTO();
        this.mapper.entity2dto(hbzSendOrder, hbzSendOrderDTOFromDB);
        return hbzSendOrderDTOFromDB;
    }
}
