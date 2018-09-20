package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzPersonConsignorRegistryDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.helper.BeanHelper;
import com.troy.keeper.hbz.po.HbzPersonConsignorRegistry;
import com.troy.keeper.hbz.repository.HbzPersonConsignorRegistryRepository;
import com.troy.keeper.hbz.service.EntityService;
import com.troy.keeper.hbz.service.HbzPersonConsignorRegistryService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzPersonConsignorRegistryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leecheng on 2017/11/16.
 */
@Service
@Transactional
public class HbzPersonConsignorRegistryServiceImpl extends BaseEntityServiceImpl<HbzPersonConsignorRegistry, HbzPersonConsignorRegistryDTO> implements HbzPersonConsignorRegistryService {
    @Autowired
    EntityService entityService;
    @Autowired
    private HbzPersonConsignorRegistryRepository hbzPersonConsignorRegistryRepository;

    @Autowired
    private HbzPersonConsignorRegistryMapper hbzPersonConsignorRegistryMapper;

    @Override
    public BaseMapper<HbzPersonConsignorRegistry, HbzPersonConsignorRegistryDTO> getMapper() {
        return hbzPersonConsignorRegistryMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzPersonConsignorRegistryRepository;
    }

    @Override
    public HbzPersonConsignorRegistryDTO findUser(HbzUserDTO user, boolean basic) {
        HbzPersonConsignorRegistryDTO query = new HbzPersonConsignorRegistryDTO();
        query.setStatus(Const.STATUS_ENABLED);
        query.setUserId(user.getId());
        List<HbzPersonConsignorRegistryDTO> registries;
        if (basic) {
            List<String> commonProps = new ArrayList<>();
            commonProps.addAll(BeanHelper.getFieldsByIncExcludeAt(HbzPersonConsignorRegistry.class, new Class[]{Id.class, Column.class, ManyToOne.class, ManyToMany.class, OneToMany.class}, new Class[]{Lob.class}));
            registries = entityService.query(HbzPersonConsignorRegistry.class, getMapper(), query, commonProps);
        } else {
            registries = query(query);
        }
        List<HbzPersonConsignorRegistryDTO> results = registries;
        if (registries == null || registries.size() == 0) return null;
        else if (registries.size() == 1) return registries.get(0);
        else if (results.size() >= 2) throw new IllegalStateException("用户存在多资质");
        throw new RuntimeException("未知错误!");
    }
}
