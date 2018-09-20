package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.dto.SitePushMessageDTO;
import com.troy.keeper.hbz.vo.SitePushMessageVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/18 10:36
 */
public interface SitePushMessageService {

    /**
     * 添加站内推送消息
     *
     * @param sitePushMessageDTO
     * @return
     */
    boolean addSitePushMessage(SitePushMessageDTO sitePushMessageDTO);

    /**
     * 删除站内推送消息
     *
     * @param sitePushMessageDTO
     * @return
     */
    boolean deleteSitePushMessage(SitePushMessageDTO sitePushMessageDTO);

    /**
     * 获取站内推送消息详情
     *
     * @param sitePushMessageDTO
     * @return
     */
    SitePushMessageVO getSitePushMessageDetail(SitePushMessageDTO sitePushMessageDTO);

    /**
     * 添加站内推送消息
     *
     * @param sitePushMessageDTO
     * @return
     */
    boolean updateSitePushMessage(SitePushMessageDTO sitePushMessageDTO);

    /**
     * 分页条件查询站内推送消息
     *
     * @param sitePushMessageDTO
     * @param pageable
     * @return
     */
    Page<SitePushMessageVO> getSitePushMessageListByPage(SitePushMessageDTO sitePushMessageDTO, Pageable pageable);

    /**
     * 推送站内消息
     *
     * @param sitePushMessageDTO
     * @return
     */
    boolean sendSitePushMessage(SitePushMessageDTO sitePushMessageDTO);

    /**
     * 通过id查询推送消息
     *
     * @param sitePushMessageDTO
     * @return
     */
    boolean getSitePushMessageById(SitePushMessageDTO sitePushMessageDTO);

    boolean sendMessageImmediately(List<HbzUserDTO> users, String title, String summary, String message);
}