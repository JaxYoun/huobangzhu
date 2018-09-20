package com.troy.keeper.management.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 李奥
 * @date 2018/2/12.
 */

@Getter
@Setter
public class HbzTendersDTO {


    private Long orderId;

    private Double registryMoney;
    private String registryMoneyValue;

    private Integer need;
    private String needValue;

    private Double bond;
    private String bondValue;

    private Integer starLevel;
    private String starLevelValue;
}
