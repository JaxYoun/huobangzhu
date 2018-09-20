package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzRecommendProductDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzRecommendProduct;
import com.troy.keeper.hbz.repository.HbzProductRepo;
import com.troy.keeper.hbz.repository.HbzRecommendProductRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/12/15.
 */
@Component
public class HbzRecommendProductMapper extends BaseMapper<HbzRecommendProduct, HbzRecommendProductDTO> {

    @Autowired
    HbzRecommendProductRepository hbzRecommendProductRepository;

    @Autowired
    HbzProductRepo hbzProductRepo;

    @Autowired
    HbzProductMapper hbzProductMapper;

    @Override
    public HbzRecommendProduct newEntity() {
        return new HbzRecommendProduct();
    }

    @Override
    public HbzRecommendProductDTO newDTO() {
        return new HbzRecommendProductDTO();
    }

    @Override
    public HbzRecommendProduct find(Long id) {
        return hbzRecommendProductRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzRecommendProduct entity, HbzRecommendProductDTO dto) {
        new Bean2Bean().addExcludeProp("product").copyProperties(entity, dto);
        if (entity.getProduct() != null) {
            dto.setProduct(hbzProductMapper.map(entity.getProduct()));
            dto.setProductId(entity.getProduct().getId());
        }
    }

    @Override
    public void dto2entity(HbzRecommendProductDTO dto, HbzRecommendProduct entity) {
        new Bean2Bean().addExcludeProp(StringHelper.conact(Const.ID_AUDIT_FIELDS, "product")).copyProperties(dto, entity);
        if (dto.getProductId() != null)
            entity.setProduct(hbzProductRepo.findOne(dto.getProductId()));
        else if (dto.getProduct() != null && dto.getProduct().getId() != null)
            entity.setProduct(hbzProductRepo.findOne(dto.getProduct().getId()));
        else entity.setProduct(null);
    }
}
