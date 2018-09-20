package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzURLDTO;
import com.troy.keeper.hbz.po.HbzUrl;
import com.troy.keeper.hbz.repository.HbzUrlRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2018/1/8.
 */
@Component
public class HbzURLMapper extends BaseMapper<HbzUrl, HbzURLDTO> {

    @Autowired
    HbzUrlRepository urReposit;

    @Override
    public HbzUrl newEntity() {
        return new HbzUrl();
    }

    @Override
    public HbzURLDTO newDTO() {
        return new HbzURLDTO();
    }

    @Override
    public HbzUrl find(Long id) {
        return urReposit.findOne(id);
    }

    @Override
    public void entity2dto(HbzUrl entity, HbzURLDTO dto) {
        new Bean2Bean().copyProperties(entity, dto);
    }

    @Override
    public void dto2entity(HbzURLDTO dto, HbzUrl entity) {
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(dto, entity);
    }
}
