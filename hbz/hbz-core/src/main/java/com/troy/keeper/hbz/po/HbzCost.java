package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.PayStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.apachecommons.CommonsLog;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/11/2.
 */
//企业支出
@Getter
@Setter
@ToString
@CommonsLog
@Entity
@Table(name = "hbz_cost")
public class HbzCost extends BaseVersionLocked {

    //任务编号
    @Column(columnDefinition = "varchar(70) comment '支出编号'")
    private String costNo;

    @ManyToOne
    @JoinColumn(name = "userid", columnDefinition = "bigint comment '用户'")
    private HbzUser user;

    //打款金融
    @Column(columnDefinition = "double comment '金额'")
    private Double amount;

    //创建时间
    @Column(columnDefinition = "bigint comment '创建时间'")
    private Long createdTime;

    //打款时间
    @Column(columnDefinition = "bigint comment '打款时'")
    private Long costTime;

    //关联业务
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(80) comment '业务类型'")
    private BusinessType businessType;

    //业务编号
    @Column(columnDefinition = "varchar(64) comment '业务编号'")
    private String businessNo;

    @Column(columnDefinition = "varchar(64) comment '付款状态'")
    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;

}
