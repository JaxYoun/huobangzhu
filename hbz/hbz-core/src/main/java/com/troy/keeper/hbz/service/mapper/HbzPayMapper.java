package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzPayDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzPay;
import com.troy.keeper.hbz.repository.HbzPayRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/10/30.
 */
@Component
public class HbzPayMapper extends BaseMapper<HbzPay, HbzPayDTO> {

    @Autowired
    private HbzPayRepository hbzPayRepository;

    @Override
    public HbzPay newEntity() {
        return new HbzPay();
    }

    @Override
    public HbzPayDTO newDTO() {
        return new HbzPayDTO();
    }

    @Override
    public HbzPay find(Long id) {
        return hbzPayRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzPay entity, HbzPayDTO dto) {
        BeanUtils.copyProperties(entity, dto);
    }

    @Override
    public void dto2entity(HbzPayDTO dto, HbzPay entity) {
        BeanUtils.copyProperties(dto, entity, StringHelper.conact(Const.ID_AUDIT_FIELDS));
    }
}
