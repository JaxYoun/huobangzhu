package com.troy.keeper.hbz.po;

import lombok.*;

import javax.persistence.*;

/**
 * @Autohor: hecj
 * @Description: 仓储审核表
 * @Date: Created in 11:05  2018/1/9.
 * @Midified By:
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hbz_warehouse_audit")
public class HbzWarehouseAudit extends BaseVersionLocked {

    @Column(columnDefinition = "varchar(100) COMMENT '备注'")
    private String recordComment;

    @Column(columnDefinition = "bigint COMMENT '审核时间'")
    private Long checkedDate;

    @Column(columnDefinition = "varchar(2) COMMENT '是否审核 00未审核 01通过 02不通过'")
    private String type;

    @Column(columnDefinition = "bigint COMMENT '用户id'")
    private Long createUserId;

    @OneToOne(fetch = FetchType.EAGER, optional = false,cascade = CascadeType.ALL)
    private Warehouse warehouse;

}
