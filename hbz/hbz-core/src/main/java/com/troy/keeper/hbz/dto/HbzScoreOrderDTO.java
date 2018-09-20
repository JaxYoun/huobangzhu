package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import lombok.Data;

/**
 * Created by leecheng on 2017/12/18.
 */
@Data
public class HbzScoreOrderDTO extends BaseDTO {

    private HbzAreaDTO area;
    @QueryColumn(propName = "area.id")
    private Long areaId;
    @QueryColumn(propName = "area.outCode")
    private String areaCode;

    private HbzUserDTO user;

    @QueryColumn(propName = "user.id")
    private Long userId;

    @QueryColumn(propName = "product.productNo")
    private String productNo;

    @QueryColumn(propName = "product.productName")
    private String productName;

    @QueryColumn(propName = "product.ware.brand.id")
    private Long brandId;

    @QueryColumn(propName = "product.ware.type.id")
    private Long typeId;

    private HbzProductDTO product;

    @QueryColumn
    String orderNo;

    @QueryColumn
    Long scoreTime;

    @QueryColumn(propName = "product.id")
    private Long productId;

    @QueryColumn(propName = "destAddr", queryOper = "like")
    private String destAddr;

    @QueryColumn(propName = "link")
    private String link;

    @QueryColumn(propName = "telephone", queryOper = "=")
    private String telephone;

    @QueryColumn
    private Integer state;

}
