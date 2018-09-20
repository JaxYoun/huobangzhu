package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.Event;

import javax.persistence.*;

/**
 * Created by lee on 2017/10/17.
 */
//事件
@Entity
@Table(name = "hbz_event")
public class HbzEvent extends BaseVersionLocked {

    @Enumerated(EnumType.STRING)
    @Column(name = "event")
    private Event event;

    @Column(name = "eventTime")
    private Long eventTime;

    @Column(name = "username")
    private String username;

    @Column(name = "eventUrl")
    private String eventUrl;

    @Lob
    @Column(name = "eventParameter")
    private String eventParameter;

    @Lob
    @Column(name = "grantedAuthorities")
    private String grantedAuthorities;

    @Column(name = "debug")
    private Boolean debug;

    private String ip;
    private Integer port;
    @Lob
    @Column(name = "requestHeader")
    private String requestHeader;

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Event getEvent() {
        return event;
    }

    public Boolean getDebug() {
        return debug;
    }

    public void setDebug(Boolean debug) {
        this.debug = debug;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Long getEventTime() {
        return eventTime;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public String getEventParameter() {
        return eventParameter;
    }

    public void setEventParameter(String eventParameter) {
        this.eventParameter = eventParameter;
    }

    public void setGrantedAuthorities(String grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    public String getGrantedAuthorities() {
        return grantedAuthorities;
    }
}
