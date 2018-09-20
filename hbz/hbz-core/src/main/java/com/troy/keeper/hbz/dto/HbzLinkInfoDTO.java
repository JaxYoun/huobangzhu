package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import com.troy.keeper.hbz.type.OrderType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by leecheng on 2017/11/30.
 */
@Getter
@Setter
public class HbzLinkInfoDTO extends BaseDTO {

    @QueryColumn
    @ValueFormat(validations = {
            @Validation(use = "link_info_create", format = ValidConstants.NOT_NULL, msg = "地点类型{fieldName}不能为空"),
            @Validation(use = "link_info_update", format = ValidConstants.NOT_NULL, msg = "地点类型{fieldName}不能为空")
    })
    private String type;

    @QueryColumn
    @ValueFormat(validations = {
            @Validation(use = "link_info_update", format = ValidConstants.NOT_NULL, msg = "收发货类型{fieldName}不能为空"),
            @Validation(use = "link_info_create", format = ValidConstants.NOT_NULL, msg = "收发货类型{fieldName}不能为空")
    })
    private Integer usein;
    @QueryColumn
    private OrderType orderType;

    @QueryColumn(propName = "orderType", queryOper = "in")
    private List<OrderType> orderTypes;

    @QueryColumn
    private Integer index;

    private HbzAreaDTO area;

    @QueryColumn(propName = "area.id")
    private Long areaId;

    private String areaCode;

    @QueryColumn
    private Integer idefault;

    @QueryColumn
    @ValueFormat(validations = {
            @Validation(use = "link_info_update", format = ValidConstants.NOT_NULL, msg = "联系人{fieldName}不能为空"),
            @Validation(use = "link_info_create", format = ValidConstants.NOT_NULL, msg = "联系人{fieldName}不能为空")
    })
    private String linker;

    @QueryColumn
    @ValueFormat(validations = {
            @Validation(use = "link_info_update", format = ValidConstants.NOT_NULL, msg = "联系电话{fieldName}不能为空"),
            @Validation(use = "link_info_create", format = ValidConstants.NOT_NULL, msg = "联系电话{fieldName}不能为空")
    })
    private String linkTelephone;

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
    private String location;

    @QueryColumn
    private String address;

    private HbzUserDTO user;

    @QueryColumn(propName = "user.id")
    private Long userId;

}
