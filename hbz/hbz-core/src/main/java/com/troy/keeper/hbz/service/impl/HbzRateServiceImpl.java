package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzRateDTO;
import com.troy.keeper.hbz.po.HbzRate;
import com.troy.keeper.hbz.repository.HbzRateRepository;
import com.troy.keeper.hbz.service.HbzRateService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzRateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2017/12/28.
 */
@Service
@Transactional
public class HbzRateServiceImpl extends BaseEntityServiceImpl<HbzRate, HbzRateDTO> implements HbzRateService {

    @Autowired
    HbzRateRepository hbzRateRepository;

    @Autowired
    HbzRateMapper hbzRateMapper;

    @Override
    public BaseMapper<HbzRate, HbzRateDTO> getMapper() {
        return hbzRateMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzRateRepository;
    }
}
