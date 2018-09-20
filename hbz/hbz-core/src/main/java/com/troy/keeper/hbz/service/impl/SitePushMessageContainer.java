package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.hbz.po.SitePushMessage;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/24 17:00
 */
public class SitePushMessageContainer {

    private final ConcurrentHashMap<Long, SitePushMessage> messageMap;

    private static SitePushMessageContainer instance = null;

    private SitePushMessageContainer() {
        super();
        messageMap = new ConcurrentHashMap<>(512);
    }

    public static SitePushMessageContainer getInstance() {
        if (instance == null) {
            synchronized (SitePushMessageContainer.class) {
                if (instance == null) {
                    instance = new SitePushMessageContainer();
                }
            }
        }
        return instance;
    }

    public ConcurrentHashMap<Long, SitePushMessage> getMessageMap() {
        return messageMap;
    }
}
