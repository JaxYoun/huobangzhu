package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.PayProgress;
import com.troy.keeper.hbz.type.PayType;
import lombok.Data;

import javax.persistence.*;

/**
 * @Author：YangJx
 * @Description：预支付订单实体类
 * @DateTime：2017/12/28 23:46
 */
@Data
@Entity
@Table(name = "hbz_web_prepay_order")
public class PrepayOrder extends BaseVersionLocked {

    @Column(columnDefinition = "bigint comment '商品id'")
    private Long commodityId;

    @Column(columnDefinition = "double comment '商品价格'")
    private Double commodityPrice;

    @Column(columnDefinition = "varchar(32) comment '商品类型'")
    private String commodityType;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '订单状态'")
    private PayProgress payProgress;

    @Column(columnDefinition = "varchar(32) comment '订单编码'")
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) comment '支付平台类型'")
    private PayType payType;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private HbzUser createUser;

    public HbzUser getCreateUser() {
        return createUser;
    }

    public void setCreateUser(HbzUser createUser) {
        this.createUser = createUser;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    public Double getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(Double commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public String getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
    }

    public PayProgress getPayProgress() {
        return payProgress;
    }

    public void setPayProgress(PayProgress payProgress) {
        this.payProgress = payProgress;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }
}
