package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.BizCode;
import lombok.Data;

import javax.persistence.*;

/**
 * 保证金质押表
 * Created by leecheng on 2018/1/31.
 */
@Data
@Entity
@Table(name = "hbz_pledge")
public class HbzPledge extends BaseVersionLocked {

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(16) comment '业务类'")
    private BizCode bizCode;

    @Column(columnDefinition = "varchar(64) comment '业务单号'")
    private String bizNo;

    @Column(columnDefinition = "varchar(64) comment '保证金编号'")
    private String bondNo;

    @Column(columnDefinition = "bigint comment '质押时间'")
    private Long bizTime;

}
