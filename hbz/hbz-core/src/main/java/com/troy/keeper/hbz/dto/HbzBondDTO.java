package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import lombok.Data;

import java.util.List;

/**
 * Created by leecheng on 2017/12/25.
 */
@Data
public class HbzBondDTO extends BaseDTO {

    //保证金编号
    @QueryColumn
    private String bondNo;

    //用户
    private HbzUserDTO user;

    @QueryColumn(propName = "user.id")
    private Long userId;

    //保证金级别
    private HbzBondGradeDTO bondGrade;

    @QueryColumn(propName = "bondGrade.id")
    private Long bondGradeId;

    //保证交纳金额
    @QueryColumn()
    private Double amount;

    //0-未交，1-已交，2-已冻结
    @QueryColumn(propName = "bondStatus")
    private Integer bondStatus;

    @QueryColumn(propName = "bondStatus", queryOper = "in")
    private List<Integer> bondStatuses;

    //查询
    //保证金类型
    @QueryColumn(propName = "bondGrade.bondType")
    private String bondType;

    //档次编码
    @QueryColumn(propName = "bondGrade.grade")
    private String grade;
}
