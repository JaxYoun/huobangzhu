package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzPersonDriverRegistryDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzPersonDriverRegistry;
import com.troy.keeper.hbz.repository.HbzPersonDriverRegistryRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/11/6.
 */
@Component
public class HbzPersonDriverRegistryMapper extends BaseMapper<HbzPersonDriverRegistry, HbzPersonDriverRegistryDTO> {

    @Autowired
    HbzPersonDriverRegistryRepository hbzPersonDriverRegistryRepository;

    @Autowired
    HbzUserMapper hbzUserMapper;
    @Autowired
    HbzUserRepository hbzUserRepository;

    @Override
    public HbzPersonDriverRegistry newEntity() {
        return new HbzPersonDriverRegistry();
    }

    @Override
    public HbzPersonDriverRegistryDTO newDTO() {
        return new HbzPersonDriverRegistryDTO();
    }

    @Override
    public HbzPersonDriverRegistry find(Long id) {
        return hbzPersonDriverRegistryRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzPersonDriverRegistry entity, HbzPersonDriverRegistryDTO dto) {
        org.springframework.beans.BeanUtils.copyProperties(entity, dto, "user");
        if (entity.getUser() != null) {
            dto.setUser(hbzUserMapper.map(entity.getUser()));
            dto.setUserId(entity.getUser().getId());
        }
    }

    @Override
    public void dto2entity(HbzPersonDriverRegistryDTO dto, HbzPersonDriverRegistry entity) {
        BeanUtils.copyProperties(dto, entity, StringHelper.conact(Const.ID_AUDIT_FIELDS, "user"));
        if (dto.getUserId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUserId()));
        else if (dto.getUser() != null && dto.getUser().getId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUser().getId()));
        else entity.setUser(null);
    }
}
