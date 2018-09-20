package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzPledgeDTO;
import com.troy.keeper.hbz.po.HbzPledge;
import com.troy.keeper.hbz.repository.HbzPledgeRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2018/1/31.
 */
@Component
public class HbzPledgeMapper extends BaseMapper<HbzPledge, HbzPledgeDTO> {

    @Autowired
    HbzPledgeRepository hbzPledgeRepository;

    @Override
    public HbzPledge newEntity() {
        return new HbzPledge();
    }

    @Override
    public HbzPledgeDTO newDTO() {
        return new HbzPledgeDTO();
    }

    @Override
    public HbzPledge find(Long id) {
        return hbzPledgeRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzPledge entity, HbzPledgeDTO dto) {
        new Bean2Bean().copyProperties(entity, dto);
    }

    @Override
    public void dto2entity(HbzPledgeDTO dto, HbzPledge entity) {
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(dto, entity);
    }

}
