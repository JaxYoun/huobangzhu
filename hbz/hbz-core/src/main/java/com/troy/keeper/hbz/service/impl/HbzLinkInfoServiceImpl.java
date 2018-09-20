package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzLinkInfoDTO;
import com.troy.keeper.hbz.po.HbzLinkInfo;
import com.troy.keeper.hbz.repository.HbzLinkInfoRepository;
import com.troy.keeper.hbz.service.HbzLinkInfoService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzLinkInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2017/11/30.
 */
@Service
@Transactional
public class HbzLinkInfoServiceImpl extends BaseEntityServiceImpl<HbzLinkInfo, HbzLinkInfoDTO> implements HbzLinkInfoService {

    @Autowired
    HbzLinkInfoMapper hbzLinkInfoMapper;
    @Autowired
    HbzLinkInfoRepository hbzLinkInfoRepository;

    @Override
    public BaseMapper<HbzLinkInfo, HbzLinkInfoDTO> getMapper() {
        return hbzLinkInfoMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzLinkInfoRepository;
    }
}
