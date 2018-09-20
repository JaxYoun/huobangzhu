package com.troy.keeper.hbz.po;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * 版本锁
 * Created by leecheng on 2017/12/1.
 */
@Getter
@Setter
@MappedSuperclass
public class BaseVersionLocked extends BaseAuditingEntity {

    /**
    @Version
    @Column(columnDefinition = "bigint comment '版本'")
    private Long versionLocked;
    */
}
