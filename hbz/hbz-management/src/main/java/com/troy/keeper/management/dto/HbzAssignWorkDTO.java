package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.HbzPlatformOrganizationDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 李奥
 * @date 2018/1/19.
 */
@Getter
@Setter
public class HbzAssignWorkDTO {

    private Long id;

    private String commodityName;

    private String workNo;

    private String platformNo;
    //预估价格
    private Double expectedAmount;

    private HbzPlatformOrganizationDTO platformOrganizationDTO;

    private String originArea;
    //    private Long    originAreaId;
    private String originAreaCode;
    private String destArea;

    private HbzAreaDTO originAreaWrapper;
    private HbzAreaDTO destAreaWrapper;
    private String originInfo;
    private String destInfo;

    //    private Long    destAreaId;
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


    //省id
    private Long provinceId;
    //市id
    private Long cityId;
    //县id
    private Long countyId;

    //到站
    //省id
    private Long provinceToId;
    //市id
    private Long cityToId;
    //县id
    private Long countyToId;

    //收货时间范围查询
    private Long smallTime;
    private Long bigTime;

    private LinkedList<Long> startCity;
    private LinkedList<Long> endCity;


}
