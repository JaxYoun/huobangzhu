package com.troy.keeper.hbz.app.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.dto.HbzOrderDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
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
public class HbzTakerInfoMapDTO extends HbzBaseMapDTO {

    @ValueFormat(validations = {
            @Validation(use = "hbz_taker_create", format = ValidConstants.NOT_NULL, msg = "[\"{fieldName}\"]报价不能为空")
    })
    Double offer;

    private Long id;

    private HbzOrderDTO order;

    @ValueFormat(validations = {
            @Validation(use = "hbz_taker_create", format = ValidConstants.NOT_NULL, msg = "[\"{fieldName}\"]订单id不能为空")
    })
    private Long orderId;

    private HbzUserDTO user;

    private Long userId;

    private HbzUserDTO agent;

    private Long agentId;

    private TakeType takeType;

    private List<TakeType> takeTypes;

    private OrderType orderType;

    private String orderNo;

    private OrderTrans orderTrans;

    private List<OrderTrans> orderTranses;
}
