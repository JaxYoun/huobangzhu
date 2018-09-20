package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzMenuDTO;
import com.troy.keeper.hbz.po.HbzMenu;
import com.troy.keeper.hbz.repository.HbzMenuRepository;
import com.troy.keeper.hbz.service.HbzMenuService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2018/1/8.
 */
@Service
@Transactional
public class HbzMenuServiceImpl extends BaseEntityServiceImpl<HbzMenu, HbzMenuDTO> implements HbzMenuService {

    @Autowired
    HbzMenuRepository hbzMenuRepository;

    @Autowired
    HbzMenuMapper hbzMenuMapper;

    @Override
    public BaseMapper<HbzMenu, HbzMenuDTO> getMapper() {
        return hbzMenuMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzMenuRepository;
    }
}
