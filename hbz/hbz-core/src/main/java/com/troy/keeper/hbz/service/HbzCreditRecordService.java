package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzCreditRecordDTO;
import com.troy.keeper.hbz.po.HbzCreditRecord;

/**
 * Created by leecheng on 2018/1/19.
 */
public interface HbzCreditRecordService extends BaseEntityService<HbzCreditRecord, HbzCreditRecordDTO> {

    String createNo();

}
