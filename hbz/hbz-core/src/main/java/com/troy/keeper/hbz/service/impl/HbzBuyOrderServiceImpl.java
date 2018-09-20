package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzBuyOrderDTO;
import com.troy.keeper.hbz.po.HbzBuyOrder;
import com.troy.keeper.hbz.repository.HbzBuyOrderRepository;
import com.troy.keeper.hbz.service.HbzBuyOrderServices;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzBuyOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2017/10/24.
 */
@Service
@Transactional
public class HbzBuyOrderServiceImpl extends BaseEntityServiceImpl<HbzBuyOrder, HbzBuyOrderDTO> implements HbzBuyOrderServices {

    @Autowired
    private HbzBuyOrderMapper hbzBuyOrderMapper;

    @Autowired
    private HbzBuyOrderRepository hbzBuyOrderRepository;

    @Override
    public BaseMapper<HbzBuyOrder, HbzBuyOrderDTO> getMapper() {
        return hbzBuyOrderMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzBuyOrderRepository;
    }

    @Override
    public HbzBuyOrderDTO getHbzBuyOrderDetail(HbzBuyOrderDTO hbzBuyOrderDTO) {
        HbzBuyOrder hbzBuyOrder = this.hbzBuyOrderRepository.findOne(hbzBuyOrderDTO.getId());
        HbzBuyOrderDTO hbzBuyOrderDTOFromDB = new HbzBuyOrderDTO();
        this.hbzBuyOrderMapper.entity2dto(hbzBuyOrder, hbzBuyOrderDTOFromDB);
        return hbzBuyOrderDTOFromDB;
    }
}
