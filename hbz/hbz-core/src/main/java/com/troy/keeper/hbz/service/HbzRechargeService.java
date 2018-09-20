package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzRechargeDTO;
import com.troy.keeper.hbz.po.HbzRecharge;

/**
 * Created by leecheng on 2017/12/4.
 */
public interface HbzRechargeService extends BaseEntityService<HbzRecharge, HbzRechargeDTO> {

    int NEW = 1;
    int ERROR = 2;
    int FAILURE = 3;
    int SUCCESS = 4;
    int COMPLETE = 5;

    String makeChargeNo();

    boolean completeRecharge(String out_trade_no);

    HbzRechargeDTO findByChargeNo(String charge);

}
