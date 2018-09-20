package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.type.ExpressCompanyType;
import lombok.Data;

/**
 * Created by leecheng on 2018/1/5.
 */
@Data
public class HbzExpressInvoiceDTO extends BaseDTO {

    @QueryColumn
    private String orderNo;

    @QueryColumn
    private String orderType;

    @QueryColumn
    private ExpressCompanyType expressCompanyType;

    @QueryColumn
    private String exNo;

    @QueryColumn
    private Long sendTime;
}
