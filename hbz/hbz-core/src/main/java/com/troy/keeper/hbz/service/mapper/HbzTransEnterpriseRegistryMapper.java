package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzTransEnterpriseRegistryDTO;
import com.troy.keeper.hbz.po.HbzTransEnterpriseRegistry;
import com.troy.keeper.hbz.repository.HbzTransEnterpriseRegistryRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/11/20.
 */
@Component
public class HbzTransEnterpriseRegistryMapper extends BaseMapper<HbzTransEnterpriseRegistry, HbzTransEnterpriseRegistryDTO> {


    @Autowired
    HbzUserMapper hbzUserMapper;

    @Autowired
    HbzUserRepository hbzUserRepository;

    @Autowired
    HbzTransEnterpriseRegistryRepository hbzTransEnterpriseRegistryRepository;

    @Override
    public HbzTransEnterpriseRegistry newEntity() {
        return new HbzTransEnterpriseRegistry();
    }

    @Override
    public HbzTransEnterpriseRegistryDTO newDTO() {
        return new HbzTransEnterpriseRegistryDTO();
    }

    @Override
    public HbzTransEnterpriseRegistry find(Long id) {
        return hbzTransEnterpriseRegistryRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzTransEnterpriseRegistry entity, HbzTransEnterpriseRegistryDTO dto) {
        new Bean2Bean().addExcludeProp("user").copyProperties(entity, dto);
        if (entity.getUser() != null) {
            dto.setUser(hbzUserMapper.map(entity.getUser()));
            dto.setUserId(entity.getUser().getId());
        }
    }

    @Override
    public void dto2entity(HbzTransEnterpriseRegistryDTO dto, HbzTransEnterpriseRegistry entity) {
        new Bean2Bean().addExcludeProp("user").addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(dto, entity);
        if (dto.getUserId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUserId()));
        else if (dto.getUser() != null && dto.getUser().getId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUser().getId()));
        else entity.setUser(null);
    }
}
