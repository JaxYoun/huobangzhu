package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzProductDTO;
import com.troy.keeper.hbz.po.HbzProduct;
import com.troy.keeper.hbz.repository.HbzProductRepo;
import com.troy.keeper.hbz.repository.HbzWareInfoRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/12/14.
 */
@Component
public class HbzProductMapper extends BaseMapper<HbzProduct, HbzProductDTO> {

    @Autowired
    HbzProductRepo hbzProductRepo;

    @Autowired
    HbzWareInfoMapper hbzWareInfoMapper;

    @Autowired
    HbzWareInfoRepository hbzWareInfoRepo;

    @Override
    public HbzProduct newEntity() {
        return new HbzProduct();
    }

    @Override
    public HbzProductDTO newDTO() {
        return new HbzProductDTO();
    }

    @Override
    public HbzProduct find(Long id) {
        return hbzProductRepo.findOne(id);
    }

    @Override
    public void entity2dto(HbzProduct entity, HbzProductDTO dto) {
        new Bean2Bean().addExcludeProp("ware").copyProperties(entity, dto);
        if (entity.getWare() != null) {
            dto.setWare(hbzWareInfoMapper.map(entity.getWare()));
            dto.setWareId(entity.getWare().getId());
        }
    }

    @Override
    public void dto2entity(HbzProductDTO dto, HbzProduct entity) {
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).addExcludeProp("ware").copyProperties(dto, entity);
        if (dto.getWareId() != null)
            entity.setWare(hbzWareInfoRepo.findOne(dto.getWareId()));
        else if (dto.getWare() != null && dto.getWare().getId() != null)
            entity.setWare(hbzWareInfoRepo.findOne(dto.getWare().getId()));
        else entity.setWare(null);
    }
}
