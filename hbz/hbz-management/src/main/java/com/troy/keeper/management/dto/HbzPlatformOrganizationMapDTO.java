package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.dto.HbzAreaDTO;
import lombok.Data;

import java.util.List;

/**
 * Created by leecheng on 2018/1/17.
 */
@Data
public class HbzPlatformOrganizationMapDTO extends SmOrgMapDTO {

    private String organizationNo;

    private HbzAreaDTO addressArea;

    private Long addressAreaId;

    private String addressAreaCode;

    private Double lat;
    private Double latLT;
    private Double latLE;
    private Double latGT;
    private Double latGE;

    private Double lng;
    private Double lngLT;
    private Double lngLE;
    private Double lngGT;
    private Double lngGE;

    private String address;
    private String state;
    private Integer orgType;

    private Long areaId;
    private List<Long> areaIds;

}
