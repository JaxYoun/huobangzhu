package com.troy.keeper.hbz.po;

import com.troy.keeper.hbz.type.PayType;

import javax.persistence.*;

/**
 * Created by leecheng on 2017/11/3.
 */
//支付账户
@Entity
@Table(name = "hbz_payaccount")
public class HbzPayAccount extends BaseVersionLocked {

    @ManyToOne
    @JoinColumn(name = "userid", columnDefinition = "bigint comment '用户'")
    private HbzUser user;

    @Column(columnDefinition = "varchar(32) comment '支付类型'")
    @Enumerated(EnumType.STRING)
    private PayType payType;

    @Column(columnDefinition = "varchar(64) comment '访问令牌'")
    private String accessToken;

    @Column(columnDefinition = "varchar(64) comment '刷新令牌'")
    private String refreshToken;

    //对应openId，支付宝唯一userId
    @Column(columnDefinition = "varchar(64) comment '账户'")
    private String account;

    @Column
    private Boolean isDefault;

    public HbzUser getUser() {
        return user;
    }

    public void setUser(HbzUser user) {
        this.user = user;
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
