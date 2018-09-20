package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.type.PayType;
import com.troy.keeper.hbz.type.WarehouseEarnestPayStatusEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2017/12/29 14:02
 */
@Data
public class WarehouseEarnestOrderDTO extends BaseDTO {

    @NotNull(message = "仓储id不能为空！")
    private Long warehouseId;

    private String warehouseName;

    @NotNull(message = "金额不能为空！")
    private Double earnestPrice;

    private WarehouseEarnestPayStatusEnum payStatus;

    private String orderNo;

    private PayType payType;

    private HbzUserDTO dealUser;

    private HbzUserDTO createUser;

    private String createDate;

    private WarehouseDTO warehouse;

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public HbzUserDTO getDealUser() {
        return dealUser;
    }

    public void setDealUser(HbzUserDTO dealUser) {
        this.dealUser = dealUser;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public WarehouseDTO getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseDTO warehouse) {
        this.warehouse = warehouse;
    }

    public Double getEarnestPrice() {
        return earnestPrice;
    }

    public void setEarnestPrice(Double earnestPrice) {
        this.earnestPrice = earnestPrice;
    }

    public HbzUserDTO getCreateUser() {
        return createUser;
    }

    public void setCreateUser(HbzUserDTO createUser) {
        this.createUser = createUser;
    }

    public WarehouseEarnestPayStatusEnum getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(WarehouseEarnestPayStatusEnum payStatus) {
        this.payStatus = payStatus;
    }
}
