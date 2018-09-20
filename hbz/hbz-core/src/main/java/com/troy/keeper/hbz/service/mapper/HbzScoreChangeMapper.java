package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzScoreChangeDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzScoreChange;
import com.troy.keeper.hbz.repository.HbzScoreChangeRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2018/1/19.
 */
@Component
public class HbzScoreChangeMapper extends BaseMapper<HbzScoreChange, HbzScoreChangeDTO> {
    @Autowired
    HbzScoreChangeRepository hbzScoreChangeRepository;
    @Autowired
    HbzUserRepository hbzUserRepository;
    @Autowired
    HbzUserMapper hbzUserMapper;

    @Override
    public HbzScoreChange newEntity() {
        return new HbzScoreChange();
    }

    @Override
    public HbzScoreChangeDTO newDTO() {
        return new HbzScoreChangeDTO();
    }

    @Override
    public HbzScoreChange find(Long id) {
        return hbzScoreChangeRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzScoreChange entity, HbzScoreChangeDTO dto) {
        new Bean2Bean().addExcludeProp("user").copyProperties(entity, dto);
        if (entity.getUser() != null) {
            dto.setUser(hbzUserMapper.map(entity.getUser()));
            dto.setUserId(entity.getUser().getId());
        }
    }

    @Override
    public void dto2entity(HbzScoreChangeDTO dto, HbzScoreChange entity) {
        new Bean2Bean().addExcludeProp(StringHelper.conact(new String[]{"user"}, Const.ID_AUDIT_FIELDS)).copyProperties(dto, entity);
        if (dto.getUserId() != null) {
            entity.setUser(hbzUserRepository.findOne(dto.getUserId()));
        } else if (dto.getUser() != null && dto.getUser().getId() != null) {
            entity.setUser(hbzUserRepository.findOne(dto.getUser().getId()));
        } else {
            entity.setUser(null);
        }
    }
}
