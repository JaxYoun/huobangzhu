package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzPersonConsignorRegistryDTO;
import com.troy.keeper.hbz.po.HbzPersonConsignorRegistry;
import com.troy.keeper.hbz.repository.HbzPersonConsignorRegistryRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/11/16.
 */
@Component
public class HbzPersonConsignorRegistryMapper extends BaseMapper<HbzPersonConsignorRegistry, HbzPersonConsignorRegistryDTO> {

    @Autowired
    private HbzPersonConsignorRegistryRepository hbzPersonConsignorRegistryRepository;

    @Autowired
    private HbzUserRepository hbzUserRepository;

    @Autowired
    private HbzUserMapper hbzUserMapper;

    @Override
    public HbzPersonConsignorRegistry newEntity() {
        return new HbzPersonConsignorRegistry();
    }

    @Override
    public HbzPersonConsignorRegistryDTO newDTO() {
        return new HbzPersonConsignorRegistryDTO();
    }

    @Override
    public HbzPersonConsignorRegistry find(Long id) {
        return hbzPersonConsignorRegistryRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzPersonConsignorRegistry entity, HbzPersonConsignorRegistryDTO dto) {
        new Bean2Bean().addExcludeProp("user").copyProperties(entity, dto);
        if (entity.getUser() != null) {
            dto.setUser(hbzUserMapper.map(entity.getUser()));
            dto.setUserId(entity.getUser().getId());
        }
    }

    @Override
    public void dto2entity(HbzPersonConsignorRegistryDTO dto, HbzPersonConsignorRegistry entity) {
        new Bean2Bean().addExcludeProp("user").addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(dto, entity);
        if (dto.getUserId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUserId()));
        else if (dto.getUser() != null && dto.getUser().getId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUser().getId()));
        else entity.setUser(null);
    }
}