package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.SitePushMessage;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/18 10:34
 */
public interface SitePushMessageRepository extends BaseRepository<SitePushMessage, Long> {

    @Query("SELECT MAX(t.id) AS maxId FROM SitePushMessage AS t")
    Long getMaxIdSitePushMessage();

    SitePushMessage getSitePushMessageByIdAndIfSendAndStatus(Long id, String ifSend, String status);

    /**
     * 获取待发送、有效、定时类的推送消息
     *
     * @param ifSend
     * @param status
     * @param pushType
     * @return
     */
    List<SitePushMessage> getDistinctByIfSendAndStatusAndPushType(String ifSend, String status, String pushType);

}
