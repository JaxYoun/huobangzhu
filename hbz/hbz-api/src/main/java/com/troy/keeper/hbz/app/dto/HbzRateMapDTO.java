package com.troy.keeper.hbz.app.dto;

import com.troy.keeper.hbz.dto.HbzOrderDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.type.OrderType;
import com.troy.keeper.hbz.type.RateType;
import lombok.Data;

import java.util.List;

/**
 * Created by leecheng on 2017/12/28.
 */
@Data
public class HbzRateMapDTO extends HbzBaseMapDTO {

    private Long id;

    private HbzOrderDTO order;

    private HbzUserDTO user;

    private Long userId;

    private Long orderId;

    private List<Long> userIds;

    private RateType type;

    private Integer star;

    private String comment;

    private OrderType orderType;
    private Integer starLT;
    private Integer starLE;
    private Integer starGT;
    private Integer starGE;

}
