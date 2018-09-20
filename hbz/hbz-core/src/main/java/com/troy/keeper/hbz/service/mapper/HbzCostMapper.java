package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzCostDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzCost;
import com.troy.keeper.hbz.repository.HbzCostRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/11/2.
 */
@Component
public class HbzCostMapper extends BaseMapper<HbzCost, HbzCostDTO> {

    @Autowired
    private HbzCostRepository hbzCostRepository;
    @Autowired
    private HbzUserRepository hbzUserRepository;
    @Autowired
    private HbzUserMapper hbzUserMapper;

    @Override
    public HbzCost newEntity() {
        return new HbzCost();
    }

    @Override
    public HbzCostDTO newDTO() {
        return new HbzCostDTO();
    }

    @Override
    public HbzCost find(Long id) {
        return hbzCostRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzCost entity, HbzCostDTO dto) {
        BeanUtils.copyProperties(entity, dto, "user");
        if (entity.getUser() != null) {
            dto.setUser(hbzUserMapper.map(entity.getUser()));
            dto.setUserId(entity.getUser().getId());
        }
    }

    @Override
    public void dto2entity(HbzCostDTO dto, HbzCost entity) {
        BeanUtils.copyProperties(dto, entity, StringHelper.conact(Const.ID_AUDIT_FIELDS, "user"));
        if (dto.getUserId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUserId()));
        else if (dto.getUser() != null && dto.getUser().getId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUser().getId()));
    }
}
