package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 保证金充值
 * Created by leecheng on 2017/12/4.
 */
@Getter
@Setter
@Entity
@Table(name = "hbz_recharge")
public class HbzRecharge extends BaseVersionLocked {

    @Column(columnDefinition = "varchar(128) comment '充值编号'")
    private String chargeNo;

    //关联用户
    @ManyToOne
    @JoinColumn(name = "userid", columnDefinition = "bigint comment '用户标置'")
    private HbzUser user;

    @Column(columnDefinition = "double comment '金额'")
    private Double money;

    @Column(columnDefinition = "bigint comment '操作时间'")
    private Long executeDate;

    @Column(name = "charge_state", columnDefinition = "int comment '充值状态'")
    private Integer state;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '身份对应保证金类型'")
    private Role role;

}
