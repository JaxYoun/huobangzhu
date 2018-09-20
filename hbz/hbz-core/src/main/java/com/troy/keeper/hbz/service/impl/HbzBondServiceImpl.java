package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.CostStaticsDTO;
import com.troy.keeper.hbz.dto.HbzBondDTO;
import com.troy.keeper.hbz.dto.HbzBondGradeDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.po.HbzBond;
import com.troy.keeper.hbz.repository.CostStaticsRepository;
import com.troy.keeper.hbz.repository.HbzBondRepository;
import com.troy.keeper.hbz.service.HbzBondGradeService;
import com.troy.keeper.hbz.service.HbzBondService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzBondMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leecheng on 2017/12/25.
 */
@Service
@Transactional
public class HbzBondServiceImpl extends BaseEntityServiceImpl<HbzBond, HbzBondDTO> implements HbzBondService {

    @Autowired
    HbzBondMapper hbzBondMapper;

    @Autowired
    HbzBondRepository hbzBondRepository;

    @Autowired
    CostStaticsRepository costStaticsRepository;

    @Autowired
    HbzBondGradeService hbzBondGradeService;

    @Autowired
    private HbzUserService hbzUserService;

    @Override
    public BaseMapper<HbzBond, HbzBondDTO> getMapper() {
        return hbzBondMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzBondRepository;
    }

    @Override
    public String createBondNo() {
        String no;
        int id = 0;
        while (true) {
            no = "BOND-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "-" + id;
            Long chargeCount = hbzBondRepository.countByBondNo(no);
            if (chargeCount.longValue() == 0L) return no;
            id++;
        }
    }

    /**
     * 费用支出统计
     */
    @Override
    public Map<String,Double> cost(CostStaticsDTO costStaticsDTO) {
        Map<String,Double> result = new HashMap<>();
        List<Object[]> lists = costStaticsRepository.queryBondCostStatistics(hbzUserService.currentUser().getId(),costStaticsDTO);
        for (Object[] objects:lists){
            result.put(objects[0].toString()+"_"+objects[1].toString(),(Double)objects[2]);
        }
        return result;
    }

    /**
     * 费用收入统计
     * @param costStaticsDTO
     * @return
     */
    @Override
    public Map<String, Double> income(CostStaticsDTO costStaticsDTO) {
        Map<String,Double> result = new HashMap<>();
        List<Object[]> lists = costStaticsRepository.queryBondIncomeStatistics(hbzUserService.currentUser().getId(),costStaticsDTO);
        for (Object[] objects:lists){
            result.put(objects[0].toString()+"_"+objects[1].toString(),(Double)objects[2]);
        }
        return result;
    }

    @Override
    public List<HbzBondDTO> findByAvailableUserBondGrade(HbzUserDTO user, String bondType, String grade) {
        HbzBondGradeDTO bondGradeDTO = new HbzBondGradeDTO();
        bondGradeDTO.setStatus("1");
        bondGradeDTO.setBondType(bondType);
        bondGradeDTO.setGrade(grade);
        List<HbzBondGradeDTO> bondGrades = hbzBondGradeService.query(bondGradeDTO);
        if (bondGrades.size() < 1) {
            throw new IllegalStateException("该保证金项目[" + bondType + "," + grade + "]不存在");
        } else if (bondGrades.size() > 1) {
            throw new IllegalStateException("存在多个保证金项目[" + bondType + "," + grade + "]");
        } else {
            HbzBondGradeDTO bondGrade = bondGrades.get(0);
            HbzBondDTO bondQuery = new HbzBondDTO();
            bondQuery.setStatus("1");
            bondQuery.setBondGradeId(bondGrade.getId());
            bondQuery.setUserId(user.getId());
            bondQuery.setBondStatus(1);
            return query(bondQuery);
        }
    }
}
