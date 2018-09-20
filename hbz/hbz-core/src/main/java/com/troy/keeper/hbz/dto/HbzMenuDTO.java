package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import com.troy.keeper.hbz.type.RequireAuthEnum;
import com.troy.keeper.hbz.type.WebModule;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by YangJx 1017-11-9
 */
@Data
@EqualsAndHashCode
public class HbzMenuDTO extends BaseDTO {


    private HbzMenuDTO parent;

    /**
     * 父菜单id
     */
    @QueryColumn(propName = "parent.id")
    private Long parentId;

    @QueryColumn(propName = "parent", queryOper = "isnull")
    private String isParentNull;

    /**
     * 菜单名称
     */
    @ValueFormat(validations = {
            @Validation(use = "menu_up", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "menu_ad", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private String name;

    /**
     * 菜单链接地址
     */
    @ValueFormat(validations = {
            @Validation(use = "menu_up", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "menu_ad", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private String url;

    /**
     * 菜单图标链接地址
     */
    @ValueFormat(validations = {
            @Validation(use = "menu_up", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "menu_ad", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private String iconUrl;

    /**
     * 菜单排序号
     */
    private Integer orderCode;

    /**
     * 是否为叶子节点，0：否，1：是
     */
    /**
    @ValueFormat(validations = {
            @Validation(use = "menu_up", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "menu_ad", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    */
    private Integer ifLeaf;

    /**
     * 所属模块编码
     */
    /**
    @ValueFormat(validations = {
            @Validation(use = "menu_up", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "menu_ad", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    */
    private WebModule webModule;

    /**
     * 是否需要认证
     */
    /**
    @ValueFormat(validations = {
            @Validation(use = "menu_up", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "menu_ad", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    */
    private RequireAuthEnum requireAuth;

    @QueryColumn(propName = "hbzRoleList.id")
    private Long roleId;
}
