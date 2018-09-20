package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by leecheng on 2017/10/27.
 */
@Getter
@Setter
@ToString
public class HbzCoordinateDTO extends BaseDTO {

    private HbzAreaDTO area;

    @QueryColumn(propName = "area.id")
    private Long areaId;

    private HbzUserDTO user;

    @ValueFormat(validations = {@Validation(use = "sync", format = ValidConstants.NULL, msg = "手动指定用户")})
    @QueryColumn(propName = "user.id", queryOper = "equal")
    private Long userId;

    @ValueFormat(validations = {@Validation(use = "sync", format = ValidConstants.NUMBER, msg = "经度必须提供数据")})
    @QueryColumn
    private Double pointX;
    @QueryColumn(propName = "pointX", queryOper = "LT")
    private Double pointXLT;
    @QueryColumn(propName = "pointX", queryOper = "LE")
    private Double pointXLE;
    @QueryColumn(propName = "pointX", queryOper = "GT")
    private Double pointXGT;
    @QueryColumn(propName = "pointX", queryOper = "GE")
    private Double pointXGE;

    @ValueFormat(validations = {@Validation(use = "sync", format = ValidConstants.NUMBER, msg = "纬度必须提供数据")})
    @QueryColumn
    private Double pointY;
    @QueryColumn(propName = "pointY", queryOper = "LT")
    private Double pointYLT;
    @QueryColumn(propName = "pointY", queryOper = "LE")
    private Double pointYLE;
    @QueryColumn(propName = "pointY", queryOper = "GT")
    private Double pointYGT;
    @QueryColumn(propName = "pointY", queryOper = "GE")
    private Double pointYGE;

    @ValueFormat(validations = {@Validation(use = "sync", format = ValidConstants.NULL, msg = "手动同步时间")})
    private Long syncMillis;
    @QueryColumn(propName = "syncMillis", queryOper = "LT")
    private Long syncMillisLT;
    @QueryColumn(propName = "syncMillis", queryOper = "LE")
    private Long syncMillisLE;
    @QueryColumn(propName = "syncMillis", queryOper = "GT")
    private Long syncMillisGT;
    @QueryColumn(propName = "syncMillis", queryOper = "GE")
    private Long syncMillisGE;

}
