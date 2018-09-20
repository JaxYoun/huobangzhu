package com.troy.keeper.hbz.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by leecheng on 2018/1/11.
 */
@Getter
@Setter
@Entity
@Table(name = "hbz_src")
public class HbzSource extends BaseVersionLocked {

    @Column(name = "src_lbl")
    private String label;

    @Column(name = "pack", columnDefinition = "varchar(1000)")
    private String pack;

    @Lob
    @Column(columnDefinition = "longtext comment '资源'")
    private String src;

}
