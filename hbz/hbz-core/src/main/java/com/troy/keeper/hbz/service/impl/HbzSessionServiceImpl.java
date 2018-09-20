package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.hbz.po.HbzSession;
import com.troy.keeper.hbz.repository.HbzSessionRepository;
import com.troy.keeper.hbz.service.HbzSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by leecheng on 2017/11/10.
 */
@Service
@Transactional
public class HbzSessionServiceImpl implements HbzSessionService {

    @Autowired
    private HbzSessionRepository sessionRepository;

    @Override
    public void save(HbzSession session) {
        sessionRepository.save(session);
    }

    @Override
    public void deleteAllBySessionId(String sessionId) {
        sessionRepository.deleteAllBySessionId(sessionId);
    }

    @Override
    public List<HbzSession> findAllBySessionId(String sessionId) {
        return sessionRepository.findAllBySessionId(sessionId);
    }

    public boolean haveSession(String sessionId) {
        return sessionRepository.countAllBySessionId(sessionId) > 0L;
    }
}
