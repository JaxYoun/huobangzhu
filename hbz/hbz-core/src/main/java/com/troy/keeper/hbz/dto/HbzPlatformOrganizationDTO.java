package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import lombok.Data;

import java.util.List;

/**
 * Created by leecheng on 2018/1/16.
 */
@Data
public class HbzPlatformOrganizationDTO extends SmOrgDTO {

    private String organizationNo;

    private HbzAreaDTO addressArea;

    @QueryColumn(propName = "addressArea.id")
    private Long addressAreaId;

    @QueryColumn(propName = "addressArea.outCode")
    private String addressAreaCode;

    @QueryColumn
    private Double lat;
    @QueryColumn(propName = "lat", queryOper = "LT")
    private Double latLT;
    @QueryColumn(propName = "lat", queryOper = "LE")
    private Double latLE;
    @QueryColumn(propName = "lat", queryOper = "GT")
    private Double latGT;
    @QueryColumn(propName = "lat", queryOper = "GE")
    private Double latGE;

    @QueryColumn
    private Double lng;
    @QueryColumn(propName = "lng", queryOper = "LT")
    private Double lngLT;
    @QueryColumn(propName = "lng", queryOper = "LE")
    private Double lngLE;
    @QueryColumn(propName = "lng", queryOper = "GT")
    private Double lngGT;
    @QueryColumn(propName = "lng", queryOper = "GE")
    private Double lngGE;

    @QueryColumn
    private String state;

    @QueryColumn
    private String address;

    @QueryColumn
    private Integer orgType;

    @QueryColumn(propName = "serviceAreas.id", queryOper = "IN")
    private List<Long> areaIds;
}
