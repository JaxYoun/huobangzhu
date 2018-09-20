package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.hbz.https.Https;
import com.troy.keeper.hbz.service.PayAccountService;
import com.troy.keeper.hbz.sys.annotation.Config;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by leecheng on 2017/11/6.
 */
@Service
public class PayAccountServiceImpl implements PayAccountService {

    @Config("com.tencent.wechat.appId")
    private String wechatAppId;

    @Override
    public Map<String, Object> wechatInvoke() {
        Map<String, Object> invokeParameter = new LinkedHashMap<>();
        invokeParameter.put("appid", wechatAppId);
        invokeParameter.put("scope", "snsapi_userinfo");
        invokeParameter.put("state", "wechat_sdk_demo");
        return invokeParameter;
    }

    @Override
    public Map<String, Object> wechatCreate(String code) {
        Https https = new Https();
        return null;
    }
}
