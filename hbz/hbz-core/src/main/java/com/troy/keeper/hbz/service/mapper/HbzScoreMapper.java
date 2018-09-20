package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.dto.HbzScoreDTO;
import com.troy.keeper.hbz.po.HbzScore;
import com.troy.keeper.hbz.repository.HbzScoreRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/12/19.
 */
@Component
public class HbzScoreMapper extends BaseMapper<HbzScore, HbzScoreDTO> {

    @Autowired
    HbzScoreRepository hbzScoreRepository;

    @Autowired
    HbzUserMapper uMapper;

    @Autowired
    HbzUserRepository urepo;

    @Override
    public HbzScore newEntity() {
        return new HbzScore();
    }

    @Override
    public HbzScoreDTO newDTO() {
        return new HbzScoreDTO();
    }

    @Override
    public HbzScore find(Long id) {
        return hbzScoreRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzScore entity, HbzScoreDTO dto) {
        new Bean2Bean().addExcludeProp("user").copyProperties(entity, dto);
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
            dto.setUser(uMapper.map(entity.getUser()));
        }
    }

    @Override
    public void dto2entity(HbzScoreDTO dto, HbzScore entity) {
        new Bean2Bean().addExcludeProp("user").copyProperties(dto, entity);
        if (dto.getUserId() != null)
            entity.setUser(urepo.findOne(dto.getUserId()));
        else if (dto.getUser() != null && dto.getUser().getId() != null)
            entity.setUser(urepo.findOne(dto.getUser().getId()));
        else entity.setUser(null);
    }
}
