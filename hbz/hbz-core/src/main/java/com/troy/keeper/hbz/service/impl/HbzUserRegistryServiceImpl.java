package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzUserRegistryDTO;
import com.troy.keeper.hbz.po.HbzUserRegistry;
import com.troy.keeper.hbz.repository.HbzUserRegistryRepository;
import com.troy.keeper.hbz.service.HbzUserRegistryService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzUserRegistryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;

/**
 * Created by leec on 2017/10/25.
 */
@Service
@Transactional
public class HbzUserRegistryServiceImpl extends BaseEntityServiceImpl<HbzUserRegistry, HbzUserRegistryDTO> implements HbzUserRegistryService {

    @Autowired
    private HbzUserRegistryMapper hme;

    @Autowired
    private HbzUserRegistryRepository hurrb;

    @Override
    public BaseMapper<HbzUserRegistry, HbzUserRegistryDTO> getMapper() {
        return hme;
    }

    @Override
    public BaseRepository getRepository() {
        return hurrb;
    }
}
