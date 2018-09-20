package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzTransSizeDTO;
import com.troy.keeper.hbz.po.HbzTransSize;
import com.troy.keeper.hbz.repository.HbzTransSizeRepository;
import com.troy.keeper.hbz.service.HbzTransSizeService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzTransSizeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2017/12/6.
 */
@Service
@Transactional
public class HbzTransSizeServiceImpl extends BaseEntityServiceImpl<HbzTransSize, HbzTransSizeDTO> implements HbzTransSizeService {

    @Autowired
    HbzTransSizeRepository r;

    @Autowired
    HbzTransSizeMapper ma;

    @Override
    public BaseMapper getMapper() {
        return ma;
    }

    @Override
    public BaseRepository getRepository() {
        return r;
    }

    @Override
    public HbzTransSizeDTO findByTransSize(Double transSize) {
        return ma.map(r.findByTransSize(transSize));
    }
}
