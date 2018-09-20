package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzPersonDriverRegistryDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.helper.BeanHelper;
import com.troy.keeper.hbz.po.HbzPersonDriverRegistry;
import com.troy.keeper.hbz.repository.HbzPersonDriverRegistryRepository;
import com.troy.keeper.hbz.service.EntityService;
import com.troy.keeper.hbz.service.HbzPersonDriverRegistryService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzPersonDriverRegistryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leecheng on 2017/11/6.
 */
@Service
@Transactional
public class HbzPersonDriverRegistryImpl extends BaseEntityServiceImpl<HbzPersonDriverRegistry, HbzPersonDriverRegistryDTO> implements HbzPersonDriverRegistryService {

    @Autowired
    EntityService entityService;

    @Autowired
    HbzPersonDriverRegistryRepository hbzPersonDriverRegistryRepository;
    @Autowired
    HbzPersonDriverRegistryMapper hbzPersonDriverRegistryMapper;

    @Override
    public BaseMapper<HbzPersonDriverRegistry, HbzPersonDriverRegistryDTO> getMapper() {
        return hbzPersonDriverRegistryMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzPersonDriverRegistryRepository;
    }

    @Override
    public HbzPersonDriverRegistryDTO find(HbzUserDTO user, boolean basic) {
        HbzPersonDriverRegistryDTO q = new HbzPersonDriverRegistryDTO();
        q.setStatus(Const.STATUS_ENABLED);
        q.setUserId(user.getId());
        List<HbzPersonDriverRegistryDTO> registries;
        if (basic) {
            List<String> commonProps = new ArrayList<>();
            commonProps.addAll(BeanHelper.getFieldsByIncExcludeAt(HbzPersonDriverRegistry.class, new Class[]{Id.class, Column.class, ManyToOne.class, ManyToMany.class, OneToMany.class}, new Class[]{Lob.class}));
            registries = entityService.query(HbzPersonDriverRegistry.class, getMapper(), q, commonProps);
        } else {
            registries = query(q);
        }
        if (registries == null || registries.size() == 0) return null;
        if (registries.size() > 1) throw new IllegalThreadStateException();
        return registries.get(0);
    }
}
