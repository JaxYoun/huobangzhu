package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import lombok.Data;

/**
 * Created by leecheng on 2017/12/26.
 */
@Data
public class HbzBondGradeDTO extends BaseDTO {

    //保证金类型
    @QueryColumn
    private String bondType;

    //档次编码
    @QueryColumn
    private String grade;

    //序号
    @QueryColumn
    private Integer index;

    //保证金名称
    @QueryColumn
    private String name;

    @QueryColumn
    private Integer multi;

    //保证金金额
    @QueryColumn
    private Double total;

}
