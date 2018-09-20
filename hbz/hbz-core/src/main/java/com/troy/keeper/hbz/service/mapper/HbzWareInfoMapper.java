package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzWareInfoDTO;
import com.troy.keeper.hbz.po.HbzWareInfo;
import com.troy.keeper.hbz.repository.HbzBrandRepository;
import com.troy.keeper.hbz.repository.HbzWareInfoRepository;
import com.troy.keeper.hbz.repository.HbzWareTypeRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/12/12.
 */
@Component
public class HbzWareInfoMapper extends BaseMapper<HbzWareInfo, HbzWareInfoDTO> {

    @Autowired
    HbzWareInfoRepository wareInfoRepository;

    @Autowired
    HbzWareTypeMapper mapper;

    @Autowired
    HbzWareTypeRepository wareTypeRepository;

    @Autowired
    HbzBrandMapper hbzBrandMapper;

    @Autowired
    HbzBrandRepository hbzBrandRepository;

    @Override
    public HbzWareInfo newEntity() {
        return new HbzWareInfo();
    }

    @Override
    public HbzWareInfoDTO newDTO() {
        return new HbzWareInfoDTO();
    }

    @Override
    public HbzWareInfo find(Long id) {
        return wareInfoRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzWareInfo entity, HbzWareInfoDTO dto) {
        new Bean2Bean().addExcludeProp("type", "brand").copyProperties(entity, dto);
        if (entity.getType() != null) {
            dto.setType(mapper.map(entity.getType()));
            dto.setTypeId(entity.getType().getId());
        }
        if (entity.getBrand() != null) {
            dto.setBrandId(entity.getBrand().getId());
            dto.setBrand(hbzBrandMapper.map(entity.getBrand()));
        }
    }

    @Override
    public void dto2entity(HbzWareInfoDTO dto, HbzWareInfo entity) {
        new Bean2Bean().addExcludeProp("type", "brand").addExcludeProp(Const.ID_FIELDS).copyProperties(dto, entity);
        if (dto.getTypeId() != null)
            entity.setType(wareTypeRepository.findOne(dto.getTypeId()));
        else if (dto.getType() != null && dto.getType().getId() != null)
            entity.setType(wareTypeRepository.findOne(dto.getType().getId()));
        else entity.setType(null);

        if (dto.getBrandId() != null)
            entity.setBrand(hbzBrandRepository.findOne(dto.getBrandId()));
        else if (!(dto == null) && dto.getBrand() != null && dto.getBrand().getId() != null)
            entity.setBrand(hbzBrandRepository.findOne(dto.getBrand().getId()));
        else entity.setBrand(null);
    }
}
