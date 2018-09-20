package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzCreditRecordDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzCreditRecord;
import com.troy.keeper.hbz.repository.HbzCreditRecordRepository;
import com.troy.keeper.hbz.service.HbzCreditRecordService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzCreditRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2018/1/19.
 */
@Service
@Transactional
public class HbzCreditRecordServiceImpl extends BaseEntityServiceImpl<HbzCreditRecord, HbzCreditRecordDTO> implements HbzCreditRecordService {

    @Autowired
    HbzCreditRecordRepository hbzCreditRecordRepository;

    @Autowired
    HbzCreditRecordMapper hbzCreditRecordMapper;

    @Override
    public BaseMapper<HbzCreditRecord, HbzCreditRecordDTO> getMapper() {
        return hbzCreditRecordMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzCreditRecordRepository;
    }

    @Override
    public String createNo() {
        String no;
        int id = 0;
        while (true) {
            no = StringHelper.frontCompWithZero(++id, 8);
            if (hbzCreditRecordRepository.countByRecNo(no) < 1) return no;
        }
    }
}
