package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import lombok.Data;

/**
 * Created by leecheng on 2017/12/12.
 */
@Data
public class HbzWareTypeDTO extends BaseDTO {

    private HbzWareTypeDTO parent;

    @QueryColumn(propName = "parent", queryOper = "isNull")
    private Boolean parentIsNull;

    @QueryColumn(propName = "parent.id", queryOper = "=")
    private Long parentId;

    @QueryColumn
    private String typeNo;

    @QueryColumn
    private String name;

    @QueryColumn
    private Integer level;

    @QueryColumn
    private String headerBit;
}
