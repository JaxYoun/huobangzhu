package com.troy.keeper.hbz.app.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.dto.HbzWareTypeDTO;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import lombok.Data;

/**
 * Created by leecheng on 2017/12/13.
 */
@Data
public class HbzWareTypeMapDTO extends HbzBaseMapDTO {

    @ValueFormat(validations = {
            @Validation(use = "wareType_attach", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "wareType_update", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "wareType_delete", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private Long id;
    private Boolean parentIsNull;
    private HbzWareTypeDTO parent;

    private Long parentId;
    private Integer level;
    private String typeNo;
    @ValueFormat(validations = {
            @Validation(use = "wareType_update", format = ValidConstants.NOT_NULL, msg = "名称{fieldName}不能为空"),
            @Validation(use = "wareType_create", format = ValidConstants.NOT_NULL, msg = "名称{fieldName}不能为空")
    })
    private String name;

    @ValueFormat(validations = {
            @Validation(use = "wareType_update", format = ValidConstants.NOT_NULL, msg = "类型图片{fieldName}不能为空"),
            @Validation(use = "wareType_create", format = ValidConstants.NOT_NULL, msg = "类型图片{fieldName}不能为空")
    })
    private String headerBit;

}
