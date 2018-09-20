package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by leecheng on 2017/12/4.
 */
@Getter
@Setter
public class HbzFormulaDTO extends BaseDTO {

    @ValueFormat(validations = {
            @Validation(use = "formula_add", format = ValidConstants.NULL, msg = "编号[{fieldName}]不能手动设置")
    })
    @QueryColumn
    private String formulaNo;

    @ValueFormat(validations = {
            @Validation(use = "formula_add", format = ValidConstants.NOT_NULL, msg = "公式类型[{fieldName}]为空")
    })
    @QueryColumn
    private String formulaType;

    @ValueFormat(validations = {
            @Validation(use = "formula_add", format = ValidConstants.NOT_NULL, msg = "适用业务[{fieldName}]为空")
    })
    @QueryColumn
    private String adjustType;

    @QueryColumn
    private String formulaState;

    @ValueFormat(validations = {
            @Validation(use = "formula_add", format = ValidConstants.NOT_NULL, msg = "名称[{fieldName}]为空")
    })
    @QueryColumn
    private String name;

    @ValueFormat(validations = {
            @Validation(use = "formula_add", format = ValidConstants.NOT_NULL, msg = "公式引用key[{fieldName}]为空")
    })
    @QueryColumn
    private String formulaKey;

    @ValueFormat(validations = {
            @Validation(use = "formula_add", format = ValidConstants.NOT_NULL, msg = "公式说明[{fieldName}]为空")
    })
    @QueryColumn
    private String formulaDesc;

    @ValueFormat(validations = {
            @Validation(use = "formula_add", format = ValidConstants.NOT_NULL, msg = "公式原型[{fieldName}]为空")
    })
    @QueryColumn
    private String formula;

    @QueryColumn
    private String formulaImage;

    @QueryColumn
    private String comments;
}
