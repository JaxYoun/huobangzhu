package com.troy.keeper.hbz.service.mapper;

import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzRefundDTO;
import com.troy.keeper.hbz.po.HbzRefund;
import com.troy.keeper.hbz.repository.HbzPayRepository;
import com.troy.keeper.hbz.repository.HbzRefundRepo;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2018/3/6.
 */
@Component
public class HbzrefundMapper extends BaseMapper<HbzRefund, HbzRefundDTO> {

    @Autowired
    HbzRefundRepo hbzRefundRepo;

    @Autowired
    HbzPayRepository payRepository;

    @Autowired
    HbzPayMapper payMapper;

    @Autowired
    HbzUserMapper userMapper;

    @Autowired
    HbzUserRepository userRepository;

    @Override
    public HbzRefund newEntity() {
        return new HbzRefund();
    }

    @Override
    public HbzRefundDTO newDTO() {
        return new HbzRefundDTO();
    }

    @Override
    public HbzRefund find(Long id) {
        return hbzRefundRepo.findOne(id);
    }

    @Override
    public void entity2dto(HbzRefund entity, HbzRefundDTO dto) {
        new Bean2Bean().addExcludeProp("pay", "user").copyProperties(entity, dto);
        if (entity.getPay() != null) {
            dto.setPayId(entity.getPay().getId());
            dto.setPay(payMapper.map(entity.getPay()));
        }
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
            dto.setUser(userMapper.map(entity.getUser()));
        }
    }

    @Override
    public void dto2entity(HbzRefundDTO dto, HbzRefund entity) {
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).addExcludeProp("pay", "user").copyProperties(dto, entity, true);
        if (dto.getPayId() != null)
            entity.setPay(payRepository.findOne(dto.getPayId()));
        else
            entity.setPay(null);

        if (dto.getUserId() != null)
            entity.setUser(userRepository.findOne(dto.getUserId()));
        else
            entity.setUser(null);
    }
}
