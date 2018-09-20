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
public class HbzAuthDTO extends BaseDTO {

    @ValueFormat(validations = {
            @Validation(use = "auth_merge", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "auth_a", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    @QueryColumn
    private String authName;

    @ValueFormat(validations = {
            @Validation(use = "auth_merge", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "auth_a", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    @QueryColumn
    private String details;

    @QueryColumn(propName = "roles.id")
    private Long roleId;

    @QueryColumn(propName = "urls.id")
    private Long urlId;

    @QueryColumn(propName = "urls.id", queryOper = "in")
    private List<Long> urlIds;

    @QueryColumn
    String comments;

    @QueryColumn
    String state;
}
