package com.troy.keeper.hbz.po;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/12/19.
 */
@Data
@Entity
@Table(name = "hbz_score")
public class HbzScore extends BaseVersionLocked {

    @ManyToOne
    @JoinColumn(name = "user", columnDefinition = "bigint comment '用户id'")
    private HbzUser user;

    @Column(columnDefinition = "double comment '积分'")
    private Double score;

}
