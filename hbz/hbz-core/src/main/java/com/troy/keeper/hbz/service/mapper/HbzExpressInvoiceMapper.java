package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzExpressInvoiceDTO;
import com.troy.keeper.hbz.po.HbzExpressInvoice;
import com.troy.keeper.hbz.repository.HbzExpressInvoiceRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2018/1/5.
 */
@Component
public class HbzExpressInvoiceMapper extends BaseMapper<HbzExpressInvoice, HbzExpressInvoiceDTO> {

    @Autowired
    private HbzExpressInvoiceRepository hbzExpressInvoiceRepository;

    @Override
    public HbzExpressInvoice newEntity() {
        return new HbzExpressInvoice();
    }

    @Override
    public HbzExpressInvoiceDTO newDTO() {
        return new HbzExpressInvoiceDTO();
    }

    @Override
    public HbzExpressInvoice find(Long id) {
        return hbzExpressInvoiceRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzExpressInvoice entity, HbzExpressInvoiceDTO dto) {
        new Bean2Bean().copyProperties(entity, dto);
    }

    @Override
    public void dto2entity(HbzExpressInvoiceDTO dto, HbzExpressInvoice entity) {
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(dto, entity);
    }
}
