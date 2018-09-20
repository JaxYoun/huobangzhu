package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.type.OrderTrans;
import com.troy.keeper.hbz.type.TimeLimit;


/**
 * @author 李奥
 * @date 2017/12/12.
 */
public class HelpBuyOrderDetailsDTO {

    //订单id
    private  Long id;
    //商品名称
    private String commodityName;
    //商品数量
    private Long commodityCount;
    //货物金额---预估价格，----商品费用
    private Double commodityAmount;
    //详细需求---货物描述
    private String buyNeedInfo;
    //配送地址
    private String destAddress;
    //联系人
    private String linker;
    //联系方法
    private String linkTelephone;
    //'配送限制'---时间要求
    private TimeLimit timeLimit;
    private  String timeLimitValue;
    //配送时间
    private String startTime;
    //'货物金额'"---商品费用
//    private Double commodityAmount;
    //运输金额
    private Double remuneration;
    //订单发布人
    private  String createUser;
    //订单发布人电话
    private  String createUserTelephone;
    //所属公司
    private  String org;
   //订单把编号
   private String orderNo;
    //订单状态
    private OrderTrans orderTrans;
    private String  orderTransValue;
    //详情配送地址
    private String   destInfo;
    //关联图片
    private String relatedPictures;

    public String getRelatedPictures() {
        return relatedPictures;
    }

    public void setRelatedPictures(String relatedPictures) {
        this.relatedPictures = relatedPictures;
    }

    public String getDestInfo() {
        return destInfo;
    }

    public void setDestInfo(String destInfo) {
        this.destInfo = destInfo;
    }

    public String getOrderTransValue() {
        return orderTransValue;
    }

    public void setOrderTransValue(String orderTransValue) {
        this.orderTransValue = orderTransValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public Long getCommodityCount() {
        return commodityCount;
    }

    public void setCommodityCount(Long commodityCount) {
        this.commodityCount = commodityCount;
    }

    public Double getCommodityAmount() {
        return commodityAmount;
    }

    public void setCommodityAmount(Double commodityAmount) {
        this.commodityAmount = commodityAmount;
    }

    public String getBuyNeedInfo() {
        return buyNeedInfo;
    }

    public void setBuyNeedInfo(String buyNeedInfo) {
        this.buyNeedInfo = buyNeedInfo;
    }

    public String getDestAddress() {
        return destAddress;
    }

    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
    }

    public String getLinker() {
        return linker;
    }

    public void setLinker(String linker) {
        this.linker = linker;
    }

    public String getLinkTelephone() {
        return linkTelephone;
    }

    public void setLinkTelephone(String linkTelephone) {
        this.linkTelephone = linkTelephone;
    }

    public TimeLimit getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(TimeLimit timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getTimeLimitValue() {
        return timeLimitValue;
    }

    public void setTimeLimitValue(String timeLimitValue) {
        this.timeLimitValue = timeLimitValue;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Double getRemuneration() {
        return remuneration;
    }

    public void setRemuneration(Double remuneration) {
        this.remuneration = remuneration;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public OrderTrans getOrderTrans() {
        return orderTrans;
    }

    public void setOrderTrans(OrderTrans orderTrans) {
        this.orderTrans = orderTrans;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateUserTelephone() {
        return createUserTelephone;
    }

    public void setCreateUserTelephone(String createUserTelephone) {
        this.createUserTelephone = createUserTelephone;
    }
}
