package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzDriverLineDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzDriverLine;
import com.troy.keeper.hbz.repository.HbzAreaRepository;
import com.troy.keeper.hbz.repository.HbzDriverLineRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/12/6.
 */
@Component
public class HbzDriverLineMapper extends BaseMapper<HbzDriverLine, HbzDriverLineDTO> {

    @Autowired
    HbzDriverLineRepository driverLineRepository;
    @Autowired
    private HbzAreaMapper areaMapper;
    @Autowired
    private HbzAreaRepository hbzAreaRepository;
    @Autowired
    private HbzUserMapper hbzUserMapper;
    @Autowired
    private HbzUserRepository user;

    @Override
    public HbzDriverLine newEntity() {
        return new HbzDriverLine();
    }

    @Override
    public HbzDriverLineDTO newDTO() {
        return new HbzDriverLineDTO();
    }

    @Override
    public HbzDriverLine find(Long id) {
        return driverLineRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzDriverLine entity, HbzDriverLineDTO dto) {
        new Bean2Bean().addExcludeProp("user", "originArea", "destArea", "transSizes").copyProperties(entity, dto);
        if (entity.getUser() != null) {
            dto.setUser(hbzUserMapper.map(entity.getUser()));
            dto.setUserId(entity.getUser().getId());
        }
        if (entity.getOriginArea() != null) {
            dto.setOriginArea(areaMapper.map(entity.getOriginArea()));
            dto.setOriginAreaId(entity.getOriginArea().getId());
        }
        if (entity.getDestArea() != null) {
            dto.setDestArea(areaMapper.map(entity.getDestArea()));
            dto.setDestAreaId(entity.getDestArea().getId());
        }
    }

    @Override
    public void dto2entity(HbzDriverLineDTO dto, HbzDriverLine entity) {
        BeanUtils.copyProperties(dto, entity, StringHelper.conact(Const.ID_AUDIT_FIELDS, "user", "originArea", "transSizes", "destArea"));
        if (entity.getUser() != null) {
            dto.setUser(hbzUserMapper.map(entity.getUser()));
            dto.setUserId(entity.getUser().getId());
        }
        if (dto.getOriginAreaId() != null)
            entity.setOriginArea(hbzAreaRepository.findOne(dto.getOriginAreaId()));
        else if (dto.getOriginArea() != null && dto.getOriginArea().getId() != null)
            entity.setOriginArea(hbzAreaRepository.findOne(dto.getOriginArea().getId()));
        else entity.setOriginArea(null);

        if (dto.getDestAreaId() != null)
            entity.setDestArea(hbzAreaRepository.findOne(dto.getDestAreaId()));
        else if (dto.getDestArea() != null && dto.getDestArea().getId() != null)
            entity.setDestArea(hbzAreaRepository.findOne(dto.getDestArea().getId()));
        else entity.setDestArea(null);

        if (dto.getUserId() != null)
            entity.setUser(user.findOne(dto.getUserId()));
        else if (dto.getUser() != null && dto.getUser().getId() != null)
            entity.setUser(user.findOne(dto.getUser().getId()));
        else entity.setUser(null);
    }
}
