package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzCoordinateDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzCoordinate;
import com.troy.keeper.hbz.repository.HbzAreaRepository;
import com.troy.keeper.hbz.repository.HbzCoordinateRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/10/27.
 */
@Component
public class HbzCoordinateMapper extends BaseMapper<HbzCoordinate, HbzCoordinateDTO> {
    @Autowired
    private HbzCoordinateRepository hbzCoordinateRepository;
    @Autowired
    HbzUserMapper hbzUserMapper;
    @Autowired
    private HbzUserRepository hbzUserRepository;
    @Autowired
    HbzAreaMapper am;
    @Autowired
    HbzAreaRepository hr;

    @Override
    public HbzCoordinate newEntity() {
        return new HbzCoordinate();
    }

    @Override
    public HbzCoordinateDTO newDTO() {
        return new HbzCoordinateDTO();
    }

    @Override
    public HbzCoordinate find(Long id) {
        return hbzCoordinateRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzCoordinate entity, HbzCoordinateDTO dto) {
        BeanUtils.copyProperties(entity, dto, "user", "area");
        if (entity.getUser() != null) {
            dto.setUser(hbzUserMapper.map(entity.getUser()));
            dto.setUserId(entity.getUser().getId());
        }
        if (entity.getArea() != null) {
            dto.setArea(am.map(entity.getArea()));
            dto.setAreaId(entity.getArea().getId());
        }
    }

    @Override
    public void dto2entity(HbzCoordinateDTO dto, HbzCoordinate entity) {
        BeanUtils.copyProperties(dto, entity, StringHelper.conact(Const.ID_AUDIT_FIELDS, "user", "area"));
        if (dto.getUserId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUserId()));
        else if (dto.getUser() != null && dto.getUser().getId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUser().getId()));
        else entity.setUser(null);

        if (dto.getArea() != null && dto.getArea().getId() != null)
            entity.setArea(hr.findOne(dto.getArea().getId()));
        else if (dto.getAreaId() != null)
            entity.setArea(hr.findOne(dto.getAreaId()));
        else entity.setArea(null);
    }
}
