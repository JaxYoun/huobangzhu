package com.troy.keeper.hbz.service.impl;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.hbz.dto.HbzRecommendProductDTO;
import com.troy.keeper.hbz.po.HbzRecommendProduct;
import com.troy.keeper.hbz.repository.HbzRecommendProductRepository;
import com.troy.keeper.hbz.service.HbzRecommendProductService;
import com.troy.keeper.hbz.service.mapper.BaseMapper;
import com.troy.keeper.hbz.service.mapper.HbzRecommendProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by leecheng on 2018/1/15.
 */
@Service
@Transactional
public class HbzRecommendProductServiceImpl extends BaseEntityServiceImpl<HbzRecommendProduct, HbzRecommendProductDTO> implements HbzRecommendProductService {

    @Autowired
    HbzRecommendProductMapper hbzRecommendProductMapper;

    @Autowired
    HbzRecommendProductRepository hbzRecommendProductRepository;

    @Override
    public BaseMapper<HbzRecommendProduct, HbzRecommendProductDTO> getMapper() {
        return hbzRecommendProductMapper;
    }

    @Override
    public BaseRepository getRepository() {
        return hbzRecommendProductRepository;
    }
}
