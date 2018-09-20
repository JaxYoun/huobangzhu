package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzCreditRecord;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by leecheng on 2018/1/19.
 */
public interface HbzCreditRecordRepository extends BaseRepository<HbzCreditRecord, Long> {

    Long countByRecNo(String recNo);

}
