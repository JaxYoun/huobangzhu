package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import lombok.Data;

/**
 * Created by leecheng on 2018/1/16.
 */
@Data
public class SmOrgDTO extends BaseDTO {

    // 父组织机构ID
    @QueryColumn(propName = "pId")
    private Long pId;

    // 层级关系标识
    @QueryColumn
    private String relationship;

    // 名称
    @QueryColumn(queryOper = "like")
    private String orgName;

    // 顺序
    @QueryColumn
    private Long orderCode;

}
