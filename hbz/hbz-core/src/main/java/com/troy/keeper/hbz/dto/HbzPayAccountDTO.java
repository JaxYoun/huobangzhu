package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import com.troy.keeper.hbz.type.PayType;

/**
 * Created by leech on 2017/11/3.
 */
public class HbzPayAccountDTO extends BaseDTO {

    private HbzUserDTO user;

    @ValueFormat(validations = {@Validation(use = "account_create", format = ValidConstants.NULL, msg = "不能手动指定用户")})
    @QueryColumn(propName = "user.id", queryOper = "equal")
    private Long userId;

    @ValueFormat(validations = {@Validation(use = "account_create", format = ValidConstants.NOT_NULL, msg = "支付类型不能为空")})
    @QueryColumn
    private PayType payType;

    @ValueFormat(validations = {@Validation(use = "account_create", format = ValidConstants.NOT_NULL, msg = "账户不能空")})
    @QueryColumn
    private String account;

    @ValueFormat(validations = {@Validation(use = "account_create", format = ValidConstants.NOT_NULL, msg = "AcccessToken不能为空")})
    private String accessToken;

    private String refreshToken;

    @QueryColumn
    private Boolean isDefault;

    public HbzUserDTO getUser() {
        return user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setUser(HbzUserDTO user) {
        this.user = user;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
