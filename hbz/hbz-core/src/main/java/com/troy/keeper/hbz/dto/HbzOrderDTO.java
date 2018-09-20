package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import com.troy.keeper.hbz.type.OrderTrans;
import com.troy.keeper.hbz.type.OrderType;
import com.troy.keeper.hbz.type.PayType;
import com.troy.keeper.hbz.type.SettlementType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by lee on 2017/10/24.
 */
@Getter
@Setter
@ToString
public class HbzOrderDTO extends BaseDTO {

    @QueryColumn
    private String completeImage;

    @QueryColumn
    @ValueFormat(validations = {@Validation(use = "buy_create", format = ValidConstants.NULL, msg = "不能手动生成订单号")})
    private String orderNo;

    //取货地址X
    @QueryColumn
    private Double originX;
    @QueryColumn(propName = "originX", queryOper = "LE")
    private Double originXLE;
    @QueryColumn(propName = "originX", queryOper = "LT")
    private Double originXLT;
    @QueryColumn(propName = "originX", queryOper = "GT")
    private Double originXGT;
    @QueryColumn(propName = "originX", queryOper = "GE")
    private Double originXGE;

    //取货地址Y
    @QueryColumn
    private Double originY;
    @QueryColumn(propName = "originY", queryOper = "LT")
    private Double originYLT;
    @QueryColumn(propName = "originY", queryOper = "LE")
    private Double originYLE;
    @QueryColumn(propName = "originY", queryOper = "GT")
    private Double originYGT;
    @QueryColumn(propName = "originY", queryOper = "GE")
    private Double originYGE;
    private String originInfo;
    //总金额
    @QueryColumn
    private Double amount;
    @QueryColumn(propName = "amount", queryOper = "LT")
    private Double amountLT;
    @QueryColumn(propName = "amount", queryOper = "LE")
    private Double amountLE;
    @QueryColumn(propName = "amount", queryOper = "GT")
    private Double amountGT;
    @QueryColumn(propName = "amount", queryOper = "GE")
    private Double amountGE;

    @QueryColumn
    private Double expectedPrice;
    @QueryColumn(propName = "expectedPrice", queryOper = "LE")
    private Double expectedPriceLE;
    @QueryColumn(propName = "expectedPrice", queryOper = "LT")
    private Double expectedPriceLT;
    @QueryColumn(propName = "expectedPrice", queryOper = "GT")
    private Double expectedPriceGT;
    @QueryColumn(propName = "expectedPrice", queryOper = "GE")
    private Double expectedPriceGE;

    //目标经度
    @ValueFormat(validations = {@Validation(use = "buy_create", format = ValidConstants.NOT_NULL, msg = "经度不能为空")})
    @QueryColumn
    private Double destX;
    @QueryColumn(propName = "destX", queryOper = "LT")
    private Double destXLT;
    @QueryColumn(propName = "destX", queryOper = "LE")
    private Double destXLE;
    @QueryColumn(propName = "destX", queryOper = "GT")
    private Double destXGT;
    @QueryColumn(propName = "destX", queryOper = "GE")
    private Double destXGE;

    //目标纬度
    @ValueFormat(validations = {@Validation(use = "buy_create", format = ValidConstants.NOT_NULL, msg = "纬度不能为空")})
    @QueryColumn
    private Double destY;
    @QueryColumn(propName = "destY", queryOper = "LT")
    private Double destYLT;
    @QueryColumn(propName = "destY", queryOper = "LE")
    private Double destYLE;
    @QueryColumn(propName = "destY", queryOper = "GT")
    private Double destYGT;
    @QueryColumn(propName = "destY", queryOper = "GE")
    private Double destYGE;
    private String destInfo;
    //订单类型
    @QueryColumn
    @ValueFormat(validations = {@Validation(use = "buy_create", format = ValidConstants.NULL, msg = "订单类型不能自己指定")})
    private OrderType orderType;

    @QueryColumn(propName = "orderType", queryOper = "in")
    private List<OrderType> orderTypes;

    @QueryColumn
    @ValueFormat(validations = {@Validation(use = "buy_create", format = ValidConstants.NOT_NULL, msg = "请指定支付类型")})
    private SettlementType settlementType;

    @QueryColumn(propName = "settlementType", queryOper = "in")
    private List<SettlementType> settlementTypes;

    @QueryColumn
    private Integer offlineProcess;

    @QueryColumn
    @ValueFormat(validations = {@Validation(use = "buy_create", format = ValidConstants.NULL, msg = "创建时，不能指定发货状态")})
    private OrderTrans orderTrans;

    @QueryColumn(propName = "orderTrans", queryOper = "in")
    private List<OrderTrans> orderTranses;

    //创建时间
    private Long createTime;
    @QueryColumn(propName = "createTime", queryOper = "LE")
    private Long createTimeLE;
    @QueryColumn(propName = "createTime", queryOper = "LT")
    private Long createTimeLT;
    @QueryColumn(propName = "createTime", queryOper = "GE")
    private Long createTimeGE;
    @QueryColumn(propName = "createTime", queryOper = "GT")
    private Long createTimeGT;

    //创建人
    private HbzUserDTO createUser;

    @QueryColumn(propName = "createUser.id")
    @ValueFormat(validations = {@Validation(use = "buy_create", format = ValidConstants.NULL, msg = "创建人不能自己指定")})
    private Long createUserId;

    //受理时间
    @QueryColumn
    Long takeTime;
    @QueryColumn(propName = "takeTime", queryOper = "LE")
    Long takeTimeLE;
    @QueryColumn(propName = "takeTime", queryOper = "LT")
    Long takeTimeLT;
    @QueryColumn(propName = "takeTime", queryOper = "GT")
    Long takeTimeGT;
    @QueryColumn(propName = "takeTime", queryOper = "GE")
    Long takeTimeGE;
    //受理人
    private HbzUserDTO takeUser;

    @ValueFormat(validations = {@Validation(use = "buy_create", format = ValidConstants.NULL, msg = "不能在创建时指定定单接收用户")})
    @QueryColumn(propName = "takeUser.id")
    private Long takeUserId;

    //揽件时间
    private Long dealTime;
    //配送人
    private HbzUserDTO dealUser;

    private HbzUserDTO agent;
    private Long agentId;
    private Long agentTime;

    @QueryColumn(propName = "dealUser.id")
    @ValueFormat(validations = {@Validation(use = "buy_create", format = ValidConstants.NULL, msg = "不能在创建时指定定单执用户")})
    private Long dealUserId;

    //支付类型-只用于传参
    private PayType payType;

    //发货区域
    private HbzAreaDTO originArea;
    private Long originAreaId;
    @QueryColumn(propName = "originArea.outCode", queryOper = "=")
    private String originAreaCode;

    @QueryColumn(propName = "originArea.outCode", queryOper = "like")
    private String originAreaCodeLIKE;

    //收货区域
    private HbzAreaDTO destArea;
    private Long destAreaId;
    @QueryColumn(propName = "destArea.outCode")
    private String destAreaCode;

    /**
     * 发货区域outcode列表，用于in条件查询
     */
    @QueryColumn(propName = "originArea.id", queryOper = "IN")
    private List<Long> originAreaIdList;

    /**
     * 送货区域outcode列表，用于in条件查询
     */
    @QueryColumn(propName = "destArea.id", queryOper = "IN")
    private List<Long> destAreaIdList;

    @QueryColumn(propName = "createTime", queryOper = "LE")
    private Long createDateLE;

    @QueryColumn(propName = "createTime", queryOper = "GE")
    private Long createDateGE;


}
