package com.troy.keeper.hbz.service;

import java.util.Map;

/**
 * Created by leecheng on 2017/11/6.
 */
//支付账户绑定、设置
public interface PayAccountService {

    //的发起登录时进行参数准备，提供AppId、scope,state
    Map<String, Object> wechatInvoke();

    Map<String, Object> wechatCreate(String code);
}
