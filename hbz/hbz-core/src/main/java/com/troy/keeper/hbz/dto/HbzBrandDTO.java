package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import lombok.Data;

/**
 * Created by leecheng on 2017/12/13.
 */
@Data
public class HbzBrandDTO extends BaseDTO {

    @QueryColumn
    private String brandNo;

    @QueryColumn
    private String headerBit;

    private HbzAreaDTO area;

    @QueryColumn(propName = "area.id")
    public Long areaId;

    @QueryColumn(propName = "area.outCode")
    public String areaCode;

    @QueryColumn
    private String name;

    @QueryColumn
    private Integer index;

    @QueryColumn
    private String standard;

    @QueryColumn
    private String summary;

    @QueryColumn
    private String introduce;

}
