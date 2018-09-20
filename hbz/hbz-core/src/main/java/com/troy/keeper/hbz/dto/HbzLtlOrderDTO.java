package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.type.*;
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
public class HbzLtlOrderDTO extends HbzOrderDTO {

    @QueryColumn
    String relatedPictures;

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
    private String commodityDescribe;
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

    @QueryColumn(queryOper = "like")
    private String originAddress;

    @QueryColumn(queryOper = "like")
    private String destAddress;

    private Double unitPrice;

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

    @QueryColumn
    private String linkMan;

    @QueryColumn
    private String destLinker;

    @QueryColumn
    private Long destlimit;

    @QueryColumn
    private String destTelephone;

    private String linkRemark;
    @QueryColumn
    private String transLen;
}
