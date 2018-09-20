package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import com.troy.keeper.hbz.type.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by leecheng on 2017/10/16.
 */
@Getter
@Setter
public class HbzRoleDTO extends BaseDTO {

    @ValueFormat(validations = {
            @Validation(use = "role_merge", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "role_add", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    @QueryColumn
    private Role role;

    @QueryColumn(propName = "role", queryOper = "in")
    private List<Role> roles;

    @ValueFormat(validations = {
            @Validation(use = "role_merge", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "role_add", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    @QueryColumn(queryOper = "like")
    private String roleName;

    @QueryColumn
    String state;

    @QueryColumn
    String comments;

    @QueryColumn(propName = "users.id")
    private Long userId;

    @QueryColumn(propName = "users.ent.id")
    private Long userEntId;

    @QueryColumn(propName = "auths.id")
    private Long authId;

    @ValueFormat(validations = {
            @Validation(use = "role_auth", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    @QueryColumn(propName = "auths.id", queryOper = "in")
    private List<Long> authIds;

    @ValueFormat(validations = {
            @Validation(use = "role_menu", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    @QueryColumn(propName = "menus.id", queryOper = "in")
    private List<Long> menuIds;

}
