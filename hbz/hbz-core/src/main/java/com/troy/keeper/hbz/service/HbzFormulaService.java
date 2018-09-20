package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzFormulaDTO;
import com.troy.keeper.hbz.po.HbzFormula;

import java.util.Map;

/**
 * Created by leecheng on 2017/12/4.
 */
public interface HbzFormulaService extends BaseEntityService<HbzFormula, HbzFormulaDTO> {

    Double calculate(String formulaKey, Map<String, Double> variables);

    String createNo();
}
