package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.hbz.dto.HbzTypeValDTO;
import com.troy.keeper.hbz.po.HbzTypeVal;
import com.troy.keeper.hbz.repository.HbzTypeValRepo;
import com.troy.keeper.hbz.service.HbzTypeValService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/11/22.
 */
@Service
@Transactional
public class HbzTypeValServiceImpl implements HbzTypeValService {

    @Autowired
    HbzTypeValRepo typeValRepo;

    @Override
    public List<HbzTypeValDTO> getByTypeAndLanguage(String type, String language) {
        return typeValRepo.getAllByTypeAndLanguage(type, language).stream().map(typeVal -> {
            HbzTypeValDTO tv = new HbzTypeValDTO();
            BeanUtils.copyProperties(typeVal, tv);
            return tv;
        }).collect(Collectors.toList());
    }

    @Override
    public HbzTypeValDTO getByTypeAndValAndLanguage(String type, String val, String language) {
        HbzTypeVal typeV = typeValRepo.getByTypeAndValAndLanguage(type, val, language);
        if (typeV == null)
            return null;
        HbzTypeValDTO tv = new HbzTypeValDTO();
        BeanUtils.copyProperties(typeV, tv);
        return tv;
    }
}
