package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import lombok.Data;

import java.util.List;

/**
 * Created by leecheng on 2018/1/18.
 */
@Data
public class HbzAssignWorkDTO extends BaseDTO {

    @QueryColumn
    private String workNo;

    @QueryColumn
    private String platformNo;

    @QueryColumn
    private Double expectedAmount;

    private HbzPlatformOrganizationDTO platformOrganization;

    @QueryColumn(propName = "platformOrganization.id")
    private Long platformOrganizationId;

    @QueryColumn(propName = "platformOrganization.id",queryOper = "in")
    private List<Long> platformOrganizationIds;

    private HbzAreaDTO originArea;

    @QueryColumn(propName = "originArea.id")
    private Long originAreaId;

    @QueryColumn(propName = "originArea.outCode")
    private String originAreaCode;

    private HbzAreaDTO destArea;

    @QueryColumn(propName = "destArea.id")
    private Long destAreaId;

    @QueryColumn(propName = "destArea.outCode")
    private String destAreaCode;

    @QueryColumn
    private String classification;

    @QueryColumn
    private String sendUser;

    @QueryColumn
    private String sendPersonPhone;

    @QueryColumn
    private String receivePerson;

    @QueryColumn
    private String receivePersonPhone;

    @QueryColumn
    private Double weight;

    @QueryColumn
    private Double volume;

    @QueryColumn
    private Long assignTime;

    @QueryColumn
    private String logisticsNo;

    @QueryColumn
    private String workStatus;

}
