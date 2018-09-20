package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.dto.HbzOrderDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.type.OrderTrans;
import lombok.Data;

/**
 * Created by leecheng on 2018/1/3.
 */
@Data
public class HbzOrderRecordMapDTO extends HbzBaseMapDTO {

    private Long id;

    private HbzOrderDTO order;

    private Long orderId;

    private HbzUserDTO user;

    private Long userId;

    private OrderTrans orderTrans;

    private String timeMillis;


}
