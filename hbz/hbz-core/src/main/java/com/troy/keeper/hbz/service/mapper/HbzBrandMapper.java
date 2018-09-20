package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzBrandDTO;
import com.troy.keeper.hbz.po.HbzBrand;
import com.troy.keeper.hbz.repository.HbzAreaRepository;
import com.troy.keeper.hbz.repository.HbzBrandRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/12/13.
 */
@Component
public class HbzBrandMapper extends BaseMapper<HbzBrand, HbzBrandDTO> {

    @Autowired
    HbzBrandRepository hbzBrandRepository;
    @Autowired
    private HbzAreaRepository areaRepository;
    @Autowired
    private HbzAreaMapper areaMapper;

    @Override
    public HbzBrand newEntity() {
        return new HbzBrand();
    }

    @Override
    public HbzBrandDTO newDTO() {
        return new HbzBrandDTO();
    }

    @Override
    public HbzBrand find(Long id) {
        return hbzBrandRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzBrand entity, HbzBrandDTO dto) {
        new Bean2Bean().addExcludeProp("area").copyProperties(entity, dto);
        if (entity.getArea() != null) {
            dto.setArea(areaMapper.map(entity.getArea()));
            dto.setAreaId(entity.getArea().getId());
        }
    }

    @Override
    public void dto2entity(HbzBrandDTO dto, HbzBrand entity) {
        Bean2Bean bean2bean = new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).addExcludeProp("area");
        bean2bean.copyProperties(dto, entity);
        if (dto.getArea() != null && dto.getArea().getId() != null)
            entity.setArea(areaRepository.findOne(dto.getArea().getId()));
        else if (dto.getAreaId() != null)
            entity.setArea(areaRepository.findOne(dto.getAreaId()));
        else
            entity.setArea(null);
    }
}
