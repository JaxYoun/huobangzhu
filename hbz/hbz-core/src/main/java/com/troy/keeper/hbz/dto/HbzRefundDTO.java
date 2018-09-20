package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.PayProgress;
import com.troy.keeper.hbz.type.RefundStatus;
import lombok.Data;

import java.util.List;

/**
 * Created by leecheng on 2018/3/6.
 */
@Data
public class HbzRefundDTO extends BaseDTO {

    private HbzPayDTO pay;

    @QueryColumn(propName = "pay.id")
    private Long payId;

    private HbzUserDTO user;

    @QueryColumn(propName = "user.id")
    private Long userId;

    @QueryColumn(propName = "refundNo")
    private String refundNo;

    @QueryColumn(propName = "pay.tradeNo")
    private String tradeNo;

    @QueryColumn(propName = "pay.payProgress")
    private PayProgress payProgress;

    @QueryColumn(propName = "pay.businessNo")
    private String businessNo;

    @QueryColumn(propName = "pay.businessType")
    private BusinessType businessType;

    @QueryColumn
    private String requestNo;

    @QueryColumn(propName = "refundStatus")
    private RefundStatus refundStatus;

    @QueryColumn(propName = "refundStatus", queryOper = "in")
    private List<RefundStatus> refundStatuses;

    @QueryColumn(propName = "bill")
    private Integer bill;

    @QueryColumn
    private Long createTime;
    @QueryColumn(propName = "createTime", queryOper = "LT")
    private Long createTimeLT;
    @QueryColumn(propName = "createTime", queryOper = "GE")
    private Long createTimeGE;
    @QueryColumn(propName = "createTime", queryOper = "GT")
    private Long createTimeGT;
    @QueryColumn(propName = "createTime", queryOper = "LE")
    private Long createTimeLE;

}
