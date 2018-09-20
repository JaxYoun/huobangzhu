package com.troy.keeper.hbz.service.mapper.web;

import com.troy.keeper.hbz.po.HbzArea;
import com.troy.keeper.hbz.type.OrderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NaiveWebFslDTO {

    /*private String status;

    private String orderNo;

    private String orderType;*/




    private String commodityName;

    private Double commodityWeight;

    private Double commodityVolume;

//    private Long orderTakeStart;

    private String commodityDescribe;


    private String transType;

    private Double maxLoad;

    private Double unitPrice;

    private Double amount;


//    private String originArea;

    private String originAddress;

//    private HbzArea destArea;

    private String destAddress;


    private String linkMan;

    private String linkTelephone;

    private String linkRemark;
}
