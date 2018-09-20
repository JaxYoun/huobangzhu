package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import lombok.Data;

import java.util.List;

/**
 * Created by leecheng on 2017/12/14.
 */
@Data
public class HbzProductDTO extends BaseDTO {

    @QueryColumn()
    private String productName;

    @QueryColumn
    private String productNo;

    @QueryColumn
    //0-停用1-启用
    private Integer state;

    @QueryColumn
    private String header;

    @QueryColumn
    private String img;

    @QueryColumn
    private Integer productStatus;

    private HbzWareInfoDTO ware;

    @QueryColumn(propName = "ware.id")
    private Long wareId;

    @QueryColumn
    private Integer index;

    @QueryColumn
    private Double score;
    @QueryColumn(propName = "score", queryOper = "LT")
    private Double scoreLT;
    @QueryColumn(propName = "score", queryOper = "LE")
    private Double scoreLE;
    @QueryColumn(propName = "score", queryOper = "GT")
    private Double scoreGT;
    @QueryColumn(propName = "score", queryOper = "GE")
    private Double scoreGE;

    private Double amount;

    @QueryColumn
    private Integer total;

    @QueryColumn
    private Integer leave;

    @QueryColumn
    private Integer recommend;

    private String message;

    private String productionAddress;

    private String summary;

    private String content;
    /**
     * 查询项
     */
    @QueryColumn(propName = "ware.wareNo")
    private String wareNo;

    @QueryColumn(propName = "ware.name")
    private String wareName;

    @QueryColumn(propName = "ware.type.id")
    private Long typeId;

    @QueryColumn(propName = "ware.type.id", queryOper = "in")
    private List<Long> typeIds;

    @QueryColumn(propName = "ware.brand.id")
    private Long brandId;

    @QueryColumn(propName = "ware.specifications")
    private String specifications;

    @QueryColumn(propName = "ware.specifications", queryOper = "in")
    private List<String> specificationses;

    @QueryColumn(propName = "ware.summary", queryOper = "like")
    private String wareSummary;

    @QueryColumn(propName = "ware.introduction", queryOper = "like")
    private String introduction;

}
