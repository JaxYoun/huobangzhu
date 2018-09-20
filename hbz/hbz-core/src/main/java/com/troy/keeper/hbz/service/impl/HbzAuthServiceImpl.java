package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzAuthDTO;
import com.troy.keeper.hbz.dto.HbzURLDTO;
import com.troy.keeper.hbz.po.HbzAuth;
import com.troy.keeper.hbz.po.HbzUrl;
import com.troy.keeper.hbz.repository.HbzAuthRepository;
import com.troy.keeper.hbz.repository.HbzUrlRepository;
import com.troy.keeper.hbz.service.HbzAuthService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzAuthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2018/1/8.
 */
@Service
@Transactional
public class HbzAuthServiceImpl extends BaseEntityServiceImpl<HbzAuth, HbzAuthDTO> implements HbzAuthService {

    @Autowired
    HbzAuthMapper hbzAuthMapper;

    @Autowired
    HbzAuthRepository hbzAuthRepository;

    @Autowired
    HbzUrlRepository hbzUrlRepository;

    @Override
    public BaseMapper<HbzAuth, HbzAuthDTO> getMapper() {
        return hbzAuthMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzAuthRepository;
    }

    @Override
    public boolean setUrls(HbzAuthDTO auth, List<HbzURLDTO> urls) {
        HbzAuth authEntity = hbzAuthRepository.findOne(auth.getId());
        List<HbzUrl> urlsArr = urls.stream().map(HbzURLDTO::getId).map(hbzUrlRepository::findOne).collect(Collectors.toList());
        authEntity.setUrls(urlsArr);
        hbzAuthRepository.save(authEntity);
        return true;
    }
}
