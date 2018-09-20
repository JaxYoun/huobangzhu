package com.troy.keeper.hbz.vo;

import com.troy.keeper.hbz.type.PayProgress;
import com.troy.keeper.hbz.type.PayType;
import com.troy.keeper.hbz.type.WebPrepayOrderLifecycle;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2017/12/29 0:06
 */
public class PrepayOrderVO {

    private Long commodityId;

    private Double commodityPrice;

    private String commodityType;

    private String code;

    private HbzUserVO createUser;

    private PayType payType;

    private PayProgress payProgress;

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
}
