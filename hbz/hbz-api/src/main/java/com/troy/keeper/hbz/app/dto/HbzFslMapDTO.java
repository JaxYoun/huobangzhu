package com.troy.keeper.hbz.app.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import com.troy.keeper.hbz.type.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by leecheng on 2017/11/10.
 */
@Getter
@Setter
public class HbzFslMapDTO {

    String relatedPictures;
    private HbzUserDTO agent;
    private Long agentId;
    private Long agentTime;
    String completeImage;
    String receiveImage;
    Long createTime;
    Integer offlineProcess;
    Long takeTime;
    Long dealTime;
    Integer paySelection;

    @ValueFormat(
            validations = {
                    @Validation(use = "fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
                    @Validation(use = "web_fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
                    @Validation(use = "web_fsl_order_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
            })
    private String transLen;

    @ValueFormat(validations = {
            @Validation(use = "fsl_order_create", format = ValidConstants.NULL, msg = "{fieldName}不能自己设置"),
            @Validation(use = "fsl_order_confirm", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "web_fsl_order_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "web_fsl_order_create", format = ValidConstants.NULL, msg = "{fieldName}不能自己设置")
    })
    private Long id;

    @ValueFormat(validations = {
            @Validation(use = "fsl_order_create", format = ValidConstants.NULL, msg = "{fieldName}不能自己设置"),
            @Validation(use = "web_fsl_order_create", format = ValidConstants.NULL, msg = "{fieldName}不能自己设置"),
            @Validation(use = "web_fsl_order_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private String orderNo;

    private String commodityDescribe;

    @ValueFormat(
            validations = {
                    @Validation(use = "fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
                    @Validation(use = "web_fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
                    @Validation(use = "web_fsl_order_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
            })
    private String commodityName;

    @ValueFormat(validations = {
            @Validation(use = "fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private CommodityType commodityType;

    private Double commodityWeight;
    private Double commodityWeightLT;
    private Double commodityWeightLE;
    private Double commodityWeightGT;
    private Double commodityWeightGE;

    private Double commodityVolume;
    private Double commodityVolumeLT;
    private Double commodityVolumeLE;
    private Double commodityVolumeGT;
    private Double commodityVolumeGE;

    private WeightUnit weightUnit;

    private VolumeUnit volumeUnit;

    @QueryColumn
    @ValueFormat(validations = {
            @Validation(use = "fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "web_fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "web_fsl_order_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private TransType transType;

    @ValueFormat(validations = {
            @Validation(use = "fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "web_fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "web_fsl_order_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private Double maxLoad;
    private Double maxLoadLT;
    private Double maxLoadLE;
    private Double maxLoadGT;
    private Double maxLoadGE;

    //订单状态
    private List<OrderTrans> orderTranses;

    @ValueFormat(validations = {
            @Validation(use = "fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "web_fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "web_fsl_order_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private String originAddress;

    @ValueFormat(validations = {
            @Validation(use = "fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private Double unitPrice;

    @ValueFormat(validations = {
            @Validation(use = "fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "web_fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "web_fsl_order_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private String destAddress;

    @ValueFormat(validations = {
            @Validation(use = "fsl_order_create", format = ValidConstants.REGEX, msg = "{fieldName}错误", conf = "\\d{4}\\-\\d{2}\\-\\d{2}\\s\\d{2}\\:\\d{2}"),
            @Validation(use = "web_fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "web_fsl_order_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private String orderTakeStart;
    private String orderTakeStartLT;
    private String orderTakeStartLE;
    private String orderTakeStartGT;
    private String orderTakeStartGE;


    private Double originX;
    private Double originXLT;
    private Double originXLE;
    private Double originXGT;
    private Double originXGE;

    private Double originY;
    private Double originYLT;
    private Double originYLE;
    private Double originYGT;
    private Double originYGE;

    @ValueFormat(validations = {
            @Validation(use = "web_fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "web_fsl_order_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private Double amount;
    private Double amountLT;
    private Double amountLE;
    private Double amountGT;
    private Double amountGE;

    @ValueFormat(validations = {
            @Validation(use = "fsl_order_create", format = ValidConstants.NOT_NULL, msg = "预估价格[{fieldName}]为空")
    })
    private Double expectedPrice;
    private Double expectedPriceLE;
    private Double expectedPriceLT;
    private Double expectedPriceGT;
    private Double expectedPriceGE;

    //目标经度
    private Double destX;
    private Double destXLT;
    private Double destXLE;
    private Double destXGT;
    private Double destXGE;

    //目标纬度
    private Double destY;
    private Double destYLT;
    private Double destYLE;
    private Double destYGT;
    private Double destYGE;

    //结算方式
    private SettlementType settlementType;
    private List<SettlementType> settlementTypes;
    //发货区域
    @ValueFormat(validations = {
            @Validation(use = "fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "web_fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "web_fsl_order_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private String originAreaCode;

    //收货区域
    @ValueFormat(validations = {
            @Validation(use = "fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "web_fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "web_fsl_order_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private String destAreaCode;

    @ValueFormat(validations = {
            @Validation(use = "fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "web_fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "web_fsl_order_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private String linkTelephone;
    @ValueFormat(validations = {
            @Validation(use = "fsl_order_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private String linkMan;

    private String destLinker;

    private String destTelephone;

    private String destlimit;

    private String linkRemark;

    //用户状态信息
    private Double locationX;
    private Double locationY;
    private Double distance;

    //创建人
    private HbzUserDTO createUser;
    private Long createUserId;
    //受理人
    private HbzUserDTO takeUser;
    private Long takeUserId;
    //配送人
    private HbzUserDTO dealUser;
    private Long dealUserId;


    //分页排序参数
    private Integer page = 0;

    private Integer size = 11;

    private List<String[]> sorts;
}
