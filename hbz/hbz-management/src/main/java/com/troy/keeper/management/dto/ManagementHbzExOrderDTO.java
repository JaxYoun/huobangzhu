package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.type.CommodityClass;
import com.troy.keeper.hbz.type.ExpressCompanyType;
import com.troy.keeper.hbz.type.OrderTrans;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 李奥   快递派件 dto
 * @date 2017/12/20.
 */
@Getter
@Setter
public class ManagementHbzExOrderDTO {
    //id
    private Long id;
    //派件表id
    private Long exId;
    //运单编号
    private String orderNo;
    //订单归属公司
    private String organizationName;
    //订单创建人
    private String createUser;
    //创建人电话
    private String createUserTelephone;
    //订单发布时间
    private String createTime;
    //取件城市
    private String originArea;
    private String originAreaCode;
    private HbzAreaDTO originAreaWrapper;
    private HbzAreaDTO destAreaWrapper;
    //取件联系人  -------
    private String originLinker;
    //取件联系电话  ------
    private String originTelephone;
    //收件城市
    private String destArea;
    private String destAreaCode;
    //收件人   --------
    private String linker;
    //收件人电话  -------
    private String telephone;
    //货物质量
    private Double commodityWeight;
    //货物体积
    private Double commodityVolume;
    //金额
    private Double amount;
    //订单状态
    private OrderTrans orderTrans;
    private String orderTransValue;
    //快递公司名称
    private ExpressCompanyType expressCompanyType;
    private String expressCompanyTypeValue;
    //快递单号
    private String trackingNumber;

    /////////////////////////////////////////////////////////////////////////////////
    //货物名称
    private String orderName;
    //货物类型
    private CommodityClass commodityClass;
    private String commodityClassValue;
    //货物描述
    private String commodityDesc;
    //取货时间---上门取货时间
    private String takeTime;
    //取货地址---取件具体地址
    private String originAddr;
    //送货地址---收货具体地址
    private String destAddr;
    //快递发件时间
    private String sendTime;
    //省id
    private Long provinceId;
    //市id
    private Long cityId;
    //县id
    private Long countyId;
    //到站
    //省id
    private Long provinceToId;
    //市id
    private Long cityToId;
    //县id
    private Long countyToId;
    //时间范围查询
    private Long smallTime;
    private Long bigTime;
    //判断 有派单记录的需展现派单记录 true有  false没有
    private Boolean isNull;
    //关联图片
    private String relatedPictures;
    //是否已经派网点任务
    private Boolean isAssigned;

}
