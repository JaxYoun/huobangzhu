package com.troy.keeper.hbz.app.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import lombok.Data;

/**
 * Created by leecheng on 2017/12/26.
 */
@Data
public class BondDTO {

    @ValueFormat(validations = {
            @Validation(use = "bond_x", format = ValidConstants.NOT_NULL, msg = "业务bondType不能空")
    })
    String bondType;

    @ValueFormat(validations = {
            @Validation(use = "bond_x", format = ValidConstants.NOT_NULL, msg = "档次grade不能空")
    })
    String grade;

}
