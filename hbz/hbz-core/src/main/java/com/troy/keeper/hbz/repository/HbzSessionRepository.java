package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzSession;

import java.util.List;

/**
 * Created by leecheng on 2017/10/16.
 */
public interface HbzSessionRepository extends BaseRepository<HbzSession, Long> {

    void deleteAllBySessionId(String sessionId);

    List<HbzSession> findAllBySessionId(String sessionId);

    Long countAllBySessionId(String sessionId);

}
