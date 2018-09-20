package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzArea;
import com.troy.keeper.hbz.repository.HbzAreaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/10/30.
 */
@Component
public class HbzAreaMapper extends BaseMapper<HbzArea, HbzAreaDTO> {

    @Autowired
    private HbzAreaRepository hbzAreaRepository;

    @Override
    public HbzArea newEntity() {
        return new HbzArea();
    }

    @Override
    public HbzAreaDTO newDTO() {
        return new HbzAreaDTO();
    }

    @Override
    public HbzArea find(Long id) {
        return hbzAreaRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzArea entity, HbzAreaDTO dto) {
        BeanUtils.copyProperties(entity, dto, "parent");
        if (entity.getParent() != null) {
            dto.setParentId(entity.getParent().getId());
            dto.setParent(map(entity.getParent()));
        }
    }

    @Override
    public void dto2entity(HbzAreaDTO dto, HbzArea entity) {
        BeanUtils.copyProperties(dto, entity, StringHelper.conact(Const.ID_AUDIT_FIELDS, "parent"));
        if (dto.getParentId() != null)
            entity.setParent(hbzAreaRepository.findOne(dto.getParentId()));
        else if (dto.getParent() != null && dto.getParent().getId() != null)
            entity.setParent(hbzAreaRepository.findOne(dto.getParent().getId()));
        else
            entity.setParent(null);
    }
}
