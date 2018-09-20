package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.PayStatus;
import com.troy.keeper.hbz.type.PayType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by leecheng on 2017/11/2.
 */
@Getter
@Setter
public class HbzCostDTO extends BaseDTO {

    private String costNo;

    private HbzUserDTO user;

    @QueryColumn(propName = "user.id")
    private Long userId;

    @QueryColumn
    private Double amount;

    @QueryColumn
    private Long createdTime;

    @QueryColumn
    private Long costTime;

    @QueryColumn
    private BusinessType businessType;

    @QueryColumn
    private String businessNo;

    @QueryColumn
    private PayStatus payStatus;

    private PayType payType;

}
