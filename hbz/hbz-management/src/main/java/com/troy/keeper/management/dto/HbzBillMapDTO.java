package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.type.BillType;
import com.troy.keeper.hbz.type.PayType;
import lombok.Data;

/**
 * Created by leecheng on 2018/3/8.
 */
@Data
public class HbzBillMapDTO extends HbzBaseMapDTO {

    private Long id;

    private String tradeNo;
    private BillType billType;
    private String requestNo;
    private String date;
    private String dateLT;
    private String dateLE;
    private String dateGE;
    private String dateGT;
    private String billContent;
    private PayType payType;
}
