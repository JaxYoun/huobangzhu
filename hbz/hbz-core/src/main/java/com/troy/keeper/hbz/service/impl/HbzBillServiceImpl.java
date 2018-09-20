package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzBillDTO;
import com.troy.keeper.hbz.po.HbzBill;
import com.troy.keeper.hbz.repository.HbzBillRepository;
import com.troy.keeper.hbz.service.HbzBillService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzBillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;

/**
 * Created by leecheng on 2018/3/8.
 */
@Service
@Transactional
public class HbzBillServiceImpl extends BaseEntityServiceImpl<HbzBill, HbzBillDTO> implements HbzBillService {

    @Autowired
    HbzBillMapper hbzBillMapper;

    @Autowired
    HbzBillRepository hbzBillRepository;

    @Override
    public BaseMapper<HbzBill, HbzBillDTO> getMapper() {
        return hbzBillMapper;
    }

    @Override
    public BaseRepository<HbzBill, Serializable> getRepository() {
        return (BaseRepository<HbzBill, Serializable>) (Object) hbzBillRepository;
    }
}
