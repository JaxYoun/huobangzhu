package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import lombok.*;

/**
 * Created by leecheng on 2017/11/8.
 */
@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
public class HbzAreaDTO extends BaseDTO {

    @QueryColumn
    private String outCode;

    @QueryColumn(propName = "outCode", queryOper = "LIKE")
    private String outCodeLIKE;

    @QueryColumn(propName = "parent.outCode")
    private String parentCode;

    @QueryColumn
    private String areaName;
    @QueryColumn
    private String areaCode;
    @QueryColumn
    private String jianPin;
    @QueryColumn
    private String pinYin;
    @QueryColumn
    private Integer level;
    @QueryColumn
    private Integer index;

    private HbzAreaDTO parent;
    @QueryColumn(propName = "parent.id")
    private Long parentId;

}
