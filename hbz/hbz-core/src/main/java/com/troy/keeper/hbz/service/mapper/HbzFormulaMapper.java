package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzFormulaDTO;
import com.troy.keeper.hbz.po.HbzFormula;
import com.troy.keeper.hbz.repository.HbzFormulaRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/12/4.
 */
@Component
public class HbzFormulaMapper extends BaseMapper<HbzFormula, HbzFormulaDTO> {

    @Autowired
    HbzFormulaRepository hbzFormulaRepository;

    @Override
    public HbzFormula newEntity() {
        return new HbzFormula();
    }

    @Override
    public HbzFormulaDTO newDTO() {
        return new HbzFormulaDTO();
    }

    @Override
    public HbzFormula find(Long id) {
        return hbzFormulaRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzFormula entity, HbzFormulaDTO dto) {
        new Bean2Bean().copyProperties(entity, dto);
    }

    @Override
    public void dto2entity(HbzFormulaDTO dto, HbzFormula entity) {
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(dto, entity);
    }
}
