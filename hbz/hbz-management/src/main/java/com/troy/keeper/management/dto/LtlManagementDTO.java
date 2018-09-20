package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.dto.HbzLtlOrderDTO;
import com.troy.keeper.hbz.type.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

/**
 * @author 李奥  专线运输零担管理
 * @date 2018/2/5.
 */
@Getter
@Setter
public class LtlManagementDTO  {

    //零担表的id
    private  Long  id;

    //接单id
    private  Long takeUserId;

    //订单号
    private String orderNo;

    //所属企业
    private String ent;

    //创建人
    private String createUser;

    //创建人电话
    private String createUsertelephone;

    //创建时间
    private String createUserTime;


    //起始地区 --取货地
    private String originArea;
    private String originAreaCode;
    private LinkedList<Long> startCity;
    private LinkedList<Long> endCity;

    //接单用户--取货联系人
    private String takeUser;

    //取货联系人电话
    private String takeUserTelephone;

    //送货地区  --到站
    private String destArea;
    private  String  destAreaCode;
    //时间范围查询
    private Long  smallTime;
    private Long  bigTime;

    //关联图片
    private String relatedPictures;

    private String transLen;

    //货物名称
    private String commodityName;

    //单价
    private Double unitPrice;

    //分类
    private CommodityType commodityType;
    private  String   commodityTypeValue;

    //质量
    private Double commodityWeight;

    //体积
    private Double commodityVolume;

    //质量单元
    private WeightUnit weightUnit;
    private  String  weightUnitValue;

    //货物描述
    private String commodityDescribe;

    //体积单位
    private VolumeUnit volumeUnit;
    private String  volumeUnitValue;

    //车辆类型要求
    private TransType transType;
    private String transTypeValue;

    //最大载重
    private Double maxLoad;

    //原地址
    private String originAddress;

    //目的地说明
    private String destAddress;

    //接运时间
    private String orderTakeStart;

    //货主电话
    private String linkTelephone;

    //联系人
    private String linkMan;

    //目标联系人
    private String destLinker;

    //送达时间限制
    private String destlimit;

    //目标电话
    private String destTelephone;

    //补充说明
    private String linkRemark;
    /////////////////////////////////////主表

    //原始地址
    private String originInfo;

    //目的地
    private String destInfo;

    //订单金额
    private Double amount;

    //订单类型
    private OrderType orderType;
    private String orderTypeValue;

    //订单状态
    private OrderTrans orderTrans;
    private String orderTransValue;

    //结算类型
    private SettlementType settlementType;
    private String settlementTypeValue;




























}
