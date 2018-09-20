package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzReimburseDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzReimburse;
import com.troy.keeper.hbz.repository.HbzReimburseRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2018/1/3.
 */
@Component
public class HbzReimburseMapper extends BaseMapper<HbzReimburse, HbzReimburseDTO> {

    @Autowired
    HbzReimburseRepository hbzReimburseRepository;

    @Override
    public HbzReimburse newEntity() {
        return new HbzReimburse();
    }

    @Override
    public HbzReimburseDTO newDTO() {
        return new HbzReimburseDTO();
    }

    @Override
    public HbzReimburse find(Long id) {
        return hbzReimburseRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzReimburse entity, HbzReimburseDTO dto) {
        BeanUtils.copyProperties(entity, dto);
    }

    @Override
    public void dto2entity(HbzReimburseDTO dto, HbzReimburse entity) {
        BeanUtils.copyProperties(dto, entity, StringHelper.conact(Const.ID_AUDIT_FIELDS));
    }
}
