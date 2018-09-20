package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.type.OrderTrans;
import com.troy.keeper.hbz.type.OrderType;
import com.troy.keeper.hbz.type.TakeType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by leecheng on 2017/11/21.
 */
@Getter
@Setter
public class HbzTakerInfoDTO extends BaseDTO {

    Double offer;

    private HbzOrderDTO order;

    @QueryColumn(propName = "order.id")
    private Long orderId;

    private HbzUserDTO user;

    @QueryColumn(propName = "user.id")
    private Long userId;

    private HbzUserDTO agent;

    @QueryColumn(propName = "agent.id")
    private Long agentId;

    @QueryColumn
    private TakeType takeType;

    @QueryColumn(propName = "takeType", queryOper = "in")
    private List<TakeType> takeTypes;

    @QueryColumn(propName = "order.orderType")
    private OrderType orderType;

    @QueryColumn(propName = "order.orderNo")
    private String orderNo;

    @QueryColumn(propName = "order.orderTrans")
    private OrderTrans orderTrans;

    @QueryColumn(propName = "order.orderTrans", queryOper = "in")
    private List<OrderTrans> orderTranses;
}
