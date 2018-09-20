package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.dto.HbzBrandDTO;
import com.troy.keeper.hbz.dto.HbzWareTypeDTO;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import lombok.Data;

import java.util.List;

/**
 * Created by leecheng on 2017/12/13.
 */
@Data
public class HbzWareInfoMapDTO extends HbzBaseMapDTO {

    @ValueFormat(validations = {
            @Validation(use = "wareInfo_attach", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "wareInfo_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "wareInfo_delete", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private Long id;

    private HbzBrandDTO brand;

    @ValueFormat(validations = {
            @Validation(use = "wareInfo_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "wareInfo_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private Long brandId;

    private HbzWareTypeDTO type;

    @ValueFormat(validations = {
            @Validation(use = "wareInfo_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "wareInfo_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private Long typeId;

    private String wareNo;

    @ValueFormat(validations = {
            @Validation(use = "wareInfo_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "wareInfo_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private String name;
    private Double marketAmount;
    private Double amount;
    private Double amountLT;
    private Double amountLE;
    private Double amountGE;
    private Double amountGT;

    private Double score;
    private Double scoreLT;
    private Double scoreLE;
    private Double scoreGT;
    private Double scoreGE;

    @ValueFormat(validations = {
            @Validation(use = "wareInfo_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "wareInfo_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private Integer state;

    @ValueFormat(validations = {
            @Validation(use = "wareInfo_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "wareInfo_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private String header;

    @ValueFormat(validations = {
            @Validation(use = "wareInfo_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "wareInfo_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private String img;

    @ValueFormat(validations = {
            @Validation(use = "wareInfo_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "wareInfo_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private String specifications;
    private List<String> specificationses;

    @ValueFormat(validations = {
            @Validation(use = "wareInfo_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "wareInfo_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private String summary;

    @ValueFormat(validations = {
            @Validation(use = "wareInfo_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "wareInfo_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private String introduction;

    @ValueFormat(validations = {
            @Validation(use = "wareInfo_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "wareInfo_create", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private String productionAddress;

}
