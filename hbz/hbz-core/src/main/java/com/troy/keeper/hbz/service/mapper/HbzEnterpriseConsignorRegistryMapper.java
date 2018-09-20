package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzEnterpriseConsignorRegistryDTO;
import com.troy.keeper.hbz.po.HbzEnterpriseConsignorRegistry;
import com.troy.keeper.hbz.repository.HbzEnterpriseConsignorRegistryRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by leecheng on 2017/11/17.
 */
@Service
public class HbzEnterpriseConsignorRegistryMapper extends BaseMapper<HbzEnterpriseConsignorRegistry, HbzEnterpriseConsignorRegistryDTO> {

    @Autowired
    HbzEnterpriseConsignorRegistryRepository hbzEnterpriseConsignorRegistryRepository;

    @Autowired
    HbzUserMapper hbzUserMapper;
    
    @Autowired
    HbzUserRepository hbzUserRepository;

    @Override
    public HbzEnterpriseConsignorRegistry newEntity() {
        return new HbzEnterpriseConsignorRegistry();
    }

    @Override
    public HbzEnterpriseConsignorRegistryDTO newDTO() {
        return new HbzEnterpriseConsignorRegistryDTO();
    }

    @Override
    public HbzEnterpriseConsignorRegistry find(Long id) {
        return hbzEnterpriseConsignorRegistryRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzEnterpriseConsignorRegistry entity, HbzEnterpriseConsignorRegistryDTO dto) {
        new Bean2Bean().addExcludeProp("user").copyProperties(entity, dto);
        if (entity.getUser() != null) {
            dto.setUser(hbzUserMapper.map(entity.getUser()));
            dto.setUserId(entity.getUser().getId());
        }
    }

    @Override
    public void dto2entity(HbzEnterpriseConsignorRegistryDTO dto, HbzEnterpriseConsignorRegistry entity) {
        new Bean2Bean().addExcludeProp("user").addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(dto, entity);
        if (dto.getUserId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUserId()));
        else if (dto.getUser() != null && dto.getUser().getId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUser().getId()));
        else entity.setUser(null);
    }
}
