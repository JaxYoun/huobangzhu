package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzTenderDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzTender;
import com.troy.keeper.hbz.repository.HbzOrderRepository;
import com.troy.keeper.hbz.repository.HbzTenderRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/11/21.
 */
@Component
public class HbzTenderMapper extends BaseMapper<HbzTender, HbzTenderDTO> {

    @Autowired
    HbzTenderRepository hbzTenderRepository;

    @Autowired
    HbzUserMapper UserMapper;

    @Autowired
    HbzOrderMapper orderMapper;

    @Autowired
    HbzUserRepository hbzUserRepository;

    @Autowired
    HbzOrderRepository hbzOrderRepository;

    @Autowired
    HbzOrderMapper hbzOrderMapper;

    @Override
    public HbzTender newEntity() {
        return new HbzTender();
    }

    @Override
    public HbzTenderDTO newDTO() {
        return new HbzTenderDTO();
    }

    @Override
    public HbzTender find(Long id) {
        return hbzTenderRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzTender entity, HbzTenderDTO dto) {
        new Bean2Bean().addExcludeProp("user", "order").copyProperties(entity, dto);
        if (entity.getOrder() != null) {
            dto.setOrder(orderMapper.map(entity.getOrder()));
            dto.setOrderId(entity.getOrder().getId());
        }
    }

    @Override
    public void dto2entity(HbzTenderDTO dto, HbzTender entity) {
        new Bean2Bean().addExcludeProp(StringHelper.conact(Const.ID_AUDIT_FIELDS, "user", "order")).copyProperties(dto, entity);
        if (dto.getOrderId() != null)
            entity.setOrder(hbzOrderRepository.findOne(dto.getOrderId()));
        else if (dto.getOrder() != null && dto.getOrder().getId() != null)
            entity.setOrder(hbzOrderRepository.findOne(dto.getOrder().getId()));
        else entity.setOrder(null);

    }
}
