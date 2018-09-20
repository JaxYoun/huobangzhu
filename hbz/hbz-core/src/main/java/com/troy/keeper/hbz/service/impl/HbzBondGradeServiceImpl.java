package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzBondGradeDTO;
import com.troy.keeper.hbz.po.HbzBondGrade;
import com.troy.keeper.hbz.repository.HbzBondGradeRepository;
import com.troy.keeper.hbz.service.HbzBondGradeService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzBondGradeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2017/12/26.
 */
@Service
@Transactional
public class HbzBondGradeServiceImpl extends BaseEntityServiceImpl<HbzBondGrade, HbzBondGradeDTO> implements HbzBondGradeService {

    @Autowired
    HbzBondGradeRepository hbzBondGradeRepository;

    @Autowired
    HbzBondGradeMapper hbzBondGradeMapper;

    @Override
    public BaseMapper<HbzBondGrade, HbzBondGradeDTO> getMapper() {
        return hbzBondGradeMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzBondGradeRepository;
    }
}
