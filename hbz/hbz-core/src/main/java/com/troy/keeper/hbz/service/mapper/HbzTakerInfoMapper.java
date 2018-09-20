package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.dto.HbzTakerInfoDTO;
import com.troy.keeper.hbz.po.HbzTakerInfo;
import com.troy.keeper.hbz.repository.HbzOrderRepository;
import com.troy.keeper.hbz.repository.HbzTakerInfoRepo;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/11/21.
 */
@Component
public class HbzTakerInfoMapper extends BaseMapper<HbzTakerInfo, HbzTakerInfoDTO> {
    @Autowired
    HbzTakerInfoRepo takerRepo;
    @Autowired
    HbzOrderMapper orderMapper;
    @Autowired
    HbzOrderRepository orderRepository;
    @Autowired
    HbzUserMapper uMapper;
    @Autowired
    HbzUserRepository urepo;

    @Override
    public HbzTakerInfo newEntity() {
        return new HbzTakerInfo();
    }

    @Override
    public HbzTakerInfoDTO newDTO() {
        return new HbzTakerInfoDTO();
    }

    @Override
    public HbzTakerInfo find(Long id) {
        return takerRepo.findOne(id);
    }

    @Override
    public void entity2dto(HbzTakerInfo entity, HbzTakerInfoDTO dto) {
        new Bean2Bean().addExcludeProp("user", "order", "agent").copyProperties(entity, dto);
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
            dto.setUser(uMapper.map(entity.getUser()));
        }
        if (entity.getAgent() != null) {
            dto.setAgentId(entity.getAgent().getId());
            dto.setAgent(uMapper.map(entity.getAgent()));
        }
        if (entity.getOrder() != null) {
            dto.setOrder(orderMapper.map(entity.getOrder()));
            dto.setOrderId(entity.getOrder().getId());
        }
    }

    @Override
    public void dto2entity(HbzTakerInfoDTO dto, HbzTakerInfo entity) {
        new Bean2Bean().addExcludeProp("user", "agent", "order").copyProperties(dto, entity);
        if (dto.getOrderId() != null)
            entity.setOrder(orderRepository.findOne(dto.getOrderId()));
        else if (dto.getOrder() != null && dto.getOrder().getId() != null)
            entity.setOrder(orderRepository.findOne(dto.getOrder().getId()));
        else entity.setOrder(null);

        if (dto.getAgentId() != null)
            entity.setAgent(urepo.findOne(dto.getAgentId()));
        else if (dto.getAgent() != null && dto.getAgent().getId() != null)
            entity.setAgent(urepo.findOne(dto.getAgent().getId()));
        else entity.setAgent(null);

        if (dto.getUserId() != null)
            entity.setUser(urepo.findOne(dto.getUserId()));
        else if (dto.getUser() != null && dto.getUser().getId() != null)
            entity.setUser(urepo.findOne(dto.getUser().getId()));
        else entity.setUser(null);

    }
}
