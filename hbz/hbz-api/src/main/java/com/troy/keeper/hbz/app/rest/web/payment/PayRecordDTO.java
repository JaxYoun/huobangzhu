package com.troy.keeper.hbz.app.rest.web.payment;

import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.PayType;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/2/8 11:53
 */
@Data
public class PayRecordDTO {

    @NotBlank(message = "订单编号必填")
    private String orderNo;

    private PayType payType;

    private BusinessType businessType;

}
