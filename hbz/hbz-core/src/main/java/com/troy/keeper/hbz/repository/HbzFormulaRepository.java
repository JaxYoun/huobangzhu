package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzFormula;

/**
 * Created by leecheng on 2017/12/4.
 */
public interface HbzFormulaRepository extends BaseRepository<HbzFormula, Long> {

    HbzFormula findByFormulaKey(String formulaKey);

}
