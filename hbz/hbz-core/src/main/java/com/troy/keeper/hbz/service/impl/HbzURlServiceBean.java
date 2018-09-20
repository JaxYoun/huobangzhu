package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzAuthDTO;
import com.troy.keeper.hbz.dto.HbzURLDTO;
import com.troy.keeper.hbz.po.HbzAuth;
import com.troy.keeper.hbz.po.HbzUrl;
import com.troy.keeper.hbz.repository.HbzAuthRepository;
import com.troy.keeper.hbz.repository.HbzUrlRepository;
import com.troy.keeper.hbz.service.HbzURlService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzURLMapper;
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
public class HbzURlServiceBean extends BaseEntityServiceImpl<HbzUrl, HbzURLDTO> implements HbzURlService {

    @Autowired
    HbzUrlRepository hbzr;

    @Autowired
    HbzURLMapper um;

    @Autowired
    HbzAuthRepository hbzAuthRepository;

    @Override
    public BaseMapper<HbzUrl, HbzURLDTO> getMapper() {
        return um;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzr;
    }

    @Override
    public boolean setAuths(HbzURLDTO url, List<HbzAuthDTO> auths) {
        try {
            HbzUrl u = hbzr.findOne(url.getId());
            List<HbzAuth> as = auths.stream().map(HbzAuthDTO::getId).map(hbzAuthRepository::findOne).collect(Collectors.toList());
            u.setAuths(as);
            hbzr.save(u);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
