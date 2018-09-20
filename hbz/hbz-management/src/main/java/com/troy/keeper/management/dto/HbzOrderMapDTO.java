package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import com.troy.keeper.hbz.type.OrderTrans;
import com.troy.keeper.hbz.type.OrderType;
import com.troy.keeper.hbz.type.PayType;
import com.troy.keeper.hbz.type.SettlementType;
import lombok.Data;

import java.util.List;

/**
 * Created by leecheng on 2017/11/30.
 */
@Data
public class HbzOrderMapDTO extends HbzBaseMapDTO {

    private HbzUserDTO agent;
    private Long agentId;
    private Long agentTime;
    String receiveImage;
    String completeImage;
    Integer offlineProcess;
    Long createTime;
    Long takeTime;
    Long dealTime;

    private String createDate;
    private String createDateStart;
    private String createDateEnd;

    private Long createDateLE;
    private Long createDateGE;

    @ValueFormat(validations = {
            @Validation(use = "send_confirm", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "buy_confirm", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "buy_get", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private Long id;

    //订单号，规范：HBZ_${类型}_${时间}_${编号}
    private String orderNo;

    //取货地址X
    @ValueFormat(validations = {
            @Validation(use = "send_create", format = ValidConstants.NOT_NULL, msg = "货物经度{fieldName}不能为空")
    })
    private Double originX;
    private Double originXLE;
    private Double originXLT;
    private Double originXGT;
    private Double originXGE;

    //取货地址Y
    @ValueFormat(validations = {
            @Validation(use = "send_create", format = ValidConstants.NOT_NULL, msg = "货物纬度{fieldName}不能为空")
    })
    private Double originY;
    private Double originYLT;
    private Double originYLE;
    private Double originYGT;
    private Double originYGE;
    private String originInfo;

    //总金额
    @ValueFormat(validations = {
            @Validation(use = "send_create", format = ValidConstants.NOT_NULL, msg = "金额{fieldName}不能为空"),
            @Validation(use = "web_send_create", format = ValidConstants.NOT_NULL, msg = "金额{fieldName}不能为空")
    })
    private Double amount;
    private Double amountLT;
    private Double amountLE;
    private Double amountGT;
    private Double amountGE;

    private Double expectedPrice;
    private Double expectedPriceLE;
    private Double expectedPriceLT;
    private Double expectedPriceGT;
    private Double expectedPriceGE;

    //目标经度
    @ValueFormat(validations = {
            @Validation(use = "send_create", format = ValidConstants.NOT_NULL, msg = "目标经度{fieldName}不能为空"),
            @Validation(use = "buy_create", format = ValidConstants.NOT_NULL, msg = "送货坐标{fieldName}不能为空")
    })
    private Double destX;
    private Double destXLT;
    private Double destXLE;
    private Double destXGT;
    private Double destXGE;

    //目标纬度
    @ValueFormat(validations = {
            @Validation(use = "send_create", format = ValidConstants.NOT_NULL, msg = "货物纬度{fieldName}不能为空"),
            @Validation(use = "buy_create", format = ValidConstants.NOT_NULL, msg = "送货坐标{fieldName}不能为空")
    })
    private Double destY;
    private Double destYLT;
    private Double destYLE;
    private Double destYGT;
    private Double destYGE;
    private String destInfo;
    //订单类型
    private OrderType orderType;

    private List<OrderType> orderTypes;

    private SettlementType settlementType;
    private List<SettlementType> settlementTypes;
    private OrderTrans orderTrans;

    private List<OrderTrans> orderTranses;

    //创建人
    private HbzUserDTO createUser;

    private Long createUserId;

    //受理人
    private HbzUserDTO takeUser;

    private Long takeUserId;

    //配送人
    private HbzUserDTO dealUser;

    private Long dealUserId;

    //支付类型-只用于传参
    private PayType payType;

    //发货区域
    private HbzAreaDTO originArea;
    private Long originAreaId;

    @ValueFormat(validations = {
            @Validation(use = "ex_order_create", format = ValidConstants.NOT_NULL, msg = "发货区域编码{fieldName}不能为空")
    })
    private String originAreaCode;

    //收货区域
    private HbzAreaDTO destArea;
    private Long destAreaId;

    @ValueFormat(validations = {
            @Validation(use = "ex_order_create", format = ValidConstants.NOT_NULL, msg = "收货区域编码{fieldName}不能为空")
    })
    private String destAreaCode;

    private Double locationX;
    private Double locationY;
    private Double distance;

    /**
     * 发货区域outcode列表，用于in条件查询
     */
    private List<Long> originAreaIdList;

    /**
     * 送货区域outcode列表，用于in条件查询
     */
    private List<Long> destAreaIdList;

}
