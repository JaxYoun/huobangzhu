package com.troy.keeper.hbz.app.dto;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.dto.HbzWareInfoDTO;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;
import lombok.Data;

import java.util.List;

/**
 * Created by leecheng on 2017/12/14.
 */
@Data
public class HbzProductMAPEDDTO extends HbzBaseMapDTO {

    @ValueFormat(validations = {
            @Validation(use = "product_cr", format = ValidConstants.NOT_NULL, msg = "商品id{fieldName}不能为空")
    })
    private String productName;

    private String productNo;

    //0-停用1-启用
    @ValueFormat(validations = {
            @Validation(use = "product_cr", format = ValidConstants.NOT_NULL, msg = "商品id{fieldName}不能为空")
    })
    private Integer state;

    @ValueFormat(validations = {
            @Validation(use = "product_cr", format = ValidConstants.NOT_NULL, msg = "商品id{fieldName}不能为空")
    })
    private String header;

    @ValueFormat(validations = {
            @Validation(use = "product_cr", format = ValidConstants.NOT_NULL, msg = "商品id{fieldName}不能为空")
    })
    private String img;

    private Integer productStatus;

    @ValueFormat(validations = {
            @Validation(use = "product_order", format = ValidConstants.NOT_NULL, msg = "商品{fieldName}不能为空"),
            @Validation(use = "product_a", format = ValidConstants.NOT_NULL, msg = "商品{fieldName}不能为空"),
            @Validation(use = "product_de", format = ValidConstants.NOT_NULL, msg = "商品{fieldName}不能为空")
    })
    private Long id;

    private HbzWareInfoDTO ware;

    @ValueFormat(validations = {
            @Validation(use = "product_cr", format = ValidConstants.NOT_NULL, msg = "商品id{fieldName}不能为空")
    })
    private Long wareId;

    private Integer index;

    @ValueFormat(validations = {
            @Validation(use = "product_cr", format = ValidConstants.NOT_NULL, msg = "积分{fieldName}不能为空")
    })
    private Double score;
    private Double scoreLT;
    private Double scoreLE;
    private Double scoreGT;
    private Double scoreGE;

    private Double amount;

    @ValueFormat(validations = {
            @Validation(use = "product_cr", format = ValidConstants.NOT_NULL, msg = "上架数量{fieldName}不能为空")
    })
    private Integer total;

    private Integer leave;

    private Integer recommend;
    private String message;
    private String productionAddress;
    @ValueFormat(validations = {
            @Validation(use = "product_cr", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private String summary;
    @ValueFormat(validations = {
            @Validation(use = "product_cr", format = ValidConstants.NOT_NULL, msg = "{fieldName}不能为空")
    })
    private String content;

    /***
     * 以下均为查询项
     */
    private String wareNo;
    private String wareName;
    private Long typeId;
    private List<Long> typeIds;
    private Long brandId;
    private String specifications;
    private List<String> specificationses;
    private String wareSummary;
    private String introduction;
}
