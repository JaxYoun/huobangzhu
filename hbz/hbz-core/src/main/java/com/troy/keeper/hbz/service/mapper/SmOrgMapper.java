package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.SmOrgDTO;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.system.domain.SmOrg;
import com.troy.keeper.system.repository.SmOrgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SmOrgMapper extends BaseMapper<SmOrg,SmOrgDTO> {

    @Autowired
    SmOrgRepository smOrgRepository;

    @Override
    public SmOrg newEntity() {
        return new SmOrg();
    }

    @Override
    public SmOrgDTO newDTO() {
        return new SmOrgDTO();
    }

    @Override
    public SmOrg find(Long id) {
        return smOrgRepository.findOne(id);
    }

    @Override
    public void entity2dto(SmOrg entity, SmOrgDTO dto) {
        new Bean2Bean().copyProperties(entity,dto);
        dto.setPId(entity.getpId());
    }

    @Override
    public void dto2entity(SmOrgDTO dto, SmOrg entity) {
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(dto,entity);
        entity.setpId(dto.getPId());
    }
}
