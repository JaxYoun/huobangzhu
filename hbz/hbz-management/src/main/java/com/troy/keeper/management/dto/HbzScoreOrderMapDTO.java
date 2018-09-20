package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.HbzProductDTO;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import lombok.Data;

/**
 * Created by leecheng on 2017/12/18.
 */
@Data
public class HbzScoreOrderMapDTO extends HbzBaseMapDTO {

    private Long id;
    private HbzAreaDTO area;
    private Long areaId;
    private String areaCode;
    private Long userId;
    private Integer state;
    private HbzProductDTO product;
    private Long brandId;
    private Long typeId;
    private String productNo;
    private String productName;
    @ValueFormat(validations = {
            @Validation(use = "h_score_order_c", format = ValidConstants.NOT_NULL, msg = "商品id不能为空")
    })
    private Long productId;

    String orderNo;
    Long scoreTime;

    @ValueFormat(validations = {
            @Validation(use = "h_score_order_c", format = ValidConstants.NOT_NULL, msg = "配送地址不能为空")
    })
    private String destAddr;

    @ValueFormat(validations = {
            @Validation(use = "h_score_order_c", format = ValidConstants.NOT_NULL, msg = "联系人不能为空")
    })
    private String link;

    @ValueFormat(validations = {
            @Validation(use = "h_score_order_c", format = ValidConstants.NOT_NULL, msg = "联系方法不能为空")
    })
    private String telephone;

}
