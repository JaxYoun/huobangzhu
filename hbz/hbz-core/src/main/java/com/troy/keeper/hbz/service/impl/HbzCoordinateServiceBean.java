package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzCoordinateDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.po.HbzCoordinate;
import com.troy.keeper.hbz.repository.HbzCoordinateRepository;
import com.troy.keeper.hbz.service.HbzCoordinateService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzCoordinateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leecheng on 2017/10/27.
 */
@Service
@Transactional
public class HbzCoordinateServiceBean extends BaseEntityServiceImpl<HbzCoordinate, HbzCoordinateDTO> implements HbzCoordinateService {

    @Autowired
    private HbzCoordinateRepository hbzCoordinateRepository;

    @Autowired
    private HbzCoordinateMapper hbzCoordinateMapper;


    @Override
    public BaseMapper<HbzCoordinate, HbzCoordinateDTO> getMapper() {
        return hbzCoordinateMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzCoordinateRepository;
    }

    @Override
    public HbzCoordinateDTO findByUser(HbzUserDTO user) {
        HbzCoordinateDTO query = new HbzCoordinateDTO();
        query.setUserId(user.getId());
        query.setStatus("1");
        query.setPage(0);
        query.setSize(1);
        List<String[]> sorts = new ArrayList<>();
        sorts.add(new String[]{"syncMillis", "DESC"});
        query.setSorts(sorts);
        Page<HbzCoordinateDTO> page = queryPage(query, query.getPageRequest());
        if (page.getTotalElements() > 0)
            return page.getContent().get(0);
        return null;
    }
}
