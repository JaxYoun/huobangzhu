package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.po.HbzOrder;
import com.troy.keeper.hbz.po.HbzUser;
import com.troy.keeper.hbz.type.OrderTrans;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 李奥
 * @date 2017/12/11.
 */
@Getter
@Setter
public class LogisticsDetailsDTO {

    //id
    private  Long   id;

    //订单   orderid
    private HbzOrder order;

    //用户id
    private HbzUser user;

    //状态
    private OrderTrans orderTrans;
    private  String orderTransValue;

    //时间
    private String timeMillis;

    //物流的展示时间+状态的字段
    private  String  Information;


}
