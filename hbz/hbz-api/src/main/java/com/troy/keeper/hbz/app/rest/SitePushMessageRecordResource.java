package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.SitePushMessageRecordDTO;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.service.SitePushMessageRecordService;
import com.troy.keeper.hbz.vo.SitePushMessageRecoredVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author：YangJx
 * @Description：推送消息记录
 * @DateTime：2018/1/26 11:15
 */
@Slf4j
@RestController
@RequestMapping("/api/sitePushMessageRecord")
public class SitePushMessageRecordResource {

    @Autowired
    private HbzUserService hbzUserService;

    @Autowired
    private SitePushMessageRecordService sitePushMessageRecordService;

    /**
     * 阅读推送消息
     *
     * @param sitePushMessageRecordDTO
     * @return
     */
    @PostMapping("/readSitePushMessageRecord")
    public ResponseDTO readSitePushMessageRecord(@RequestBody SitePushMessageRecordDTO sitePushMessageRecordDTO) {
        if (sitePushMessageRecordDTO.getSitePushMessageId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入消息id", null);
        }
        sitePushMessageRecordDTO.setConsumerId(hbzUserService.currentUser().getId());
        sitePushMessageRecordDTO.setStatus(Const.STATUS_ENABLED);
//        sitePushMessageRecordDTO.setIfRead(Const.STATUS_DISABLED);

        SitePushMessageRecoredVO sitePushMessageRecoredVO = this.sitePushMessageRecordService.readSitePushMessageRecord(sitePushMessageRecordDTO, true);
        if (sitePushMessageRecoredVO == null) {
            return new ResponseDTO(Const.STATUS_OK, "消息已被消费", null);
        } else {
            return new ResponseDTO(Const.STATUS_OK, "成功", sitePushMessageRecoredVO);
        }
    }

    /**
     * 分页条件查询推送记录
     *
     * @param sitePushMessageRecordDTO
     * @return
     */
    @PostMapping("/getMySitePushMessageRecordListByPage")
    public ResponseDTO getMySitePushMessageRecordListByPage(@RequestBody SitePushMessageRecordDTO sitePushMessageRecordDTO) {
        sitePushMessageRecordDTO.setStatus(Const.STATUS_ENABLED);
        sitePushMessageRecordDTO.setConsumerId(hbzUserService.currentUser().getId());
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.DESC, "createdDate"));
        orderList.add(new Sort.Order(Sort.Direction.DESC, "id"));
        Pageable pageable = new PageRequest(sitePushMessageRecordDTO.getPage(), sitePushMessageRecordDTO.getSize(), new Sort(orderList));
        return new ResponseDTO(Const.STATUS_OK, "成功", this.sitePushMessageRecordService.getSitePushMessageRecordListByPage(sitePushMessageRecordDTO, pageable, true));
    }

    /**
     * 获取发送记录详情
     *
     * @param sitePushMessageRecordDTO
     * @return
     */
    @PostMapping("/getSitePushMessageRecordDetail")
    public ResponseDTO getSitePushMessageRecordDetail(@RequestBody SitePushMessageRecordDTO sitePushMessageRecordDTO) {
        if (sitePushMessageRecordDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入id", null);
        }
        return new ResponseDTO(Const.STATUS_OK, "成功", this.sitePushMessageRecordService.getSitePushMessageRecordDetail(sitePushMessageRecordDTO.getId(), true));
    }

    /**
     * 获取当前用户的未阅读站内消息条数
     *
     * @return
     */
    @PostMapping("/getUnreadMessageCount")
    public ResponseDTO getMyUnreadSitePushMessageRecordCount() {
        String phoneNo = this.hbzUserService.currentUser().getTelephone();
        if(StringUtils.isBlank(phoneNo)) {
            return new ResponseDTO(Const.STATUS_ERROR, "电话号码无效!", null);
        } else {
            return new ResponseDTO(Const.STATUS_OK, "查询成功！", this.sitePushMessageRecordService.getMyUnreadSitePushMessageRecordCount(phoneNo,true));
        }
    }
}
