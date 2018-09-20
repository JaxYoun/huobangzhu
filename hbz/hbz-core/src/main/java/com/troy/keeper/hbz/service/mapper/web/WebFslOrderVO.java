package com.troy.keeper.hbz.service.mapper.web;

import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.hbz.po.HbzUser;
import com.troy.keeper.hbz.type.*;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebFslOrderVO extends BaseDTO {

    private Long id;

    private String orderNo;

    private String commodityName;

    private CommodityClass commodityClass;

    private Double commodityWeight;

    private Double commodityVolume;

    private WeightUnit weightUnit;

    private VolumeUnit volumeUnit;

    private TransType transType;

    private Double maxLoad;

    //订单状态
    private OrderTrans orderTrans;

    private String originAddress;

    private String destAddress;

    private String orderTakeStart;

    private String linkTelephone;

    private Double originX;

    private Double originY;

    private Double amount;

    //目标经度
    private Double destX;

    //目标纬度
    private Double destY;

    //结算方式
    private SettlementType settlementType;

    //发货区域
    private String originAreaCode;

    //收货区域
    private String destAreaCode;

    //用户状态信息
    private Double locationX;
    private Double locationY;

    //分页排序参数
    private Integer page = 0;

    private Integer size = 11;

    private List<String[]> sorts;

    private String status;

    private OrderType orderType;

    private HbzUser createUser;

    private HbzUser takeUser;

    private HbzUser dealUser;

    public String getOrderTakeStart() {
        return orderTakeStart;
    }
    public void setOrderTakeStart(String orderTakeStart) {
        this.orderTakeStart = orderTakeStart;
    }
}
