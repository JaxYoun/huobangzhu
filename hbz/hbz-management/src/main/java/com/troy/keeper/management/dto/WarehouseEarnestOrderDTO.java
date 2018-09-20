package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.type.PayType;
import com.troy.keeper.hbz.type.WarehouseEarnestPayStatusEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @Autohor: hecj
 * @Description: 仓储租赁诚意金订单DTO
 * @Date: Created in 14:43  2018/1/18.
 * @Midified By:
 */
@Getter
@Setter
public class WarehouseEarnestOrderDTO {

    private Long id;

    private String warehouseName;

    private Double earnestPrice;

    private WarehouseEarnestPayStatusEnum payStatus;

    private String payStatusValue;

    private String orderNo;

    private PayType payType;

    private String payTypeValue;

    private String dealUser;

    private String createUser;

    private Long createDate;

    //查询使用
    private String createDateStart;//开始时间
    private String createDateEnd;//结束时间

    private WarehouseManageDTO warehouseDTO;

    private String status;

}
