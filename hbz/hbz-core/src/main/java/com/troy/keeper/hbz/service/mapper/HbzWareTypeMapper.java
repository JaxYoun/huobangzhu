package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzWareTypeDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzWareType;
import com.troy.keeper.hbz.repository.HbzWareTypeRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/12/12.
 */
@Component
public class HbzWareTypeMapper extends BaseMapper<HbzWareType, HbzWareTypeDTO> {

    @Autowired
    HbzWareTypeRepository hbzWareTypeRepository;

    @Override
    public HbzWareType newEntity() {
        return new HbzWareType();
    }

    @Override
    public HbzWareTypeDTO newDTO() {
        return new HbzWareTypeDTO();
    }

    @Override
    public HbzWareType find(Long id) {
        return hbzWareTypeRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzWareType entity, HbzWareTypeDTO dto) {
        new Bean2Bean().addExcludeProp("parent").copyProperties(entity, dto);
        if (entity.getParent() != null) {
            dto.setParent(map(entity.getParent()));
            dto.setParentId(entity.getParent().getId());
        }
    }

    @Override
    public void dto2entity(HbzWareTypeDTO dto, HbzWareType entity) {
        new Bean2Bean().addExcludeProp(StringHelper.conact(Const.ID_AUDIT_FIELDS, "parent")).copyProperties(dto, entity);
        if (dto.getParentId() != null)
            entity.setParent(find(dto.getParentId()));
        else if (dto.getParent() != null && dto.getParent().getId() != null)
            entity.setParent(find(dto.getParent().getId()));
        else entity.setParent(null);
    }
}
