package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzOrderRecordDTO;
import com.troy.keeper.hbz.po.HbzOrderRecord;
import com.troy.keeper.hbz.repository.HbzOrderRecordRepository;
import com.troy.keeper.hbz.service.HbzOrderRecordService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzOrderRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by leecheng on 2017/11/3.
 */
@Service
@Transactional
public class HbzOrderRecordServiceImpl extends BaseEntityServiceImpl<HbzOrderRecord, HbzOrderRecordDTO> implements HbzOrderRecordService {

    @Autowired
    private HbzOrderRecordRepository hbzOrderRecordRepository;

    @Autowired
    private HbzOrderRecordMapper hbzOrderRecordMapper;

    @Override
    public BaseMapper<HbzOrderRecord, HbzOrderRecordDTO> getMapper() {
        return hbzOrderRecordMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzOrderRecordRepository;
    }

    @Override
    public boolean saveOrderRec(HbzOrderRecordDTO orderRecord) {
        HbzOrderRecordDTO query = new HbzOrderRecordDTO();
        query.setOrderId(orderRecord.getOrderId());
        query.setOrderTrans(orderRecord.getOrderTrans());
        List<HbzOrderRecordDTO> list = query(query);
        if (list != null && list.size() > 0) list.stream().forEach(this::delete);
        orderRecord.setStatus(Const.STATUS_ENABLED);
        return save(orderRecord) != null;
    }
}
