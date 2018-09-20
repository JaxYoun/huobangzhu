package com.troy.keeper.hbz.sys;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.session.ExpiringSession;
import org.springframework.session.Session;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Created by leecheng on 2017/11/9.
 */
@Getter
@Setter
@ToString
public class ExtSession implements Session, ExpiringSession, Serializable {

    private String id;

    private Map<String, Object> data;

    private long creationTime;

    private long lastAccessedTime;

    private int maxInactiveIntervalInSeconds;

    public Map<String, Object> getSessionData() {
        return data;
    }

    public ExtSession(String sessionId, Map<String, Object> sessionData, long creationTime, long lastAccessedTime, int maxInactiveIntervalInSeconds) {
        this.id = sessionId;
        this.data = sessionData;
        this.creationTime = creationTime;
        this.lastAccessedTime = lastAccessedTime;
        this.maxInactiveIntervalInSeconds = maxInactiveIntervalInSeconds;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public <T> T getAttribute(String attributeName) {
        return (T) data.get(attributeName);
    }

    @Override
    public Set<String> getAttributeNames() {
        return data.keySet();
    }

    @Override
    public void setAttribute(String attributeName, Object attributeValue) {
        data.put(attributeName, attributeValue);
    }

    @Override
    public void removeAttribute(String attributeName) {
        data.remove(attributeName);
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    @Override
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    @Override
    public void setMaxInactiveIntervalInSeconds(int interval) {
        this.maxInactiveIntervalInSeconds = interval;
    }

    @Override
    public int getMaxInactiveIntervalInSeconds() {
        return maxInactiveIntervalInSeconds;
    }

    @Override
    public boolean isExpired() {
        return false;
    }
}
