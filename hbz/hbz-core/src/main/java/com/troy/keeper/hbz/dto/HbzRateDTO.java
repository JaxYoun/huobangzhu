package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.type.OrderType;
import com.troy.keeper.hbz.type.RateType;
import lombok.Data;

import java.util.List;

/**
 * Created by leecheng on 2017/12/28.
 */
@Data
public class HbzRateDTO extends BaseDTO {

    private HbzOrderDTO order;

    @QueryColumn(propName = "order.id")
    private Long orderId;

    private HbzUserDTO user;

    @QueryColumn(propName = "user.id")
    private Long userId;

    @QueryColumn(propName = "user.id", queryOper = "in")
    private List<Long> userIds;

    @QueryColumn
    private RateType type;

    @QueryColumn
    private Integer star;

    @QueryColumn
    private String comment;

    @QueryColumn(propName = "order.orderType")
    private OrderType orderType;

    @QueryColumn(propName = "star", queryOper = "LT")
    private Integer starLT;
    @QueryColumn(propName = "star", queryOper = "LE")
    private Integer starLE;
    @QueryColumn(propName = "star", queryOper = "GT")
    private Integer starGT;
    @QueryColumn(propName = "star", queryOper = "GE")
    private Integer starGE;

}
