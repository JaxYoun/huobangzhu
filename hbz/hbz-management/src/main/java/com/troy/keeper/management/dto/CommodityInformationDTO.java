package com.troy.keeper.management.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

/**
 * @author 李奥
 * @date 2017/12/24.
 */
@Getter
@Setter
public class CommodityInformationDTO {
    //ID
    private Long  id;

      //货物编号
    private  String  commodityNumber;

    //货物名称
    private String commodityName;

    //规格
    private  Double specification;

    //包装单位
    private  String packageUnit;
    private String  packageUnitValue;

    //体积
    private Double  volume;

    //重量
    private Double   weight;

    //价格
    private Double price;

    //条码
    private String barcode;

    //货物名称简拼
    private  String  jianpin;

    //状态
    private String dataStatus;

    //计费重量
    private  String  billingWeight;
}
