package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzSourceDTO;
import com.troy.keeper.hbz.po.HbzSource;
import com.troy.keeper.hbz.repository.HbzSourceRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2018/1/12.
 */
@Component
public class HbzSourceMapper extends BaseMapper<HbzSource, HbzSourceDTO> {

    @Autowired
    HbzSourceRepository hbzSourceRepository;

    @Override
    public HbzSource newEntity() {
        return new HbzSource();
    }

    @Override
    public HbzSourceDTO newDTO() {
        return new HbzSourceDTO();
    }

    @Override
    public HbzSource find(Long id) {
        return hbzSourceRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzSource entity, HbzSourceDTO dto) {
        new Bean2Bean().copyProperties(entity, dto);
    }

    @Override
    public void dto2entity(HbzSourceDTO dto, HbzSource entity) {
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(dto, entity);
    }
}
