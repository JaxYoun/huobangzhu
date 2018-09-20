package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzBrandDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzBrand;
import com.troy.keeper.hbz.repository.HbzBrandRepository;
import com.troy.keeper.hbz.service.HbzBrandService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzBrandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2017/12/28.
 */
@Service
@Transactional
public class HbzBrandServiceImpl extends BaseEntityServiceImpl<HbzBrand, HbzBrandDTO> implements HbzBrandService {

    @Autowired
    HbzBrandRepository hbzBrandRepository;

    @Autowired
    HbzBrandMapper hbzBrandMapper;

    @Override
    public BaseMapper<HbzBrand, HbzBrandDTO> getMapper() {
        return hbzBrandMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzBrandRepository;
    }

    @Override
    public String createBrandNo() {
        String no;
        int id = 0;
        while (true) {
            no = StringHelper.frontCompWithZore(id++, 6);
            Long brandCoiunt = hbzBrandRepository.countByBrandNo(no);
            if (brandCoiunt.longValue() == 0L) return no;
        }
    }
}
