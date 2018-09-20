package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzProductDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzProduct;
import com.troy.keeper.hbz.repository.HbzProductRepo;
import com.troy.keeper.hbz.service.HbzProductService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2017/12/29.
 */
@Service
@Transactional
public class HbzProductServiceImpl extends BaseEntityServiceImpl<HbzProduct, HbzProductDTO> implements HbzProductService {

    @Override
    public String createProductNo() {
        String productNo;
        int i = 0;
        while (true) {
            productNo = StringHelper.frontCompWithZore(++i, 6);
            if (hbzProductRepo.countAllByProductNo(productNo) < 1L) return productNo;
        }
    }

    @Autowired
    HbzProductRepo hbzProductRepo;

    @Autowired
    HbzProductMapper hbzProductMapper;

    @Override
    public BaseMapper<HbzProduct, HbzProductDTO> getMapper() {
        return hbzProductMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzProductRepo;
    }
}
