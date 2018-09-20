package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzReimburseDTO;
import com.troy.keeper.hbz.po.HbzReimburse;
import com.troy.keeper.hbz.repository.HbzReimburseRepository;
import com.troy.keeper.hbz.service.HbzReimburseService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzReimburseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2018/1/3.
 */
@Service
@Transactional
public class HbzReimburseServiceImpl extends BaseEntityServiceImpl<HbzReimburse, HbzReimburseDTO> implements HbzReimburseService {

    @Autowired
    HbzReimburseRepository hbzReimburseRepository;

    @Autowired
    HbzReimburseMapper hbzReimburseMapper;

    @Override
    public BaseMapper<HbzReimburse, HbzReimburseDTO> getMapper() {
        return hbzReimburseMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzReimburseRepository;
    }
}
