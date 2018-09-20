package com.troy.keeper.hbz.app.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import com.troy.keeper.hbz.type.TimeLimit;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by leecheng on 2017/11/10.
 */
@Getter
@Setter
public class HbzBuyOrderMapDTO extends HbzOrderMapDTO {

    private String relatedPictures;

    //配送地址
    @ValueFormat(validations = {
            @Validation(use = "buy_create", format = ValidConstants.NOT_NULL, msg = "地址{fieldName}不能为空"),
            @Validation(use = "web_buy_create", format = ValidConstants.NOT_NULL, msg = "地址{fieldName}不能为空")
    })
    private String destAddress;

    //商品名
    @ValueFormat(validations = {
            @Validation(use = "buy_create", format = ValidConstants.NOT_NULL, msg = "商品名{fieldName}不能为空"),
            @Validation(use = "web_buy_create", format = ValidConstants.NOT_NULL, msg = "商品名{fieldName}不能为空")
    })
    private String commodityName;

    //商品数量
    @ValueFormat(validations = {
            @Validation(use = "buy_create", format = ValidConstants.NOT_NULL, msg = "商品数量{fieldName}不能为空"),
            @Validation(use = "web_buy_create", format = ValidConstants.NOT_NULL, msg = "商品数量{fieldName}不能为空")
    })
    private Long commodityCount;

    private String buyNeedInfo;

    @ValueFormat(validations = {
            @Validation(use = "buy_create", format = ValidConstants.NOT_NULL, msg = "联系人{fieldName}不能为空"),
            @Validation(use = "web_buy_create", format = ValidConstants.NOT_NULL, msg = "联系人{fieldName}不能为空")
    })
    private String linker;

    @ValueFormat(validations = {
            @Validation(use = "buy_create", format = ValidConstants.NOT_NULL, msg = "联系电话{fieldName}不能为空"),
            @Validation(use = "web_buy_create", format = ValidConstants.NOT_NULL, msg = "联系电话{fieldName}不能为空")
    })
    private String linkTelephone;

    @ValueFormat(validations = {
            @Validation(use = "buy_create", format = ValidConstants.NOT_NULL, msg = "配送时间限制{fieldName}不能为空"),
            @Validation(use = "web_buy_create", format = ValidConstants.NOT_NULL, msg = "配送时间限制{fieldName}不能为空")
    })
    private TimeLimit timeLimit;

    private String startTime;
    private String startTimeLT;
    private String startTimeLE;
    private String startTimeGT;
    private String startTimeGE;

    private String endTime;
    private String endTimeLT;
    private String endTimeLE;
    private String endTimeGT;
    private String endTimeGE;

    @ValueFormat(validations = {
            @Validation(use = "buy_create", format = ValidConstants.NOT_NULL, msg = "金额{fieldName}不能为空"),
            @Validation(use = "web_buy_create", format = ValidConstants.NOT_NULL, msg = "金额{fieldName}不能为空")
    })
    private Double commodityAmount;

    @ValueFormat(validations = {@Validation(use = "buy_confirm", format = ValidConstants.NOT_NULL, msg = "金额{fieldName}不能为空")})
    private Double remuneration;
}
