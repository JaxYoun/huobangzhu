package com.troy.keeper.hbz.app.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import com.troy.keeper.hbz.type.TimeLimit;
import lombok.*;

/**
 * Created by leecheng on 2017/10/24.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HbzSendOrderMapDTO extends HbzOrderMapDTO {

    private String relatedPictures;

    @ValueFormat(validations = {
            @Validation(use = "send_create", format = ValidConstants.NOT_NULL, msg = "货物质量{fieldName}不能为空"),
            @Validation(use = "web_send_create", format = ValidConstants.NOT_NULL, msg = "货物质量{fieldName}不能为空")
    })
    private Double commodityWeight;

    @ValueFormat(validations = {
            @Validation(use = "send_create", format = ValidConstants.NOT_NULL, msg = "货物体积{fieldName}不能为空"),
            @Validation(use = "web_send_create", format = ValidConstants.NOT_NULL, msg = "货物体积{fieldName}不能为空")
    })
    private Double commodityVolume;

    //配送地址
    @ValueFormat(validations = {
            @Validation(use = "send_create", format = ValidConstants.NOT_NULL, msg = "目的地{fieldName}不能为空"),
            @Validation(use = "web_send_create", format = ValidConstants.NOT_NULL, msg = "目的地{fieldName}不能为空")
    })
    private String destAddress;

    //配送地址
    @ValueFormat(validations = {
            @Validation(use = "send_create", format = ValidConstants.NOT_NULL, msg = "货物地{fieldName}不能为空"),
            @Validation(use = "web_send_create", format = ValidConstants.NOT_NULL, msg = "货物地{fieldName}不能为空")
    })
    private String originAddress;

    //商品名
    @ValueFormat(validations = {
            @Validation(use = "web_send_create", format = ValidConstants.NOT_NULL, msg = "货物描述{fieldName}不能为空")
    })
    private String commodityDesc;
    private String commodityName;
    @ValueFormat(validations = {
            @Validation(use = "send_create", format = ValidConstants.NOT_NULL, msg = "取货联系{fieldName}不能为空"),
            @Validation(use = "web_send_create", format = ValidConstants.NOT_NULL, msg = "取货联系{fieldName}不能为空")
    })
    private String originLinker;

    @ValueFormat(validations = {
            @Validation(use = "send_create", format = ValidConstants.NOT_NULL, msg = "取货人手机{fieldName}不能为空"),
            @Validation(use = "web_send_create", format = ValidConstants.NOT_NULL, msg = "取货人手机{fieldName}不能为空")
    })
    private String originLinkTelephone;

    @ValueFormat(validations = {
            @Validation(use = "send_create", format = ValidConstants.NOT_NULL, msg = "接收人{fieldName}不能为空"),
            @Validation(use = "web_send_create", format = ValidConstants.NOT_NULL, msg = "接收人{fieldName}不能为空")
    })
    private String linker;

    @ValueFormat(validations = {
            @Validation(use = "send_create", format = ValidConstants.NOT_NULL, msg = "接收电话{fieldName}不能为空"),
            @Validation(use = "web_send_create", format = ValidConstants.NOT_NULL, msg = "接收电话{fieldName}不能为空")
    })
    private String linkTelephone;

    @ValueFormat(validations = {
            @Validation(use = "send_create", format = ValidConstants.NOT_NULL, msg = "配送时间限制{fieldName}不能为空"),
            @Validation(use = "web_send_create", format = ValidConstants.NOT_NULL, msg = "配送时间限制{fieldName}不能为空")
    })
    private TimeLimit timeLimit;

    @ValueFormat(validations = {
            @Validation(use = "send_create", format = ValidConstants.NOT_NULL, msg = "取货时间限制{fieldName}不能为空"),
            @Validation(use = "web_send_create", format = ValidConstants.NOT_NULL, msg = "取货时间限制{fieldName}不能为空")
    })
    private Integer takeLimit;

    private String orderTakeTime;

    private String startTime;

    private String endTime;

    private String orderTakeTimeLT;
    private String orderTakeTimeLE;
    private String orderTakeTimeGT;
    private String orderTakeTimeGE;

    private String startTimeLT;
    private String startTimeLE;
    private String startTimeGT;
    private String startTimeGE;

    private String endTimeLT;
    private String endTimeLE;
    private String endTimeGT;
    private String endTimeGE;
}
