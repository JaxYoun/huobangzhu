package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.type.BillType;
import com.troy.keeper.hbz.type.PayType;
import lombok.Data;

/**
 * Created by leecheng on 2018/3/8.
 */
@Data
public class HbzBillDTO extends BaseDTO {

    @QueryColumn
    private String tradeNo;

    @QueryColumn
    private BillType billType;

    @QueryColumn
    private String requestNo;

    @QueryColumn
    private Long date;
    @QueryColumn(propName = "date", queryOper = "LT")
    private Long dateLT;
    @QueryColumn(propName = "date", queryOper = "LE")
    private Long dateLE;
    @QueryColumn(propName = "date", queryOper = "GT")
    private Long dateGT;
    @QueryColumn(propName = "date", queryOper = "GE")
    private Long dateGE;

    @QueryColumn
    private String billContent;

    @QueryColumn
    private PayType payType;

}
