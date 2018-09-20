package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzUserRegistryDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzUserRegistry;
import com.troy.keeper.hbz.repository.HbzUserRegistryRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/10/25.
 */
@Component
public class HbzUserRegistryMapper extends BaseMapper<HbzUserRegistry, HbzUserRegistryDTO> {

    @Autowired
    private HbzUserMapper hbzUserMapper;

    @Autowired
    private HbzUserRepository hbzUserRepository;

    @Autowired
    private HbzUserRegistryRepository hbzUserRegistryRepository;

    @Override
    public HbzUserRegistry newEntity() {
        return new HbzUserRegistry();
    }

    @Override
    public HbzUserRegistryDTO newDTO() {
        return new HbzUserRegistryDTO();
    }

    @Override
    public HbzUserRegistry find(Long id) {
        return hbzUserRegistryRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzUserRegistry entity, HbzUserRegistryDTO dto) {
        BeanUtils.copyProperties(entity, dto, "user");
        if (entity.getUser() != null) {
            dto.setUser(hbzUserMapper.map(entity.getUser()));
            dto.setUserId(entity.getUser().getId());
        }
    }

    @Override
    public void dto2entity(HbzUserRegistryDTO dto, HbzUserRegistry entity) {
        BeanUtils.copyProperties(dto, entity, StringHelper.conact(Const.ID_AUDIT_FIELDS, "user"));
        if (dto.getUserId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUserId()));
        else if (dto.getUser() != null && dto.getUser().getId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUser().getId()));
        else entity.setUser(null);
    }
}
