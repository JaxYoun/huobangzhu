package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzTransSizeDTO;
import com.troy.keeper.hbz.po.HbzTransSize;
import com.troy.keeper.hbz.repository.HbzTransSizeRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/12/6.
 */
@Component
public class HbzTransSizeMapper extends BaseMapper<HbzTransSize, HbzTransSizeDTO> {
    @Autowired
    HbzTransSizeRepository r;

    @Override
    public HbzTransSize newEntity() {
        return new HbzTransSize();
    }

    @Override
    public HbzTransSizeDTO newDTO() {
        return new HbzTransSizeDTO();
    }

    @Override
    public HbzTransSize find(Long id) {
        return r.findOne(id);
    }

    @Override
    public void entity2dto(HbzTransSize entity, HbzTransSizeDTO dto) {
        new Bean2Bean().copyProperties(entity, dto);
    }

    @Override
    public void dto2entity(HbzTransSizeDTO dto, HbzTransSize entity) {
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(dto, entity);
    }
}
