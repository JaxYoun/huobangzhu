package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.dto.HbzPlatformOrganizationDTO;
import com.troy.keeper.hbz.po.HbzPlatformOrganization;
import com.troy.keeper.hbz.repository.HbzAreaRepository;
import com.troy.keeper.hbz.repository.HbzPlatformOrganizationRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2018/1/16.
 */
@Component
public class HbzPlatformOrganizationMapper extends BaseMapper<HbzPlatformOrganization, HbzPlatformOrganizationDTO> {

    @Autowired
    HbzAreaRepository hbzAreaRepository;

    @Autowired
    HbzAreaMapper hbzAreaMapper;

    @Autowired
    HbzPlatformOrganizationRepository hbzPlatformOrganizationRepository;

    @Override
    public HbzPlatformOrganization newEntity() {
        return new HbzPlatformOrganization();
    }

    @Override
    public HbzPlatformOrganizationDTO newDTO() {
        return new HbzPlatformOrganizationDTO();
    }

    @Override
    public HbzPlatformOrganization find(Long id) {
        return hbzPlatformOrganizationRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzPlatformOrganization entity, HbzPlatformOrganizationDTO dto) {
        new Bean2Bean().addExcludeProp("addressArea").copyProperties(entity, dto);
        if (entity.getAddressArea() != null) {
            dto.setAddressArea(hbzAreaMapper.map(entity.getAddressArea()));
            dto.setAddressAreaId(entity.getAddressArea().getId());
        }
    }

    @Override
    public void dto2entity(HbzPlatformOrganizationDTO dto, HbzPlatformOrganization entity) {
        new Bean2Bean().addExcludeProp("addressArea").copyProperties(dto, entity);
        if (dto.getAddressAreaId() != null) {
            entity.setAddressArea(hbzAreaRepository.findOne(dto.getAddressAreaId()));
        } else if (dto.getAddressArea() != null && dto.getAddressArea().getId() != null) {
            entity.setAddressArea(hbzAreaRepository.findOne(dto.getAddressArea().getId()));
        } else {
            entity.setAddressArea(null);
        }
    }
}
