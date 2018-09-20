package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.po.HbzArea;
import com.troy.keeper.hbz.type.OrderTrans;
import com.troy.keeper.hbz.type.TimeLimit;


/**
 * @author 李奥   帮我买
 * @date 2017/12/12.
 */
public class HelpBuyTableDTO {

    //订单id
   private  Long id;
    //订单号
    private String orderNo;
   //订单归属公司
   private String organizationName;
   //商品名称
   private String commodityName;
    //商品数量
    private Long commodityCount;
    //货物金额---预估价格
    private Double commodityAmount;
    //运输金额
    private Double remuneration;
    //订单创建人
    private String createUser;
    //创建电话
    private String createUserTelephone;
    //订单发布时间
    private String creatTime;
    //配送时间要求
    private TimeLimit timeLimit;
    private String timeLimitValue;
    //指定配送到货时间
    private String startTime;
    //订单归属城市
    private String destArea;
    private String destAreaCode;
    //接单人
    private  String teakUser;
    //接单人电话
    private  String teakUserTelephone;
    //是否付款
    private  OrderTrans isPayOf;
    // 订单状态
    private OrderTrans orderTrans;
    private  String   orderTransValue;
    //创建时间范围
    private Long  smallTime;
    private Long  bigTime;

    //详情配送地址
    private String   destInfo;


 //省id
 private  Long provinceToId;
 //市id
 private  Long cityToId;
 //县id
 private  Long  countyToId;


 public String getDestAreaCode() {
  return destAreaCode;
 }

 public void setDestAreaCode(String destAreaCode) {
  this.destAreaCode = destAreaCode;
 }

 public String getDestInfo() {
  return destInfo;
 }

 public void setDestInfo(String destInfo) {
  this.destInfo = destInfo;
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getDestArea() {
  return destArea;
 }

 public void setDestArea(String destArea) {
  this.destArea = destArea;
 }

 public Long getProvinceToId() {
  return provinceToId;
 }

 public void setProvinceToId(Long provinceToId) {
  this.provinceToId = provinceToId;
 }

 public Long getCityToId() {
  return cityToId;
 }

 public void setCityToId(Long cityToId) {
  this.cityToId = cityToId;
 }

 public Long getCountyToId() {
  return countyToId;
 }

 public void setCountyToId(Long countyToId) {
  this.countyToId = countyToId;
 }

 public OrderTrans getIsPayOf() {
  return isPayOf;
 }

 public void setIsPayOf(OrderTrans isPayOf) {
  this.isPayOf = isPayOf;
 }

 public String getOrderNo() {
  return orderNo;
 }

 public void setOrderNo(String orderNo) {
  this.orderNo = orderNo;
 }

 public String getOrganizationName() {
  return organizationName;
 }

 public void setOrganizationName(String organizationName) {
  this.organizationName = organizationName;
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

 public Double getRemuneration() {
  return remuneration;
 }

 public void setRemuneration(Double remuneration) {
  this.remuneration = remuneration;
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

 public String getCreatTime() {
  return creatTime;
 }

 public void setCreatTime(String creatTime) {
  this.creatTime = creatTime;
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

 public String getTeakUser() {
  return teakUser;
 }

 public void setTeakUser(String teakUser) {
  this.teakUser = teakUser;
 }

 public String getTeakUserTelephone() {
  return teakUserTelephone;
 }

 public void setTeakUserTelephone(String teakUserTelephone) {
  this.teakUserTelephone = teakUserTelephone;
 }

 public OrderTrans getOrderTrans() {
  return orderTrans;
 }

 public void setOrderTrans(OrderTrans orderTrans) {
  this.orderTrans = orderTrans;
 }

 public String getOrderTransValue() {
  return orderTransValue;
 }

 public void setOrderTransValue(String orderTransValue) {
  this.orderTransValue = orderTransValue;
 }

 public Long getSmallTime() {
  return smallTime;
 }

 public void setSmallTime(Long smallTime) {
  this.smallTime = smallTime;
 }

 public Long getBigTime() {
  return bigTime;
 }

 public void setBigTime(Long bigTime) {
  this.bigTime = bigTime;
 }
}
