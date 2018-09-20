package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.sys.annotation.QueryColumn;
import com.troy.keeper.hbz.type.BizCode;
import lombok.Data;

/**
 * Created by leecheng on 2018/1/31.
 */
@Data
public class HbzPledgeDTO extends BaseDTO {

    @QueryColumn
    private BizCode bizCode;

    @QueryColumn
    private String bizNo;

    @QueryColumn
    private String bondNo;

    @QueryColumn
    private Long bizTime;
}
