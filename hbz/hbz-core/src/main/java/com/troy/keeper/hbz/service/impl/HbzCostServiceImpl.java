package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzCostDTO;
import com.troy.keeper.hbz.po.HbzCost;
import com.troy.keeper.hbz.repository.HbzCostRepository;
import com.troy.keeper.hbz.service.HbzCostService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzCostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by lee on 2017/11/2.
 */
@Service
@Transactional
public class HbzCostServiceImpl extends BaseEntityServiceImpl<HbzCost, HbzCostDTO> implements HbzCostService {

    @Autowired
    private HbzCostMapper hbzCostMapper;

    @Autowired
    private HbzCostRepository hbzCostRepository;

    @Override
    public BaseMapper<HbzCost, HbzCostDTO> getMapper() {
        return hbzCostMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzCostRepository;
    }
}
