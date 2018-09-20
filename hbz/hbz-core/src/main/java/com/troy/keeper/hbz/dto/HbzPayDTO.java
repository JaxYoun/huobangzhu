package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.PayProgress;
import com.troy.keeper.hbz.type.PayType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by leecheng on 2017/10/30.
 */
@Getter
@Setter
@ToString
public class HbzPayDTO extends BaseDTO {

    @QueryColumn
    private BusinessType businessType;

    //订单号--对应了具体业务订单号-----------------
    @QueryColumn
    private String businessNo;

    //贸易订单号--发送到支付接口的订单号-----------
    @QueryColumn
    private String tradeNo;

    //订单支付现金
    @QueryColumn
    private Double fee;

    //被创建的订单号------------------------------
    @QueryColumn
    private String createdNo;

    //支付类型
    @QueryColumn
    private PayType payType;

    //支付进度
    @QueryColumn
    private PayProgress payProgress;

    //支付进度查询项
    @QueryColumn(propName = "payProgress", queryOper = "IN")
    private List<PayProgress> payProgresses;

    private String details;

    private String response;

    @QueryColumn
    private Integer bill;

}
