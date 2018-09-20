package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.po.HbzWeightSection;
import com.troy.keeper.hbz.repository.HbzWeightSectionRepository;
import com.troy.keeper.hbz.service.HbzWeightSectionService;
import com.troy.keeper.hbz.type.CommodityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Iterator;

/**
 * Created by leecheng on 2017/12/6.
 */
@Service
@Transactional
public class HbzWeightSectionServiceImpl implements HbzWeightSectionService {

    @Autowired
    HbzWeightSectionRepository hbzWeightSectionRepository;

    @Override
    public String getFormula(CommodityClass commodityClass, Double weight) {
        Iterable<HbzWeightSection> sections = hbzWeightSectionRepository.findAll();
        Iterator<HbzWeightSection> sectionIterator = sections.iterator();
        HbzWeightSection weightSection = null;
        while (sectionIterator.hasNext()) {
            HbzWeightSection section = sectionIterator.next();
            boolean isPass = true;
            isPass = isPass && section.getCommodityClass() == commodityClass;
            if (section.getLeftBoundary() != null) {
                isPass = isPass && weight > section.getLeftBoundary();
            }
            if (section.getRightBoundary() != null) {
                isPass = isPass && weight <= section.getRightBoundary();
            }
            if (isPass) {
                weightSection = section;
                break;
            }
        }
        String formulaKey = Const.NULL();
        if (weightSection != null) {
            formulaKey = weightSection.getFormulaKey();
        }
        return formulaKey;
    }
}
