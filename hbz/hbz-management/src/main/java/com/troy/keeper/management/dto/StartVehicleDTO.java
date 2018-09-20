package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.type.ServiceMethodType;
import com.troy.keeper.hbz.type.ShippingStatus;
import com.troy.keeper.hbz.type.TransitState;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;


/**
 * @author 李奥
 * @date 2018/1/3.
 */
@Getter
@Setter
public class StartVehicleDTO {

    //装车id
    private Long   id;

    //收货信息的id
    private Long  cargoInformationId;

    //车辆信息的id
    private  Long   vehicleInformationId;


    //车辆编号  装车编号
    private  String vehicleNumber;


    //发车时间
    private  String   receiptDate;

    //预计到达时间
    private  String   receiptToDate;

    //发出站区域
    private String originArea;
//    private Long  originAreaId;
    private String  originAreaCode;
    //到站区域
    private String destArea;
//    private Long  destAreaId;
    private String  destAreaCode;

/////////////////// //收货信息///////////////////////
    //货物名称
    private String commodityName;

    //运单数量---件数
    private Integer waybillQuantity;

    //单位名称
    private  String  receiverUserCompanyName;

    //电话
    private  String  receiverUserTelephone;

    //包装状态  数据字典
    private  String packagingStatus;
    private String  packagingStatusName;


    //单个重量
    private Double singleWeight;

    //单个体积
    private Double singleVolume;

    //付款方式   数据字典
    private String  paymentMethod;
    private  String  paymentMethodName;

    //代收款
    private  Double onCollection;

    //垫付货款
    private Double advancePayment;

    //中转费
    private Double transferFee;

    //总运费
    private Double fotalFee;

    //数量
    private  Integer  amount;

    //服务方式
    private ServiceMethodType serviceMethodType;
    private String  serviceMethodTypeValue;

    //运单编号
    private String waybillNumber;

    //备注
    private  String  remarks;

    //收货运输状态
    private ShippingStatus shippingStatus;
    private  String  shippingStatusValue;

    //是否卸货  1--代表是  0----代表否
    private  String  cargoStatus;

    //物流编号
    private String  trackingNumber;

    //运送货物的类型  1--代表整车  0---代表零担
    private String goodsType;

    //发车编号
    private String startNumber;

    //在途状态
    private TransitState transitState;
    private String transitStateValue;


    //车牌号
    private  String numberPlate;

    //车辆类型
    private String  vehicleType;
    private  String   vehicleTypeValue;

    //司机名称
    private String  driverName;

    //司机电话
    private String  driverTelephone;

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

    //收货时间范围查询
    private Long  smallTime;
    private Long  bigTime;

    //货物编码
    private  String  commodityNumber;

    //当前站点的 机构id
    private Long smOrgId;

    //包装单位
    private  String packageUnit;
    private String  packageUnitValue;























}
