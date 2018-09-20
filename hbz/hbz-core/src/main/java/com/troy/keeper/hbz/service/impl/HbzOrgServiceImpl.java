package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzOrgDTO;
import com.troy.keeper.hbz.po.HbzOrg;
import com.troy.keeper.hbz.repository.HbzOrgRepository;
import com.troy.keeper.hbz.service.HbzOrgService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzOrgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2017/10/30.
 */
@Service
@Transactional
public class HbzOrgServiceImpl extends BaseEntityServiceImpl<HbzOrg, HbzOrgDTO> implements HbzOrgService {

    @Autowired
    private HbzOrgMapper hbzOrgMapper;

    @Autowired
    private HbzOrgRepository hbzOrgRepository;

    @Override
    public BaseMapper<HbzOrg, HbzOrgDTO> getMapper() {
        return hbzOrgMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzOrgRepository;
    }
}
