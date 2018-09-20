package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzEnterpriseConsignorRegistryDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.helper.BeanHelper;
import com.troy.keeper.hbz.po.HbzEnterpriseConsignorRegistry;
import com.troy.keeper.hbz.repository.HbzEnterpriseConsignorRegistryRepository;
import com.troy.keeper.hbz.service.EntityService;
import com.troy.keeper.hbz.service.HbzEnterpriseConsignorRegistryService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzEnterpriseConsignorRegistryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leecheng on 2017/11/17.
 */
@Service
@Transactional
public class HbzEnterpriseConsignorRegistryServiceImpl extends BaseEntityServiceImpl<HbzEnterpriseConsignorRegistry, HbzEnterpriseConsignorRegistryDTO> implements HbzEnterpriseConsignorRegistryService {

    @Autowired
    EntityService entityService;

    @Autowired
    HbzEnterpriseConsignorRegistryMapper hbzEnterpriseConsignorRegistryMapper;

    @Autowired
    HbzEnterpriseConsignorRegistryRepository hbzEnterpriseConsignorRegistryRepository;

    @Override
    public BaseMapper<HbzEnterpriseConsignorRegistry, HbzEnterpriseConsignorRegistryDTO> getMapper() {
        return hbzEnterpriseConsignorRegistryMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzEnterpriseConsignorRegistryRepository;
    }

    @Override
    public HbzEnterpriseConsignorRegistryDTO find(HbzUserDTO user, boolean basic) {
        HbzEnterpriseConsignorRegistryDTO query = new HbzEnterpriseConsignorRegistryDTO();
        query.setStatus(Const.STATUS_ENABLED);
        query.setUserId(user.getId());
        List<HbzEnterpriseConsignorRegistryDTO> registries;
        if (basic) {
            List<String> commonProps = new ArrayList<>();
            commonProps.addAll(BeanHelper.getFieldsByIncExcludeAt(HbzEnterpriseConsignorRegistry.class, new Class[]{Id.class, Column.class, ManyToOne.class, ManyToMany.class, OneToMany.class}, new Class[]{Lob.class}));
            registries = entityService.query(HbzEnterpriseConsignorRegistry.class, getMapper(), query, commonProps);
        } else {
            registries = query(query);
        }
        if (registries == null || registries.size() == 0) return null;
        else if (registries.size() == 1) return registries.get(0);
        else if (registries.size() >= 2) throw new IllegalStateException("用户存在多资质");
        throw new RuntimeException("未知错误!");
    }
}
