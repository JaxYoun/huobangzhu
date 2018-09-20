package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.dto.BaseDTO;
import com.troy.keeper.hbz.dto.HbzPayDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.PayProgress;
import com.troy.keeper.hbz.type.RefundStatus;
import lombok.Data;

import java.util.List;

/**
 * Created by leecheng on 2018/3/6.
 */
@Data
public class HbzRefundMapDTO extends HbzBaseMapDTO {

    @ValueFormat(validations = {
            @Validation(use = "create", format = ValidConstants.NULL, msg = "不能手动设置退款单"),
            @Validation(use = "post", format = ValidConstants.NOT_NULL, msg = "退款单为空")
    })
    private Long id;

    private HbzPayDTO pay;

    @ValueFormat(validations = {
            @Validation(use = "create", format = ValidConstants.NOT_NULL, msg = "支付id不能为空")
    })
    private Long payId;

    private Long userId;

    private HbzUserDTO user;

    @ValueFormat(validations = {
            @Validation(use = "create", format = ValidConstants.NULL, msg = "不能手动设置退款单号")
    })
    private String refundNo;

    @ValueFormat(validations = {
            @Validation(use = "create", format = ValidConstants.NULL, msg = "不能手动设置请求单号")
    })
    private String requestNo;

    @ValueFormat(validations = {
            @Validation(use = "create", format = ValidConstants.NOT_NULL, msg = "状态不能为空")
    })
    private RefundStatus refundStatus;

    private List<RefundStatus> refundStatuses;

    @ValueFormat(validations = {
            @Validation(use = "create", format = ValidConstants.NULL, msg = "不能手动设置对账号")
    })
    private Integer bill;
    private String tradeNo;
    private PayProgress payProgress;
    private String businessNo;
    private BusinessType businessType;
    private String createTime;
    private String createTimeLE;
    private String createTimeGE;
    private String createTimeGT;
    private String createTimeLT;

}
