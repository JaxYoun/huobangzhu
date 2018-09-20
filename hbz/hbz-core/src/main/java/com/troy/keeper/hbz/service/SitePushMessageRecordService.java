package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.SitePushMessageRecordDTO;
import com.troy.keeper.hbz.vo.SitePushMessageRecoredVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/25 11:18
 */
public interface SitePushMessageRecordService {

    /**
     * 分页条件查询推送记录
     *
     * @param sitePushMessageRecordDTO
     * @param pageable
     * @return
     */
    Page<SitePushMessageRecoredVO> getSitePushMessageRecordListByPage(SitePushMessageRecordDTO sitePushMessageRecordDTO, Pageable pageable, boolean... isApp);

    /**
     * 后台管理端、从app列表获取消息推送记录详情
     *
     * @param id
     * @return
     */
    SitePushMessageRecoredVO getSitePushMessageRecordDetail(Long id, boolean... isApp);


    /**
     * app端阅读送消息记录
     *
     * @param sitePushMessageRecordDTO
     * @return
     */
    SitePushMessageRecoredVO readSitePushMessageRecord(SitePushMessageRecordDTO sitePushMessageRecordDTO, boolean... isApp);

    /**
     * 获取当前用户的未阅读站内消息条数
     *
     * @param phoneNo
     * @param isApp
     * @return
     */
    Integer getMyUnreadSitePushMessageRecordCount(String phoneNo, boolean... isApp);

}
