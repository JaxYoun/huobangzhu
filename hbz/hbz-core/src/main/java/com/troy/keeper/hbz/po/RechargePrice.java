package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Author：YangJx
 * @Description：保证金价格 针对不同类型的保证金有不同的价格
 * @DateTime：2017/12/21 10:21
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hbz_recharge_price")
public class RechargePrice extends BaseVersionLocked {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(32) comment '保证金类型'")
    private Role role;

    @Column(nullable = false, columnDefinition = "double comment '保证金价格'")
    private Double price;

}
