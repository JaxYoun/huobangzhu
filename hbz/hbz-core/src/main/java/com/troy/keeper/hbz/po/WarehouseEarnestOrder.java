package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.PayType;
import com.troy.keeper.hbz.type.WarehouseEarnestPayStatusEnum;
import lombok.Data;

import javax.persistence.*;

/**
 * @Author：YangJx
 * @Description：仓储租赁诚意金订单实体类
 * @DateTime：2017/12/29 13:52
 */
@Data
@Entity
@Table(name = "hbz_web_warehouse_earnest_order")
public class WarehouseEarnestOrder extends BaseVersionLocked {

    @Column(columnDefinition = "varchar(255) comment '仓储名字'")
    private String warehouseName;

    @Column(columnDefinition = "double comment '诚意金金额'")
    private Double earnestPrice;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '订单状态'")
    private WarehouseEarnestPayStatusEnum payStatus;

    @Column(columnDefinition = "varchar(255) comment '订单编码'")
    private String orderNo;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '支付平台类型'")
    private PayType payType;

    @ManyToOne(fetch = FetchType.EAGER)
    private HbzUser createUser;

    @ManyToOne(fetch = FetchType.EAGER)
    private HbzUser dealUser;

    /**
     * 所属仓储
     */
    @ManyToOne(fetch = FetchType.EAGER)
    private Warehouse warehouse;

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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public HbzUser getDealUser() {
        return dealUser;
    }

    public void setDealUser(HbzUser dealUser) {
        this.dealUser = dealUser;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Double getEarnestPrice() {
        return earnestPrice;
    }

    public void setEarnestPrice(Double earnestPrice) {
        this.earnestPrice = earnestPrice;
    }

    public HbzUser getCreateUser() {
        return createUser;
    }

    public void setCreateUser(HbzUser createUser) {
        this.createUser = createUser;
    }

    public WarehouseEarnestPayStatusEnum getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(WarehouseEarnestPayStatusEnum payStatus) {
        this.payStatus = payStatus;
    }
}
