package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzUser;
import com.troy.keeper.hbz.repository.HbzOrgRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by leecheng on 2017/10/16.
 */
@Service
public class HbzUserMapper extends BaseMapper<HbzUser, HbzUserDTO> {

    @Autowired
    private HbzUserRepository hbzUserRepository;
    @Autowired
    private HbzOrgMapper hbzOrgMapper;
    @Autowired
    private HbzOrgRepository hbzOrgRepository;

    @Override
    public HbzUser newEntity() {
        return new HbzUser();
    }

    @Override
    public HbzUserDTO newDTO() {
        return new HbzUserDTO();
    }

    @Override
    public HbzUser find(Long id) {
        return hbzUserRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzUser entity, HbzUserDTO dto) {
        BeanUtils.copyProperties(entity, dto, "roles", "org", "ent");
        if (entity.getOrg() != null) {
            dto.setOrg(hbzOrgMapper.map(entity.getOrg()));
            dto.setOrgId(entity.getOrg().getId());
        }
        if (entity.getEnt() != null) {
            dto.setEnt(hbzOrgMapper.map(entity.getEnt()));
            dto.setEntId(entity.getEnt().getId());
        }
    }

    @Override
    public void dto2entity(HbzUserDTO dto, HbzUser entity) {
        BeanUtils.copyProperties(dto, entity, StringHelper.conact(Const.ID_AUDIT_FIELDS, "roles", "org", "ent"));

        if (dto.getOrgId() != null)
            entity.setOrg(hbzOrgRepository.findOne(dto.getOrgId()));
        else if (dto.getOrg() != null && dto.getOrg().getId() != null)
            entity.setOrg(hbzOrgRepository.findOne(dto.getOrg().getId()));
        else entity.setOrg(null);

        if (dto.getEntId() != null)
            entity.setEnt(hbzOrgRepository.findOne(dto.getEntId()));
        else if (dto.getEnt() != null && dto.getEnt().getId() != null)
            entity.setEnt(hbzOrgRepository.findOne(dto.getEnt().getId()));
        else entity.setOrg(null);
    }
}
