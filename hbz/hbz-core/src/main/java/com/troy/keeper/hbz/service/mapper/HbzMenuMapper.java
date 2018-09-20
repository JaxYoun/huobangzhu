package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzMenuDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzMenu;
import com.troy.keeper.hbz.repository.HbzMenuRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2018/1/8.
 */
@Component
public class HbzMenuMapper extends BaseMapper<HbzMenu, HbzMenuDTO> {

    @Autowired
    HbzMenuRepository hbzMenuRepository;

    @Override
    public HbzMenu newEntity() {
        return new HbzMenu();
    }

    @Override
    public HbzMenuDTO newDTO() {
        return new HbzMenuDTO();
    }

    @Override
    public HbzMenu find(Long id) {
        return hbzMenuRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzMenu entity, HbzMenuDTO dto) {
        new Bean2Bean()
                .addExcludeProp("parent").copyProperties(entity, dto);
        if (entity.getParent() != null) {
            dto.setParentId(entity.getParent().getId());
            dto.setParent(map(entity.getParent()));
        }
    }

    @Override
    public void dto2entity(HbzMenuDTO dto, HbzMenu entity) {
        new Bean2Bean().addExcludeProp(StringHelper.conact(Const.ID_AUDIT_FIELDS, "parent")).copyProperties(dto, entity);
        if (dto.getParentId() != null)
            entity.setParent(find(dto.getParentId()));
        else if (dto.getParent() != null && dto.getParent().getId() != null)
            entity.setParent(find(dto.getParent().getId()));
        else
            entity.setParent(null);
    }
}
