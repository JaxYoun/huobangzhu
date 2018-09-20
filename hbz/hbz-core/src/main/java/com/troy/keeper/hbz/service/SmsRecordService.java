package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.SmsRecordDTO;
import com.troy.keeper.hbz.po.SmsRecord;
import com.troy.keeper.hbz.vo.SmsRecordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/22 17:00
 */
public interface SmsRecordService {

    /**
     * 分页条件查询短信发送记录
     *
     * @param smsRecordDTO
     * @return
     */
    Page<SmsRecordVO> getSmsRecordListByPage(SmsRecordDTO smsRecordDTO, Pageable pageable);

    /**
     * 获取短信发送记录详情
     *
     * @param smsRecordDTO
     * @return
     */
    SmsRecordVO getSmsRecordDetail(SmsRecordDTO smsRecordDTO);

    static SmsRecordVO entityToVo(SmsRecord smsRecord) {
        if (smsRecord == null) {
            return null;
        }
        SmsRecordVO smsRecordVO = new SmsRecordVO();
        BeanUtils.copyProperties(smsRecord, smsRecordVO);
        return smsRecordVO;
    }
}