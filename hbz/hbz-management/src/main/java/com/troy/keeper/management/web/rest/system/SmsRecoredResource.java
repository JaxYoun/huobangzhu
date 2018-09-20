package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.SmsRecordDTO;
import com.troy.keeper.hbz.service.SmsRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author：YangJx
 * @Description：短信发送记录
 * @DateTime：2018/1/22 16:52
 */
@Slf4j
@RestController
@RequestMapping("/api/manager/smsRecord")
public class SmsRecoredResource {

    @Autowired
    private SmsRecordService smsRecordService;

    /**
     * 分页条件查询短信发送记录
     *
     * @param smsRecordDTO
     * @return
     */
    @PostMapping("/getSmsRecordListByPage")
    public ResponseDTO getSmsRecordListByPage(@RequestBody SmsRecordDTO smsRecordDTO) {
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.DESC, "createdDate"));
        orderList.add(new Sort.Order(Sort.Direction.DESC, "id"));
        Pageable pageable = new PageRequest(smsRecordDTO.getPage(), smsRecordDTO.getSize(), new Sort(orderList));
        smsRecordDTO.setStatus(Const.STATUS_ENABLED);
        return new ResponseDTO(Const.STATUS_OK, "成功", this.smsRecordService.getSmsRecordListByPage(smsRecordDTO, pageable));
    }

    /**
     * 获取短信发送记录详情
     *
     * @param smsRecordDTO
     * @return
     */
    @PostMapping("/getSmsRecordDetail")
    public ResponseDTO getSmsRecordDetail(@RequestBody SmsRecordDTO smsRecordDTO) {
        if (smsRecordDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入id", null);
        }
        return new ResponseDTO(Const.STATUS_OK, "成功", this.smsRecordService.getSmsRecordDetail(smsRecordDTO));
    }

}
