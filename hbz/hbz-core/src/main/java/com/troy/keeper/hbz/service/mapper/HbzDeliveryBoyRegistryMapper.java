package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzDeliveryBoyRegistryDTO;
import com.troy.keeper.hbz.po.HbzDeliveryBoyRegistry;
import com.troy.keeper.hbz.repository.HbzDeliveryBoyRegistryRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/11/17.
 */
@Component
public class HbzDeliveryBoyRegistryMapper extends BaseMapper<HbzDeliveryBoyRegistry, HbzDeliveryBoyRegistryDTO> {

    @Autowired
    HbzUserMapper hbzUserMapper;

    @Autowired
    HbzUserRepository hbzUserRepository;

    @Autowired
    HbzDeliveryBoyRegistryRepository hbzDeliveryBoyRegistryRepository;

    @Override
    public HbzDeliveryBoyRegistry newEntity() {
        return new HbzDeliveryBoyRegistry();
    }

    @Override
    public HbzDeliveryBoyRegistryDTO newDTO() {
        return new HbzDeliveryBoyRegistryDTO();
    }

    @Override
    public HbzDeliveryBoyRegistry find(Long id) {
        return hbzDeliveryBoyRegistryRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzDeliveryBoyRegistry entity, HbzDeliveryBoyRegistryDTO dto) {
        new Bean2Bean().addExcludeProp("user").copyProperties(entity, dto);
        if (entity.getUser() != null) {
            dto.setUser(hbzUserMapper.map(entity.getUser()));
            dto.setUserId(entity.getUser().getId());
        }
    }

    @Override
    public void dto2entity(HbzDeliveryBoyRegistryDTO dto, HbzDeliveryBoyRegistry entity) {
        new Bean2Bean().addExcludeProp("user").addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(dto, entity);
        if (dto.getUserId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUserId()));
        else if (dto.getUser() != null && dto.getUser().getId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUser().getId()));
        else entity.setUser(null);
    }
}
