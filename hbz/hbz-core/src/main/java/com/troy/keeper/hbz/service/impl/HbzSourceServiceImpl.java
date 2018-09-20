package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzSourceDTO;
import com.troy.keeper.hbz.po.HbzSource;
import com.troy.keeper.hbz.repository.HbzSourceRepository;
import com.troy.keeper.hbz.service.HbzSourceService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzSourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2018/1/12.
 */
@Service
@Transactional
public class HbzSourceServiceImpl extends BaseEntityServiceImpl<HbzSource, HbzSourceDTO> implements HbzSourceService {

    @Autowired
    HbzSourceRepository hbzSourceRepository;

    @Autowired
    HbzSourceMapper hbzSourceMapper;

    @Override
    public BaseMapper<HbzSource, HbzSourceDTO> getMapper() {
        return hbzSourceMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzSourceRepository;
    }
}
