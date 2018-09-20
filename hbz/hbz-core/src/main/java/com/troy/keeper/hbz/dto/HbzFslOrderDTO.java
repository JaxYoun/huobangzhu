package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.annotation.ExcelCell;
import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.type.CommodityType;
import com.troy.keeper.hbz.type.TransType;
import com.troy.keeper.hbz.type.VolumeUnit;
import com.troy.keeper.hbz.type.WeightUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by leecheng on 2017/11/10.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class HbzFslOrderDTO extends HbzOrderDTO {

    @QueryColumn
    String relatedPictures;

    @QueryColumn
    private String transLen;

    @QueryColumn(queryOper = "like")
    private String commodityName;

    @QueryColumn
    private CommodityType commodityType;

    @QueryColumn
    private Double commodityWeight;
    @QueryColumn(propName = "commodityWeight", queryOper = "LT")
    private Double commodityWeightLT;
    @QueryColumn(propName = "commodityWeight", queryOper = "LE")
    private Double commodityWeightLE;
    @QueryColumn(propName = "commodityWeight", queryOper = "GT")
    private Double commodityWeightGT;
    @QueryColumn(propName = "commodityWeight", queryOper = "GE")
    private Double commodityWeightGE;

    @QueryColumn
    private Double commodityVolume;
    @QueryColumn(propName = "commodityVolume", queryOper = "LT")
    private Double commodityVolumeLT;
    @QueryColumn(propName = "commodityVolume", queryOper = "LE")
    private Double commodityVolumeLE;
    @QueryColumn(propName = "commodityVolume", queryOper = "GT")
    private Double commodityVolumeGT;
    @QueryColumn(propName = "commodityVolume", queryOper = "GE")
    private Double commodityVolumeGE;

    @QueryColumn
    private WeightUnit weightUnit;

    @QueryColumn
    private VolumeUnit volumeUnit;

    @QueryColumn
    private TransType transType;

    @QueryColumn(propName = "transType", queryOper = "IN")
    private List<TransType> transTypes;

    @QueryColumn
    private Double maxLoad;
    @QueryColumn(propName = "maxLoad", queryOper = "LT")
    private Double maxLoadLT;
    @QueryColumn(propName = "maxLoad", queryOper = "LE")
    private Double maxLoadLE;
    @QueryColumn(propName = "maxLoad", queryOper = "GT")
    private Double maxLoadGT;
    @QueryColumn(propName = "maxLoad", queryOper = "GE")
    private Double maxLoadGE;

    private Double unitPrice;

    @QueryColumn(queryOper = "like")
    private String originAddress;

    @QueryColumn(queryOper = "like")
    private String destAddress;
    private String commodityDescribe;

    @QueryColumn
    private Long orderTakeStart;
    @QueryColumn(propName = "orderTakeStart", queryOper = "LT")
    private Long orderTakeStartLT;
    @QueryColumn(propName = "orderTakeStart", queryOper = "LE")
    private Long orderTakeStartLE;
    @QueryColumn(propName = "orderTakeStart", queryOper = "GT")
    private Long orderTakeStartGT;
    @QueryColumn(propName = "orderTakeStart", queryOper = "GE")
    private Long orderTakeStartGE;

    @QueryColumn
    private String linkTelephone;

    @ExcelCell(title = "联系人")
    @QueryColumn
    private String linkMan;

    @ExcelCell(title = "收货联系人")
    @QueryColumn
    private String destLinker;

    @ExcelCell(title = "送达时间限制")
    @QueryColumn
    private Long destlimit;

    @ExcelCell(title = "收货电话")
    @QueryColumn
    private String destTelephone;

    @ExcelCell(title = "补充说明")
    private String linkRemark;

}
