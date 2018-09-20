package com.troy.keeper.hbz.po;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by leecheng on 2017/12/18.
 */
@Getter
@Data
@Entity
@Table(name = "hbz_bin")
public class HbzBinary extends BaseVersionLocked {

    @Column(columnDefinition = "varchar(100) comment '业务哈希'")
    private String bzHash;

    @Column(columnDefinition = "varchar(100) comment '组哈希'")
    private String gpHash;

    @Column(columnDefinition = "varchar(100) comment '键哈希'")
    private String keyHash;

    @Column(name = "ckey",unique = true, columnDefinition = "varchar(200) comment '引用键'")
    private String key;

    @Column(columnDefinition = "varchar(1000) comment '数据路径'")
    private String path;

    @Column(columnDefinition = "varchar(100) comment '显示'")
    private String display;

    @Lob
    @Column(columnDefinition = "longtext comment '数据'")
    private String data;

}
