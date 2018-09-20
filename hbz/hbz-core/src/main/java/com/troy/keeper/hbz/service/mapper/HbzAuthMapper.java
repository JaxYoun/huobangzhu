package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzAuthDTO;
import com.troy.keeper.hbz.po.HbzAuth;
import com.troy.keeper.hbz.repository.HbzAuthRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2018/1/8.
 */
@Component
public class HbzAuthMapper extends BaseMapper<HbzAuth, HbzAuthDTO> {

    @Autowired
    HbzAuthRepository hbzAuthRepository;

    @Override
    public HbzAuth newEntity() {
        return new HbzAuth();
    }

    @Override
    public HbzAuthDTO newDTO() {
        return new HbzAuthDTO();
    }

    @Override
    public HbzAuth find(Long id) {
        return hbzAuthRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzAuth entity, HbzAuthDTO dto) {
        BeanUtils.copyProperties(entity, dto);
    }

    @Override
    public void dto2entity(HbzAuthDTO dto, HbzAuth entity) {
        BeanUtils.copyProperties(dto, entity, Const.AUDIT_FIELDS);
    }
}
