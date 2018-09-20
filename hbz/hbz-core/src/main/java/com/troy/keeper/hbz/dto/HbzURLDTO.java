package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import lombok.Data;

import java.util.List;

/**
 * Created by leecheng on 2018/1/8.
 */
@Data
public class HbzURLDTO extends BaseDTO {

    @ValueFormat(validations = {
            @Validation(use = "u_up", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "u_c", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    @QueryColumn
    String urlLabel;

    @ValueFormat(validations = {
            @Validation(use = "u_up", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "u_c", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    @QueryColumn
    String urlPattern;

    @ValueFormat(validations = {
            @Validation(use = "u_a", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    @QueryColumn(propName = "auths.id", queryOper = "in")
    List<Long> authIds;
    @QueryColumn
    String state;
    @QueryColumn
    private String pack;

    @QueryColumn(propName = "auths.id")
    Long authId;

    @QueryColumn
    String comments;


}
