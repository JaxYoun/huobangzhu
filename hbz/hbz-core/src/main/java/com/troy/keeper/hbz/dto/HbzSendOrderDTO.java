package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
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
public class HbzSendOrderDTO extends HbzOrderDTO {

    @QueryColumn
    private String relatedPictures;

    @QueryColumn
    private Double commodityWeight;

    @QueryColumn
    private Double commodityVolume;

    //配送地址
    @QueryColumn
    private String destAddress;

    //配送地址
    @QueryColumn
    private String originAddress;
    @QueryColumn
    private String commodityName;
    //商品描述
    @QueryColumn
    private String commodityDesc;
    @QueryColumn
    private String originLinker;
    @QueryColumn
    private String originLinkTelephone;
    @QueryColumn
    private String linker;
    @QueryColumn
    private String linkTelephone;
    @QueryColumn
    private TimeLimit timeLimit;
    @QueryColumn
    private Integer takeLimit;
    @QueryColumn
    private Long orderTakeTime;
    @QueryColumn
    private Long startTime;
    @QueryColumn
    private Long endTime;

    @QueryColumn(propName = "orderTakeTime", queryOper = "LT")
    private Long orderTakeTimeLT;
    @QueryColumn(propName = "orderTakeTime", queryOper = "LE")
    private Long orderTakeTimeLE;
    @QueryColumn(propName = "orderTakeTime", queryOper = "GT")
    private Long orderTakeTimeGT;
    @QueryColumn(propName = "orderTakeTime", queryOper = "GE")
    private Long orderTakeTimeGE;

    @QueryColumn(propName = "startTime", queryOper = "LT")
    private Long startTimeLT;
    @QueryColumn(propName = "startTime", queryOper = "LE")
    private Long startTimeLE;
    @QueryColumn(propName = "startTime", queryOper = "GT")
    private Long startTimeGT;
    @QueryColumn(propName = "startTime", queryOper = "GE")
    private Long startTimeGE;

    @QueryColumn(propName = "endTime", queryOper = "LT")
    private Long endTimeLT;
    @QueryColumn(propName = "endTime", queryOper = "LE")
    private Long endTimeLE;
    @QueryColumn(propName = "endTime", queryOper = "GT")
    private Long endTimeGT;
    @QueryColumn(propName = "endTime", queryOper = "GE")
    private Long endTimeGE;
}
