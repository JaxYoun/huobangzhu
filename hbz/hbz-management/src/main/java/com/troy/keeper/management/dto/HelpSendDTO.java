package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.type.OrderTrans;
import com.troy.keeper.hbz.type.TimeLimit;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 李奥    帮我送
 * @date 2017/12/17.
 */
@Getter
@Setter
public class HelpSendDTO {

    //订单id
    private  Long id;
    //订单号
    private String orderNo;
    //企业部门名称
    private String organizationName;
    //货物地址---取货地址
    private String originAddress;
    //取货联系人
    private String originLinker;
    //取货联系人--联系方法
    private String originLinkTelephone;
    //订单创建人
    private  String  createUser;
    //创建人电话
    private String createUserTelephone;
    //订单发布时间
    private  String createTime;
    //配送限制----时间要求
    private TimeLimit timeLimit;
    private  String  timeLimitValue;
    //指定配送到货时间
    private  String  startTime;
    //订单归属城市
    private  String  destArea;
    private String  destAreaCode;
    //送货地址
    private  String  originArea;
    private String  originAreaCode;
    //目的地址
    private String destInfo;
    //取货时间
    private String orderTakeTime;

    //接单人
    private  String takeUser;
    //接单人联系电话
    private  String  takeUserTelephone;
    //是否支付----订单状态
    private OrderTrans orderTrans;
    private  String orderTransValue;

 ////////////////////以上是table的数据//////////////

///////以下是详细数据/////////////////////

    //货物重量
    private Double commodityWeight;
    //货物体积
    private Double commodityVolume;
    //货物品描述
    private String commodityDesc;
    //联系方法
//    private String originLinkTelephone;
//    //开始配送时间-----起送时间
//    private String startTime;
    //配送地址
    private String destAddress;
    //送货联系人
    private String linker;
    //联系方法
    private String linkTelephone;
    //取货时间---起送时间
    private String takeTime;
    //配送费用-----订单金额
    private Double amount;
    //省id
    private  Long provinceId;
    //市id
    private  Long cityId;
    //县id
    private  Long  countyId;
    //时间范围查询
    private Long  smallTime;
    private Long  bigTime;
    //取货时间要求
    private Integer takeLimit;
    //商品 名称
    private String commodityName;
    //关联图片
    private String relatedPictures;

}
