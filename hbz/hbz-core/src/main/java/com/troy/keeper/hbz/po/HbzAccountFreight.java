package com.troy.keeper.hbz.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/11/20.
 */
//常用司机
@Setter
@Getter
@Entity
@Table(name = "hbz_account_freight")
public class HbzAccountFreight extends BaseVersionLocked {

    @ManyToOne
    @JoinColumn(name = "host", columnDefinition = "bigint comment '记录所属'")
    private HbzUser host;

    @ManyToOne
    @JoinColumn(name = "user", columnDefinition = "bigint comment '常用用户'")
    private HbzUser user;

    @Column(name = "seq", columnDefinition = "int comment '序号'")
    private Integer index;

}
