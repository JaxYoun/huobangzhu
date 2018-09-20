package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.dto.HbzProductDTO;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import lombok.Data;

import java.util.List;

/**
 * Created by leecheng on 2017/12/15.
 */
@Data
public class HbzRecommendProductMapDTO extends HbzBaseMapDTO {

    private String recommendName;

    @ValueFormat(validations = {
            @Validation(use = "recommend_product_d", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "recommend_product_att", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "recommend_product_upd", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private Long id;

    HbzProductDTO product;

    @ValueFormat(validations = {
            @Validation(use = "recommend_product_upd", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "recommend_product_cr", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    Long productId;

    @ValueFormat(validations = {
            @Validation(use = "recommend_product_upd", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空"),
            @Validation(use = "recommend_product_cr", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private String useType;
    private Integer state;

    private Double score;
    private Double scoreLT;
    private Double scoreLE;
    private Double scoreGT;
    private Double scoreGE;

    Integer index;

    @ValueFormat(validations = {
            @Validation(use = "recommend_product_upd", format = ValidConstants.NOT_NULL, msg = "内容摘要{fieldName}不能为空"),
            @Validation(use = "recommend_product_cr", format = ValidConstants.NOT_NULL, msg = "内容摘要{fieldName}不能为空")
    })
    String summary;
    private String headerBit;
    @ValueFormat(validations = {
            @Validation(use = "recommend_product_upd", format = ValidConstants.NOT_NULL, msg = "内容{fieldName}不能为空"),
            @Validation(use = "recommend_product_cr", format = ValidConstants.NOT_NULL, msg = "内容{fieldName}不能为空")
    })
    String introduce;

    /**
     * 查询项
     */
    private String wareName;

    private Long typeId;

    private List<Long> typeIds;

    private Long brandId;

    private String specifications;

    private List<String> specificationses;

    private String productSummary;

    private String productIntroduction;

    private String productName;

    private String productNo;
}