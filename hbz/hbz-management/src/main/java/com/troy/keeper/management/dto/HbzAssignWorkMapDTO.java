package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.HbzPlatformOrganizationDTO;
import lombok.Data;

/**
 * Created by leecheng on 2018/1/18.
 */
@Data
public class HbzAssignWorkMapDTO extends HbzBaseMapDTO {

    private Long id;

    private String workNo;

    private String platformNo;
    private Double expectedAmount;
    private HbzPlatformOrganizationDTO platformOrganization;
    private Long platformOrganizationId;

    private HbzAreaDTO originArea;
    private Long originAreaId;
    private String originAreaCode;
    private HbzAreaDTO destArea;
    private Long destAreaId;
    private String destAreaCode;
    private String classification;

    private String sendUser;

    private String sendPersonPhone;

    private String receivePerson;

    private String receivePersonPhone;

    private Double weight;

    private Double volume;

    private String assignTime;

    private String logisticsNo;

    private String workStatus;
}
