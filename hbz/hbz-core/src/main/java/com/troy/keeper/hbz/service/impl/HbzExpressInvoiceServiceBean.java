package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzExpressInvoiceDTO;
import com.troy.keeper.hbz.po.HbzExpressInvoice;
import com.troy.keeper.hbz.repository.HbzExpressInvoiceRepository;
import com.troy.keeper.hbz.service.HbzExpressInvoiceService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzExpressInvoiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2018/1/5.
 */
@Service
@Transactional
public class HbzExpressInvoiceServiceBean extends BaseEntityServiceImpl<HbzExpressInvoice, HbzExpressInvoiceDTO> implements HbzExpressInvoiceService {

    @Autowired
    HbzExpressInvoiceMapper hbzExpressInvoiceMapper;

    @Autowired
    HbzExpressInvoiceRepository hbzExpressInvoiceRepository;

    @Override
    public BaseMapper<HbzExpressInvoice, HbzExpressInvoiceDTO> getMapper() {
        return hbzExpressInvoiceMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzExpressInvoiceRepository;
    }
}
