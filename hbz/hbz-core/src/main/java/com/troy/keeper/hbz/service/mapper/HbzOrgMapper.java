package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzOrgDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzOrg;
import com.troy.keeper.hbz.repository.HbzOrgRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/10/30.
 */
@Component
public class HbzOrgMapper extends BaseMapper<HbzOrg, HbzOrgDTO> {

    @Autowired
    private HbzOrgRepository hbzOrgRepository;

    @Override
    public HbzOrg newEntity() {
        return new HbzOrg();
    }

    @Override
    public HbzOrgDTO newDTO() {
        return new HbzOrgDTO();
    }

    @Override
    public HbzOrg find(Long id) {
        return hbzOrgRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzOrg entity, HbzOrgDTO dto) {
        BeanUtils.copyProperties(entity, dto, "parent");
        if (entity.getParent() != null) {
            dto.setParentId(entity.getParent().getId());
            dto.setParent(map(entity.getParent()));
        }
    }

    @Override
    public void dto2entity(HbzOrgDTO dto, HbzOrg entity) {
        BeanUtils.copyProperties(dto, entity, StringHelper.conact(Const.ID_AUDIT_FIELDS, "parent"));
        if (dto.getParentId() != null)
            entity.setParent(hbzOrgRepository.findOne(dto.getParentId()));
        else if (dto.getParent() != null && dto.getParent().getId() != null)
            entity.setParent(hbzOrgRepository.findOne(dto.getParent().getId()));
    }
}
