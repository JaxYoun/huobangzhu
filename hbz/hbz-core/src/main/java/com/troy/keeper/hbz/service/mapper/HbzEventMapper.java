package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzEventDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzEvent;
import com.troy.keeper.hbz.repository.HbzEventRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by leecheng on 2017/10/17.
 */
@Component
public class HbzEventMapper extends BaseMapper<HbzEvent, HbzEventDTO> {

    @Autowired
    private HbzEventRepository hbzEventRepository;

    @Override
    public HbzEvent newEntity() {
        return new HbzEvent();
    }

    @Override
    public HbzEventDTO newDTO() {
        return new HbzEventDTO();
    }

    @Override
    public HbzEvent find(Long id) {
        return hbzEventRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzEvent entity, HbzEventDTO dto) {
        BeanUtils.copyProperties(entity, dto);
    }

    @Override
    public void dto2entity(HbzEventDTO dto, HbzEvent entity) {
        BeanUtils.copyProperties(dto, entity, StringHelper.conact(Const.ID_AUDIT_FIELDS));
    }
}
