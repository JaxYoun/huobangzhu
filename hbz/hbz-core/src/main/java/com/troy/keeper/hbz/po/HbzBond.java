package com.troy.keeper.hbz.po;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/12/25.
 */
@Data
@Entity
@Table(name = "hbz_bond")
public class HbzBond extends BaseVersionLocked {

    //保证金编号
    @Column(columnDefinition = "varchar(128) comment '保证金编号'")
    private String bondNo;

    //用户
    @ManyToOne
    @JoinColumn(name = "user", columnDefinition = "bigint comment '用户'")
    private HbzUser user;

    //包证金档次
    @ManyToOne
    @JoinColumn(name = "grade", columnDefinition = "bigint comment '档次'")
    private HbzBondGrade bondGrade;

    //保证交纳金额
    @Column(columnDefinition = "double comment '金额'")
    private Double amount;

    //0-未交，1-已交，2-已冻结
    @Column
    private Integer bondStatus;

}
