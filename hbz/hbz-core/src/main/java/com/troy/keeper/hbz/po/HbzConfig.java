package com.troy.keeper.hbz.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by leecheng on 2017/10/19.
 */
//配置
@Getter
@Setter
@Entity
@Table(name = "hbz_config")
public class HbzConfig extends BaseVersionLocked {

    @Column(name = "propKey", columnDefinition = "varchar(255) comment '属性名'")
    private String key;

    @Lob
    @Column(name = "propValue", columnDefinition = "longtext comment '值'")
    private String value;

}
