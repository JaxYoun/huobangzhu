package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzAccountFreightDTO;
import com.troy.keeper.hbz.po.HbzAccountFreight;
import com.troy.keeper.hbz.repository.HbzAccountFreightRepo;
import com.troy.keeper.hbz.service.HbzAccountFreightService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzAccountFreightMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2017/11/20.
 */
@Service
@Transactional
public class HbzAccountFreightServiceImpl extends BaseEntityServiceImpl<HbzAccountFreight, HbzAccountFreightDTO> implements HbzAccountFreightService {
    @Autowired
    HbzAccountFreightMapper hbzAccountFreightMapper;
    @Autowired
    HbzAccountFreightRepo repository;

    @Override
    public BaseMapper<HbzAccountFreight, HbzAccountFreightDTO> getMapper() {
        return hbzAccountFreightMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return repository;
    }
}
