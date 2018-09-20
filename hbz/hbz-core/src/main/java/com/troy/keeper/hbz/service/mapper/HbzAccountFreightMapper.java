package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.dto.HbzAccountFreightDTO;
import com.troy.keeper.hbz.po.HbzAccountFreight;
import com.troy.keeper.hbz.repository.HbzAccountFreightRepo;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/11/20.
 */
@Component
public class HbzAccountFreightMapper extends BaseMapper<HbzAccountFreight, HbzAccountFreightDTO> {

    @Autowired
    HbzAccountFreightRepo hbzAccountFreightReposies;
    @Autowired
    HbzUserMapper hbzUserMapper;
    @Autowired
    HbzUserRepository hbzUserRepository;

    @Override
    public HbzAccountFreight newEntity() {
        return new HbzAccountFreight();
    }

    @Override
    public HbzAccountFreightDTO newDTO() {
        return new HbzAccountFreightDTO();
    }

    @Override
    public HbzAccountFreight find(Long id) {
        return hbzAccountFreightReposies.findOne(id);
    }

    @Override
    public void entity2dto(HbzAccountFreight entity, HbzAccountFreightDTO dto) {
        new Bean2Bean().addExcludeProp("user").addExcludeProp("host").copyProperties(entity, dto);
        if (entity.getUser() != null) {
            dto.setUser(hbzUserMapper.map(entity.getUser()));
            dto.setUserId(entity.getUser().getId());
        }
        if (entity.getHost() != null) {
            dto.setHost(hbzUserMapper.map(entity.getHost()));
            dto.setHostId(entity.getHost().getId());
        }
    }

    @Override
    public void dto2entity(HbzAccountFreightDTO dto, HbzAccountFreight entity) {
        new Bean2Bean().addExcludeProp("user").addExcludeProp("host").copyProperties(dto, entity);
        if (dto.getUser() != null && dto.getUser().getId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUser().getId()));
        else if (dto.getUserId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUserId()));
        else entity.setUser(null);
        if (dto.getHost() != null && dto.getHost().getId() != null)
            entity.setHost(hbzUserRepository.findOne(dto.getHost().getId()));
        else if (dto.getHostId() != null)
            entity.setHost(hbzUserRepository.findOne(dto.getHostId()));
        else entity.setHost(null);
    }
}
