package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.type.PayProgress;
import com.troy.keeper.hbz.type.PayType;
import com.troy.keeper.hbz.type.WebPrepayOrderLifecycle;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2017/12/29 0:04
 */
public class PrepayOrderDTO extends BaseDTO {

    @NotNull(message = "商品id为必填项！")
    private Long commodityId;

    @NotNull(message = "商品价格为必填项！")
    private Double commodityPrice;

    @NotNull(message = "类型为必填项！")
    @NotBlank(message = "类型必填项！")
    private String commodityType;

    private String code;

    private HbzUserDTO createUser;

    private PayType payType;

    private PayProgress payProgress;

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public HbzUserDTO getCreateUser() {
        return createUser;
    }

    public void setCreateUser(HbzUserDTO createUser) {
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
