package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import com.troy.keeper.hbz.type.Role;
import com.troy.keeper.hbz.type.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

/**
 * Created by leecheng on 2017/10/16.
 */
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HbzUserDTO extends BaseDTO {

    @QueryColumn(queryOper = "like")
    @ValueFormat(validations = {
            @Validation(use = "web_user_add", format = ValidConstants.NOT_NULL, msg = "必须提交用户名"),
            @Validation(use = "platform_user_update", format = ValidConstants.NOT_NULL, msg = "必须提交用户名"),
            @Validation(use = "app_user_registry", format = ValidConstants.NULL, msg = "不能自己指定用户名"),
            @Validation(use = "platform_user_add", format = ValidConstants.NOT_NULL, msg = "用户名不能为空")
    })
    private String login;

    @QueryColumn(queryOper = "like")
    @ValueFormat(validations = {
            @Validation(use = "web_user_add", format = ValidConstants.NOT_NULL, msg = "必须提交用户名"),
            @Validation(use = "platform_user_update", format = ValidConstants.NOT_NULL, msg = "必须提交昵称"),
            @Validation(use = "platform_user_add", format = ValidConstants.NOT_NULL, msg = "昵称不能为空")
    })
    private String nickName;

    @QueryColumn(queryOper = "like", propName = "nickName")
    private String nickNameLike;

    @ValueFormat(validations = {@Validation(use = "app_user_registry", format = ValidConstants.NOT_NULL, msg = "密码不能为空")})
    private String password;

    @QueryColumn(queryOper = "like")
    private String firstName;

    @QueryColumn(queryOper = "like")
    private String lastName;

    @QueryColumn(queryOper = "equal")
    private String email;

    @QueryColumn(queryOper = "equal")
    private Boolean activated;
    @QueryColumn(queryOper = "equal")
    private Integer starLevel;
    @QueryColumn(queryOper = "equal")
    private Integer userStarLevel;
    @QueryColumn(queryOper = "equal")
    @ValueFormat(validations = {@Validation(use = "app_user_registry", format = ValidConstants.NULL, msg = "非法操作")})
    private String langKey;

    @QueryColumn(queryOper = "equal")
    private String imageUrl;

    @QueryColumn(queryOper = "equal")
    @ValueFormat(validations = {@Validation(use = "app_user_registry", format = ValidConstants.NULL, msg = "非法操作")})
    private String activationKey;

    private Integer score;

    private Integer userScore;

    @QueryColumn(queryOper = "equal")
    @ValueFormat(validations = {@Validation(use = "app_user_registry", format = ValidConstants.NULL, msg = "非法操作")})
    private String resetKey;

    @QueryColumn(queryOper = "equal")
    @ValueFormat(validations = {@Validation(use = "app_user_registry", format = ValidConstants.NULL, msg = "非法操作")})
    private Instant resetDate = null;

    @QueryColumn(queryOper = "like")
    @ValueFormat(validations = {
            @Validation(use = "web_user_add", format = ValidConstants.NOT_NULL, msg = "必须提供手机号"),
            @Validation(use = "platform_user_update", format = ValidConstants.NOT_NULL, msg = "必须提交电话"),
            @Validation(use = "platform_user_add", format = ValidConstants.NOT_NULL, msg = "电话不能为空"),
            @Validation(use = "app_user_registry", format = "regex", msg = "电话不正确", conf = "\\d+")
    })
    private String telephone;

    @QueryColumn
    private Sex sex;

    @QueryColumn(propName = "roles.roleName", queryOper = "like")
    private String roleName;

    @QueryColumn(propName = "roles.role")
    private Role role;

    @QueryColumn(propName = "roles.role", queryOper = "In")
    private List<Role> roles;

    private HbzOrgDTO org;

    @ValueFormat(validations = {
            @Validation(use = "web_user_add", format = ValidConstants.NOT_NULL, msg = "用户的组织机构id必须提交")
    })
    @QueryColumn(propName = "org.id")
    private Long orgId;

    private HbzOrgDTO ent;

    @QueryColumn(propName = "ent.id")
    private Long entId;

    @ValueFormat(validations = {
            @Validation(use = "platform_user_role_link", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    @QueryColumn(propName = "roles.id", queryOper = "in")
    private List<Long> roleIds;

    @QueryColumn
    String comments;
}
