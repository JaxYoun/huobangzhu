package com.troy.keeper.hbz.vo;

import com.troy.keeper.hbz.dto.BaseDTO;
import com.troy.keeper.hbz.type.PayType;
import com.troy.keeper.hbz.type.WarehouseEarnestPayStatusEnum;
import lombok.Data;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2017/12/29 14:04
 */
@Data
public class WarehouseEarnestOrderVO extends BaseDTO {

    private String warehouseName;

    private Double earnestPrice;

    private WarehouseEarnestPayStatusEnum payStatus;

    private String orderNo;

    private PayType payType;

    private HbzUserVO createUser;

    private HbzUserVO dealUser;

    private String createDate;

    private String lastUpdateDate;

    private WarehouseVO warehouse;

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public HbzUserVO getCreateUser() {
        return createUser;
    }

    public void setCreateUser(HbzUserVO createUser) {
        this.createUser = createUser;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public HbzUserVO getDealUser() {
        return dealUser;
    }

    public void setDealUser(HbzUserVO dealUser) {
        this.dealUser = dealUser;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public WarehouseVO getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseVO warehouse) {
        this.warehouse = warehouse;
    }

    public Double getEarnestPrice() {
        return earnestPrice;
    }

    public void setEarnestPrice(Double earnestPrice) {
        this.earnestPrice = earnestPrice;
    }

    public WarehouseEarnestPayStatusEnum getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(WarehouseEarnestPayStatusEnum payStatus) {
        this.payStatus = payStatus;
    }
}
