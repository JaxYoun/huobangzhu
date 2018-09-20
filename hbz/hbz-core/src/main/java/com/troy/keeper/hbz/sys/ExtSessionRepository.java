package com.troy.keeper.hbz.sys;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.SessionStorageProvider;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.helper.subcomponent.LoginHelper;
import com.troy.keeper.hbz.helper.subcomponent.ObjHelper;
import com.troy.keeper.hbz.po.HbzSession;
import com.troy.keeper.hbz.service.HbzSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.SessionRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leecheng on 2017/11/9.
 */
public class ExtSessionRepository implements SessionRepository<ExtSession> {

    @Autowired
    private HbzSessionService hbzSessionService;

    @Autowired(required = false)
    private RedisTemplate redisTemplate;

    @Value("${session.provider}")
    private SessionStorageProvider sessionStorageProvider;

    @Override
    public ExtSession createSession() {
        String sessionId = StringHelper.uuid();
        Map<String, Object> session = new LinkedHashMap<>();
        long current = System.currentTimeMillis();
        return new ExtSession(sessionId, session, current, current, 300);
    }

    @Override
    public void save(ExtSession session) {
        HbzSession hbzSession = findSession(session.getId());
        String val = ObjHelper.obj2str(session);
        HbzSession s;
        if (hbzSession != null) {
            s = hbzSession;
        } else {
            s = new HbzSession();
        }
        String loginFor = LoginHelper.getLoginFor();
        String language = LoginHelper.getLanguage();
        s.setSessionId(session.getId());
        s.setCreationTime(session.getCreationTime());
        s.setLastAccessedTime(session.getLastAccessedTime());
        s.setStatus(Const.STATUS_ENABLED);

        if (StringHelper.notNullAndEmpty(loginFor))
            s.setLoginFor(loginFor);
        if (StringHelper.notNullAndEmpty(language))
            s.setLanguage(language);
        s.setData(val);
        saveSession(session.getId(), s);
    }

    @Override
    public ExtSession getSession(String id) {
        HbzSession hbzSession = findSession(id);
        if (hbzSession == null) {
            return null;
        }
        String val = hbzSession.getData();
        ExtSession session = (ExtSession) ObjHelper.str2obj(val);
        Long currentTimeMillis = System.currentTimeMillis();
        Long lastAccessTimeMillis = session.getLastAccessedTime();
        String loginFor = hbzSession.getLoginFor();
        if (loginFor == null) {
            return session;
        } else {
            switch (loginFor) {
                case "Web": {
                    if (currentTimeMillis - lastAccessTimeMillis > (long) 60 * (long) 60 * (long) 1000) {
                        session_delete(hbzSession.getSessionId());
                        return null;
                    }
                }
                break;
                case "App": {
                    if (currentTimeMillis - lastAccessTimeMillis > (long) 30 * (long) 24 * (long) 60 * (long) 60 * (long) 1000) {
                        session_delete(hbzSession.getSessionId());
                        return null;
                    }
                }
                break;
            }
        }
        return session;
    }

    @Override
    public void delete(String id) {
        session_delete(id);
    }

    private void saveSession(String key, HbzSession value) {
        if (sessionStorageProvider.equals(SessionStorageProvider.redis)) {
            HashOperations<String, String, String> hash = redisTemplate.opsForHash();
            hash.put("SESSION_STORE", key, ObjHelper.obj2str(value));
        } else if (sessionStorageProvider.equals(SessionStorageProvider.db)) {
            hbzSessionService.save(value);
        } else {
            throw new IllegalStateException("无法找到session持久化方案");
        }
    }

    public HbzSession findSession(String key) {
        if (sessionStorageProvider.equals(SessionStorageProvider.redis)) {
            HashOperations<String, String, String> hash = redisTemplate.opsForHash();
            return (HbzSession) ObjHelper.str2obj(hash.get("SESSION_STORE", key));
        } else if (sessionStorageProvider.equals(SessionStorageProvider.db)) {
            List<HbzSession> sessions = hbzSessionService.findAllBySessionId(key);
            if (sessions == null || sessions.size() == 0) return null;
            return sessions.get(0);
        } else {
            throw new IllegalStateException("无法找到session持久化方案");
        }
    }

    private void session_delete(String key) {
        if (sessionStorageProvider.equals(SessionStorageProvider.redis)) {
            HashOperations<String, String, String> hash = redisTemplate.opsForHash();
            hash.delete("SESSION_STORE", key);
        } else if (sessionStorageProvider.equals(SessionStorageProvider.db)) {
            hbzSessionService.deleteAllBySessionId(key);
        } else {
            throw new IllegalStateException("无法找到session持久化方案");
        }
    }
}