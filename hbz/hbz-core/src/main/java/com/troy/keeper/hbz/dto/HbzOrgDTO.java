package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import com.troy.keeper.hbz.type.OrgType;
import lombok.Data;

/**
 * Created by leecheng on 2017/10/30.
 */
@Data
public class HbzOrgDTO extends BaseDTO {

    @QueryColumn(propName = "parent", queryOper = "isNull")
    private String isParentNull;

    @ValueFormat(validations = {
            @Validation(use = "create", format = ValidConstants.NOT_NULL, msg = "组织机构类型提供")
    })
    @QueryColumn
    private OrgType orgType;

    @ValueFormat(validations = {
            @Validation(use = "org_add", format = ValidConstants.NOT_NULL, msg = "组织机构名称不能为空"),
            @Validation(use = "create", format = ValidConstants.NOT_NULL, msg = "组织机构名称不能为空"),
            @Validation(use = "update", format = ValidConstants.NOT_NULL, msg = "组织机构名称不能为空")
    })
    @QueryColumn
    private String organizationName;

    private HbzOrgDTO parent;

    @QueryColumn(propName = "parent.id", queryOper = "equal")
    private Long parentId;

    private String address;

    private String linkMan;

    private String telephone;

    private String description;

}
