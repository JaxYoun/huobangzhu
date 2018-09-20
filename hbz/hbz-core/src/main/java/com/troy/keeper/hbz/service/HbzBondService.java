package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.CostStaticsDTO;
import com.troy.keeper.hbz.dto.HbzBondDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.po.HbzBond;

import java.util.List;
import java.util.Map;

/**
 * Created by leecheng on 2017/12/25.
 */
public interface HbzBondService extends BaseEntityService<HbzBond, HbzBondDTO> {

    String createBondNo();

    List<HbzBondDTO> findByAvailableUserBondGrade(HbzUserDTO user, String bondType, String grade);

    //费用支出统计
    Map<String,Double> cost(CostStaticsDTO costStaticsDTO);

    //费用收入统计
    Map<String,Double> income(CostStaticsDTO costStaticsDTO);
}
