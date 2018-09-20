package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.SitePushMessageRecord;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/18 16:07
 */
public interface SitePushMessageRecordRepository extends BaseRepository<SitePushMessageRecord, Long> {

    SitePushMessageRecord getSitePushMessageRecordByConsumerIdAndSitePushMessageIdAndStatus(Long consumerId, Long sitePushMessageId, String status);

    @Modifying
    @Query("update SitePushMessageRecord t set t.ifRead = ?1, t.readTime = ?2 where t.id = ?3")
    void updateIfReadAndReadTimeById (String ifRead, Long readTime, Long id);

    Integer countByPhoneNoAndStatusAndIfRead(String phoneNo, String status, String ifRead);
}
