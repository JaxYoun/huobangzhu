package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.type.BusinessType;
import com.troy.keeper.hbz.type.PayProgress;
import com.troy.keeper.hbz.type.PayType;
import lombok.Data;

import java.util.List;

/**
 * Created by leecheng on 2018/3/12.
 */

@Data
public class HbzPayMapDTO extends HbzBaseMapDTO {

    private Long id;

    private String createdDate;
    private String createdDateLT;
    private String createdDateLE;
    private String createdDateGT;
    private String createdDateGE;

    private BusinessType businessType;

    private String businessNo;

    private String tradeNo;

    private Double fee;

    private String createdNo;

    private PayType payType;

    private PayProgress payProgress;

    private List<PayProgress> payProgresses;

    private String details;

    private String response;

    private Integer bill;

}
