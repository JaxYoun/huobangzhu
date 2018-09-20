package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import lombok.Data;

import java.util.List;

/**
 * Created by leecheng on 2017/12/15.
 */
@Data
public class HbzRecommendProductDTO extends BaseDTO {

    @QueryColumn
    private String recommendName;

    HbzProductDTO product;

    @QueryColumn(propName = "product.id")
    Long productId;

    @QueryColumn
    private String useType;

    @QueryColumn
    private Integer state;

    @QueryColumn(propName = "index")
    Integer index;

    @QueryColumn(propName = "summary", queryOper = "like")
    String summary;

    private String headerBit;

    @QueryColumn(queryOper = "like")
    String introduce;

    /**
     * 查询项
     */
    @QueryColumn(propName = "product.ware.name")
    private String wareName;

    @QueryColumn(propName = "product.ware.type.id")
    private Long typeId;

    @QueryColumn(propName = "product.ware.type.id", queryOper = "in")
    private List<Long> typeIds;

    @QueryColumn(propName = "product.ware.brand.id")
    private Long brandId;

    @QueryColumn(propName = "product.ware.specifications")
    private String specifications;

    @QueryColumn(propName = "product.ware.specifications", queryOper = "in")
    private List<String> specificationses;

    @QueryColumn(propName = "product.ware.summary", queryOper = "like")
    private String productSummary;

    @QueryColumn(propName = "product.ware.introduction", queryOper = "like")
    private String productIntroduction;

    @QueryColumn(propName = "product.productName", queryOper = "like")
    private String productName;

    @QueryColumn(propName = "product.productNo")
    private String productNo;

    @QueryColumn
    private Double score;
    @QueryColumn(propName = "product.score", queryOper = "LT")
    private Double scoreLT;
    @QueryColumn(propName = "product.score", queryOper = "LE")
    private Double scoreLE;
    @QueryColumn(propName = "product.score", queryOper = "GT")
    private Double scoreGT;
    @QueryColumn(propName = "product.score", queryOper = "GE")
    private Double scoreGE;
}
