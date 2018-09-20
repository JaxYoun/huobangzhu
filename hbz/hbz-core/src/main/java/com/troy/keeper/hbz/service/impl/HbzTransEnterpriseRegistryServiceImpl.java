package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzTransEnterpriseRegistryDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.helper.BeanHelper;
import com.troy.keeper.hbz.po.HbzTransEnterpriseRegistry;
import com.troy.keeper.hbz.repository.HbzTransEnterpriseRegistryRepository;
import com.troy.keeper.hbz.service.EntityService;
import com.troy.keeper.hbz.service.HbzTransEnterpriseRegistryService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzTransEnterpriseRegistryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leecheng on 2017/11/20.
 */
@Service
@Transactional
public class HbzTransEnterpriseRegistryServiceImpl extends BaseEntityServiceImpl<HbzTransEnterpriseRegistry, HbzTransEnterpriseRegistryDTO> implements HbzTransEnterpriseRegistryService {

    @Autowired
    EntityService entityService;

    @Autowired
    HbzTransEnterpriseRegistryMapper map;

    @Autowired
    HbzTransEnterpriseRegistryRepository repo;

    @Override
    public BaseMapper<HbzTransEnterpriseRegistry, HbzTransEnterpriseRegistryDTO> getMapper() {
        return map;
    }

    @Override
    public BaseRepository getRepository() {
        return repo;
    }

    @Override
    public HbzTransEnterpriseRegistryDTO findTransEnterpriseRegistry(HbzUserDTO user, boolean basic) {
        HbzTransEnterpriseRegistryDTO query = new HbzTransEnterpriseRegistryDTO();
        query.setStatus(Const.STATUS_ENABLED);
        query.setUserId(user.getId());
        List<HbzTransEnterpriseRegistryDTO> results;
        if (basic) {
            List<String> commonProps = new ArrayList<>();
            commonProps.addAll(BeanHelper.getFieldsByIncExcludeAt(HbzTransEnterpriseRegistry.class, new Class[]{Id.class, Column.class, ManyToOne.class, ManyToMany.class, OneToMany.class}, new Class[]{Lob.class}));
            results = entityService.query(HbzTransEnterpriseRegistry.class, getMapper(), query, commonProps);
        } else {
            List<HbzTransEnterpriseRegistryDTO> registries = query(query);
            results = registries;
        }
        if (results == null || results.size() == 0) return null;
        else if (results.size() == 1) return results.get(0);
        else if (results.size() >= 2) throw new IllegalStateException("用户存在多资质");
        throw new RuntimeException("未知错误!");
    }
}
