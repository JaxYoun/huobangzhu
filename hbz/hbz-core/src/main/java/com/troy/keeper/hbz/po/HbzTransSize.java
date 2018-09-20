package com.troy.keeper.hbz.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by leecheng on 2017/12/6.
 */
//车辆规格
@Entity
@Table(name = "hbz_trans_size")
@Data
public class HbzTransSize extends BaseVersionLocked {

    @Column(columnDefinition = "double comment '车辆规格'")
    private Double transSize;

}
