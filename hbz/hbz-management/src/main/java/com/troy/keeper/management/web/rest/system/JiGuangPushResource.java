package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.helper.JpushUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：YangJx
 * @Description：极光推送
 * @DateTime：2018/1/23 15:07
 */
@Slf4j
@RestController
@RequestMapping("/api/manager/jpush")
public class JiGuangPushResource {

    @Autowired
    private JpushUtils jpushUtils;

    /**
     * 激光推送测试
     *
     * @return
     */
    @PostMapping("/pushByJiGuangTest")
    public ResponseDTO pushByJiGuangTest() {
        String[] aliasArr = {"hbz_18080818652"};
//        jpushUtils.jiguangPush(aliasArr, "kkkkk");
        return new ResponseDTO(Const.STATUS_OK, "成功", null);
    }
}
