package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzBillDTO;
import com.troy.keeper.hbz.po.HbzBill;
import com.troy.keeper.hbz.repository.HbzBillRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2018/3/8.
 */
@Component
public class HbzBillMapper extends BaseMapper<HbzBill, HbzBillDTO> {

    @Autowired
    HbzBillRepository hbzBillRepository;

    @Override
    public HbzBill newEntity() {
        return new HbzBill();
    }

    @Override
    public HbzBillDTO newDTO() {
        return new HbzBillDTO();
    }

    @Override
    public HbzBill find(Long id) {
        return hbzBillRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzBill entity, HbzBillDTO dto) {
        new Bean2Bean().copyProperties(entity, dto);
    }

    @Override
    public void dto2entity(HbzBillDTO dto, HbzBill entity) {
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(dto, entity);
    }
}
