package com.troy.keeper.hbz.app.dto;

import com.troy.keeper.hbz.dto.HbzOrderDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by leecheng on 2017/11/21.
 */
@Getter
@Setter
public class HbzTenderMapDTO {

    private Long id;

    private HbzOrderDTO order;
    private Long orderId;

    private Integer need;

    private Double registryMoney;

    private Double bond;

    private Integer starLevel;

}
