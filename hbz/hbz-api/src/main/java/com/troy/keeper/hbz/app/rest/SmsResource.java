package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.dto.SmsVO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by leecheng on 2017/10/25.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/sms")
public class SmsResource {

    @Autowired
    private SmsService smsService;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public ResponseDTO smsSend(@RequestBody SmsVO sms) {
        if (smsService.send(sms.getPhone(), sms.getMsg())) {
            return new ResponseDTO(Const.STATUS_OK, "短信发送成功");
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "短信发送失败");
        }
    }

}
