package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.type.ServiceMethodType;
import com.troy.keeper.hbz.type.ShippingStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;


/**
 * @author 李奥
 * @date 2018/1/15.
 */
@Getter
@Setter
public class OutsourcingDetailsDTO {

    //id
    private Long  id;

    private Long  cargoInformationId;

    //物流编号
    private String  trackingNumber;

    //运单编号
    private String waybillNumber;

    //收货日期
    private String   receiptDate;

    //发出站区域
    private String originArea;
//    private Long  originAreaId;
    private String originAreaCode;
    //到站区域
    private String destArea;
//    private Long  destAreaId;
    private String destAreaCode;
    //货物编码
    private  String  commodityNumber;

    //货物名称
    private String commodityName;

    //包装单位
    private  String packageUnit;
    private  String  packageUnitValue;

    //数量
    private  Integer  amount;

    //实际重量
    private Double   weight;

    //计费重量
    private Double  billingWeight;

    //体积
    private Double  volume;

    //单价
    private Double price;


    //声明价值
    private  Double declareValue;


    //保率
    private Double ratio;

    //包装状态  数据字典
    private  String packagingStatus;
    private String  packagingStatusName;

    //开票员     数据字典
    private  String   billingUser;
    private  String   billingUserName;

    //是否回单
    private  String   isReceipt;

    //等话放货
    private String isDelivery;

    //服务方式
    private ServiceMethodType serviceMethodType;
    private String  serviceMethodTypeValue;

    //中转战
    private  String inWar;

    //回单数
    private Double  receiptNumber;

    //代收款
    private  Double onCollection;

    //中转费
    private Double transferFee;

    //垫付货款
    private Double advancePayment;

    //备注
    private  String  remarks;

    //运送货物的类型  1--代表整车  0---代表零担
    private String goodsType;

    //总运费
    private Double fotalFee;


    //收货时间范围查询
    private Long  smallTime;
    private Long  bigTime;

    //省id
    private  Long provinceId;
    //市id
    private  Long cityId;
    //县id
    private  Long  countyId;


    private LinkedList<Long> startCity;
    private LinkedList<Long> endCity;

    //到站
    //省id
    private  Long provinceToId;
    //市id
    private  Long cityToId;
    //县id
    private  Long  countyToId;
    //////////////托运方/////////////////////
    //id
    private Long   shipperUserId;

    //单位名称
    private  String  shipperUserCompanyName;

    //联系人
    private  String   shipperUserName;

    //电话
    private  String  shipperUserTelephone;

    //地址
    private  String  shipperUserAddress;

    ////////////////////收货方/////////
    //id
    private Long receiverUserId;

    //单位名称
    private  String  receiverUserCompanyName;

    //联系人
    private  String   receiverUserName;

    //电话
    private  String  receiverUserTelephone;

    //地址
    private  String  receiverUserAddress;

    //邮编
    private  String   receiverUserZipCode;

    /////////////费用表//////////////////
    //id
    private Long feeId;


    //运费
    private Double shippingCosts;

    //提货费
    private  Double   deliveryFee;

    //送货费
    private Double deliveryCharges;

    //保费
    private Double   premium;

    //包装费
    private Double  packagingFee;

    //其他费用
    private  Double  otherFee;

    //付款方式   数据字典
    private String  paymentMethod;
    private  String  paymentMethodName;

    //库存数量
    private Integer inventoryQuantity;


    //单个重量
    private   Double singleWeight;


    //单个体积
    private Double singleVolume;

    //收货运输状态
    private ShippingStatus shippingStatus;
    private  String   shippingStatusValue;




}
