package com.troy.keeper.hbz.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by leecheng on 2017/11/9.
 */
//Session
@Getter
@Setter
@Entity
@Table(name = "hbz_session")
public class HbzSession extends BaseVersionLocked implements Serializable {

    @Column(name = "sessionId", columnDefinition = "varchar(255) comment '会话TOKEN'")
    private String sessionId;

    @Column(name = "loginFor", columnDefinition = "varchar(8) comment '登录终端'")
    private String loginFor;

    @Column(name = "creationTime", columnDefinition = "bigint comment '创建时间'")
    private Long creationTime;

    @Column(name = "lastAccessedTime", columnDefinition = "bigint comment '最后访问时间'")
    private Long lastAccessedTime;

    @Lob
    @Column(name = "sessionData", columnDefinition = "longtext comment '会话数据序列化'")
    private String data;

    @Column(columnDefinition = "text comment '登录语言'")
    private String language;
}
