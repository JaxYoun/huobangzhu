package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzEventDTO;
import com.troy.keeper.hbz.po.HbzEvent;
import com.troy.keeper.hbz.repository.HbzEventRepository;
import com.troy.keeper.hbz.service.HbzEventService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzEventMapper;
import com.troy.keeper.hbz.sys.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/10/17.
 */
@Service
@Transactional
public class HbzEventServiceImpl extends BaseEntityServiceImpl<HbzEvent, HbzEventDTO> implements HbzEventService {

    @Autowired
    private HbzEventRepository hbzEventRepository;

    @Autowired
    private HbzEventMapper hbzEventMapper;

    @Override
    public BaseMapper<HbzEvent, HbzEventDTO> getMapper() {
        return hbzEventMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzEventRepository;
    }
}
