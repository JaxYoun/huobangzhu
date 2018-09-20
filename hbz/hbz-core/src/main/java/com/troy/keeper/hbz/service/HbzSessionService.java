package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.po.HbzSession;

import java.util.List;

/**
 * Created by leecheng on 2017/11/10.
 */
public interface HbzSessionService {

    void save(HbzSession session);

    void deleteAllBySessionId(String sessionId);

    List<HbzSession> findAllBySessionId(String sessionId);

    boolean haveSession(String sessionId);
}
