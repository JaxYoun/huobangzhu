package com.troy.keeper.hbz.po;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by leecheng on 2017/11/22.
 */
//字典
@Getter
@Setter
@Entity
@Table(name = "hbz_typeVal")
public class HbzTypeVal extends BaseVersionLocked {

    @Column(columnDefinition = "varchar(64) comment '类型'")
    private String type;

    @Column(columnDefinition = "varchar(64) comment '值'")
    private String val;

    @Column(columnDefinition = "varchar(64) comment '呈现值'")
    private String name;

    @Column(columnDefinition = "varchar(32) comment '语言'")
    private String language;

}
