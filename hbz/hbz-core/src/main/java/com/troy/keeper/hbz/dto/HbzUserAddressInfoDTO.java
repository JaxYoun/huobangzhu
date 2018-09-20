package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import lombok.Data;
import lombok.Setter;

/**
 * Created by leecheng on 2017/10/27.
 */
@Data
@Setter
public class HbzUserAddressInfoDTO extends BaseDTO {

    private HbzUserDTO user;

    @QueryColumn(propName = "user.id")
    @ValueFormat(validations = {
            @Validation(use = "user_add_update", format = ValidConstants.NULL, msg = "不能指定用户id"),
            @Validation(use = "user_add_create", format = ValidConstants.NULL, msg = "不能指定用户id")
    })
    private Long userId;

    private HbzAreaDTO area;

    @QueryColumn(propName = "area.id")
    private Long areaId;

    @QueryColumn(propName = "area.outCode")
    private String areaCode;

    @QueryColumn
    private Integer index;

    @QueryColumn
    @ValueFormat(validations = {
            @Validation(use = "user_add_update", format = ValidConstants.NOT_NULL, msg = "收发货{fieldName}类型不能为空不能为空"),
            @Validation(use = "user_add_create", format = ValidConstants.NOT_NULL, msg = "收发货{fieldName}类型不能为空不能为空")})
    private Integer type;

    @QueryColumn
    @ValueFormat(validations = {@Validation(use = "user_add_update", format = ValidConstants.NOT_NULL, msg = "联系人{fieldName}不能为空"),
            @Validation(use = "user_add_create", format = ValidConstants.NOT_NULL, msg = "联系人{fieldName}不能为空")})
    private String linker;

    @QueryColumn
    @ValueFormat(validations = {
            @Validation(use = "user_add_update", format = ValidConstants.NOT_NULL, msg = "联系{fieldName}电话不能为空"),
            @Validation(use = "user_add_create", format = ValidConstants.NOT_NULL, msg = "联系{fieldName}电话不能为空")})
    private String telephone;

    @QueryColumn
    private Integer idefault;

    @QueryColumn
    private Double destX;

    @QueryColumn
    private Double destY;

    @ValueFormat(validations = {
            @Validation(use = "user_add_update", format = ValidConstants.NOT_NULL, msg = "细地址{fieldName}不能为空"),
            @Validation(use = "user_add_create", format = ValidConstants.NOT_NULL, msg = "细地址{fieldName}不能为空")})
    @QueryColumn
    private String destAddress;

    @QueryColumn(propName = "destAddress", queryOper = "like")
    private String destAddressLike;

}
