package com.troy.keeper.management.dto;

/**
 * @author 李奥
 * @date 2017/12/14.  平台付款详情
 */
public class PlatformDetailsDTO {

    //订单Id
    private Long id;

    //付款编号
    private  String  paymentNumber;

    //账号类型
    private String   accountType;

    //付款金额
    private  String paymentAmount;

    //付款账号
    private String paymentAccount;

    //付款人名称
    private  String   payerName;

    //付款日期
    private  String paymentDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
}
