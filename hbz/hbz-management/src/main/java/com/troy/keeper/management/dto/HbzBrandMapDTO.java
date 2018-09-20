package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import lombok.Data;

/**
 * Created by leecheng on 2017/12/21.
 */

@Data
public class HbzBrandMapDTO extends HbzBaseMapDTO {

    /**
    @ValueFormat(validations = {
            @Validation(use = "h_brand_n", format = ValidConstants.NOT_NULL, msg = "{fieldName}必须填写"),
            @Validation(use = "h_brand_u", format = ValidConstants.NOT_NULL, msg = "{fieldName}必须填写")
    })
    */
    private String brandNo;

    @ValueFormat(validations = {
            @Validation(use = "h_brand_n", format = ValidConstants.NOT_NULL, msg = "{fieldName}必须填写"),
            @Validation(use = "h_brand_u", format = ValidConstants.NOT_NULL, msg = "{fieldName}必须填写")
    })
    private String headerBit;

    private HbzAreaDTO area;

    public Long areaId;

    @ValueFormat(validations = {
            @Validation(use = "h_brand_n", format = ValidConstants.NOT_NULL, msg = "{fieldName}必须填写"),
            @Validation(use = "h_brand_u", format = ValidConstants.NOT_NULL, msg = "{fieldName}必须填写")
    })
    public String areaCode;

    @ValueFormat(validations = {
            @Validation(use = "h_brand_d", format = ValidConstants.NOT_NULL, msg = "{fieldName}必须填写"),
            @Validation(use = "h_brand_u", format = ValidConstants.NOT_NULL, msg = "{fieldName}必须填写")
    })
    Long id;

    @ValueFormat(validations = {
            @Validation(use = "h_brand_n", format = ValidConstants.NOT_NULL, msg = "{fieldName}必须填写"),
            @Validation(use = "h_brand_u", format = ValidConstants.NOT_NULL, msg = "{fieldName}必须填写")
    })
    private String name;

    private Integer index;

    /**
    @ValueFormat(validations = {
            @Validation(use = "h_brand_n", format = ValidConstants.NOT_NULL, msg = "{fieldName}必须填写"),
            @Validation(use = "h_brand_u", format = ValidConstants.NOT_NULL, msg = "{fieldName}必须填写")
    })
    */
    private String standard;

    /**
    @ValueFormat(validations = {
            @Validation(use = "h_brand_n", format = ValidConstants.NOT_NULL, msg = "{fieldName}必须填写"),
            @Validation(use = "h_brand_u", format = ValidConstants.NOT_NULL, msg = "{fieldName}必须填写")
    })
    */
    private String summary;

    /**
    @ValueFormat(validations = {
            @Validation(use = "h_brand_n", format = ValidConstants.NOT_NULL, msg = "{fieldName}为空"),
            @Validation(use = "h_brand_u", format = ValidConstants.NOT_NULL, msg = "{fieldName}为空")
    })
    */
    private String introduce;

}
