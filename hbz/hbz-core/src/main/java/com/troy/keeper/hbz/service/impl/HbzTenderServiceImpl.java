package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzTenderDTO;
import com.troy.keeper.hbz.po.HbzTender;
import com.troy.keeper.hbz.repository.HbzTenderRepository;
import com.troy.keeper.hbz.service.HbzTenderService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzTenderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2017/11/21.
 */
@Service
@Transactional
public class HbzTenderServiceImpl extends BaseEntityServiceImpl<HbzTender, HbzTenderDTO> implements HbzTenderService {
    @Autowired
    HbzTenderMapper hbzTenderMapper;
    @Autowired
    HbzTenderRepository hbzTenderRepox;

    @Override
    public BaseMapper<HbzTender, HbzTenderDTO> getMapper() {
        return hbzTenderMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzTenderRepox;
    }

    @Override
    public HbzTenderDTO findByOrderId(Long orderid) {
        return getMapper().map(hbzTenderRepox.findByOrderId(orderid));
    }
}
