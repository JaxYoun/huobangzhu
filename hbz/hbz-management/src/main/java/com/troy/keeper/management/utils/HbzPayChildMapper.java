package com.troy.keeper.management.utils;

import com.troy.keeper.hbz.dto.HbzPayDTO;
import com.troy.keeper.hbz.po.HbzPay;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.management.dto.HbzPayChildDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author 李奥
 * @date 2017/12/14.
 */
@Component
public class HbzPayChildMapper extends BaseMapper<HbzPay, HbzPayChildDTO> {
    @Override
    public HbzPay newEntity() {
        return null;
    }

    @Override
    public HbzPayChildDTO newDTO() {
        return null;
    }

    @Override
    public HbzPay find(Long id) {
        return null;
    }

    @Override
    public void entity2dto(HbzPay entity, HbzPayChildDTO dto) {
        BeanUtils.copyProperties(entity, dto);
    }

    @Override
    public void dto2entity(HbzPayChildDTO dto, HbzPay entity) {

    }
}
