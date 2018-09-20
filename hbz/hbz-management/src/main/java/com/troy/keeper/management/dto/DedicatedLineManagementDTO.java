package com.troy.keeper.management.dto;
import com.troy.keeper.hbz.po.HbzArea;
import com.troy.keeper.hbz.po.HbzOrg;
import com.troy.keeper.hbz.po.HbzUser;
import com.troy.keeper.hbz.type.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.LinkedList;

/**
 * @author 李奥
 * @date 2017/11/28.
 */
@Getter
@Setter
public class DedicatedLineManagementDTO {
    //整车id
    private  Long id;
    //接单用户的id
    private  Long takeUserId;

    ////////货物详情//////////////////
    //货物名称
    private String commodityName;
    //接运时间
    private String orderTakeStart;
    //货物描述
    private String commodityDescribe;
    //质量
    private Double commodityWeight;
    //体积
    private Double commodityVolume;

    ///////车辆要求////////////////////
    //车辆类型要求
    private TransType transType;
    private  String   transTypeValue;
    //最大载重
    private Double maxLoad;

    //车长
    private String transLen;
    //图片
    private String relatedPictures;

    ////////取货信息///////////////////
    //取货地址   HbzArea
    private String  originArea;
    private String originAreaCode;
    //具体地址-----》原地址
    private String originAddress;
    //货主电话
    private String linkTelephone;
    //联系人
    private String linkMan;

    ////////送货地址///////////////////////
    //送货地址 HbzArea
    private String  destArea;
    private String  destAreaCode;
    //具体地址------》目的地说明
    private String destAddress;
    //联系人
    private String destLinker;
    //联系电话
    private String destTelephone;

/////////联系方式////////////////////////////
    //补充说明
    private String linkRemark;

    //////////付款方式/////////////////////////////
    //结算类型
    private SettlementType settlementType;
    private  String    settlementTypeValue;
    ///////////费用 ///////////////////////////////////
    //订单金额--总价
    private Double amount;
    //单价
    private Double unitPrice;

    ///////////订单发布人信息////////////////////////////////
    //联系人 HbzUser
    private String createUser;
    //联系电话 HbzUser
    private String createUsertelephone;
    //所属公司  创建订单的 所属公司  HbzUser createUser HbzOrg org  OrgType orgType;
    private  String  org;
    private  String  orgValue;

    ////////////////总界面//////////////////////////////////
    //订单号
    private String orderNo;
    //订单发布时间HbzUser----中的创建时间
    private String createUserTime;
    //指定运输时间
    private String destlimit;
    //订单状态
    private OrderTrans orderTrans;
    private  String   orderTransValue;
    //订单类型
    private OrderType orderType;
    private  String   orderTypeValue;


    private LinkedList<Long> startCity;
    private LinkedList<Long> endCity;
    ////////////接单人//////////////
    private String takeUser;
    private String takeUserTelephone;


    //时间范围查询
    private Long  smallTime;
    private Long  bigTime;

    //线下处理
    private Integer offlineProcess;

}
