package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzBondGradeDTO;
import com.troy.keeper.hbz.po.HbzBondGrade;
import com.troy.keeper.hbz.repository.HbzBondGradeRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/12/26.
 */
@Component
public class HbzBondGradeMapper extends BaseMapper<HbzBondGrade, HbzBondGradeDTO> {

    @Autowired
    HbzBondGradeRepository hbzBondGradeRepository;

    @Override
    public HbzBondGrade newEntity() {
        return new HbzBondGrade();
    }

    @Override
    public HbzBondGradeDTO newDTO() {
        return new HbzBondGradeDTO();
    }

    @Override
    public HbzBondGrade find(Long id) {
        return hbzBondGradeRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzBondGrade entity, HbzBondGradeDTO dto) {
        new Bean2Bean().copyProperties(entity, dto);
    }

    @Override
    public void dto2entity(HbzBondGradeDTO dto, HbzBondGrade entity) {
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(dto, entity);
    }
}
