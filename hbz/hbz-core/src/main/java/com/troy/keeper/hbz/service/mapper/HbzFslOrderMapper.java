package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzFslOrderDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzFslOrder;
import com.troy.keeper.hbz.repository.HbzAreaRepository;
import com.troy.keeper.hbz.repository.HbzFslOrderRepository;
import com.troy.keeper.hbz.repository.HbzOrderRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/11/10.
 */
@Component
public class HbzFslOrderMapper extends BaseMapper<HbzFslOrder, HbzFslOrderDTO> {

    @Autowired
    private HbzOrderRepository hbzOrderRepository;

    @Autowired
    private HbzUserRepository hbzUserRepository;

    @Autowired
    private HbzUserMapper hbzUserMapper;

    @Autowired
    private HbzAreaMapper areaMapper;

    @Autowired
    private HbzAreaRepository hbzAreaRepository;

    @Autowired
    private HbzFslOrderRepository hbzFslOrderRepository;

    @Override
    public HbzFslOrder newEntity() {
        return new HbzFslOrder();
    }

    @Override
    public HbzFslOrderDTO newDTO() {
        return new HbzFslOrderDTO();
    }

    @Override
    public HbzFslOrder find(Long id) {
        return hbzFslOrderRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzFslOrder entity, HbzFslOrderDTO dto) {
        BeanUtils.copyProperties(entity, dto, "createUser", "takeUser", "dealUser", "agent", "originArea", "destArea");
        if (entity.getCreateUser() != null) {
            dto.setCreateUser(hbzUserMapper.map(entity.getCreateUser()));
            dto.setCreateUserId(entity.getCreateUser().getId());
        }
        if (entity.getTakeUser() != null) {
            dto.setTakeUser(hbzUserMapper.map(entity.getTakeUser()));
            dto.setTakeUserId(entity.getTakeUser().getId());
        }
        if (entity.getDealUser() != null) {
            dto.setDealUser(hbzUserMapper.map(entity.getDealUser()));
            dto.setDealUserId(entity.getDealUser().getId());
        }
        if (entity.getAgent() != null) {
            dto.setAgent(hbzUserMapper.map(entity.getAgent()));
            dto.setAgentId(entity.getAgent().getId());
        }
        if (entity.getOriginArea() != null) {
            dto.setOriginArea(areaMapper.map(entity.getOriginArea()));
            dto.setOriginAreaId(entity.getOriginArea().getId());
        }
        if (entity.getDestArea() != null) {
            dto.setDestArea(areaMapper.map(entity.getDestArea()));
            dto.setDestAreaId(entity.getDestArea().getId());
        }
    }

    @Override
    public void dto2entity(HbzFslOrderDTO dto, HbzFslOrder entity) {
        BeanUtils.copyProperties(dto, entity, StringHelper.conact(Const.ID_AUDIT_FIELDS, "createUser", "takeUser", "dealUser", "agent", "originArea", "destArea"));
        if (dto.getCreateUserId() != null)
            entity.setCreateUser(hbzUserRepository.findOne(dto.getCreateUserId()));
        else if (dto.getCreateUser() != null && dto.getCreateUser().getId() != null)
            entity.setCreateUser(hbzUserRepository.findOne(dto.getCreateUser().getId()));
        else
            entity.setCreateUser(null);

        if (dto.getTakeUserId() != null)
            entity.setTakeUser(hbzUserRepository.findOne(dto.getTakeUserId()));
        else if (dto.getTakeUser() != null && dto.getTakeUser().getId() != null)
            entity.setTakeUser(hbzUserRepository.findOne(dto.getTakeUser().getId()));
        else
            entity.setTakeUser(null);

        if (dto.getDealUserId() != null)
            entity.setDealUser(hbzUserRepository.findOne(dto.getDealUserId()));
        else if (dto.getDealUser() != null && dto.getDealUser().getId() != null)
            entity.setDealUser(hbzUserRepository.findOne(dto.getDealUser().getId()));
        else
            entity.setDealUser(null);

        if (dto.getAgentId() != null)
            entity.setAgent(hbzUserRepository.findOne(dto.getAgentId()));
        else if (dto.getAgent() != null && dto.getAgent().getId() != null)
            entity.setAgent(hbzUserRepository.findOne(dto.getAgent().getId()));
        else entity.setAgent(null);

        if (dto.getOriginAreaId() != null)
            entity.setOriginArea(hbzAreaRepository.findOne(dto.getOriginAreaId()));
        else if (dto.getOriginArea() != null && dto.getOriginArea().getId() != null)
            entity.setOriginArea(hbzAreaRepository.findOne(dto.getOriginArea().getId()));
        else entity.setOriginArea(null);

        if (dto.getDestAreaId() != null)
            entity.setDestArea(hbzAreaRepository.findOne(dto.getDestAreaId()));
        else if (dto.getDestArea() != null && dto.getDestArea().getId() != null)
            entity.setDestArea(hbzAreaRepository.findOne(dto.getDestArea().getId()));
        else entity.setDestArea(null);
    }
}
