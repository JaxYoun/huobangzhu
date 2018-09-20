package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import lombok.Data;

/**
 * Created by leecheng on 2018/1/19.
 */
@Data
public class HbzCreditRecordDTO extends BaseDTO {

    @QueryColumn
    private String type;

    @QueryColumn
    private String recNo;

    private HbzUserDTO user;

    @QueryColumn(propName="user.id")
    private Long userId;

    @QueryColumn
    private Integer delta;

    @QueryColumn
    private String action;

    @QueryColumn
    private String adjustType;

    @QueryColumn
    private String msg;

    @QueryColumn
    private Long time;
    @QueryColumn(propName = "time", queryOper = "LT")
    private Long timeLT;
    @QueryColumn(propName = "time", queryOper = "LE")
    private Long timeLE;
    @QueryColumn(propName = "time", queryOper = "GT")
    private Long timeGT;
    @QueryColumn(propName = "time", queryOper = "GE")
    private Long timeGE;

}
