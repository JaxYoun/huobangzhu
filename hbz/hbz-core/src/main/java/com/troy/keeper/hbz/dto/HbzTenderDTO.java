package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by leecheng on 2017/11/21.
 */
@Getter
@Setter
public class HbzTenderDTO extends BaseDTO {

    private HbzOrderDTO order;

    @QueryColumn(propName = "order.id")
    private Long orderId;

    @QueryColumn
    private Double registryMoney;

    @QueryColumn
    private Integer need;

    private Double bond;

    private Integer starLevel;

}
