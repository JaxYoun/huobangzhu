package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzOrderRecordDTO;
import com.troy.keeper.hbz.po.HbzOrderRecord;

/**
 * Created by leecheng on 2017/11/3.
 */
public interface HbzOrderRecordService extends BaseEntityService<HbzOrderRecord, HbzOrderRecordDTO> {

    boolean saveOrderRec(HbzOrderRecordDTO orderRecord);

}
