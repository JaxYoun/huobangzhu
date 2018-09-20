package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzUserAddressInfoDTO;
import com.troy.keeper.hbz.po.HbzUserAddressInfo;
import com.troy.keeper.hbz.repository.HbzUserAddressInfoRepository;
import com.troy.keeper.hbz.service.HbzUserAddressInfoService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzUserAddressInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;

/**
 * Created by leecheng on 2017/10/27.
 */
@Service
@Transactional
public class HbzUserAddressInfoServiceImpl extends BaseEntityServiceImpl<HbzUserAddressInfo, HbzUserAddressInfoDTO> implements HbzUserAddressInfoService {
    @Autowired
    private HbzUserAddressInfoMapper hbzUserAddressInfoMapper;
    @Autowired
    private HbzUserAddressInfoRepository hbzUserAddressInfoRepository;

    @Override
    public BaseMapper<HbzUserAddressInfo, HbzUserAddressInfoDTO> getMapper() {
        return hbzUserAddressInfoMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzUserAddressInfoRepository;
    }
}
