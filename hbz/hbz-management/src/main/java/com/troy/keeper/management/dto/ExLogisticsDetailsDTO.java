package com.troy.keeper.management.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 李奥
 * @date 2017/12/21.
 */
@Getter
@Setter
public class ExLogisticsDetailsDTO {

    //快递记录表id
    private Long id;

    //货物描述
    private String commodityDesc;

    //发件时间
    private String sendTime;

   //最后的前段展示 两个合并的字段
    private  String Information;

}
