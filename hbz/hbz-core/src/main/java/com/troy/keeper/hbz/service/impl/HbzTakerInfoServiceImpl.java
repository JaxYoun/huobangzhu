package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzTakerInfoDTO;
import com.troy.keeper.hbz.po.HbzTakerInfo;
import com.troy.keeper.hbz.repository.HbzTakerInfoRepo;
import com.troy.keeper.hbz.service.HbzTakerInfoService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzTakerInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2017/11/21.
 */
@Service
@Transactional
public class HbzTakerInfoServiceImpl extends BaseEntityServiceImpl<HbzTakerInfo, HbzTakerInfoDTO> implements HbzTakerInfoService {

    @Autowired
    HbzTakerInfoMapper hbzTakerInfoMapper;

    @Autowired
    HbzTakerInfoRepo hbzTakerInfoRepo;

    @Override
    public BaseMapper<HbzTakerInfo, HbzTakerInfoDTO> getMapper() {
        return hbzTakerInfoMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzTakerInfoRepo;
    }
}
