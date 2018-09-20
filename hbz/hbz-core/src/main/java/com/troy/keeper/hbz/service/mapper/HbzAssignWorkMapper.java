package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzAssignWorkDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzAssignWork;
import com.troy.keeper.hbz.repository.HbzAreaRepository;
import com.troy.keeper.hbz.repository.HbzAssignWorkRepository;
import com.troy.keeper.hbz.repository.HbzPlatformOrganizationRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2018/1/18.
 */
@Component
public class HbzAssignWorkMapper extends BaseMapper<HbzAssignWork, HbzAssignWorkDTO> {
    @Autowired
    HbzAssignWorkRepository hbzAssignWorkRepository;

    @Autowired
    HbzAreaMapper hbzAreaMapper;

    @Autowired
    HbzPlatformOrganizationRepository hbzPlatformOrganizationRepository;

    @Autowired
    HbzPlatformOrganizationMapper hbzPlatformOrganizationMapper;

    @Autowired
    HbzAreaRepository hbzAreaRepository;

    @Override
    public HbzAssignWork newEntity() {
        return new HbzAssignWork();
    }

    @Override
    public HbzAssignWorkDTO newDTO() {
        return new HbzAssignWorkDTO();
    }

    @Override
    public HbzAssignWork find(Long id) {
        return hbzAssignWorkRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzAssignWork entity, HbzAssignWorkDTO dto) {
        new Bean2Bean().addExcludeProp("platformOrganization", "originArea", "destArea").copyProperties(entity, dto);
        if (entity.getPlatformOrganization() != null) {
            dto.setPlatformOrganization(hbzPlatformOrganizationMapper.map(entity.getPlatformOrganization()));
            dto.setPlatformOrganizationId(entity.getPlatformOrganization().getId());
        }
        if (entity.getOriginArea() != null) {
            dto.setOriginArea(hbzAreaMapper.map(entity.getOriginArea()));
            dto.setOriginAreaId(entity.getOriginArea().getId());
        }
        if (entity.getDestArea() != null) {
            dto.setDestArea(hbzAreaMapper.map(entity.getDestArea()));
            dto.setDestAreaId(entity.getDestArea().getId());
        }
    }

    @Override
    public void dto2entity(HbzAssignWorkDTO dto, HbzAssignWork entity) {
        new Bean2Bean().addExcludeProp(StringHelper.conact(Const.ID_AUDIT_FIELDS, "platformOrganization", "originArea", "destArea")).copyProperties(dto, entity);
        if (dto.getPlatformOrganizationId() != null)
            entity.setPlatformOrganization(hbzPlatformOrganizationRepository.findOne(dto.getPlatformOrganizationId()));
        else if (dto.getPlatformOrganization() != null && dto.getPlatformOrganization().getId() != null)
            entity.setPlatformOrganization(hbzPlatformOrganizationRepository.findOne(dto.getPlatformOrganization().getId()));
        else
            entity.setPlatformOrganization(null);


        if (dto.getOriginAreaId() != null)
            entity.setOriginArea(hbzAreaRepository.findOne(dto.getOriginAreaId()));
        else if (dto.getOriginArea() != null && dto.getOriginArea().getId() != null)
            entity.setOriginArea(hbzAreaRepository.findOne(dto.getOriginArea().getId()));
        else entity.setOriginArea(null);

        if (dto.getDestAreaId() != null)
            entity.setDestArea(hbzAreaRepository.findOne(dto.getDestAreaId()));
        else if (dto.getDestArea() != null && dto.getDestArea().getId() != null)
            entity.setDestArea(hbzAreaRepository.findOne(dto.getDestArea().getId()));
        else entity.setDestArea(null);
    }
}
