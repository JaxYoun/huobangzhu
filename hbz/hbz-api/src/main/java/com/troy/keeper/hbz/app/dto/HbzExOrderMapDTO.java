package com.troy.keeper.hbz.app.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import com.troy.keeper.hbz.type.CommodityClass;
import lombok.Data;

/**
 * Created by leecheng on 2017/11/30.
 */
@Data
public class HbzExOrderMapDTO extends HbzOrderMapDTO {

    private String relatedPictures;

    @ValueFormat(validations = {
            @Validation(use = "ex_order_create", format = ValidConstants.NOT_NULL, msg = "货物名称{fieldName}不能为空")
    })
    private String commodityName;

    @ValueFormat(validations = {
            @Validation(use = "ex_order_create", format = ValidConstants.NOT_NULL, msg = "取货联系人{fieldName}不能为空")
    })
    private String originLinker;

    @ValueFormat(validations = {
            @Validation(use = "ex_order_create", format = ValidConstants.NOT_NULL, msg = "取货联系人电话{fieldName}不能为空")
    })
    private String originTelephone;

    @ValueFormat(validations = {
            @Validation(use = "ex_order_create", format = ValidConstants.NOT_NULL, msg = "取货地址{fieldName}不能为空")
    })
    private String originAddr;

    @ValueFormat(validations = {
            @Validation(use = "ex_order_create", format = ValidConstants.NOT_NULL, msg = "送达地址{fieldName}不能为空")
    })
    private String destAddr;

    @ValueFormat(validations = {
            @Validation(use = "ex_order_create", format = ValidConstants.NOT_NULL, msg = "货物类型{fieldName}不能为空")
    })
    private CommodityClass commodityClass;

    private Double commodityWeight;

    private Double commodityVolume;

    @ValueFormat(validations = {
            @Validation(use = "ex_order_create", format = ValidConstants.NOT_NULL, msg = "取件时间{fieldName}不能为空")
    })
    private String orderTakeTime;
    private String orderTakeTimeLE;
    private String orderTakeTimeLT;
    private String orderTakeTimeGT;
    private String orderTakeTimeGE;

    private String commodityDesc;

    @ValueFormat(validations = {
            @Validation(use = "ex_order_create", format = ValidConstants.NOT_NULL, msg = "收货联系人{fieldName}不能为空")
    })
    private String linker;

    @ValueFormat(validations = {
            @Validation(use = "ex_order_create", format = ValidConstants.NOT_NULL, msg = "收货联系电话{fieldName}不能为空")
    })
    private String telephone;

}
