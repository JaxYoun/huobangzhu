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
public class HbzBuyOrderDTO extends HbzOrderDTO {
    //相关图片
    @QueryColumn
    private String relatedPictures;
    //配送地址
    @QueryColumn()
    private String destAddress;

    //商品名
    @QueryColumn(propName = "commodityName", queryOper = "like")
    private String commodityName;

    //商品数量
    @QueryColumn(propName = "commodityCount", queryOper = "equal")
    private Long commodityCount;

    @QueryColumn(propName = "buyNeedInfo", queryOper = "like")
    private String buyNeedInfo;

    @QueryColumn(propName = "linker", queryOper = "like")
    private String linker;
    @QueryColumn(propName = "linkTelephone", queryOper = "like")
    private String linkTelephone;

    @QueryColumn
    private TimeLimit timeLimit;

    @QueryColumn
    private Long startTime;
    @QueryColumn(propName = "startTime", queryOper = "LT")
    private Long startTimeLT;
    @QueryColumn(propName = "startTime", queryOper = "LE")
    private Long startTimeLE;
    @QueryColumn(propName = "startTime", queryOper = "GT")
    private Long startTimeGT;
    @QueryColumn(propName = "startTime", queryOper = "GE")
    private Long startTimeGE;
    @QueryColumn
    private Long endTime;
    @QueryColumn(propName = "endTime", queryOper = "LT")
    private Long endTimeLT;
    @QueryColumn(propName = "endTime", queryOper = "LE")
    private Long endTimeLE;
    @QueryColumn(propName = "endTime", queryOper = "GT")
    private Long endTimeGT;
    @QueryColumn(propName = "endTime", queryOper = "GE")
    private Long endTimeGE;
    @QueryColumn
    private Double commodityAmount;
    @QueryColumn
    private Double remuneration;
}
