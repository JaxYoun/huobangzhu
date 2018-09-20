package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.type.CommodityClass;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by leecheng on 2017/11/29.
 */
@Getter
@Setter
public class HbzExOrderDTO extends HbzOrderDTO {

    @QueryColumn
    private String relatedPictures;

    @QueryColumn
    private String commodityName;

    @QueryColumn
    private String originLinker;

    @QueryColumn
    private String originTelephone;

    @QueryColumn
    private String originAddr;

    @QueryColumn
    private String destAddr;

    @QueryColumn
    private CommodityClass commodityClass;

    @QueryColumn
    private Double commodityWeight;

    @QueryColumn
    private Double commodityVolume;

    @QueryColumn
    private Long orderTakeTime;
    @QueryColumn(propName = "orderTakeTime", queryOper = "LT")
    private Long orderTakeTimeLT;
    @QueryColumn(propName = "orderTakeTime", queryOper = "GT")
    private Long orderTakeTimeGT;
    @QueryColumn(propName = "orderTakeTime", queryOper = "LE")
    private Long orderTakeTimeLE;
    @QueryColumn(propName = "orderTakeTime", queryOper = "GE")
    private Long orderTakeTimeGE;

    @QueryColumn
    private String commodityDesc;

    @QueryColumn
    private String linker;

    @QueryColumn
    private String telephone;

}
