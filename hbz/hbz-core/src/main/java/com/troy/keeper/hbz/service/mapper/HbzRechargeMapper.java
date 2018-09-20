package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzRechargeDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.po.HbzRecharge;
import com.troy.keeper.hbz.repository.HbzRechargeRepository;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/12/4.
 */
@Component
public class HbzRechargeMapper extends BaseMapper<HbzRecharge, HbzRechargeDTO> {

    @Autowired
    HbzRechargeRepository rechargeRepository;

    @Autowired
    private HbzUserMapper hbzUserMapper;

    @Autowired
    private HbzUserRepository hbzUserRepository;

    @Override
    public HbzRecharge newEntity() {
        return new HbzRecharge();
    }

    @Override
    public HbzRechargeDTO newDTO() {
        return new HbzRechargeDTO();
    }

    @Override
    public HbzRecharge find(Long id) {
        return rechargeRepository.findOne(id);
    }

    @Override
    public void entity2dto(HbzRecharge entity, HbzRechargeDTO dto) {
        BeanUtils.copyProperties(entity, dto, "user");
        if (entity.getUser() != null) {
            dto.setUser(hbzUserMapper.map(entity.getUser()));
            dto.setUserId(entity.getUser().getId());
        }
    }

    @Override
    public void dto2entity(HbzRechargeDTO dto, HbzRecharge entity) {
        BeanUtils.copyProperties(dto, entity, StringHelper.conact(Const.ID_AUDIT_FIELDS, "user"));
        if (dto.getUserId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUserId()));
        else if (dto.getUser() != null && dto.getUser().getId() != null)
            entity.setUser(hbzUserRepository.findOne(dto.getUser().getId()));
        else entity.setUser(null);
    }
}
