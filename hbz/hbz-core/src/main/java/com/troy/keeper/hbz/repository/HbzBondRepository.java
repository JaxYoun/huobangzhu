package com.troy.keeper.hbz.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.po.HbzBond;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by leecheng on 2017/12/25.
 */
public interface HbzBondRepository extends BaseRepository<HbzBond, Long> {

    Long countByBondNo(String bondNo);

    ///**
    // * 费用支出统计查询
    // */
    //@Query("SELECT hbg.bondType,hbg.grade,sum(hb.amount) FROM HbzBond hb INNER JOIN HbzBondGrade hbg ON hbg.id = hb.bondGrade.id where ((hbg.bondType='BOND_FSL' and hbg.grade = 'C500') or (hbg.bondType='BOND_LTL' and hbg.grade = 'C200') or (hbg.bondType='BOND_SL' and hbg.grade = 'EC2000')) and hb.user.id=?1 and (hb.bondStatus = 1 or hb.bondStatus = 2) group by hbg.bondType,hbg.grade,hbg.name")
    //List<Object[]> queryCostStatistics(Long currentId);
}
