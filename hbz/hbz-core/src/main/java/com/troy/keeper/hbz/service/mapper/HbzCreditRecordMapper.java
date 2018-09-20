package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzCreditRecordDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzCreditRecord;
import com.troy.keeper.hbz.repository.HbzCreditRecordRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2018/1/19.
 */
@Component
public class HbzCreditRecordMapper extends BaseMapper<HbzCreditRecord, HbzCreditRecordDTO> {
    @Autowired
    HbzCreditRecordRepository hbzCreditRecordRepository;
    @Autowired
    HbzCreditRecordMapper hbzCreditRecordMapper;
    @Autowired
    HbzUserRepository hbzUserRepository;
    @Autowired
    HbzUserMapper hbzUserMapper;

    @Override
    public HbzCreditRecord newEntity() {
        return new HbzCreditRecord();
    }

    @Override
    public HbzCreditRecordDTO newDTO() {
        return new HbzCreditRecordDTO();
    }

    @Override
    public HbzCreditRecord find(Long id) {
        return hbzCreditRecordRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzCreditRecord entity, HbzCreditRecordDTO dto) {
        new Bean2Bean().addExcludeProp("user").copyProperties(entity, dto);
        if (entity.getUser() != null) {
            dto.setUser(hbzUserMapper.map(entity.getUser()));
            dto.setUserId(entity.getUser().getId());
        }
    }

    @Override
    public void dto2entity(HbzCreditRecordDTO dto, HbzCreditRecord entity) {
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
