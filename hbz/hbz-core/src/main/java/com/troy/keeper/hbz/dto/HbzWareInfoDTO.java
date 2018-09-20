package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import lombok.Data;

import java.util.List;

/**
 * Created by leecheng on 2017/12/12.
 */
@Data
public class HbzWareInfoDTO extends BaseDTO {

    private HbzWareTypeDTO type;

    @QueryColumn(propName = "type.id")
    private Long typeId;

    @QueryColumn(propName = "type.id", queryOper = "in")
    private List<Long> typeIds;

    @QueryColumn(propName = "name", queryOper = "like")
    private String name;

    @QueryColumn
    private String wareNo;
    private HbzBrandDTO brand;
    @QueryColumn
    private Integer state;
    @QueryColumn(propName = "brand.id")
    private Long brandId;
    private Double marketAmount;
    @QueryColumn
    private Double amount;
    @QueryColumn(propName = "amount", queryOper = "LT")
    private Double amountLT;
    @QueryColumn(propName = "amount", queryOper = "LE")
    private Double amountLE;
    @QueryColumn(propName = "amount", queryOper = "GE")
    private Double amountGE;
    @QueryColumn(propName = "amount", queryOper = "GT")
    private Double amountGT;

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

    @QueryColumn
    private String header;

    @QueryColumn
    private String img;

    @QueryColumn
    private String specifications;

    @QueryColumn(propName = "specifications", queryOper = "in")
    private List<String> specificationses;

    @QueryColumn
    private String summary;

    @QueryColumn
    private String introduction;

    @QueryColumn
    private String productionAddress;

}
