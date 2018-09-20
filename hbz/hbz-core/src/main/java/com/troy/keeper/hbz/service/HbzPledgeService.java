package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzBondDTO;
import com.troy.keeper.hbz.dto.HbzPledgeDTO;
import com.troy.keeper.hbz.po.HbzPledge;
import com.troy.keeper.hbz.type.BizCode;

/**
 * Created by leecheng on 2018/1/31.
 */
public interface HbzPledgeService extends BaseEntityService<HbzPledge, HbzPledgeDTO> {

    boolean pledge(HbzBondDTO bond, String bizNo, BizCode bizCode);

    int unPledge(String bizNo, BizCode bizCode);

    int unPledge(String bizNo, BizCode bizCode,String bondType,String grade);
}
