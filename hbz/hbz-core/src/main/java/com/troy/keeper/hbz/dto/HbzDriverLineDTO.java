package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.annotation.ExcelCell;
import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.type.TransType;
import lombok.Data;

import java.util.List;

/**
 * Created by leecheng on 2017/12/6.
 */
@Data
public class HbzDriverLineDTO extends BaseDTO {

    private HbzUserDTO user;
    @QueryColumn(propName = "user.id")
    private Long userId;

    private HbzAreaDTO originArea;

    @QueryColumn(propName = "originArea.outCode")
    private String originAreaCode;

    @QueryColumn(propName = "originArea.id")
    private Long originAreaId;

    @QueryColumn(propName = "destArea.outCode")
    private String destAreaCode;

    private HbzAreaDTO destArea;

    @QueryColumn(propName = "destArea.id")
    private Long destAreaId;

    @QueryColumn
    private TransType transType;

    @QueryColumn
    private Double maxLoad;

    @QueryColumn
    private Double unitPrices;

    @QueryColumn(propName = "transSizes.transSize", queryOper = "in")
    private List<Double> transSizes;

    @QueryColumn(propName = "transSizes.transSize", queryOper = "=")
    private Double transSize;

    @QueryColumn(propName = "transSizes.transSize", queryOper = "lt")
    private Double transSizeLT;

    @QueryColumn(propName = "transSizes.transSize", queryOper = "le")
    private Double transSizeLE;

    @QueryColumn(propName = "transSizes.transSize", queryOper = "gt")
    private Double transSizeGT;

    @QueryColumn(propName = "transSizes.transSize", queryOper = "ge")
    private Double transSizeGE;
}
